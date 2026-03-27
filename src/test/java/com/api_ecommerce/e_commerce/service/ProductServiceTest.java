package com.api_ecommerce.e_commerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.api_ecommerce.e_commerce.dto.product.ProductRequest;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.enums.ProductStatus;
import com.api_ecommerce.e_commerce.exceptions.AlreadyExistsException;
import com.api_ecommerce.e_commerce.repository.CategoryRepository;
import com.api_ecommerce.e_commerce.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class ProductServiceTest {
	
	@InjectMocks
	private ProductService productService;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private CategoryRepository categoryRepository;
	/*
	 * 
	 * 
	 * Category category = findCategoryById(productRequest.categoryId());
	 * 
	 * Product product = new Product(productRequest.name(),
	 * productRequest.description(), productRequest.quantity(),
	 * productRequest.price(), category, ProductStatus.DISPONIVEL);
	 * 
	 * validateNameOfProduct(productRequest, product);
	 * 
	 * saveProduct(product);
	 */
	
	@Test
	public void createProductSucess() {
		ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
		
		Category category = new Category("Categoria");
		ProductRequest dto = new ProductRequest("name", "description", new BigDecimal(100), 1L, 100, ProductStatus.INDISPONIVEL);
		
		when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
		when(productRepository.findByName(any())).thenReturn(Optional.empty());
		
		productService.createProduct(dto);
		
		verify(productRepository).save(productCaptor.capture());
		
		assertNotNull(productCaptor.getValue());
		assertEquals(ProductStatus.DISPONIVEL, productCaptor.getValue().getStatus());
	}
	
	@Test
	public void createProductWithNameRepeated() {		
		Category category = new Category("Categoria");
		ProductRequest dto = new ProductRequest("name", "description", new BigDecimal(100), 1L, 100, ProductStatus.INDISPONIVEL);
		
		when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
		when(productRepository.findByName("name")).thenReturn(Optional.of(new Product()));
		
		assertThrows(AlreadyExistsException.class, () -> productService.createProduct(dto));
	}
	
	@Test
	public void updateProductSucess() {
		ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
		
		Category category1 = new Category("Category 1");
		category1.setId(1L);
		
		Category category2 = new Category("Category 2");
		category2.setId(2L);
		
		Product productBeforeUpdate = new Product("Product", "Description", 100, new BigDecimal(50), category1, ProductStatus.DISPONIVEL);
		ProductRequest dto = new ProductRequest("dto", "description updated", new BigDecimal(100), 2L, 100, ProductStatus.INDISPONIVEL);
		
		when(productRepository.findById(1L)).thenReturn(Optional.of(productBeforeUpdate));
		when(categoryRepository.findById(any())).thenReturn(Optional.of(category2));
		when(productRepository.findByName(any())).thenReturn(Optional.empty());
		
		productService.editProduct(1L,dto);
		
		verify(productRepository).save(productCaptor.capture());
		
		Product productAfterUpdate = productCaptor.getValue();
		
		assertEquals(productAfterUpdate.getName(), dto.name());
		assertEquals(productAfterUpdate.getDescricao(), dto.description());
		assertEquals(productAfterUpdate.getPrice(), dto.price());
		assertEquals(productAfterUpdate.getCategory().getId(), dto.categoryId());
		assertEquals(productAfterUpdate.getStatus(), dto.status());
	}
	
	@Test
	public void updateProductWithNameRepeated() {		
		Category category1 = new Category("Category 1");
		category1.setId(1L);
		
		Category category2 = new Category("Category 2");
		category2.setId(2L);
		
		Product productBeforeUpdate = new Product("Product", "Description", 100, new BigDecimal(50), category1, ProductStatus.DISPONIVEL);
		productBeforeUpdate.setId(2L);
		
		ProductRequest dto = new ProductRequest("dto", "description updated", new BigDecimal(100), 2L, 100, ProductStatus.INDISPONIVEL);
		
		Product productRepeated = new Product();
		productRepeated.setId(1L);
		
		when(productRepository.findById(1L)).thenReturn(Optional.of(productBeforeUpdate));
		when(productRepository.findByName("dto")).thenReturn(Optional.of(productRepeated));
		
		assertThrows(AlreadyExistsException.class, () -> productService.editProduct(1L,dto));
	}
}
