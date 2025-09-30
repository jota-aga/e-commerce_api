package com.api_ecommerce.e_commerce.dto.order_item;

import java.math.BigDecimal;

public record OrderItemResponse (String productName, String productDescription, BigDecimal productPrice, int quantity){}
