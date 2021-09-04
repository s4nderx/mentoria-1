package com.dextra.mentoria.products.services;

import com.dextra.mentoria.products.dto.ProductDTO;
import com.dextra.mentoria.products.entities.Category;
import com.dextra.mentoria.products.entities.Product;
import com.dextra.mentoria.products.repositories.ProductRepository;
import com.dextra.mentoria.products.services.exceptions.DataIntegrityException;
import com.dextra.mentoria.products.services.exceptions.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    final ProductRepository repository;

    final CategoryService categoryService;

    public ProductServiceImpl(ProductRepository repository, CategoryService categoryService) {
        this.repository = repository;
        this.categoryService = categoryService;
    }

    @Override
    public ProductDTO create(ProductDTO dto) {
        Product product = new Product();
        this.copyDTOToEntity(dto, product);
        product = repository.save(product);
        return new ProductDTO(product);
    }

    @Override
    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        Product product = this.find(id);
        this.copyDTOToEntity(dto, product);
        product = repository.save(product);
        return new ProductDTO(product);
    }

    @Override
    public void delete(Long id) {
        try {
            this.repository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new NotFoundException("Id not found " + id);
        } catch (DataIntegrityViolationException e){
            throw new DataIntegrityException("Integrity violation");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product entity = this.find(id);
        return new ProductDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        return this.repository.findAll(pageable).map(ProductDTO::new);
    }

    @Override
    public Product find(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new NotFoundException("Entity not found."));
    }

    private void copyDTOToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.getCategories().clear();
        dto.getCategories().forEach(categoryDTO -> {
            Category categoryEntity = this.categoryService.find(categoryDTO.getId());
            entity.addCategory(categoryEntity);
        });
    }

}
