package com.dextra.mentoria.products.controllers;

import com.dextra.mentoria.products.controllers.exceptions.StandardError;
import com.dextra.mentoria.products.dto.request.ProductRequest;
import com.dextra.mentoria.products.dto.response.ProductResponse;
import com.dextra.mentoria.products.entities.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

public interface IProductController {

    @PostMapping
    @Operation(summary = "Insert a new Product")
    ResponseEntity<Product> insert(@Valid @RequestBody ProductRequest request);

    @PutMapping(value = "/{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(
            summary = "Update the product with id in param",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value =
                                                    """
                                                                    {
                                                                        "timestamp": "2021-10-17T14:55:30.385445300Z",
                                                                        "status": 404,
                                                                        "error": "Resource not found",
                                                                        "message": "Entity not found.",
                                                                        "path": "/products/1000"
                                                                    }
                                                            """
                                    ),
                                    schema = @Schema(implementation = StandardError.class)
                            )
                    )
            })
    void update(@Valid @RequestBody ProductRequest request, @PathVariable Long id);

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(
            summary = "Delete the product with id in param",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value =
                                                    """
                                                                    {
                                                                        "timestamp": "2021-10-17T14:55:30.385445300Z",
                                                                        "status": 404,
                                                                        "error": "Resource not found",
                                                                        "message": "Entity not found.",
                                                                        "path": "/products/1000"
                                                                    }
                                                            """
                                    ),
                                    schema = @Schema(implementation = StandardError.class)
                            )
                    )
            })
    void delete(@PathVariable Long id);

    @GetMapping()
    @ResponseStatus(OK)
    @Operation(summary = "Retrieve a page of products")
    Page<ProductResponse> findAll(Pageable pageable);


    @GetMapping(value = "/{id}")
    @ResponseStatus(OK)
    @Operation(
            summary = "Retrieve one product searching by id",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value =
                                                    """
                                                                    {
                                                                        "timestamp": "2021-10-17T14:55:30.385445300Z",
                                                                        "status": 404,
                                                                        "error": "Resource not found",
                                                                        "message": "Entity not found.",
                                                                        "path": "/products/1000"
                                                                    }
                                                            """
                                    ),
                                    schema = @Schema(implementation = StandardError.class)
                            )
                    )
            }
    )
    Product findById(@PathVariable Long id);

}
