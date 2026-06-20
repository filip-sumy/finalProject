package org.spring.finalproject.service;

import org.spring.finalproject.dto.ClientDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ClientService {

    List<ClientDto> findAll();

    Page<ClientDto> findAll(
            String search,
            Pageable pageable);

    ClientDto findById(Long id);

    ClientDto save(ClientDto dto);

    ClientDto update(Long id,
                     ClientDto dto);

    void delete(Long id);

    ClientDto findByEmail(String email);
}
