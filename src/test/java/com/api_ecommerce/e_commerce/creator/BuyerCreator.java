package com.api_ecommerce.e_commerce.creator;

import java.time.LocalDate;

import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.User;

public class BuyerCreator {
	
	public static Buyer simpleBuyer() {
		User user = UserCreator.userBuyer();
		return new Buyer(1L, "name", LocalDate.now().minusYears(20), "11237419484", "adress", user);
	}
}
