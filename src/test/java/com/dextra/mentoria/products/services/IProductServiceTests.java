package com.dextra.mentoria.products.services;

import com.dextra.mentoria.products.dto.request.ProductRequest;
import com.dextra.mentoria.products.dto.response.ProductResponse;
import com.dextra.mentoria.products.entities.Product;
import com.dextra.mentoria.products.repositories.ProductRepository;
import com.dextra.mentoria.products.services.exceptions.DataIntegrityException;
import com.dextra.mentoria.products.services.exceptions.NotFoundException;
import com.dextra.mentoria.products.tests.Factory;
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

import java.math.BigDecimal;
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

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private Product product;

    @BeforeEach
    void setUp() throws Exception {
        this.existingId = 1L;
        this.nonExistingId = Long.MAX_VALUE;
        this.dependentId = 5L;
        this.product = Factory.createProduct();
        PageImpl<Product> page = new PageImpl<>(List.of(this.product));

        when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        when(repository.save(any())).thenReturn(this.product);
        when(repository.findById(this.existingId)).thenReturn(Optional.of(this.product));
        when(repository.findById(this.nonExistingId)).thenReturn(Optional.empty());
        doNothing().when(repository).deleteById(this.existingId);
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(this.nonExistingId);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(this.dependentId);
        when(ICategoryService.find(this.existingId)).thenReturn(Factory.createCategory());
        when(modelMapper.map(any(), eq(Product.class))).thenReturn(Factory.createProduct());
        when(modelMapper.map(any(), eq(ProductResponse.class))).thenReturn(Factory.createProductResponse());
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
    public void findShouldReturnAnProductWhenIdExist() {
        Product product = this.service.find(this.existingId);
        assertNotNull(product);
        assertEquals(product, this.product);
        verify(repository, times(1)).findById(this.existingId);
    }

    @Test
    public void findShouldThrowNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(NotFoundException.class, () -> this.service.find(this.nonExistingId));
        verify(repository, times(1)).findById(this.nonExistingId);
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
