package com.pamela.helpdesk.services;

import com.pamela.helpdesk.domain.Chamado;
import com.pamela.helpdesk.domain.Cliente;
import com.pamela.helpdesk.domain.Tecnico;
import com.pamela.helpdesk.domain.dtos.ChamadoDTO;
import com.pamela.helpdesk.domain.enums.Prioridade;
import com.pamela.helpdesk.domain.enums.Status;
import com.pamela.helpdesk.repository.ChamadoRepository;
import com.pamela.helpdesk.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ChamadoService {

    @Autowired
    private ChamadoRepository chamadoRepository;

    @Autowired
    private TecnicoService tecnicoService;

    @Autowired
    private ClienteService clienteService;

    public Chamado findById(final Integer id){
        final Optional<Chamado> obj = chamadoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! ID: " + id));
    }

    public List<Chamado> findAll() {
        return chamadoRepository.findAll();
    }

    public Chamado create(@Valid final ChamadoDTO objDTO) {
        return chamadoRepository.save(newChamado(objDTO));
    }

    public Chamado update(final Integer id, final ChamadoDTO objDTO) {
        objDTO.setId(id);
        Chamado oldObj = findById(id);
        oldObj = newChamado(objDTO);
        return chamadoRepository.save(oldObj);
    }

    private Chamado newChamado(final ChamadoDTO obj){
        final Tecnico tecnico = tecnicoService.findById(obj.getTecnico());
        final Cliente cliente = clienteService.findById(obj.getCliente());

        final Chamado chamado = new Chamado();
        if (obj.getId() != null){
            chamado.setId(obj.getId());
        }
        if (obj.getStatus().equals(2)){
            chamado.setDataFechamento(LocalDate.now());
        }
        chamado.setTecnico(tecnico);
        chamado.setCliente(cliente);
        chamado.setPrioridade(Prioridade.toEnum(obj.getPrioridade()));
        chamado.setStatus(Status.toEnum(obj.getStatus()));
        chamado.setTitulo(obj.getTitulo());
        chamado.setObservacoes(obj.getObservacoes());

        return chamado;
    }

}
