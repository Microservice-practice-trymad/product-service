package com.trymad.product_service.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trymad.product_service.entity.Product;
import com.trymad.product_service.repository.ProductRepository;
import com.trymad.product_service.web.dto.ProductCreateDto;
import com.trymad.product_service.web.dto.ProductListDto;
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

	// TODO add custom exception for this case
	public List<Product> getAll(List<Long> ids) {
		final List<Product> products = productRepository.findAllById(ids);
		if(products.size() != ids.size()) throw new EntityNotFoundException("Not all id was founded");

		return products;
	}

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

	public Product changeQuantity(Long id, int value) {
		final Product product = productService.get(id);
		final int newCount = product.getCount() + value;
		if(newCount < 0) {
			throw new IllegalArgumentException("The quantity of products cannot be less than 0");
		}

		product.setCount(newCount);

		return productRepository.save(product);
	}

	public List<Product> changeAll(Set<ProductListDto> productList) {
		final List<Product> changedProducts = new ArrayList<>();
		productList.forEach(currProductList -> {
			final Product product = productService.changeQuantity(currProductList.id(), currProductList.count());
			changedProducts.add(product);
		});

		return changedProducts;
	}

	public void delete(Long id) {
		productRepository.deleteById(id);
	}


}
