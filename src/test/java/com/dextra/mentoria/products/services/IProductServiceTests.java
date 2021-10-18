package com.dextra.mentoria.products.services;

import com.dextra.mentoria.products.dtos.request.ProductRequest;
import com.dextra.mentoria.products.dtos.response.ProductResponse;
import com.dextra.mentoria.products.entities.Product;
import com.dextra.mentoria.products.repositories.ProductRepository;
import com.dextra.mentoria.products.services.exceptions.DataIntegrityException;
import com.dextra.mentoria.products.services.exceptions.NotFoundException;
import com.dextra.mentoria.products.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.DateFormat;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class IProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private ICategoryService ICategoryService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    @BeforeEach
    void setUp() {
        this.existingId = 1L;
        this.nonExistingId = Long.MAX_VALUE;
        this.dependentId = 5L;
        Product product = Factory.createProduct();
        PageImpl<Product> page = new PageImpl<>(List.of(product));

        when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        when(repository.save(any())).thenReturn(product);
        when(repository.findById(this.existingId)).thenReturn(Optional.of(product));
        when(repository.findById(this.nonExistingId)).thenReturn(Optional.empty());
        doNothing().when(repository).deleteById(this.existingId);
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(this.nonExistingId);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(this.dependentId);
        when(ICategoryService.findById(this.existingId)).thenReturn(Factory.createCategory());
        when(modelMapper.map(any(), eq(Product.class))).thenReturn(Factory.createProduct());
        when(modelMapper.map(any(), eq(ProductResponse.class))).thenReturn(Factory.createProductResponse());

        when(this.objectMapper.registerModule(any())).thenReturn(this.objectMapper);
        when(this.objectMapper.setDateFormat(any())).thenReturn(this.objectMapper);

    }

    @Test
    public void findAllPagedShouldReturnPage(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductResponse> res = service.findAllPaged(pageable);
        assertNotNull(res);
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExist(){
        assertDoesNotThrow(() -> this.service.delete(this.existingId));

        verify(repository, times(1)).deleteById(this.existingId);
    }

    @Test
    public void deleteShouldThrowNotFoundExceptionWhenIdDoesNotExist(){
        assertThrows(NotFoundException.class, () -> this.service.delete(this.nonExistingId));

        verify(repository, times(1)).deleteById(this.nonExistingId);
    }

    @Test
    public void deleteShouldThrowDataIntegrityExceptionWhenIdIsDependent(){

        assertThrows(DataIntegrityException.class, () -> this.service.delete(this.dependentId));

        verify(repository, times(1)).deleteById(this.dependentId);
    }

    @Test
    public void findByIdShouldReturnAnProductResponseWhenIdExist() {
        Product dto = this.service.findById(this.existingId);
        assertNotNull(dto);
        verify(repository, times(1)).findById(this.existingId);
    }

    @Test
    public void findByIdShouldThrowNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(NotFoundException.class, () -> this.service.findById(this.nonExistingId));
        verify(repository, times(1)).findById(this.nonExistingId);
    }

    @Test
    public void createShouldReturnAnNewProductResponseWithIdWhenIdIsNull() {
        ProductRequest request = Factory.createProductRequest();
        Product response = this.service.create(request);
        assertNotNull(response.getId());
        verify(this.repository, times(1)).save(any());
    }

    @Test
    public void updateShouldReturnAnProductResponseWhenIdExist() {
        ProductRequest request = Factory.createProductRequest();
        this.service.update(this.existingId, request);

        verify(this.repository, times(1)).save(any());
    }

    @Test
    public void updateShouldThrowNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(NotFoundException.class, () -> this.service.update(this.nonExistingId, Factory.createProductRequest()));
    }

}
