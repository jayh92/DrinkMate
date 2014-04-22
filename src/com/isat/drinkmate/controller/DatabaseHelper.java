package com.isat.drinkmate.controller;

import java.util.ArrayList;
import java.util.List;

import com.isat.drinkmate.model.Drink;
import com.isat.drinkmate.model.Ingredient;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/*
 * DatabaseHelper.java
 * 
 * Class represents a the database that is used within the application
 * 
 * ISAT 480
 * Author(s): Jack Phillips
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "DatabaseHelper";
 
    // Database Version
    private static final int DATABASE_VERSION = 3;
 
    // Database Name
    private static final String DATABASE_NAME = "drinkmatedb";
 
    // Table Names
    private static final String TABLE_DRINKS = "drinks";
    private static final String TABLE_INGREDIENTS = "ingredients";
    private static final String TABLE_COMPLETE = "complete";
 
    // Common column names
    private static final String KEY_ID = "id";
    
    // DRINKS table
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_INGREDIENTS = "ingredients";
    
    // INGREDIENTS table
    //private static final String KEY_INGREDIENTS_NAME = "name";
    private static final String KEY_TYPE = "type";
    private static final String KEY_AMOUNT = "amount";
    
    // COMPLETEDRINKS table
    private static final String KEY_DRINK_ID = "drink_id";
    private static final String KEY_INGREDIENT_ID = "ingredient_id";
    
    // Table Create Statements
    private static final String CREATE_DRINKS_TABLE = "CREATE TABLE "
    		+ TABLE_DRINKS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME
    		+ " TEXT," + KEY_DESCRIPTION + " TEXT, " + KEY_INGREDIENTS + " TEXT)";
    
    private static final String CREATE_INGREDIENTS_TABLE = "CREATE TABLE "
    		+ TABLE_INGREDIENTS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME
    		+ " TEXT," + KEY_TYPE + " TEXT, " + KEY_AMOUNT + " REAL)";
    
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
    	Log.i("DatabaseHelper:onCreate", "ON CREATE DATABASE HELPER");
    	
        // creating required tables
        db.execSQL(CREATE_DRINKS_TABLE);
        db.execSQL(CREATE_INGREDIENTS_TABLE);
        //db.execSQL(CREATE_COMPLETE_TABLE);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	Log.i("DatabaseHelper:onUpgrade", "DB updated from " + oldVersion +  " to " + newVersion);
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRINKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
 
        // create new tables
        onCreate(db);
    }
    /****** DRINKS_TABLE METHODS ******/
    /*
     * Creating a Drink
     * 
     * @param Drink drink
     * @return int
     */
    public int createDrink(Drink drink)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("DatabaseHelper:createDrink", "Creating Drink: " + drink.getDrinkName());
        
        // set values for drink in DB
        ContentValues values = new ContentValues();
        values.put(KEY_ID, drink.getDrinkID());
        values.put(KEY_NAME, drink.getDrinkName());
        values.put(KEY_DESCRIPTION, drink.getDrinkDescription());
        values.put(KEY_INGREDIENTS, drink.getCSVIngredient());
 
        // insert row - cast from long may not work
        int drinks_id = (int) db.insert(TABLE_DRINKS, null, values);
        return drinks_id;
    }
    /*
     * get single drink
     * 
     * @param int drink_id - id of drink
     * @return Drink - drink to be returned
     */
    public Drink getDrink(int drink_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_DRINKS + " WHERE "
                + KEY_ID + " = " + drink_id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
 
        // set drink values
        Drink tempDrink = new Drink();
        tempDrink.setDrinkID(c.getInt(c.getColumnIndex(KEY_ID)));
        tempDrink.setDrinkName((c.getString(c.getColumnIndex(KEY_NAME))));
        tempDrink.setDrinkDescription((c.getString(c.getColumnIndex(KEY_DESCRIPTION))));
        tempDrink.setDrinkIngredients((c.getString(c.getColumnIndex(KEY_INGREDIENTS))));
 
        return tempDrink;
    }
    
    /*
     * Get all drinks
     * 
     * @return List<Drink> - all drinks in DB
     */
    public List<Drink> getAllDrinks()
    {
    	List<Drink> drinks = new ArrayList<Drink>();
    	String selectQuery = "SELECT * FROM " + TABLE_DRINKS;
    	
    	// log the transaction
    	Log.e(LOG, selectQuery);
    	
    	SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Drink td = new Drink();
                td.setDrinkID(c.getInt((c.getColumnIndex(KEY_ID))));
                td.setDrinkName((c.getString(c.getColumnIndex(KEY_NAME))));
                td.setDrinkDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
                td.setDrinkIngredients((c.getString(c.getColumnIndex(KEY_INGREDIENTS))));
                
                // adding to drinks list
                drinks.add(td);
            } while (c.moveToNext());
        }
        return drinks;
    }
    
    /*
     * Update a drink in the DB
     * 
     * @param Drink drink - drink to be updated
     * @return int
     */
    public int updateDrink(Drink drink)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("DatabaseHelper:updateDrink ", "UPDATED " + drink.getCSVIngredient());
        
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, drink.getDrinkName());
        values.put(KEY_DESCRIPTION, drink.getDrinkDescription());
        values.put(KEY_INGREDIENTS, drink.getCSVIngredient());
 
        // updating row
        return db.update(TABLE_DRINKS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(drink.getDrinkID())});
    }
    
    /*
     * Return the number of Drinks in Drinks table
     * 
     * @return int - number of drinks in DB
     */
    public int getDrinkCount()
    {
    	String countQuery = "SELECT * FROM " + TABLE_DRINKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
 
        int count = cursor.getCount();
        cursor.close();
 
        return count;
    }
    
    /*
     * Deletes a drink from Drinks table
     * 
     * @param int id - id of drink to be removed
     */
    public void deleteDrink(int id)
    {
    	SQLiteDatabase db = this.getReadableDatabase();
    	db.delete(TABLE_DRINKS, KEY_ID + " = ?", new String[] {String.valueOf(id)});
    } 
    
    /*
     * Delete all drinks in db
     * 
     */
    public void deleteAllDrinks() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	db.delete(TABLE_DRINKS, null, null);
    }
    
    /****** INGREDIENTS_TABLE METHODS ******/
    
    /*
     * Creates ingredient
     * 
     *@param Ingredient ingredient
     *@return int 
     */
    public int createIngredient(Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("CREATE INGREDIENT", "Creating Ingredient: " + ingredient.getIngredientName());
        ContentValues values = new ContentValues();
        values.put(KEY_ID, ingredient.getIngredientID());
        values.put(KEY_NAME, ingredient.getIngredientName());
        values.put(KEY_TYPE, ingredient.getIngredientType());
        values.put(KEY_AMOUNT, ingredient.getIngredientAmount());
 
        // insert row
        int ingredient_id = (int) db.insert(TABLE_INGREDIENTS, null, values);
 
        return ingredient_id;
    }
    /*
     * Get all ingredients
     * 
     * @return List<Ingredient>
     */
    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        String selectQuery = "SELECT  * FROM " + TABLE_INGREDIENTS;
 
        Log.e(LOG, selectQuery);
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
            	Ingredient t = new Ingredient();
                t.setIngredientID(c.getInt((c.getColumnIndex(KEY_ID))));
                t.setIngredientName(c.getString(c.getColumnIndex(KEY_NAME)));
                t.setIngredientType(c.getString(c.getColumnIndex(KEY_TYPE)));
                t.setIngredientAmount(c.getDouble((c.getColumnIndex(KEY_AMOUNT))));
                
                // adding to tags list
                ingredients.add(t);
            } while (c.moveToNext());
        }
        return ingredients;
    }
    
    /*
     * Update ingredient
     * 
     * @param Ingredient ingredient
     * @return int
     */
    public int updateIngredient(Ingredient ingredient) 
    {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, ingredient.getIngredientName());
        
        // updating row
        return db.update(TABLE_INGREDIENTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(ingredient.getIngredientID()) });
    }
    /*
     * Returns the ingredient by the ID provided
     * 
     * @param int ingredient_id
     * @return Ingredient - object from other drinkmate packages
     */
    public Ingredient getIngredient(int ingredient_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Ingredient temp = new Ingredient();
        String selectQuery = "SELECT  * FROM " + TABLE_INGREDIENTS + " WHERE "
                + KEY_ID + " = " + ingredient_id;
        // logging
        Log.e(LOG, selectQuery);
 
        Cursor c = db.rawQuery(selectQuery, null);
 
        if (c != null)
            c.moveToFirst();
        try {
	        temp.setIngredientID(c.getInt(c.getColumnIndex(KEY_ID)));
	        temp.setIngredientName((c.getString(c.getColumnIndex(KEY_NAME))));
	        temp.setIngredientType((c.getString(c.getColumnIndex(KEY_TYPE))));
	        temp.setIngredientAmount((c.getDouble(c.getColumnIndex(KEY_AMOUNT))));
        }
	    catch (Exception e) {
	    	System.out.println("databasehelper:getIngredient failed");
	    }
        return temp;
    }
    
    /*
     * Delete ingredient
     * 
     * @param int ingredient_id
     */
    public void deleteIngredient(int ingredient_id) 
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INGREDIENTS, KEY_ID + " = ?",
                new String[] { String.valueOf(ingredient_id) });
    }
    /*
     * Delete all drinks in db
     */
    public void deleteAllIngredients() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	db.delete(TABLE_INGREDIENTS, null, null);
    }
    /*
     * Return the number of Drinks in Drinks table
     * 
     * @return int - number of drinks in DB
     */
    public int getIngredientCount()
    {
    	String countQuery = "SELECT * FROM " + TABLE_INGREDIENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
 
        int count = cursor.getCount();
        cursor.close();
 
        return count;
    }
    /*
     * Close database
     */
    public void closeDB()
    {
    	SQLiteDatabase db = this.getReadableDatabase();
    	if (db != null && db.isOpen())
    		db.close();
    }
}
