package com.katerynamykh.taskprofitsoft.backend.controller;

import com.katerynamykh.taskprofitsoft.backend.dto.chain.ChainResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.chain.CreateChainRequestDto;
import com.katerynamykh.taskprofitsoft.backend.service.RestorantChainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

@Tag(name = "RestorantChain management API", description = "REST endpoints for managing chains of restorants")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restorant-chains")
public class RestorantChainController {
    private final RestorantChainService chainService;

    /**
     * Retrieve all restorant chains GET /api/restorant-chains
     * 
     * @return OK (status code 200)
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retrieve all restorant chains", description = "Retrieve all restorant chains")
    List<ChainResponseDto> retrieveAll(){
        return chainService.findAll();
    }
    
    /**
     * Create a restorant chain POST /api/restorant-chains
     * 
     * @param chainDto
     * @return CREATED (status code 201)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new restorant chain", description = "Create a new restorant chain")
    ChainResponseDto create(@Parameter(name = "chainDto", required = true) @RequestBody @Valid CreateChainRequestDto chainDto) {
        return chainService.save(chainDto);
    }
    
    /**
     * Update restorant chain PUT /api/restorant-chains/{id}
     * 
     * @param id - (required)
     * @param chainDto - (required)
     * @return OK (status code 200)
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update restorant chain data", description = "Update restorant chain data")
    public ChainResponseDto updateById(@Parameter(name = "chain id", required = true) @PathVariable Long id,
            @Parameter(name = "chainDto", required = true) @RequestBody @Valid CreateChainRequestDto chainDto) {
        return chainService.update(id, chainDto);
    }

    /**
     * Delete a restorant chain DELETE /api/restorant-chains/{id}
     * 
     * @param id - (required)
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a resorant chain", description = "Delete a resorant chain by id")
    public void delete(@Parameter(name = "chain id", required = true) @PathVariable Long id) {
        chainService.deleteById(id);
    }
}
