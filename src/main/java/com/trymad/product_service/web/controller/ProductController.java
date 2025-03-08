package com.trymad.product_service.web.controller;

import org.springframework.web.bind.annotation.RestController;

import com.trymad.product_service.service.ProductService;
import com.trymad.product_service.web.dto.ProductCreateDto;
import com.trymad.product_service.web.dto.ProductDto;
import com.trymad.product_service.web.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor

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

	@DeleteMapping("{id}")
	public void delete(@PathVariable Long id) {
		productService.delete(id);
	}
	
}
