DrinkMate app, ISAT 480

Developers: Jay Harris, Dan Silvernail, Jack Phillips

| PROJECT OVERVIEW FOR DRINKMATE |
  - Native Android development
  - Basic features
      - Simple, intuitive User Interface
      - BAC Calculator
      - Random drink generator (On shake event)
      - View different drinks based on user input
        - User will either browse all drinks or search by ingredients
  - Packages: 
      - com.isat.drinkmate.model
        - Drink.java
        - Ingredient.java
      - com.isat.drinkmate.view
        - MainActivity.java
        - MultiSelectionSpinner.java
      - com.isat.drinkmate.controller
        - BacCalculator.java
        - DatabaseHelper.java
        - RandomDrink.java
      - com.isat.drinkmate.testing
        - BacActivityTest.java
        - DrinkTest.java

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
- 3/05/14: Tabs have been implemented, multi selection spinner for search is working, moved functionality from removed activity classes into more light weight classes.
- 3/06/14: Added more tests to BacCalculator, added default "m" and "f" constructors to BacCalculator.java
- 3/15/14: Attempts to get open source sensor simulator failed to launch, porting to device necessary for testing "shake" functionality.
- 4/01/14: Port to device troubles with visual assets. Changed "drawable" ImageViews to TextViews for screen compatability
- 4/12/14: Porting to device and "shake" test to get a random drink is up and running
- 4/15/14: Modifications to database necessary current implementation will work but is clunky.
- 4/18/14: All for debugging and application information has been converted into a LogCat implementation.
- 4/21/14: Database V.3 is being used. Code cleaned up in MainActivity.java and DatabaseHelper.java

| TESTING |

-  Android's test suite is based on JUnit tests and they can implement components of the phone. Class tests have been implemented in the com.isat.drinkmate.testing package.
  - Android: http://developer.android.com/tools/testing/testing_android.html
  - JUnit: http://www.vogella.com/tutorials/JUnit/article.html
  - Sensor tests: Ported to device and tested shake functionality first by changing background color of device then to selecting a random drink based on the proper event listener. 

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

| Tabs |

- OVERVIEW
  - 3 tabs (Search, Random, BAC) are currently implemented
  - Link to example code: http://www.codeproject.com/Articles/107693/Tabbed-Applications-in-Android
  - Modification to formatting of each tab element aside from Search (i.e. Main) must be done programatically (cannot drag and drop)
- SEARCH
  - Browse drinks (Spinner) and Search by Ingredient (custom MultiSelectionSpinner)
  - When drink is found a Toast message displays the drink name
- RANDOM
  - Shake to get random drink or press button
  - Toast message with drink name when found
- BAC
  - Calculator is operational
  - Displays Toast message of what is medically suggested ("You're sober, "Seek medical attention", etc)

| NOTES |

- Further formatting of tabbed views not necessary but would add visual appeal
- CSV for ingredients in Drinks table may need to be changed for scalability
- Look into splash screens at beginning of program if the database gets heafty and needs to load.
- Made Drink.java "Parcelable" to pass data in a Bundle from one intent to another. By default Android will not let you pass user created objects between Activities.
- All tabs are processed at MainActivity.java
- In later Android versions FragmentTabs are more common than our implementation (may be API issues however for Fragment)
