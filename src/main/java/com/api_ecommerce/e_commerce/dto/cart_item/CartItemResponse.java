package com.api_ecommerce.e_commerce.dto.cart_item;

import com.api_ecommerce.e_commerce.dto.product.ProductResponse;

public record CartItemResponse(ProductResponse product, int quantity) 
{}
