package com.dextra.mentoria.products.services;

import com.dextra.mentoria.products.dtos.request.ProductRequest;
import com.dextra.mentoria.products.dtos.response.ProductResponse;
import com.dextra.mentoria.products.entities.Category;
import com.dextra.mentoria.products.entities.Product;
import com.dextra.mentoria.products.repositories.ProductRepository;
import com.dextra.mentoria.products.services.exceptions.DataIntegrityException;
import com.dextra.mentoria.products.services.exceptions.NotFoundException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {

    private final ProductRepository repository;

    private final ICategoryService categoryService;

    private final ModelMapper modelMapper;

    private final ObjectMapper objectMapper;

    public ProductService(ProductRepository repository, ICategoryService categoryService, ModelMapper modelMapper, ObjectMapper objectMapper) {
        this.repository = repository;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;

        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.setDateFormat(DateFormat.getDateInstance(DateFormat.LONG));
    }

    @Override
    @Transactional()
    public Product create(ProductRequest request) {
        Product product = modelMapper.map(request, Product.class);
        Set<Long> categoriesIds = product.getCategories().stream().map(Category::getId).collect(Collectors.toSet());
        product.getCategories().clear();
        product.getCategories().addAll(this.categoryService.findAllById(categoriesIds));
        return repository.save(product);
    }

    @Override
    @Transactional
    public void update(Long id, ProductRequest dto) {
        Product product = this.findById(id);
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
    public Page<ProductResponse> findAllPaged(Pageable pageable) {
        return this.repository.findAll(pageable).map(product -> modelMapper.map(product, ProductResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new NotFoundException("Entity not found."));
    }

    @Override
    public Product patchUpdate(Long id, JsonPatch patch) {
        try {
            Product product = this.findById(id);

            this.objectMapper
                    .disable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
                    .enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)
                    .setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));

            JsonNode jsonNode = objectMapper.convertValue(product, JsonNode.class);
            JsonNode patchJsonNode = patch.apply(jsonNode);
            Product productPersist = objectMapper.treeToValue(patchJsonNode, Product.class);
            return this.repository.save(productPersist);
        } catch (JsonPatchException e) {
            System.out.println(e.toString());
            throw new NotFoundException("Id not found " + id);
        } catch (JsonProcessingException e){
            System.out.println(e.toString());
            throw new NotFoundException("Id not found " + id);
        }
    }

    private void copyDTOToEntity(ProductRequest dto, Product entity) {
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.getCategories().clear();
        dto.getCategories().forEach(categoryDTO -> {
            Category categoryEntity = this.categoryService.find(categoryDTO.getId());
            entity.addCategory(categoryEntity);
        });
    }

}
