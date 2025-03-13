package com.trymad.product_service.web.controller;

import org.springframework.web.bind.annotation.RestController;

import com.trymad.product_service.service.ProductService;
import com.trymad.product_service.web.dto.ProductCreateDto;
import com.trymad.product_service.web.dto.ProductDto;
import com.trymad.product_service.web.dto.ProductListDto;
import com.trymad.product_service.web.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

	@GetMapping
	public List<ProductDto> get(@RequestParam List<Long> ids) {
		return productMapper.toDto(productService.getAll(ids));
	}

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

	@PutMapping("{id}/change-quantity")
	public ProductDto inrcease(
		@PathVariable Long id, 
		@RequestParam int count) {
		return productMapper.toDto(productService.changeQuantity(id, count));
	}

	@PutMapping("change-quantity")
	public List<ProductDto> changeAll(@RequestBody Set<ProductListDto> products) {
		return productMapper.toDto(productService.changeAll(products));
	}

	@DeleteMapping("{id}")
	public void delete(@PathVariable Long id) {
		productService.delete(id);
	}
	
}
