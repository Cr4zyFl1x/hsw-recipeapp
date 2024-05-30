package nrw.florian.cookbook.db.recipeproperty;

/**
 * @author Florian J. Kleine-Vorholt
 */
public class RecipePropertyEntity {

    private int _id;

    private int recipeID;

    private RecipeProperty property;


    public RecipePropertyEntity(final RecipeProperty property)
    {
        this.property = property;
    }

    public RecipePropertyEntity(final int recipeID, final RecipeProperty property)
    {
        this.recipeID = recipeID;
        this.property = property;
    }

    RecipePropertyEntity(int _id, int recipeID, RecipeProperty property) {
        this._id = _id;
        this.recipeID = recipeID;
        this.property = property;
    }

    public RecipeProperty getProperty() {
        return property;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public int getId() {
        return _id;
    }

    void setId(int _id) {
        this._id = _id;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }
}