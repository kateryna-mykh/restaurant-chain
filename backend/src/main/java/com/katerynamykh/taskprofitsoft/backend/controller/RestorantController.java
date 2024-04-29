package com.katerynamykh.taskprofitsoft.backend.controller;

import com.katerynamykh.taskprofitsoft.backend.dto.chain.UploadResult;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.CreatedRestorantRequestDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.DetaildRestorantResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.FilteredRestorantsDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.RestorantResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.SearchRestorantDto;
import com.katerynamykh.taskprofitsoft.backend.service.RestorantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Restorant management API", description = "REST endpoints for managing restorants")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restorants")
public class RestorantController {
    private final RestorantService restorantService;

    /**
     * Create a restorant POST /api/restorants
     * 
     * @param restorantDto - (required)
     * @return CREATED (status code 201)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new restorant", description = "Create a new restorant")
    RestorantResponseDto create(
            @Parameter(name = "restorantDto", required = true) @RequestBody @Valid CreatedRestorantRequestDto restorantDto) {
        return restorantService.save(restorantDto);
    }

    /**
     * Retrieve a resorant GET /api/restorants/{id}
     * 
     * @param id - (required)
     * @return OK (status code 200)
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retrieve restorant by id", description = "Retrieve restorant by id")
    public DetaildRestorantResponseDto retrieveById(
            @Parameter(name = "restorant id", required = true) @PathVariable Long id) {
        return restorantService.findById(id);
    }

    /**
     * Update a restorant PUT /api/restorants/{id}
     * 
     * @param id           - (required)
     * @param restorantDto - (required)
     * @return OK (status code 200)
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update restorant data", description = "Update restorant data")
    public RestorantResponseDto updateById(
            @Parameter(name = "restorant id", required = true) @PathVariable Long id,
            @Parameter(name = "restorantDto", required = true) @RequestBody @Valid CreatedRestorantRequestDto restorantDto) {
        return restorantService.update(id, restorantDto);
    }

    /**
     * Delete a restorant DELETE /api/restorants/{id}
     * 
     * @param id - (required)
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a resorant", description = "Delete a resorant by id")
    public void delete(@Parameter(name = "restorant id", required = true) @PathVariable Long id) {
        restorantService.deleteById(id);
    }

    /**
     * Search reqeuest POST /api/restorants/_list
     * 
     * @param searchParams - (not required)
     * @return OK (status code 200)
     */
    @GetMapping("/_list")
    @Operation(summary = "Search restorants by params", description = "Search restorants by params")
    FilteredRestorantsDto search(
            @Parameter(name = "search_params", required = false) @RequestBody SearchRestorantDto searchParams) {
        return restorantService.search(searchParams);
    }

    /**
     * Generate report POST /api/restorants/_report
     * 
     * @param httpServletResponse - (required)
     * @param searchParams        - (not required)
     */
    @PostMapping(value = "/_report", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "Generate report by params request", description = "Generate report by params request")
    void generateReport(HttpServletResponse httpServletResponse,
            @Parameter(name = "search_params", required = false) SearchRestorantDto searchParams) {
        restorantService.generateReport(httpServletResponse, searchParams);
    }

    /**
     * Upload restorants POST /api/restorants/upload
     * 
     * @param file - (required)
     * @return CREATED (status code 201)
     */
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Upload restorants from file", description = "Upload restorants from file")
    UploadResult uploadRestorants(@RequestParam("file") MultipartFile file) {
        return restorantService.uploadFromFile(file);
    }
}
