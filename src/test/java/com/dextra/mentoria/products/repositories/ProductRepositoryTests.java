package com.dextra.mentoria.products.repositories;

import com.dextra.mentoria.products.entities.Product;
import com.dextra.mentoria.products.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        this.existingId = 1L;
        this.nonExistingId = Long.MAX_VALUE;
        this.countTotalProducts = 5L;
    }

    @Test
    public void deleteShouldDeleteProductWhenIdExists(){
        repository.deleteById(this.existingId);
        Optional<Product> res = repository.findById(this.existingId);
        assertFalse(res.isPresent());
    }

    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist(){
        assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(this.nonExistingId);
        });
    }

    @Test
    public void  createShouldInsertWithAutoincrementNewProductWhenIdIsNull(){
        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);

        assertNotNull(product.getId());
        assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    public void findByIdShouldReturnAnNonEmptyOptionalWhenIdExists(){
        Optional<Product> res = repository.findById(this.existingId);
        assertTrue(res.isPresent());
    }

    @Test
    public void findByIdShouldReturnAnEmptyOptionalWhenIdDoesNotExists(){
        Optional<Product> res = repository.findById(this.nonExistingId);
        assertTrue(res.isEmpty());
    }

}
