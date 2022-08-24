package com.pamela.helpdesk.services;

import com.pamela.helpdesk.domain.Pessoa;
import com.pamela.helpdesk.domain.Tecnico;
import com.pamela.helpdesk.domain.dtos.TecnicoDTO;
import com.pamela.helpdesk.repository.PessoaRepository;
import com.pamela.helpdesk.repository.TecnicoRepository;
import com.pamela.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.pamela.helpdesk.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TecnicoService {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public Tecnico findById(final Integer id){
        final Optional<Tecnico> obj = tecnicoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id));
    }

    public List<Tecnico> findAll() {
        return tecnicoRepository.findAll();
    }

    public Tecnico create(final TecnicoDTO objDTO) {
        objDTO.setId(null);
        objDTO.setSenha(encoder.encode(objDTO.getSenha()));
        validaPorCpfEEmail(objDTO);
        final Tecnico newObj = new Tecnico(objDTO);
        return  tecnicoRepository.save(newObj);
    }

    public void delete(final Integer id) {
        final Tecnico obj = findById(id);
        if(obj.getChamados().size() > 0){
            throw new DataIntegrityViolationException("Técnico possui ordens de serviço e não pode ser deletado");
        }

        tecnicoRepository.deleteById(id);
    }

    public Tecnico update(final Integer id, final TecnicoDTO objDTO) {
        objDTO.setId(id);
        Tecnico oldObj = findById(id);

        if (!objDTO.getSenha().equals(oldObj.getSenha())){
            objDTO.setSenha(encoder.encode(objDTO.getSenha()));
        }

        validaPorCpfEEmail(objDTO);
        oldObj = new Tecnico(objDTO);
        return tecnicoRepository.save(oldObj);
    }

    private void validaPorCpfEEmail(final TecnicoDTO objDTO) {
        Optional<Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
        if(obj.isPresent() && obj.get().getId() != objDTO.getId()){
            throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
        }

        obj = pessoaRepository.findByEmail(objDTO.getEmail());
        if(obj.isPresent() && obj.get().getId() != objDTO.getId()){
            throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
        }
    }


}
