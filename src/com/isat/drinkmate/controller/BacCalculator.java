package com.isat.drinkmate.controller;

import java.text.DecimalFormat;

public class BacCalculator {
	public static final double MAN_CONSTANT = .58;
	public static final double WOMAN_CONSTANT = .49;

	private int ounces;
	private double percent, weight, hoursDrinking;
	private String gender;

	/*
	 * Default constructor
	 */
	public BacCalculator() {
		this.ounces = 0;
		this.percent = 0;
		this.weight = 0;
		this.hoursDrinking = 0;
		this.gender = "";
	}

	/*
	 * Overloaded Constructor
	 * 
	 * @param String gender
	 */
	public BacCalculator(String gender) {
		if (gender.equalsIgnoreCase("m")) // set metrics for a male
		{
			this.ounces = 12; // average ounces in a beer, assumes user has had
								// 1 drink
			this.percent = 4.5; // average alcohol content for a beer
			this.weight = 195; // average male weight in U.S.
			this.hoursDrinking = 1; // assumes been drinking for 1 hour
			this.gender = "m";
		} else if (gender.equalsIgnoreCase("f")) // set metrics for a female
		{
			this.ounces = 12; // average ounces in a beer, assumes user has had
								// 1 drink
			this.percent = 4.5; // average alcohol content for a beer
			this.weight = 166; // average female weight in U.S.
			this.hoursDrinking = 1; // assumes been drinking for 1 hour
			this.gender = "f";

		} else // set empty metrics by calling constructor w/o params
		{
			new BacCalculator();
		}
	}

	/*
	 * Overloaded Constructor
	 * 
	 * @param int ounces
	 * 
	 * @param double percent
	 * 
	 * @param double weight
	 * 
	 * @param double hoursDrinking
	 * 
	 * @param String gender
	 */
	public BacCalculator(int ounces, double percent, double weight,
			double hoursDrinking, String gender) {
		this.ounces = ounces;
		this.percent = percent;
		this.weight = weight;
		this.hoursDrinking = hoursDrinking;
		this.gender = gender;
	}

	/*
	 * Returns string result based on BAC calculation
	 * 
	 * @return String
	 */
	public String getBacString() {
		// show response to BAC calculation
		double bac = getBac();
		String res = "";

		// format to 3 decimal places
		DecimalFormat df = new DecimalFormat("#.###");
		bac = Double.valueOf(df.format(bac));

		if (bac <= 0) {
			res = ("\nBAC: " + bac + "%\nThis is a negligible amount of alcohol");
		} else if (bac >= 0 && bac <= 0.08) {
			res = ("\nBAC: " + bac + "%\nYou are not above the legal limit");
		} else if (bac >= 0.08 && bac <= .18) {
			res = ("\nBAC: " + bac + "%\nYou are over the legal limit");
		} else if (bac >= .18) {
			res = ("\nBAC: " + bac + "%\nYou have a dangerous level of booze in you");
		} else
			res = ("\nERROR: Invalid input");

		return res;
	}

	/*
	 * Calculates the BAC of a user and returns the double value. It also checks
	 * for negative values.
	 * 
	 * @return double result
	 */
	public double getBac() {
		double result = 0;

		// decipher gender
		if (this.gender.equalsIgnoreCase("m")) {
			result = (this.ounces * this.percent * 0.075)
					/ (this.weight * MAN_CONSTANT)
					- (this.hoursDrinking * 0.015);
		} else if (this.gender.equalsIgnoreCase("f")) {
			result = (this.ounces * this.percent * 0.075)
					/ (this.weight * WOMAN_CONSTANT)
					- (this.hoursDrinking * 0.015);
		} else {
			System.out.println("INVALID GENDER");
		}
		// check for negative values
		if (result < 0)
			result = 0;

		return result;
	}

	/*
	 * Calculates the BAC of a user and returns the double value. It also checks
	 * for negative values.
	 * 
	 * @param int ounces - ounces of alcohol consumed
	 * 
	 * @param double percent - alcohols percentage
	 * 
	 * @param double weight - weight of user
	 * 
	 * @param int hoursDrinking - time drinking
	 * 
	 * @return double result
	 */
	public double getBac(int ounces, double percent, double weight,
			double hoursDrinking, String gender) {
		double result = 0;

		// decipher gender
		if (gender.equalsIgnoreCase("m")) {
			result = (ounces * percent * 0.075) / (weight * MAN_CONSTANT)
					- (hoursDrinking * 0.015);
		} else if (gender.equalsIgnoreCase("f")) {
			result = (ounces * percent * 0.075) / (weight * WOMAN_CONSTANT)
					- (hoursDrinking * 0.015);
		} else {
			System.out.println("INVALID GENDER");
		}
		// check for negative values
		if (result < 0)
			result = 0;

		return result;
	}

	/*
	 * All get methods
	 */
	public String getGender() {
		return this.gender;
	}

	public int getOunces() {
		return this.ounces;
	}

	public double getWeight() {
		return this.weight;
	}

	public double getHours() {
		return this.hoursDrinking;
	}

	public Double getPercent() {
		return this.percent;
	}
}
