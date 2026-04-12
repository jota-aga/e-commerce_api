package com.api_ecommerce.e_commerce.service.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.api_ecommerce.e_commerce.creator.RoleCreator;
import com.api_ecommerce.e_commerce.creator.UserCreator;
import com.api_ecommerce.e_commerce.dto.user.LoginRequest;
import com.api_ecommerce.e_commerce.dto.user.LoginResponse;
import com.api_ecommerce.e_commerce.dto.user.RegisterBuyerRequest;
import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.Role;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.exceptions.ConflictException;
import com.api_ecommerce.e_commerce.exceptions.NotAuthorizedException;
import com.api_ecommerce.e_commerce.repository.BuyerRepository;
import com.api_ecommerce.e_commerce.repository.CartRepository;
import com.api_ecommerce.e_commerce.repository.RoleRepository;
import com.api_ecommerce.e_commerce.repository.UserRepository;
import com.api_ecommerce.e_commerce.service.AuthService;
import com.api_ecommerce.e_commerce.service.TokenService;

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
	private CartRepository cartRepository;
	
	private RegisterBuyerRequest dto;
	private Role role;
	
	@BeforeEach
	public void setUp() {
		dto = new RegisterBuyerRequest("username", "senha", "nome", "11237419484", LocalDate.now().minusYears(20), "endereço");
		role =  RoleCreator.roleBuyer();
	}
	
	@Test
	public void registerBuyerSucesso() {
		ArgumentCaptor<Buyer> captorBuyer = ArgumentCaptor.forClass(Buyer.class);
		ArgumentCaptor<Cart> captorCart = ArgumentCaptor.forClass(Cart.class);
		
		
		
		when(roleRepository.findRoleByName(Role.Value.BUYER.name())).thenReturn(Optional.of(role));
		
		when(userRepository.findByUsername(dto.username())).thenReturn(Optional.empty());
				
		when(buyerRepository.findByCpf(dto.cpf())).thenReturn(Optional.empty());
		
		authService.registerBuyer(dto);
		
		verify(buyerRepository).save(captorBuyer.capture());
		verify(cartRepository).save(captorCart.capture());
		
		assertNotNull(captorBuyer.getValue());
		assertNotNull(captorCart.getValue());
	}
	
	@Test
	public void registerBuyerUsernameRepetido() {				
		when(userRepository.findByUsername(dto.username())).thenReturn(Optional.of(new User()));		
		when(roleRepository.findRoleByName(Role.Value.BUYER.name())).thenReturn(Optional.of(role));
		
		assertThrows(ConflictException.class, () -> authService.registerBuyer(dto));
		
		verify(userRepository, never()).save(any());
		verify(cartRepository, never()).save(any());
		verify(buyerRepository, never()).save(any());
	}
	
	@Test
	public void registerBuyerCPFRepetido() {		
		when(roleRepository.findRoleByName(Role.Value.BUYER.name())).thenReturn(Optional.of(role));
		
		when(userRepository.findByUsername(dto.username())).thenReturn(Optional.empty());
				
		when(buyerRepository.findByCpf(dto.cpf())).thenReturn(Optional.of(new Buyer()));
		
		assertThrows(ConflictException.class, () -> {
			authService.registerBuyer(dto);
			});
		
		verify(userRepository, never()).save(any());
		verify(cartRepository, never()).save(any());
		verify(buyerRepository, never()).save(any());
	}
	
	@Test
	public void doLoginBuyerSucess() {
		User user = UserCreator.userBuyer();
		LoginRequest dto = new LoginRequest(user.getUsername(), user.getPassword());
		
		when(userRepository.findByUsername(dto.username())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(dto.password(), user.getPassword())).thenReturn(true);
		when(tokenService.generateToken(user)).thenReturn("token");
		
		LoginResponse response = authService.doLogin(dto);
		
		assertEquals(response.acessToken(), "token");
	}
	
	@Test
	public void doLoginAdminSucess() {
		User user = UserCreator.userAdmin();
		LoginRequest dto = new LoginRequest(user.getUsername(), user.getPassword());
		
		when(userRepository.findByUsername(dto.username())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(dto.password(), user.getPassword())).thenReturn(true);
		when(tokenService.generateToken(user)).thenReturn("token");
		
		LoginResponse response = authService.doLogin(dto);
		
		assertEquals(response.acessToken(), "token");
	}
	
	@Test
	public void doLoginBuyerUsernameNotFound() {
		User user = UserCreator.userBuyer();
		LoginRequest dto = new LoginRequest(user.getUsername(), user.getPassword());
		
		when(userRepository.findByUsername(dto.username())).thenReturn(Optional.empty());
		
		assertThrows(NotAuthorizedException.class, () -> authService.doLogin(dto));
	}
	
	@Test
	public void doLoginAdminUsernameNotFound() {
		User user = UserCreator.userAdmin();
		LoginRequest dto = new LoginRequest(user.getUsername(), user.getPassword());
		
		when(userRepository.findByUsername(dto.username())).thenReturn(Optional.empty());
		
		assertThrows(NotAuthorizedException.class, () -> authService.doLogin(dto));
	}
	
	@Test
	public void doLoginBuyerPasswordDoesntMatche() {
		User user = UserCreator.userBuyer();
		LoginRequest dto = new LoginRequest(user.getUsername(), user.getPassword());
		
		when(userRepository.findByUsername(dto.username())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(dto.password(), user.getPassword())).thenReturn(false);
		
		assertThrows(NotAuthorizedException.class, () -> authService.doLogin(dto));
	}
	
	@Test
	public void doLoginAdminPasswordDoesntMatche() {
		User user = UserCreator.userAdmin();
		LoginRequest dto = new LoginRequest(user.getUsername(), user.getPassword());
		
		when(userRepository.findByUsername(dto.username())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(dto.password(), user.getPassword())).thenReturn(false);
		
		assertThrows(NotAuthorizedException.class, () -> authService.doLogin(dto));
	}
}
