package com.api_ecommerce.e_commerce.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.api_ecommerce.e_commerce.dto.user.RegisterSellerRequest;
import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.Role;
import com.api_ecommerce.e_commerce.entity.Seller;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.exceptions.AlreadyExistsException;
import com.api_ecommerce.e_commerce.repository.BuyerRepository;
import com.api_ecommerce.e_commerce.repository.CartRepository;
import com.api_ecommerce.e_commerce.repository.RoleRepository;
import com.api_ecommerce.e_commerce.repository.SellerRepository;
import com.api_ecommerce.e_commerce.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class AuthServiceTest {
	
	@InjectMocks
	private AuthService authService;
	
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
	
	@Mock
	private CartRepository cartRepository;
	
	@Test
	public void registerBuyerSucesso() {
		ArgumentCaptor<Buyer> captorBuyer = ArgumentCaptor.forClass(Buyer.class);
		ArgumentCaptor<User> captorUser = ArgumentCaptor.forClass(User.class);
		ArgumentCaptor<Cart> captorCart = ArgumentCaptor.forClass(Cart.class);
		
		RegisterBuyerRequest dto = new RegisterBuyerRequest("username", "senha", "nome", "11237419484", LocalDate.now().minusYears(20), "endereço");
		Role role =  new Role(Role.Value.BUYER.name());
		
		when(roleRepository.findRoleByName(Role.Value.BUYER.name())).thenReturn(Optional.of(role));
		
		when(userRepository.findByUsername(dto.username())).thenReturn(Optional.empty());
		
		when(userRepository.save(any())).thenAnswer(Invocation -> Invocation.getArgument(0, User.class));
		
		when(buyerRepository.findByCpf(dto.cpf())).thenReturn(Optional.empty());
		
		authService.registerBuyer(dto);
		
		verify(userRepository).save(captorUser.capture());
		verify(buyerRepository).save(captorBuyer.capture());
		verify(cartRepository).save(captorCart.capture());
		
		assertNotNull(captorUser.getValue());
		assertNotNull(captorBuyer.getValue());
		assertNotNull(captorCart.getValue());
	}
	
	@Test
	public void registerBuyerCPFRepetido() {
		RegisterBuyerRequest dto = new RegisterBuyerRequest("username", "senha", "nome", "11237419484", LocalDate.now().minusYears(20), "endereço");
		Role role =  new Role(Role.Value.BUYER.name());
		
		when(roleRepository.findRoleByName(Role.Value.BUYER.name())).thenReturn(Optional.of(role));
		
		when(userRepository.findByUsername(dto.username())).thenReturn(Optional.empty());
		
		when(userRepository.save(any())).thenAnswer(Invocation -> Invocation.getArgument(0, User.class));
		
		when(buyerRepository.findByCpf(dto.cpf())).thenReturn(Optional.of(new Buyer()));
		
		assertThrows(AlreadyExistsException.class, () -> {
			authService.registerBuyer(dto);
			});
	}
	
	@Test
	public void registerSellerSucesso() {
		ArgumentCaptor<Seller> captorSeller = ArgumentCaptor.forClass(Seller.class);
		ArgumentCaptor<User> captorUser = ArgumentCaptor.forClass(User.class);
		
		RegisterSellerRequest dto = new RegisterSellerRequest("username", "senha", "nome", "05.229.773/0001-68");
		Role role =  new Role(Role.Value.SELLER.name());
		
		when(roleRepository.findRoleByName(Role.Value.SELLER.name())).thenReturn(Optional.of(role));
		
		when(userRepository.findByUsername(dto.username())).thenReturn(Optional.empty());
		
		when(userRepository.save(any())).thenAnswer(Invocation -> Invocation.getArgument(0, User.class));
		
		when(sellerRepository.findByCnpj(dto.cnpj())).thenReturn(Optional.empty());
		
		authService.registerSeller(dto);
		
		verify(userRepository).save(captorUser.capture());
		verify(sellerRepository).save(captorSeller.capture());
		
		assertNotNull(captorUser.getValue());
		assertNotNull(captorSeller.getValue());
	}
	
	@Test
	public void registerSellerCNPJRepetido() {
		ArgumentCaptor<Seller> captorSeller = ArgumentCaptor.forClass(Seller.class);
		ArgumentCaptor<User> captorUser = ArgumentCaptor.forClass(User.class);
		
		RegisterSellerRequest dto = new RegisterSellerRequest("username", "senha", "nome", "05.229.773/0001-68");
		Role role =  new Role(Role.Value.SELLER.name());
		
		when(roleRepository.findRoleByName(Role.Value.SELLER.name())).thenReturn(Optional.of(role));
		
		when(userRepository.findByUsername(dto.username())).thenReturn(Optional.empty());
		
		when(userRepository.save(any())).thenAnswer(Invocation -> Invocation.getArgument(0, User.class));
		
		when(sellerRepository.findByCnpj(dto.cnpj())).thenReturn(Optional.of(new Seller()));
		
		assertThrows(AlreadyExistsException.class, () -> {
			authService.registerSeller(dto);
			});
	}
}
