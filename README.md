DrinkMate app, ISAT 480

Developers: Jay Harris, Dan Silvernail, Jack Phillips

| PROJECT OVERVIEW FOR DRINKMATE |
  - Native Android development
  - Basic features to be developed
      - Simple, intuitive User Interface
      - BAC Calculator
      - Random drink generator
      - View different drinks based on user input
  - Packages: 
      - com.isat.drinkmate
        - BacActivity.java
        - MainActivity.java
        - RandomActivity.java
      - com.isat.drinkmate.model
        - Drink.java
        - Ingredient.java
      - helper
        - DatabaseHelper.java
      - testing
        - BacActivityTest.java
        - DrinkTest.java
        - ... More classes required


| Development Log |

- 2/18/14: Screen activites setup (MainActivity, BacActivity, RandomActivity)
- 2/18/14: Testing for BacActivity initiated.
- 2/18/14: BAC Functionality is complete.
- 2/21/14: RandomActivity modified and displaying hardcoded information.
- 2/22/14: Search added to file AndroidManifest.xml for later searching purposes
- 2/23/14: Basic funtional setup for Drink.java and Ingredient.java classes added.
- 2/24/14: DatabaseHelper.java set up to help with databae interactions
- 2/24/14: SQLite Database can set and retrieve rows.
- 2/26/14: RandomActivity is now selecting random drink from Database information


| TESTING |

- Testing will be based on JUnit Tests and Android Test Suite. Android's test suite is based on JUnit tests and they can implement components of the phone. 
  
  - Android: http://developer.android.com/tools/testing/testing_android.html
  - JUnit: http://www.vogella.com/tutorials/JUnit/article.html
  

| SQLITE DATABASE  |

  - Name: drinkmatedb
  
  - Drinks Table:  
    - INTEGER id
    - TEXT name
    - TEXT description
    - TEXT ingredients
  
  - Ingredients Table: 
    - INTEGER id
    - TEXT name
    - TEXT type 
    - REAL amount
  
- Example Code: http://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables/

| SEARCH BAR |

- Development in progress.
  - Android Documentation: http://developer.android.com/guide/topics/search/index.html

| NOTES |

- activity_random.xml displayed formatting issues. Made layout into <TableLayout> but further skinning needed
  - Formatting issues will cause program to terminate
- CSV for ingredients in Drinks table may need to be changed for scalability
- .png labels on different views need to be tested on different devices
- Look into splash screens at beginning of program if the database gets heafty and needs to load.
- Made Drink.java "Parcelable" to pass data in a Bundle from one intent to another. By default Android will not let you pass user created objects between Activities.
