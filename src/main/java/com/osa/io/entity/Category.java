package com.osa.io.entity;

public enum Category {
	Electronics, Auto, Gifts, Books, Accessories, Pets, Sports, Shoes;
	
	public static boolean isCategoryValid(String other) {
		
		for (Category c : Category.values()) {
			if (c.toString().equals(other)) {
				// valid other category name
				return true;
			}
		}
		
		return false;
	}
}