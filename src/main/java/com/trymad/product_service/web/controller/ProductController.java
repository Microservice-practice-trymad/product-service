package com.trymad.product_service.web.controller;

import org.springframework.web.bind.annotation.RestController;

import com.trymad.product_service.service.ProductService;
import com.trymad.product_service.web.dto.ProductCreateDto;
import com.trymad.product_service.web.dto.ProductDto;
import com.trymad.product_service.web.mapper.ProductMapper;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor

@Validated
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
	
	private final ProductMapper productMapper;
	private final ProductService productService;

	@GetMapping("{id}")
	public ProductDto getById(@PathVariable Long id) {
		return productMapper.toDto(productService.get(id));
	}

	@PostMapping
	public ProductDto create(@RequestBody ProductCreateDto createDto) {
		return productMapper.toDto(productService.create(createDto));
	}

	@PutMapping("{id}")
	public ProductDto update(@PathVariable Long id, @RequestBody ProductCreateDto updateDto) {
		return productMapper.toDto(productService.update(updateDto, id));
	}

	@PatchMapping("{id}/decrease-quantity")
	public ProductDto decrease(
		@PathVariable Long id, 

		@RequestParam
		@Min(value = 0, message = "count must be at least 0") 
		int count) {
		return productMapper.toDto(productService.changeQuantity(id, count * -1));
	}

	@PatchMapping("{id}/increase-quantity")
	public ProductDto inrcease(
		@PathVariable Long id, 

		@RequestParam
		@Min(value = 0, message = "count must be at least 0") 
		int count) {
		return productMapper.toDto(productService.changeQuantity(id, count));
	}

	@DeleteMapping("{id}")
	public void delete(@PathVariable Long id) {
		productService.delete(id);
	}
	
}
