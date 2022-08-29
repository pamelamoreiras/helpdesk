package com.pamela.helpdesk.service;

import com.pamela.helpdesk.domain.Chamado;
import com.pamela.helpdesk.domain.Cliente;
import com.pamela.helpdesk.domain.Tecnico;
import com.pamela.helpdesk.domain.dtos.ChamadoDTO;
import com.pamela.helpdesk.domain.enums.Prioridade;
import com.pamela.helpdesk.domain.enums.Status;
import com.pamela.helpdesk.repository.ChamadoRepository;
import com.pamela.helpdesk.services.ChamadoService;
import com.pamela.helpdesk.services.ClienteService;
import com.pamela.helpdesk.services.TecnicoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ChamadoServiceTest {

    @Mock
    ChamadoRepository chamadoRepository;

    @Mock
    TecnicoService tecnicoService;

    @Mock
    ClienteService clienteService;

    @InjectMocks
    ChamadoService chamadoService;

    @Test
    void quandoBuscarIdRetornaOChamado() {

        final var idValido = 2;

        final var chamadoParaCriar = this.criarChamado();

        Mockito.when(chamadoRepository.findById(Mockito.any())).thenReturn(Optional.of(chamadoParaCriar));

        final var result = chamadoService.findById(idValido);

        assertNotNull(result);
        assertEquals(result.getId(), chamadoParaCriar.getId());

    }

    @Test
    void retornaTodosOsChamados() {

        final var chamadoParaCriar = this.criarChamado();

        Mockito.when(chamadoRepository.findAll()).thenReturn(List.of(chamadoParaCriar));

        final var result = chamadoService.findAll();

        assertNotNull(result);

    }

    @Test
    void quandoReceberOsDadosRetornaChamadoCriado() {

        final var chamadoParaCriar = this.criarChamado();

        final var chamadoDTOParaCriar = this.criarChamadoDTO();

        Mockito.when(chamadoRepository.save(chamadoParaCriar)).thenReturn(chamadoParaCriar);

        final var result = chamadoService.create(chamadoDTOParaCriar);

        assertNotNull(result);
        assertEquals(result.getId(), chamadoDTOParaCriar.getId());

    }

    @Test
    void quandoReceberOsDadosRetornaChamadoAlterado() {

        final var idValido = 2;

        final var chamadoParaCriar = this.criarChamado();

        final var chamadoDTOParaCriar = this.criarChamadoDTO();

        Mockito.when(chamadoRepository.findById(Mockito.any())).thenReturn(Optional.of(chamadoParaCriar));
        Mockito.when(chamadoRepository.save(Mockito.any())).thenReturn(chamadoParaCriar);

        final var result = chamadoService.update(idValido, chamadoDTOParaCriar);

        assertNotNull(result);

    }


    private Chamado criarChamado() {

        Cliente cli2 = new Cliente(null,
                "Marie Curie",
                "322.429.140-06",
                "curie@mail.com",
                "123");
        Tecnico tec2 = new Tecnico(null,
                "Richard Stallman",
                "903.347.070-56",
                "stallman@mail.com",
                "123");

        Chamado c1 = new Chamado(null,
                Prioridade.MEDIA,
                Status.ANDAMENTO,
                "Chamado 01",
                "Primeiro chamado",
                tec2,
                cli2);

        return c1;
    }

    private ChamadoDTO criarChamadoDTO() {
        ChamadoDTO chamadoDTO = new ChamadoDTO(criarChamado());
        return chamadoDTO;
    }

}
