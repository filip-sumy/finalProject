package org.spring.finalproject.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.finalproject.entity.Role;
import org.spring.finalproject.dto.ClientDto;
import org.spring.finalproject.entity.Client;
import org.spring.finalproject.exception.EntityInUseException;
import org.spring.finalproject.exception.EntityNotFoundException;
import org.spring.finalproject.mapper.ClientMapper;
import org.spring.finalproject.repository.ClientRepository;
import org.spring.finalproject.repository.OrderRepository;
import org.spring.finalproject.service.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;
    private final ClientMapper clientMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<ClientDto> findAll() {

        return clientRepository.findAll()
                .stream()
                .map(clientMapper::toDto)
                .toList();
    }
    @Transactional(readOnly = true)
    @Override
    public Page<ClientDto> findAll(
            String search,
            Pageable pageable) {

        Page<Client> page;

        if (search != null && !search.isBlank()) {
            page = clientRepository
                    .findByFirstNameContainingIgnoreCase(
                            search.trim(), pageable);
        } else {
            page = clientRepository.findAll(pageable);
        }

        return page.map(clientMapper::toDto);
    }
    @Override
    @Transactional(readOnly = true)
    public ClientDto findById(Long id) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Client not found: " + id));

        return clientMapper.toDto(client);
    }

    @Override
    public ClientDto save(ClientDto dto) {

        Client client = clientMapper.toEntity(dto);

        client.setRole(Role.ROLE_CLIENT);

        client.setPassword(
                passwordEncoder.encode(
                        dto.getPassword()));

        Client saved = clientRepository.save(client);

        log.info("Client created: {}", saved.getEmail());

        return clientMapper.toDto(saved);
    }

    @Override
    public ClientDto update(Long id, ClientDto dto) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Client not found: " + id));

        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        client.setEmail(dto.getEmail());
        client.setPhone(dto.getPhone());
        client.setAddress(dto.getAddress());

        if (dto.getPassword() != null
                && !dto.getPassword().isBlank()) {

            client.setPassword(
                    passwordEncoder.encode(
                            dto.getPassword()));
        }

        log.info("Client updated: {}", id);

        return clientMapper.toDto(client);
    }

    @Override
    public void delete(Long id) {

        if (!clientRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Client not found: " + id);
        }

        if (orderRepository.existsByClient_Id(id)) {
            throw new EntityInUseException(
                    "error.delete.client.in.use");
        }

        clientRepository.deleteById(id);

        log.warn("Client deleted: {}", id);
    }
}
