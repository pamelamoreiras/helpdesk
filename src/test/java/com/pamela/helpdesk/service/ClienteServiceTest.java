package com.pamela.helpdesk.service;

import com.pamela.helpdesk.domain.Cliente;
import com.pamela.helpdesk.domain.dtos.ClienteDTO;
import com.pamela.helpdesk.repository.ClienteRepository;
import com.pamela.helpdesk.repository.PessoaRepository;
import com.pamela.helpdesk.services.ClienteService;
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
public class ClienteServiceTest {

    @Mock
    ClienteRepository clienteRepository;

    @Mock
    PessoaRepository pessoaRepository;

    @Mock
    BCryptPasswordEncoder encoder;

    @InjectMocks
    ClienteService clienteService;

    @Test
    void quandoReceberIdRetornaCliente() {
        final var idValido = 2;

        final var clienteCriado = this.criarCliente();

        Mockito.when(clienteRepository.findById(idValido)).thenReturn(Optional.of(clienteCriado));

        final var result = clienteService.findById(idValido);

        assertNotNull(result);
        assertEquals(result.getId(), clienteCriado.getId());

    }

    @Test
    void retornaTodosOsClientes() {

        final var clienteCriado = this.criarCliente();

        Mockito.when(clienteRepository.findAll()).thenReturn(List.of(clienteCriado));

        final var result = clienteService.findAll();

        assertNotNull(result);

    }

    @Test
    void quandoReceberOsDadosDoClienteDTORetornaCliente(){
        final var clienteDTOCriado = this.criarClienteDTO();

        final var clienteCriado = this.criarCliente();

        Mockito.when(clienteRepository.save(Mockito.any())).thenReturn(clienteCriado);

        final var result = clienteService.create(clienteDTOCriado);

        assertNotNull(result);
        assertEquals(result.getCpf(), clienteDTOCriado.getCpf());

    }

    @Test
    void quandoForInformadoUmIdValidoEOsDadosDeUmClienteDtoRetornaOClienteAlretado() {

        final var idValido = 1;

        final var clienteDtoCriado = this.criarClienteDTO();

        final var clienteCriado = this.criarCliente();

        clienteDtoCriado.setSenha("1234");

        Mockito.when(clienteRepository.findById(Mockito.any())).thenReturn(Optional.of(clienteCriado));
        Mockito.when(clienteRepository.save(Mockito.any())).thenReturn(clienteCriado);

        final var result = clienteService.update(idValido, clienteDtoCriado);

        assertNotNull(result);
        assertEquals(clienteDtoCriado.getCpf(), result.getCpf());

    }

    @Test
    void quandoInformarIdDeletaOCliente() {
        final var idValido = 2;

        final var clienteCriado = this.criarCliente();

        Mockito.when(clienteRepository.findById(Mockito.any())).thenReturn(Optional.of(clienteCriado));
        Mockito.doNothing().when(clienteRepository).deleteById(2);

        clienteService.delete(idValido);

        Mockito.verify(clienteRepository,Mockito.times(1)).deleteById(Mockito.any());

    }



    private Cliente criarCliente(){

        final Cliente cli1 = new Cliente(2,
                "Alef chaves",
                "21173009019",
                "alef@gmail.com",
                encoder.encode("123"));

        return cli1;
    }

    private ClienteDTO criarClienteDTO(){

        final ClienteDTO cli1 = new ClienteDTO(criarCliente());

        return cli1;
    }
}
