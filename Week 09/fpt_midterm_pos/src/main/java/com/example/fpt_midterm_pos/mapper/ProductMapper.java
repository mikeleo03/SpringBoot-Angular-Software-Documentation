package com.example.fpt_midterm_pos.mapper;

import com.example.fpt_midterm_pos.data.model.Product;
import com.example.fpt_midterm_pos.dto.ProductDTO;
import com.example.fpt_midterm_pos.dto.ProductSaveDTO;
import com.example.fpt_midterm_pos.dto.ProductShowDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    // Product - ProductDTO
    ProductDTO toProductDTO(Product product);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toProduct(ProductDTO productDTO);

    // Product - ProductShowDTO
    ProductShowDTO toShowDTO(Product product);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    Product toProduct(ProductShowDTO productShowDTO);

    // Product - ProductSaveDTO
    ProductSaveDTO toProductSaveDTO(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toProduct(ProductSaveDTO productSaveDTO);

    // List of Product - List of ProductDTO
    List<ProductDTO> toProductDTOList(List<Product> products);

    // List of Product - List of ProductSaveDTO
    List<Product> toProductList(List<ProductSaveDTO> productSaveDTOs);
}