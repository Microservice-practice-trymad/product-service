package com.trymad.product_service.service;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trymad.product_service.entity.Product;
import com.trymad.product_service.repository.ProductRepository;
import com.trymad.product_service.web.dto.ProductCreateDto;
import com.trymad.product_service.web.mapper.ProductMapper;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__(@Lazy))

@Service
@Transactional
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductMapper productMapper;
	@Lazy private final ProductService productService;

	@Transactional(readOnly = true)
	public Product get(Long id) {
		return productRepository.findById(id).orElseThrow(
			() -> new EntityNotFoundException("Product with id " + id + " not found")
		);
	}

	public Product create(ProductCreateDto createDto) {
		if(productRepository.existsByName(createDto.name())) {
			throw new EntityExistsException("Product with name " + createDto.name() + " already exists");
		}

		final Product product = productMapper.toEntity(createDto);
		final LocalDateTime now = LocalDateTime.now();
		product.setCreatedAt(now);
		product.setUpdatedAt(now);
		
		return productRepository.save(product);
	}

	public Product update(ProductCreateDto updateDto, Long id) {
		if(!productRepository.existsById(id)) {
			throw new EntityNotFoundException("Product with id " + id + " not found");
		}

		if(updateDto.name() != null && productRepository.existsByName(updateDto.name())) {
			throw new EntityExistsException("Product with name " + updateDto.name() + " already exists");
		}
		
		final Product product = productService.get(id);
		if(updateDto.name() != null) product.setName(updateDto.name());
		if(updateDto.price() != null) product.setPrice(updateDto.price());
		if(updateDto.count() != null) product.setCount(updateDto.count());
		product.setUpdatedAt(LocalDateTime.now());

		return productRepository.save(product);
	}

	public void delete(Long id) {
		productRepository.deleteById(id);
	}


}
