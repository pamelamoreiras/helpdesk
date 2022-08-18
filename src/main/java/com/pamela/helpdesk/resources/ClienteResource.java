package com.pamela.helpdesk.resources;

import com.pamela.helpdesk.domain.Cliente;
import com.pamela.helpdesk.domain.dtos.ClienteDTO;
import com.pamela.helpdesk.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/cliente")
public class ClienteResource {

    @Autowired
    private ClienteService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ClienteDTO> findById(@PathVariable final Integer id){
        final Cliente obj = service.findById(id);
        return ResponseEntity.ok().body(new ClienteDTO(obj));
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> findAll(){
        final List<Cliente> list = service.findAll();
        final List<ClienteDTO> listDTO = list.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());

        return ResponseEntity.ok().body(listDTO);
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> create (@Valid @RequestBody final ClienteDTO objDTO){
        final Cliente newObj = service.create(objDTO);
        final URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ClienteDTO> update(@PathVariable final Integer id, @RequestBody final ClienteDTO objDTO){
        final Cliente obj = service.update(id,objDTO);
        return ResponseEntity.ok().body(new ClienteDTO(obj));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ClienteDTO> delete(@PathVariable final Integer id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
