package com.api_ecommerce.e_commerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.api_ecommerce.e_commerce.dto.user.RegisterBuyerRequest;
import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Role;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.repository.BuyerRepository;
import com.api_ecommerce.e_commerce.repository.RoleRepository;
import com.api_ecommerce.e_commerce.repository.SellerRepository;
import com.api_ecommerce.e_commerce.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class UserServiceTest {
	
	@InjectMocks
	private UserService userService;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private TokenService tokenService;
	
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	
	@Mock
	private RoleRepository roleRepository;
	
	@Mock
	private BuyerRepository buyerRepository;
	
	@Mock
	private SellerRepository sellerRepository;
	
	@Test
	public void registerBuyerSucesso() {
		ArgumentCaptor<Buyer> captorBuyer = ArgumentCaptor.forClass(Buyer.class);
		ArgumentCaptor<User> captorUser = ArgumentCaptor.forClass(User.class);
		
		RegisterBuyerRequest dto = new RegisterBuyerRequest("username", "senha", "nome", "11237419484", LocalDate.now().minusYears(20), "endereço");
		Role role =  new Role(Role.Value.BUYER.name());
		
		when(roleRepository.findRoleByName(Role.Value.BUYER.name())).thenReturn(Optional.of(role));
		
		when(userRepository.findByUsername(dto.username())).thenReturn(Optional.empty());
		
		when(userRepository.save(any())).thenAnswer(Invocation -> Invocation.getArgument(0, User.class));
		
		when(buyerRepository.findByCpf(dto.cpf())).thenReturn(Optional.empty());
		
		userService.registerBuyer(dto);
		
		verify(userRepository).save(captorUser.capture());
		verify(buyerRepository).save(captorBuyer.capture());
		
		assertEquals(captorBuyer.getValue().getCpf(), dto.cpf());
		assertEquals(captorUser.getValue().getUsername(), dto.username());
	}
}
