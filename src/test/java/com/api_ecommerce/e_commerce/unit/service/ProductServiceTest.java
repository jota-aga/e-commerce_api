package com.api_ecommerce.e_commerce.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.api_ecommerce.e_commerce.creator.CategoryCreator;
import com.api_ecommerce.e_commerce.creator.ProductCreator;
import com.api_ecommerce.e_commerce.dto.product.ProductRequest;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.enums.ProductStatus;
import com.api_ecommerce.e_commerce.exceptions.ConflictException;
import com.api_ecommerce.e_commerce.exceptions.NotFoundException;
import com.api_ecommerce.e_commerce.repository.CartItemRepository;
import com.api_ecommerce.e_commerce.repository.CategoryRepository;
import com.api_ecommerce.e_commerce.repository.OrderItemRepository;
import com.api_ecommerce.e_commerce.repository.ProductRepository;
import com.api_ecommerce.e_commerce.service.ProductService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class ProductServiceTest {
	
	@InjectMocks
	private ProductService productService;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	@Mock
	private CartItemRepository cartItemRespositoy;
	
	@Mock
	private OrderItemRepository orderItemRepository;
	
	
	private Category category;
	private ProductRequest dto;
	
	@BeforeEach
	public void setUp() {
		category = CategoryCreator.simpleCategory();
		dto = ProductCreator.availableProductRequest();
	}
	
	@Test
	public void createProductSuccess() {
		ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
				
		when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
		when(productRepository.findByName(dto.name())).thenReturn(Optional.empty());
		
		productService.createProduct(dto);
		
		verify(productRepository).save(productCaptor.capture());
		
		assertNotNull(productCaptor.getValue());
		assertEquals(ProductStatus.AVAILABLE, productCaptor.getValue().getStatus());
	}
	
	@Test
	public void createProductWithNameRepeated() {		
		
		when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
		when(productRepository.findByName(dto.name())).thenReturn(Optional.of(new Product()));
		
		assertThrows(ConflictException.class, () -> productService.createProduct(dto));
		
		verify(productRepository, never()).save(any());
	}
	
	@Test
	public void updateProductSuccess() {
		ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
		
		Product productBeforeUpdate = Product.builder()
											 .id(2L)
											 .name("Product Before")
											 .description("Description Before")
											 .price(new BigDecimal(50))
											 .quantity(100)
											 .status(ProductStatus.AVAILABLE)
											 .category(category)
											 .build();
		Category newCategory = Category.builder()
									   .id(1L)
									   .name("New Category")
									   .build();
		
		when(productRepository.findById(1L)).thenReturn(Optional.of(productBeforeUpdate));
		when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(newCategory));
		when(productRepository.findByName(dto.name())).thenReturn(Optional.empty());
		
		productService.updateProduct(1L,dto);
		
		verify(productRepository).save(productCaptor.capture());
		
		Product productAfterUpdate = productCaptor.getValue();
		
		assertEquals(productAfterUpdate.getName(), dto.name());
		assertEquals(productAfterUpdate.getDescription(), dto.description());
		assertEquals(productAfterUpdate.getPrice(), dto.price());
		assertEquals(productAfterUpdate.getCategory().getId(), dto.categoryId());
		assertEquals(productAfterUpdate.getStatus(), dto.status());
	}
	
	@Test
	public void updateProductWithNameRepeated() {		
		Product productBeforeUpdate = Product.builder()
											 .id(2L)
											 .name("Product Before")
											 .description("Description Before")
											 .price(new BigDecimal(50))
											 .quantity(100)
											 .status(ProductStatus.AVAILABLE)
											 .category(category)
											 .build();
				
		Product productRepeated = Product.builder()
										 .id(Long.MAX_VALUE)
										 .build();
		
		when(productRepository.findById(productBeforeUpdate.getId())).thenReturn(Optional.of(productBeforeUpdate));
		when(productRepository.findByName(dto.name())).thenReturn(Optional.of(productRepeated));
		
		assertThrows(ConflictException.class, () -> productService.updateProduct(productBeforeUpdate.getId(),dto));
		
		verify(productRepository, never()).save(any());
	}
	
	@Test
	public void updateProductWhenNotFound() {		
		Product productBeforeUpdate = Product.builder()
											 .id(2L)
											 .name("Product Before")
											 .description("Description Before")
											 .price(new BigDecimal(50))
											 .quantity(100)
											 .status(ProductStatus.AVAILABLE)
											 .category(category)
											 .build();
				
		
		when(productRepository.findById(productBeforeUpdate.getId())).thenReturn(Optional.empty());
		
		assertThrows(NotFoundException.class, () -> productService.updateProduct(productBeforeUpdate.getId(),dto));
		
		verify(productRepository, never()).save(any());
	}
}
