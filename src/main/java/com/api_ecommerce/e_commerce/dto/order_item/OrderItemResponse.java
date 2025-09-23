package com.api_ecommerce.e_commerce.dto.order_item;

import java.math.BigDecimal;

import com.api_ecommerce.e_commerce.dto.product.ProductDTO;
import com.api_ecommerce.e_commerce.entity.Product;

public record OrderItemResponse (String productName, String productDescription, BigDecimal productPrice, int quantity){}
