package com.api_ecommerce.e_commerce.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api_ecommerce.e_commerce.dto.user.LoginRequest;
import com.api_ecommerce.e_commerce.dto.user.LoginResponse;
import com.api_ecommerce.e_commerce.dto.user.RegisterBuyerRequest;
import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.Role;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.exceptions.ConflictException;
import com.api_ecommerce.e_commerce.exceptions.NotAuthorizedException;
import com.api_ecommerce.e_commerce.exceptions.NotFoundException;
import com.api_ecommerce.e_commerce.repository.BuyerRepository;
import com.api_ecommerce.e_commerce.repository.CartRepository;
import com.api_ecommerce.e_commerce.repository.RoleRepository;
import com.api_ecommerce.e_commerce.repository.UserRepository;

@Service
public class AuthService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BuyerRepository buyerRepository;

	@Autowired
	private CartRepository cartRepository;
	
	@Transactional
	public void registerBuyer(RegisterBuyerRequest dto) {
		User user = createUser(dto.username(), dto.password(), Role.Value.BUYER.name());
		
		Buyer buyer = Buyer.builder()
						   .name(dto.name())
						   .birthday(dto.birthday())
						   .cpf(dto.cpf())
						   .adress(dto.adress())
						   .user(user)
						   .build();
				
		validateCpfRepeated(buyer);
		buyer = buyerRepository.save(buyer);

		Cart cart = Cart.builder()
				        .buyer(buyer)
				        .build();
		cartRepository.save(cart);
	}
	
	public LoginResponse doLogin(LoginRequest login) {
		User user = findUserByUsername(login.username());
				
		isLoginCorrect(login, user, passwordEncoder);
		
		String token = tokenService.generateToken(user);
		
		return new LoginResponse(token);
	}
	
	public User findUserById(Long id) {
		Optional<User> user = userRepository.findById(id);
		
		return user.orElseThrow(() -> new NotFoundException("User"));
	}
	
	public User findUserByUsername(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		
		return user.orElseThrow(() -> new NotAuthorizedException("Username or password incorrect"));
	}
	
	private void isLoginCorrect(LoginRequest loginRequest, User user, PasswordEncoder passwordEncoder) {
		if(passwordEncoder.matches(loginRequest.password(), user.getPassword()) == false) throw new NotAuthorizedException("Username or password incorrect");
	}
	
	private Role findRoleByName(String roleName) {
		Optional<Role> optionalRole = roleRepository.findRoleByName(roleName);
		
		Role role = optionalRole.orElseThrow(() -> new NotFoundException("Role"));
		
		return role;
	}
	
	private void validateRepeatedUsername(User user) {
		Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
		
		if(optionalUser.isPresent()) {
			if(user.getId() == null || user.getId() == 0) {
				throw new ConflictException("Username already registered");
			}
			else {
				User repeatedUser = optionalUser.get();
				
				if(user.getId() != repeatedUser.getId()) 
					throw new ConflictException("Username already registered");
			}
		}
	}
	
	private void validateCpfRepeated(Buyer buyer) {
		Optional<Buyer> optionalBuyer = buyerRepository.findByCpf(buyer.getCpf());
		
		if(optionalBuyer.isPresent()) {
			if(buyer.getId() == null) throw new ConflictException("CPF already registered");
			
			
			else {
				Buyer repeatedBuyer = optionalBuyer.get();
				
				if(!repeatedBuyer.getId().equals(buyer.getId())) throw new ConflictException("CPF already registered");
			}
		}
	}
	
	private User createUser(String username, String password, String roleName) {
		Role role = findRoleByName(roleName);
		User user = User.builder()
						.username(username)
						.password(password)
						.role(role)
						.build();
		
		validateRepeatedUsername(user);
		
		return user;
	}
}
