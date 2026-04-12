package com.api_ecommerce.e_commerce.service.auth;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.api_ecommerce.e_commerce.dto.user.RegisterBuyerRequest;
import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.exceptions.ConflictException;
import com.api_ecommerce.e_commerce.repository.BuyerRepository;
import com.api_ecommerce.e_commerce.repository.CartRepository;
import com.api_ecommerce.e_commerce.repository.RoleRepository;
import com.api_ecommerce.e_commerce.repository.UserRepository;
import com.api_ecommerce.e_commerce.service.AuthService;

@SpringBootTest
@Transactional
public class AuthServiceIntegrationTest {
	@Autowired
	private AuthService authService;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private BuyerRepository buyerRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private RegisterBuyerRequest dto;
	
	@BeforeEach
	public void setUp() {
		dto = new RegisterBuyerRequest("username", "password","name", 
				"11237419484",LocalDate.now().minusYears(20) , "adress");
	}
	
	@Test
	public void registerBuyerSucess() {
		authService.registerBuyer(dto);
		
		User user = authService.findUserByUsername(dto.username());
		Buyer buyer = buyerRepository.findByUser(user).get();
		Cart cart = cartRepository.findByBuyerId(buyer.getId()).get();
		
		assertNotNull(user);
		assertNotNull(cart);
		assertNotNull(buyer);
		
	}
	
	@Test
	public void registerBuyerButCpfRepeated() {
		RegisterBuyerRequest anotherDto = new RegisterBuyerRequest("username random", 
				"password","name", "11237419484",LocalDate.now().minusYears(20) , "adress");
		
		authService.registerBuyer(anotherDto);
		
		assertThrows(ConflictException.class,() -> authService.registerBuyer(dto));
		
		Optional<User> user = userRepository.findByUsername(dto.username());
		
		assertTrue(user.isEmpty());	
	}
}
