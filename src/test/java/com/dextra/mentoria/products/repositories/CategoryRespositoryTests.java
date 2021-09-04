package com.dextra.mentoria.products.repositories;

import com.dextra.mentoria.products.entities.Category;
import com.dextra.mentoria.products.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CategoryRespositoryTests {

    @Autowired
    private CategoryRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalCategories;

    @BeforeEach
    void setUp() throws Exception {
        this.existingId = 1L;
        this.nonExistingId = Long.MAX_VALUE;
        this.countTotalCategories = 4L;
    }

    @Test
    public void deleteShouldDeleteCategoryWhenIdExists(){
        repository.deleteById(this.existingId);
        Optional<Category> res = repository.findById(this.existingId);
        assertFalse(res.isPresent());
    }

    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist(){
        assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(this.nonExistingId);
        });
    }

    @Test
    public void  createShouldInsertWithAutoincrementNewCategoryWhenIdIsNull(){
        Category category = Factory.createCategory();
        category.setId(null);

        category = repository.save(category);

        assertNotNull(category.getId());
        assertEquals(countTotalCategories + 1, category.getId());
    }

    @Test
    public void findByIdShouldReturnAnNonEmptyOptionalWhenIdExists(){
        Optional<Category> res = repository.findById(this.existingId);
        assertTrue(res.isPresent());
    }

    @Test
    public void findByIdShouldReturnAnEmptyOptionalWhenIdDoesNotExists(){
        Optional<Category> res = repository.findById(this.nonExistingId);
        assertTrue(res.isEmpty());
    }

}
