package com.katerynamykh.taskprofitsoft.backend.controller;

import com.katerynamykh.taskprofitsoft.backend.dto.chain.ChainResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.chain.ChainWithLocationsDto;
import com.katerynamykh.taskprofitsoft.backend.dto.chain.CreateChainRequestDto;
import com.katerynamykh.taskprofitsoft.backend.service.RestaurantChainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "RestaurantChain management API", description = "REST endpoints for managing chains of restaurants")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restaurant-chains")
public class RestaurantChainController {
    private final RestaurantChainService chainService;

    /**
     * Retrieve all restaurant chains GET /api/restaurant-chains
     * 
     * @return OK (status code 200)
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retrieve all restaurant chains", description = "Retrieve all restaurant chains")
    List<ChainWithLocationsDto> retrieveAll(@ParameterObject Pageable pageable) {
        return chainService.findAll(pageable);
    }

    /**
     * Create a restaurant chain POST /api/restaurant-chains
     * 
     * @param chainDto
     * @return CREATED (status code 201)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new restaurant chain", description = "Create a new restaurant chain")
    ChainResponseDto create(
            @Parameter(name = "chainDto", required = true) @RequestBody @Valid CreateChainRequestDto chainDto) {
        return chainService.save(chainDto);
    }

    /**
     * Update restaurant chain PUT /api/restaurant-chains/{id}
     * 
     * @param id       - (required)
     * @param chainDto - (required)
     * @return OK (status code 200)
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update restaurant chain data", description = "Update restaurant chain data")
    public ChainResponseDto updateById(
            @Parameter(name = "chain id", required = true) @PathVariable Long id,
            @Parameter(name = "chainDto", required = true) @RequestBody @Valid CreateChainRequestDto chainDto) {
        return chainService.update(id, chainDto);
    }

    /**
     * Delete a restaurant chain DELETE /api/restaurant-chains/{id}
     * 
     * @param id - (required)
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a restaurant chain", description = "Delete a restaurant chain by id")
    public void delete(@Parameter(name = "chain id", required = true) @PathVariable Long id) {
        chainService.deleteById(id);
    }
}
