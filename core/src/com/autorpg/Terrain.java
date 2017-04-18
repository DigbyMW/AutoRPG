package com.autorpg;

public class Terrain {
	public void average_test(double num1, double num2, double num3, double num4, double variation) {
		System.out.println("" + average(num1, num2, num3, num4, variation));
	}
	
	private double average(double num1, double num2, double num3, double num4, double variation) {
		double result;
		
		// Get mean average
		result = num1 + num2 + num3 + num4;
		result /= 4;
		
		// Add variation
		result = result - (variation / 2) // Allows variation to go up or down
		// Randomization:
		+ (Math.random() * variation);
		
		// Keep variation between 0 and 1.
		if (result < 0) {
			result = 0;
		} else if (result > 1) {
			result = 1;
		}
		
		return result;
	}
}
