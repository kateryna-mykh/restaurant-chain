package com.katerynamykh.taskprofitsoft.backend.controller;

import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.CreatedRestaurantRequestDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.DetaildRestaurantResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.FilteredRestaurantsDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.RestaurantResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.SearchRestaurantDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.UploadResult;
import com.katerynamykh.taskprofitsoft.backend.service.RestaurantService;
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

@Tag(name = "Restaurant management API", description = "REST endpoints for managing restaurants")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    /**
     * Create a restaurant POST /api/restaurants
     * 
     * @param restaurantDto - (required)
     * @return CREATED (status code 201)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new restaurant", description = "Create a new restaurant")
    RestaurantResponseDto create(
            @Parameter(name = "restaurantDto", required = true) @RequestBody @Valid CreatedRestaurantRequestDto restaurantDto) {
        return restaurantService.save(restaurantDto);
    }

    /**
     * Retrieve a restaurant GET /api/restaurants/{id}
     * 
     * @param id - (required)
     * @return OK (status code 200)
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retrieve restaurant by id", description = "Retrieve restaurant by id")
    public DetaildRestaurantResponseDto retrieveById(
            @Parameter(name = "restaurant id", required = true) @PathVariable Long id) {
        return restaurantService.findById(id);
    }

    /**
     * Update a restaurant PUT /api/restaurants/{id}
     * 
     * @param id            - (required)
     * @param restaurantDto - (required)
     * @return OK (status code 200)
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update restaurant data", description = "Update restaurant data")
    public RestaurantResponseDto updateById(
            @Parameter(name = "restaurant id", required = true) @PathVariable Long id,
            @Parameter(name = "restaurantDto", required = true) @RequestBody @Valid CreatedRestaurantRequestDto restaurantDto) {
        return restaurantService.update(id, restaurantDto);
    }

    /**
     * Delete a restaurant DELETE /api/restaurants/{id}
     * 
     * @param id - (required)
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a resorant", description = "Delete a resorant by id")
    public void delete(@Parameter(name = "restaurant id", required = true) @PathVariable Long id) {
        restaurantService.deleteById(id);
    }

    /**
     * Search request POST /api/restaurants/_list
     * 
     * @param searchParams - (not required)
     * @return OK (status code 200)
     */
    @PostMapping("/_list")
    @Operation(summary = "Search restaurants by params", description = "Search restaurants by params")
    FilteredRestaurantsDto search(
            @Parameter(name = "search_params", required = false) @RequestBody SearchRestaurantDto searchParams) {
        return restaurantService.search(searchParams);
    }

    /**
     * Generate report POST /api/restaurants/_report
     * 
     * @param httpServletResponse - (required)
     * @param searchParams        - (not required)
     */
    @PostMapping(value = "/_report", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "Generate report by params request", description = "Generate report by params request")
    void generateReport(HttpServletResponse httpServletResponse,
            @Parameter(name = "search_params", required = false) SearchRestaurantDto searchParams) {
        restaurantService.generateReport(httpServletResponse, searchParams);
    }

    /**
     * Upload restaurants POST /api/restaurants/upload
     * 
     * @param file - (required)
     * @return CREATED (status code 201)
     */
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Upload restaurants from file", description = "Upload restaurants from file")
    UploadResult uploadRestaurants(@RequestParam("file") MultipartFile file) {
        return restaurantService.uploadFromFile(file);
    }
}
