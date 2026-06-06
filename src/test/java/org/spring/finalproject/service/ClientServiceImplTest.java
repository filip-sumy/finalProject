package org.spring.finalproject.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spring.finalproject.dto.ClientDto;
import org.spring.finalproject.entity.Client;
import org.spring.finalproject.entity.Role;
import org.spring.finalproject.exception.EntityNotFoundException;
import org.spring.finalproject.mapper.ClientMapper;
import org.spring.finalproject.exception.EntityInUseException;
import org.spring.finalproject.repository.ClientRepository;
import org.spring.finalproject.repository.OrderRepository;
import org.spring.finalproject.service.impl.ClientServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    void save_setsClientRoleAndEncodesPassword() {

        ClientDto dto = new ClientDto();
        dto.setPassword("Password1!");
        dto.setEmail("client@test.com");

        Client entity = new Client();
        Client saved = new Client();
        saved.setId(1L);

        when(clientMapper.toEntity(dto)).thenReturn(entity);
        when(passwordEncoder.encode("Password1!"))
                .thenReturn("encoded");
        when(clientRepository.save(entity)).thenReturn(saved);
        when(clientMapper.toDto(saved)).thenReturn(dto);

        clientService.save(dto);

        assertEquals(Role.ROLE_CLIENT, entity.getRole());
        assertEquals("encoded", entity.getPassword());
        verify(clientRepository).save(entity);
    }

    @Test
    void delete_throws_whenClientNotFound() {

        when(clientRepository.existsById(99L)).thenReturn(false);

        assertThrows(
                EntityNotFoundException.class,
                () -> clientService.delete(99L));
    }

    @Test
    void delete_throws_whenClientHasOrders() {

        when(clientRepository.existsById(1L)).thenReturn(true);
        when(orderRepository.existsByClient_Id(1L)).thenReturn(true);

        assertThrows(
                EntityInUseException.class,
                () -> clientService.delete(1L));
    }
}
