package com.api_ecommerce.e_commerce.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.api_ecommerce.e_commerce.dto.category.CategoryDTO;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.exceptions.ConflictException;
import com.api_ecommerce.e_commerce.repository.CategoryRepository;
import com.api_ecommerce.e_commerce.repository.ProductRepository;
import com.api_ecommerce.e_commerce.service.CategoryService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class CategoryServiceTest {
	
	@InjectMocks
	private CategoryService categoryService;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	@Mock
	private ProductRepository productRepository;
	
	private Category category;
	private Category categoryWithProducts;
	
	@BeforeEach
	public void setUp() {
		category = CategoryCreator.categoryWithProducts();
		categoryWithProducts = CategoryCreator.categoryWithProducts();
	}
	
	@Test
	public void createCategorySucess() {
		CategoryDTO dto = new CategoryDTO(category.getName());
		
		when(categoryRepository.findByName(dto.name())).thenReturn(Optional.empty());
		
		categoryService.createCategory(dto);
	}
	
	@Test
	public void createCategoryRepeatedName() {
		CategoryDTO dto = new CategoryDTO(category.getName());
		
		when(categoryRepository.findByName(dto.name())).thenReturn(Optional.of(new Category()));
		
		assertThrows(ConflictException.class, () -> categoryService.createCategory(dto));
	}
	
	@Test
	public void updateCategorySucess() {
		ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);
		CategoryDTO dto = new CategoryDTO("New Name");
		
		when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
		when(categoryRepository.findByName(dto.name())).thenReturn(Optional.empty());
		
		categoryService.updateCategory(category.getId(), dto);
		
		verify(categoryRepository).save(categoryArgumentCaptor.capture());
		
		assertEquals(dto.name(), categoryArgumentCaptor.getValue().getName());
	}
	
	@Test
	public void updateCategoryRepeatedName() {		
		CategoryDTO dto = new CategoryDTO("New Name");
		Category repeatedCategory = Category.builder()
										.id(Long.MAX_VALUE)
										.name("Repeated")
										.build();
		
		when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
		when(categoryRepository.findByName(dto.name())).thenReturn(Optional.of(repeatedCategory));		
		
		assertThrows(ConflictException.class, () -> categoryService.updateCategory(category.getId(),dto));
	}
	
	@Test
	public void deleteCategorySucess() {
		when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(categoryWithProducts));
		
		categoryService.deleteCategory(categoryWithProducts.getId());
		
		verify(categoryRepository, atLeastOnce()).delete(categoryWithProducts);
	}
}
