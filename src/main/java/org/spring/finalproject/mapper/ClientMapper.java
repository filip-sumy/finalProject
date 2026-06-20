package org.spring.finalproject.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.spring.finalproject.dto.ClientDto;
import org.spring.finalproject.dto.response.ClientResponseDto;
import org.spring.finalproject.entity.Client;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientMapper {
    private final ModelMapper modelMapper;

    public ClientDto toDto(Client client) {
        return modelMapper.map(client,ClientDto.class);
    }

    public Client toEntity(ClientDto dto) {
        return modelMapper.map(dto, Client.class);
    }
    public ClientResponseDto toResponseDto(Client client) {
        return modelMapper.map(client, ClientResponseDto.class);
    }
}
