package com.dextra.mentoria.products.services;

import com.dextra.mentoria.products.dto.request.ProductRequest;
import com.dextra.mentoria.products.dto.response.ProductResponse;
import com.dextra.mentoria.products.entities.Category;
import com.dextra.mentoria.products.entities.Product;
import com.dextra.mentoria.products.repositories.ProductRepository;
import com.dextra.mentoria.products.services.exceptions.DataIntegrityException;
import com.dextra.mentoria.products.services.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService implements IProductService {

    final ProductRepository repository;

    final ICategoryService ICategoryService;

    final
    ModelMapper modelMapper;

    public ProductService(ProductRepository repository, ICategoryService ICategoryService, ModelMapper modelMapper) {
        this.repository = repository;
        this.ICategoryService = ICategoryService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductResponse create(ProductRequest request) {
        Product product = modelMapper.map(request, Product.class);
        return modelMapper.map(repository.save(product), ProductResponse.class);
    }

    @Override
    @Transactional
    public void update(Long id, ProductRequest dto) {
        Product product = this.find(id);
        this.copyDTOToEntity(dto, product);
        repository.save(product);
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
    public ProductResponse findById(Long id) {
        return  modelMapper.map(this.find(id), ProductResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> findAllPaged(Pageable pageable) {
        return this.repository.findAll(pageable).map(product -> modelMapper.map(product, ProductResponse.class));
    }

    @Override
    public Product find(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new NotFoundException("Entity not found."));
    }

    private void copyDTOToEntity(ProductRequest dto, Product entity) {
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.getCategories().clear();
        dto.getCategories().forEach(categoryDTO -> {
            Category categoryEntity = this.ICategoryService.find(categoryDTO.getId());
            entity.addCategory(categoryEntity);
        });
    }

}
