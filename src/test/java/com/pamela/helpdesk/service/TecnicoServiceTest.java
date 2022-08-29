package com.pamela.helpdesk.service;

import com.pamela.helpdesk.domain.Tecnico;
import com.pamela.helpdesk.domain.dtos.TecnicoDTO;
import com.pamela.helpdesk.repository.PessoaRepository;
import com.pamela.helpdesk.repository.TecnicoRepository;
import com.pamela.helpdesk.services.TecnicoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class TecnicoServiceTest {

    @Mock
    TecnicoRepository tecnicoRepository;

    @Mock
    PessoaRepository pessoaRepository;

    @Mock
    BCryptPasswordEncoder encoder;

    @InjectMocks
    TecnicoService tecnicoService;

    @Test
    void quandoBuscarIdRetornaTecnico() {

        final var idValido = 2;

        final var tecnicoParaCriar = criarTecnico();

        Mockito.when(tecnicoRepository.findById(idValido)).thenReturn(Optional.of(tecnicoParaCriar));

        final var result = tecnicoService.findById(idValido);

        assertEquals(result.getId(), tecnicoParaCriar.getId());
    }

    @Test
    void retornaTodosOsTecnicos() {

        final var tecnicoParaCriar = criarTecnico();

        Mockito.when(tecnicoRepository.findAll()).thenReturn(List.of(tecnicoParaCriar));

        final var result = tecnicoService.findAll();

        assertNotNull(result);
    }

    @Test
    void quandoReceberOsDadosDeTecnicoDTORetornaTecnico() {

        final var tecnicoParaCriar = this.criarTecnico();

        final var tecnicoDTOParaCriar = this.criarTecnicoDTO();

        Mockito.when(tecnicoRepository.save(Mockito.any())).thenReturn(tecnicoParaCriar);

        final var result = tecnicoService.create(tecnicoDTOParaCriar);

        assertNotNull(result);
        assertEquals(result.getId(), tecnicoParaCriar.getId());
    }

    @Test
    void quandoForInformadoUmIdValidoEOsDadosDeUmTecnicoDtoRetornaOTecnicoAlretado(){

        final var idValido = 2;

        final var tecnicoParaCriar = this.criarTecnico();

        final var tecnicoDTOParaCriar = this.criarTecnicoDTO();

        tecnicoDTOParaCriar.setSenha("123");

        Mockito.when(tecnicoRepository.findById(Mockito.any())).thenReturn(Optional.of(tecnicoParaCriar));
        Mockito.when(tecnicoRepository.save(Mockito.any())).thenReturn(tecnicoParaCriar);

        final var result = tecnicoService.update(idValido, tecnicoDTOParaCriar);

        assertNotNull(result);
        assertEquals(result.getId() ,tecnicoDTOParaCriar.getId());
    }

    @Test
    void quandoInformarIdDeletaOTecnico() {

        final var idValido = 2;

        final var tecnicoParaCriar = criarTecnico();

        Mockito.when(tecnicoRepository.findById(idValido)).thenReturn(Optional.of(tecnicoParaCriar));
        Mockito.doNothing().when(tecnicoRepository).deleteById(idValido);

        tecnicoService.delete(idValido);

        Mockito.verify(tecnicoRepository, Mockito.times(1)).deleteById(Mockito.any());

    }

    private Tecnico criarTecnico(){

        final Tecnico tec1 = new Tecnico(2,
                "Alef chaves",
                "21173009019",
                "alef@gmail.com",
                encoder.encode("123"));

        return tec1;
    }

    private TecnicoDTO criarTecnicoDTO(){

        final TecnicoDTO tec1 = new TecnicoDTO(criarTecnico());

        return tec1;
    }

}
