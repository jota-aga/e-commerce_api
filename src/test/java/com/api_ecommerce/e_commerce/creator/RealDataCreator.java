package com.api_ecommerce.e_commerce.creator;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.CartItem;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.OrderItem;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.entity.Role;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.enums.ProductStatus;
import com.api_ecommerce.e_commerce.repository.BuyerRepository;
import com.api_ecommerce.e_commerce.repository.CartItemRepository;
import com.api_ecommerce.e_commerce.repository.CartRepository;
import com.api_ecommerce.e_commerce.repository.CategoryRepository;
import com.api_ecommerce.e_commerce.repository.OrderItemRepository;
import com.api_ecommerce.e_commerce.repository.OrderRepository;
import com.api_ecommerce.e_commerce.repository.ProductRepository;
import com.api_ecommerce.e_commerce.repository.RoleRepository;
import com.api_ecommerce.e_commerce.repository.UserRepository;

public class RealDataCreator {
	
	public static User createUserBuyer(RoleRepository roleRepository, UserRepository userRepository) {
		Role role = roleRepository.findRoleByName(Role.Value.BUYER.name()).get();
		User user = User.builder()
						.username("username")
						.password("password")
						.role(role)
						.build();
		
		return userRepository.save(user);
	}
	
	public static Buyer createBuyer(User user, 
									BuyerRepository buyerRepository) {
		
		Buyer buyer = Buyer.builder()
						   .name("name")
						   .cpf("11237419484")
						   .adress("adress")
						   .birthday(LocalDate.now().minusYears(20))
						   .user(user)
						   .build();
		
		return buyerRepository.save(buyer);
	}
	
	public static Cart createCart(Buyer buyer, CartRepository cartRepository) {
		Cart cart = Cart.builder()
						.buyer(buyer)
						.build();
		
		return cartRepository.save(cart);
	}
	
	public static Category createCategory(CategoryRepository categoryRepository) {
		Category category = Category.builder()
									.name("category")
									.build();
		
		return categoryRepository.save(category);
	}
	
	public static Product createProduct(Category category, ProductRepository productRepository) {
		
		Product product = Product.builder()
								 .name("product")
								 .category(category)
								 .description("description")
								 .price(new BigDecimal(100))
								 .quantity(50)
								 .status(ProductStatus.AVAILABLE)
								 .build();
		
		return productRepository.save(product);
	}
	
	public static CartItem createCartItem(CartItemRepository cartItemRepository, Cart cart, Product product) {
		CartItem cartItem = CartItem.builder()
									.cart(cart)
									.product(product)
									.quantity(5)
									.build();
		
		return cartItemRepository.save(cartItem);
	}
	
	public static Order createOrder(OrderRepository orderRepository, Buyer buyer) {
		Order order = Order.builder()
						   .buyer(buyer)
						   .createdAt(LocalDate.now())
						   .build();
						   
		return orderRepository.save(order);
	}
	
	public static OrderItem createOrderItem(OrderItemRepository orderItemRepository, Order order, Product product) {
		OrderItem orderItem = OrderItem.builder()
							.order(order)
							.product(product)
							.productName(product.getName())
							.productDescription(product.getDescription())
							.productPrice(product.getPrice())
						   .build();
						   
		return orderItemRepository.save(orderItem);
	}
}
