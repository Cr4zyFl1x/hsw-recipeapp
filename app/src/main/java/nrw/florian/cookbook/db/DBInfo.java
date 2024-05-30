package nrw.florian.cookbook.db;


/**
 * Holds the database information
 * <p>
 *     Like DB-Name, Table-Names, SQL-Statements
 * </p>
 *
 * @author Florian J. Kleine-Vorholt
 */
public interface DBInfo {

    /**
     * Holds the database name
     */
    String DATABASE_NAME = "COOKBOOK";


    /**
     * Holds the table name for storing the recipes
     */
    String TABLE_RECIPE = "Recipe";

    /**
     * Contains the SQL statement to create the Recipe table
     */
    String SCHEMA_RECIPE =
            "CREATE TABLE " + TABLE_RECIPE + "(" +
                    "_id INTEGER PRIMARY KEY," +
                    "title VARCHAR(255) NOT NULL," +
                    "image BLOB NULL DEFAULT NULL," +
                    "defaultServings INTEGER NOT NULL," +
                    "instructions TEXT NOT NULL," +
                    "category VARCHAR(255) NOT NULL," +
                    "difficulty VARCHAR(255) NOT NULL," +
                    "workTime INTEGER NULL DEFAULT NULL," +
                    "calories INTEGER NULL DEFAULT NULL" +
            ");";


    /**
     * Holds the table name for storing the ingredients
     */
    String TABLE_INGREDIENT = "Ingredient";

    /**
     * Contains the SQL statement to create the Ingredient table
     */
    String SCHEMA_INGREDIENT =
            "CREATE TABLE " + TABLE_INGREDIENT + "(" +
                    "_id INTEGER PRIMARY KEY," +
                    "recipeID INTEGER NOT NULL," +
                    "name VARCHAR(255) NOT NULL," +
                    "unit VARCHAR(255) NOT NULL," +
                    "quantity INTEGER NOT NULL," +
                    "FOREIGN KEY (recipeID) REFERENCES " + TABLE_RECIPE + "(_id)" +
            ");";


    /**
     * Holds the table name for storing the recipe ingredient mapping
     */
    String TABLE_RECIPE_RECIPEPROPERTY = "RecipeCategory";

    /**
     * Contains the SQL statement to create the RecipeIngredient table
     */
    String SCHEMA_RECIPE_RECIPEPROPERTY =
            "CREATE TABLE " + TABLE_RECIPE_RECIPEPROPERTY + "(" +
                    "_id INTEGER PRIMARY KEY," +
                    "recipeId INTEGER NOT NULL," +
                    "property VARCHAR(255) NOT NULL," +
                    "FOREIGN KEY (recipeId) REFERENCES " + TABLE_RECIPE + "(_id)" +
            ");";


    /**
     * Holds the table name for storing the shopping list
     */
    String TABLE_SHOPPINGLISTITEM = "ShoppingListItem";

    /**
     * Contains the SQL statement to create the ShoppingList table
     */
    String SCHEMA_SHOPPINGLISTITEM =
            "CREATE TABLE " + TABLE_SHOPPINGLISTITEM + "(" +
                    "_id INTEGER PRIMARY KEY," +
                    "title VARCHAR(255) NOT NULL," +
                    "done BOOLEAN NOT NULL DEFAULT 0" +
            ");";
}