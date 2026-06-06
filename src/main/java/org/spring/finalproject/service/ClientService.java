package org.spring.finalproject.service;

import org.spring.finalproject.dto.request.ClientDto;

import java.util.List;

public interface ClientService {

    List<ClientDto> findAll();

    ClientDto findById(Long id);

    ClientDto save(ClientDto dto);

    ClientDto update(Long id,
                     ClientDto dto);

    void delete(Long id);
}
