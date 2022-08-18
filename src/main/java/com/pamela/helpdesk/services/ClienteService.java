package com.pamela.helpdesk.services;

import com.pamela.helpdesk.domain.Cliente;
import com.pamela.helpdesk.domain.Pessoa;
import com.pamela.helpdesk.domain.dtos.ClienteDTO;
import com.pamela.helpdesk.repository.ClienteRepository;
import com.pamela.helpdesk.repository.PessoaRepository;
import com.pamela.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.pamela.helpdesk.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public Cliente findById(final Integer id){
        final Optional<Cliente> obj = clienteRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id));
    }

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Cliente create(final ClienteDTO objDTO) {
        objDTO.setId(null);
        objDTO.setSenha(encoder.encode(objDTO.getSenha()));
        validaPorCpfEEmail(objDTO);
        final Cliente newObj = new Cliente(objDTO);
        return  clienteRepository.save(newObj);
    }

    public Cliente update(final Integer id, final ClienteDTO objDTO) {
        objDTO.setId(id);
        Cliente oldObj = findById(id);
        validaPorCpfEEmail(objDTO);
        oldObj = new Cliente(objDTO);
        return clienteRepository.save(oldObj);
    }

    private void validaPorCpfEEmail(final ClienteDTO objDTO) {
        Optional<Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
        if(obj.isPresent() && obj.get().getId() != objDTO.getId()){
            throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
        }

        obj = pessoaRepository.findByEmail(objDTO.getEmail());
        if(obj.isPresent() && obj.get().getId() != objDTO.getId()){
            throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
        }
    }

    public void delete(final Integer id) {
        final Cliente obj = findById(id);
        if(obj.getChamados().size() > 0){
            throw new DataIntegrityViolationException("Técnico possui ordens de serviço e não pode ser deletado");
        }

        clienteRepository.deleteById(id);
    }

}
