package com.isat.drinkmate.model;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

/*
 * Class represents a single Drink to be used with our DrinkMate application
 * 
 * ISAT 480
 * Author(s): Jackson Phillips
 */

public class Drink implements Parcelable{
	
	// set up private variables
	private int id;
	private String name, description, ingredients;
	private ArrayList<Ingredient> ingredientList;
	
	public Drink()
	{
		/*
		 * Default Constructor for a drink - set values to null if none exist
		 */
		id = -1;
		name = "";
		description = "";
		ingredientList = new ArrayList<Ingredient>();
	}
	public Drink(String name, String description, ArrayList<Ingredient> ingredientList)
	{
		/*
		 * Constructor if all parameters are set
		 * 
		 * @param String name - name of drink
		 * @param String description - description of drink
		 * @param ArrayList<Ingredient> ingredientList - list of ingredients
		 */
		//this.id = id;
		this.name = name;
		this.description = description;
		this.ingredientList = ingredientList;
	}
	public Drink(int id, String name, String description, String ingredients)
	{
		/*
		 * Constructor if all parameters are set - uses CSV
		 * 
		 * @param String name - name of drink
		 * @param String description - description of drink
		 * @param ArrayList<Ingredient> ingredientList - list of ingredients
		 */
		this.id = id;
		this.name = name;
		this.description = description;
		this.ingredients = ingredients;
	}
	public Drink(String name, String description, String ingredients)
	{
		/*
		 * Constructor if all parameters are set - uses CSV
		 * 
		 * @param String name - name of drink
		 * @param String description - description of drink
		 * @param ArrayList<Ingredient> ingredientList - list of ingredients
		 */
		this.name = name;
		this.description = description;
		this.ingredients = ingredients;
	}
	public Drink(int id, String name, String description, ArrayList<Ingredient> ingredientList)
	{
		/*
		 * Constructor if all parameters are set - uses arralist and id
		 * 
		 * @param String name - name of drink
		 * @param String description - description of drink
		 * @param ArrayList<Ingredient> ingredientList - list of ingredients
		 */
		this.id = id;
		this.name = name;
		this.description = description;
		this.ingredientList = ingredientList;
	}
	public int getDrinkID()
	{
		/*
		 * Returns the id of the drink
		 * 
		 * @return int id
		 */
		return id;
	}
	public String getDrinkName()
	{
		/*
		 * Returns the name of the drink
		 * 
		 * @return String name
		 */
		return name;
	}
	public String getDrinkDescription()
	{
		/*
		 * Returns the description of the drink
		 * 
		 * @return String description
		 */
		return description;
	}
	public String getAllIngredientNames()
	{
		/*
		 * Returns a String of all the ingredient names
		 * 
		 * @return result
		 */
		String result = "";
		for (Ingredient i : ingredientList){
			result += i.getIngredientName() + "\n";
		}
		return result;
	}
	public String getAllArrayIngredientInfo()
	{
		/*
		 * Returns a String of all ingredient information
		 * 
		 * @return String result
		 */
		String result = "";
		for (Ingredient i : ingredientList) {
			result += i.getIngredientName() + " (" + i.getIngredientType() + ")";
			if (i.getIngredientType().equalsIgnoreCase("garnish"))
				result += " add " + (int)i.getIngredientAmount() + "\n";
			else
				result += " add " + i.getIngredientAmount() + " oz\n";
		}
		return result;
	}
	public Ingredient getIngredient(int pos)
	{
		/*
		 * Returns the Ingredient from the given position
		 * 
		 * @param int pos - position of the element
		 * @return Ingredient
		 */
		return ingredientList.get(pos);
	}
	public String getAllInformation()
	{
		/*
		 * Returns all the information associated with drink
		 * 
		 * @return String
		 */
		return getDrinkID() + " " + getDrinkName() + "\n" + getDrinkDescription() + "\n"
				+ getAllArrayIngredientInfo();
	}
	
	/*
	 * Returns string of the CSV version of Drink
	 */
	public String getAllCSVDrinkInfo()
	{
		return "Drink Name: " + getDrinkName() + "(id = " + getDrinkID() + ")"
				+ " Description: " + getDrinkDescription()
				+ " Ingredient: " + getCSVIngredient();
	}
	public void setDrinkID(int id)
	{
		/*
		 * Sets the ID of the drink
		 * 
		 * @param int id
		 */
		this.id = id;
	}
	public void setDrinkName(String name)
	{
		/*
		 * Returns the name of the drink
		 * 
		 * @param String name
		 */
		if (name.length() <= 0)
			this.name = "No Drink Name";
		else
			this.name = name;
	}
	public void setDrinkDescription(String description)
	{
		/*
		 * Returns the description of the drink
		 * 
		 * @param String description
		 */
		if (description.length() <= 0)
			this.description = "No Description Available";
		else
			this.description = description;
	}
	
	/*
	 * Set drink ingredients to String
	 */
	public void setDrinkIngredients(String ingredients)
	{
		this.ingredients = ingredients;
	}
	/*
	 * Set ingredient list to an array of ingredients
	 */
	public void setIngredientsArray(ArrayList<Ingredient> ingredientList) {
		this.ingredientList = ingredientList;
		String result = "";
		for (int i = 0; i < this.ingredientList.size(); i++) {
			result = (ingredientList.get(i).getAllIngredientInfo());
		}
		setDrinkIngredients(result);
	}
	
	/*
	 * Returns CSV of ingredient
	 */
	public String getCSVIngredient()
	{
		return ingredients;
	}
	
	public void addIngredientToDrink(Ingredient ingredient)
	{
		/*
		 * Adds an ingredient to the ingredients array list
		 * 
		 * @param Ingredient ingredient
		 */
		ingredientList.add(ingredient);
	}
	
	/*** PASSING TO OTHER ACTIVITIES ***/
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 1;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(description);
		dest.writeString(ingredients);
		
	}
	public Drink(Parcel source)
	{
		id = source.readInt();
		name = source.readString();
		description = source.readString();
		//ingredients = source.createTypedArrayList(ingredientList);
		ingredients = source.readString();
	}
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		 
        @Override
        public Drink createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new Drink(source);
        }
 
        @Override
        public Drink[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Drink[size];
        }
    };
}
