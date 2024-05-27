package nrw.florian.cookbook.db;

public interface DBInfo {

    /**
     * Holds the database name
     */
    String DATABASE_NAME = "COOKBOOK";

    /**
     * Holds the table name for storing the ingredients
     */
    String TABLE_INGREDIENT = "Ingredient";
    String SCHEMA_INGREDIENT =
            "CREATE TABLE " + TABLE_INGREDIENT + "(" +
                    "_id INTEGER PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL" +
            ");";

    String TABLE_RECIPE = "Recipe";

    String TABLE_RECIPE_INGREDIENT = "RecipeIngredient";

    String TABLE_SHOPPINGLIST = "ShoppingList";
}
