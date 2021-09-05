package com.dextra.mentoria.products.services;

import com.dextra.mentoria.products.dto.CategoryDTO;
import com.dextra.mentoria.products.dto.ProductDTO;
import com.dextra.mentoria.products.entities.Category;
import com.dextra.mentoria.products.repositories.CategoryRepository;
import com.dextra.mentoria.products.services.exceptions.DataIntegrityException;
import com.dextra.mentoria.products.services.exceptions.NotFoundException;
import com.dextra.mentoria.products.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

    @InjectMocks
    private CategoryServiceImpl service;

    @Mock
    private CategoryRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private Category category;

    @BeforeEach
    void setUp() {
        this.existingId = 1L;
        this.nonExistingId = Long.MAX_VALUE;
        this.dependentId = 5L;
        this.category = Factory.createCategory();
        PageImpl<Category> page = new PageImpl<>(List.of(this.category));

        when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        when(repository.save(any())).thenReturn(this.category);
        when(repository.findById(this.existingId)).thenReturn(Optional.of(this.category));
        when(repository.findById(this.nonExistingId)).thenReturn(Optional.empty());
        doNothing().when(repository).deleteById(this.existingId);
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(this.nonExistingId);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(this.dependentId);

    }

    @Test
    public void findAllPagedShouldReturnPage(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryDTO> res = service.findAllPaged(pageable);
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
    public void findShouldReturnAnCategoryWhenIdExist() {
        Category category = this.service.find(this.existingId);
        assertNotNull(category);
        assertEquals(category, this.category);
        verify(repository, times(1)).findById(this.existingId);
    }

    @Test
    public void findShouldThrowNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(NotFoundException.class, () -> this.service.find(this.nonExistingId));
        verify(repository, times(1)).findById(this.nonExistingId);
    }

    @Test
    public void findByIdShouldReturnAnCategoryDtoWhenIdExist() {
        CategoryDTO dto = this.service.findById(this.existingId);
        assertNotNull(dto);
        verify(repository, times(1)).findById(this.existingId);
    }

    @Test
    public void findByIdShouldThrowNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(NotFoundException.class, () -> this.service.findById(this.nonExistingId));
        verify(repository, times(1)).findById(this.nonExistingId);
    }

    @Test
    public void createShouldReturnAnNewCategoryDtoWithIdWhenIdIsNull() {
        CategoryDTO dto = Factory.createCategoryDTO();
        dto.setId(null);
        dto = this.service.create(dto);
        assertNotNull(dto.getId());
        verify(this.repository, times(1)).save(any());
    }

    @Test
    public void updateShouldReturnAnCategoryWhenIdExist() {
        CategoryDTO dto = Factory.createCategoryDTO();
        dto = this.service.update(this.existingId, dto);

        assertNotNull(dto);
        verify(this.repository, times(1)).save(any());
    }

    @Test
    public void updateShouldThrowNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(NotFoundException.class, () -> this.service.update(this.nonExistingId, Factory.createCategoryDTO()));
        verify(spy(this.service), times(1)).find(this.nonExistingId);
    }

    @Test
    public void updateShouldPersistChangesInAnExistingCategory() {
        CategoryDTO dto = Factory.createCategoryDTO();
        dto.setName("TESTS");

        CategoryDTO updatedDTO = this.service.update(this.existingId, dto);

        assertEquals(updatedDTO.getId(), dto.getId());
        assertEquals(updatedDTO.getName(), dto.getName());

        verify(this.repository, times(1)).save(any());
    }

}
