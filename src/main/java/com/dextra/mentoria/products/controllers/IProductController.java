package com.dextra.mentoria.products.controllers;

import com.dextra.mentoria.products.controllers.exceptions.StandardError;
import com.dextra.mentoria.products.dtos.request.ProductRequest;
import com.dextra.mentoria.products.dtos.response.ProductResponse;
import com.dextra.mentoria.products.entities.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
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


    @Operation(
            summary = "Patch update in a product",
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
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "JsonPatch body.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            [
                                             { "op": "test",  "path": "/attribute",  "value": "value to test"  },
                                             { "op": "remove",  "path": "/attribute"  },
                                             { "op": "add",  "path": "/attribute",  "value": [ "new", "value" ] },
                                             { "op": "replace", "path": "/attribute",  "value": 42 },
                                             { "op": "move",  "from": "/attribute",  "path": "/attribute" },
                                             { "op": "copy", "from": "/attribute",  "path": "/attribute" }
                                             ]
                                            """
                            )
                    )
            )

    )
    @PatchMapping("/{id}")
    Product patchUpdate(@PathVariable Long id, @RequestBody JsonPatch patch) throws JsonPatchException, JsonProcessingException;
}
