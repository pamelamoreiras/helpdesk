package com.pamela.helpdesk.resources;

import com.pamela.helpdesk.domain.Chamado;
import com.pamela.helpdesk.domain.dtos.ChamadoDTO;
import com.pamela.helpdesk.services.ChamadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/chamados")
public class ChamadoResources {

    @Autowired
    private ChamadoService chamadoService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ChamadoDTO> findById(@PathVariable final Integer id){
        final Chamado obj = chamadoService.findById(id);
        return ResponseEntity.ok().body(new ChamadoDTO(obj));
    }

    @GetMapping
    public ResponseEntity<List<ChamadoDTO>> findAll(){
        final List<Chamado> list = chamadoService.findAll();
        final List<ChamadoDTO> listDTO = list.stream().map(obj -> new ChamadoDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDTO);
    }

    @PostMapping
    public ResponseEntity<ChamadoDTO> create(@Valid @RequestBody final ChamadoDTO objDTO){
        final Chamado obj = chamadoService.create(objDTO);
        final URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ChamadoDTO> update(@PathVariable final Integer id, @Valid @RequestBody final ChamadoDTO obj){
        final Chamado newObj = chamadoService.update(id, obj);
        return ResponseEntity.ok().body(new ChamadoDTO(newObj));
    }
}
