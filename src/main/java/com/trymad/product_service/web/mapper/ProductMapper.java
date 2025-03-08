package com.trymad.product_service.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.trymad.product_service.entity.Product;
import com.trymad.product_service.web.dto.ProductCreateDto;
import com.trymad.product_service.web.dto.ProductDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
	
	Product toEntity(ProductDto dto);

	Product toEntity(ProductCreateDto dto);

	ProductDto toDto(Product product);

}
