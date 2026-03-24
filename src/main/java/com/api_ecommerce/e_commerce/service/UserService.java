package com.api_ecommerce.e_commerce.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api_ecommerce.e_commerce.dto.user.LoginRequest;
import com.api_ecommerce.e_commerce.dto.user.LoginResponse;
import com.api_ecommerce.e_commerce.dto.user.RegisterBuyerRequest;
import com.api_ecommerce.e_commerce.dto.user.RegisterRequest;
import com.api_ecommerce.e_commerce.dto.user.RegisterSellerRequest;
import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Role;
import com.api_ecommerce.e_commerce.entity.Seller;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.exceptions.AlreadyExistsException;
import com.api_ecommerce.e_commerce.exceptions.IdNotFoundException;
import com.api_ecommerce.e_commerce.exceptions.RoleNotFoundException;
import com.api_ecommerce.e_commerce.exceptions.UsernameOrPasswordIncorrectException;
import com.api_ecommerce.e_commerce.repository.BuyerRepository;
import com.api_ecommerce.e_commerce.repository.RoleRepository;
import com.api_ecommerce.e_commerce.repository.SellerRepository;
import com.api_ecommerce.e_commerce.repository.UserRepository;

@Service
public class UserService {
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
	private SellerRepository sellerRepository;

	@Autowired
	private CartRepository cartRepository;
	
	public void registerBuyer(RegisterBuyerRequest dto) {
		User user = createUser(dto.username(), dto.password(), Role.Value.BUYER.name());
		validateRepeatedUsername(dto.username());
		
		Buyer buyer = new Buyer(dto.name(), dto.birthday(), dto.cpf(), dto.adress(), user);
		validateCpfRepeated(buyer);
		buyer = buyerRepository.save(buyer);

		Cart cart = new Cart(buyer);
		cartRepository.save(cart);
	}
	
	@Transactional
	public void registerSeller(RegisterSellerRequest dto) {
		User user = createUser(dto.username(), dto.password(), Role.Value.SELLER.name());
		validateRepeatedUsername(dto.username());
		
		Seller seller = new Seller(dto.name(), dto.cnpj(), user);
		validateCnpjRepeated(seller);
		sellerRepository.save(seller);
	}
	
	public LoginResponse doLogin(LoginRequest login) {
		User user = findUserByUsername(login.username());
				
		isLoginCorrect(login, user, passwordEncoder);
		
		String token = tokenService.generateToken(user);
		
		return new LoginResponse(token);
	}
	
	public User findUserById(Long id) {
		Optional<User> user = userRepository.findById(id);
		
		return user.orElseThrow(() -> new IdNotFoundException("User"));
	}
	
	public User findUserByUsername(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		
		return user.orElseThrow(() -> new UsernameOrPasswordIncorrectException());
	}
	
	private void isLoginCorrect(LoginRequest loginRequest, User user, PasswordEncoder passwordEncoder) {
		if(passwordEncoder.matches(loginRequest.password(), user.getPassword()) == false) throw new UsernameOrPasswordIncorrectException();
	}
	
	private Role findRoleByName(String roleName) {
		Optional<Role> optionalRole = roleRepository.findRoleByName(roleName);
		
		Role role = optionalRole.orElseThrow(() -> new RoleNotFoundException());
		
		return role;
	}
	
	private void validateRepeatedUsername(String username) {
		Optional<User> optionalUser = userRepository.findByUsername(username);
		
		if(optionalUser.isPresent()) {
			throw new AlreadyExistsException("Username");
		}
	}
	
	private void validateCpfRepeated(Buyer buyer) {
		Optional<Buyer> optionalBuyer = buyerRepository.findByCpf(buyer.getCpf());
		
		if(optionalBuyer.isPresent()) {
			if(buyer.getId() == null) throw new AlreadyExistsException("CPF");
			
			
			else {
				Buyer repeatedBuyer = optionalBuyer.get();
				
				if(!repeatedBuyer.getId().equals(buyer.getId())) throw new AlreadyExistsException("CPF");
			}
		}
	}
	
	private void validateCnpjRepeated(Seller seller) {
		Optional<Seller> optionalSeller = sellerRepository.findByCnpj(seller.getCnpj());
		
		if(optionalSeller.isPresent()) {
			if(seller.getId() == null) throw new AlreadyExistsException("CNPJ");
			
			
			else {
				Seller repeatedSeller = optionalSeller.get();
				
				if(!repeatedSeller.getId().equals(seller.getId())) throw new AlreadyExistsException("CNPJ");
			}
		}
	}
	
	private User createUser(String username, String password, String roleName) {
		Role role = roleRepository.findRoleByName(roleName).orElseThrow(() -> new RoleNotFoundException());
		User user = new User(username, password, role);
		validateRepeatedUsername(user.getUsername());
		user = userRepository.save(user);
		
		return user;
	}
}
