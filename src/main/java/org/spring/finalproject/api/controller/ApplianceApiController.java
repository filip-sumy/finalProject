package org.spring.finalproject.api.controller;

import lombok.RequiredArgsConstructor;
import org.spring.finalproject.dto.ApplianceDto;
import org.spring.finalproject.service.ApplianceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/appliances")
@RequiredArgsConstructor
public class ApplianceApiController {

    private final ApplianceService applianceService;

    @GetMapping
    public Page<ApplianceDto> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sortOrder = direction.equalsIgnoreCase("desc")
                ? Sort.by(sort).descending()
                : Sort.by(sort).ascending();

        return applianceService.findAll(search, PageRequest.of(page, size, sortOrder));
    }

    @GetMapping("/{id}")
    public ApplianceDto findById(@PathVariable Long id) {
        return applianceService.findById(id);
    }
}
