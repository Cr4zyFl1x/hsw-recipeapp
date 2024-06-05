package nrw.florian.cookbook.db.recipeproperty;

import java.io.Serializable;

/**
 * @author Florian J. Kleine-Vorholt
 */
public class RecipePropertyEntity implements Serializable {

    private static final long serialVersionUID = 23456543487876234L;

    /**
     * The database id of the property
     */
    private int _id;

    /**
     * The id of the recipe
     */
    private int recipeID;

    /**
     * The property
     */
    private final RecipeProperty property;



    /**
     * Creates a new property entity
     * @param property  The property
     */
    public RecipePropertyEntity(final RecipeProperty property)
    {
        this.property = property;
    }


    /**
     * Creates a new property entity
     * @param recipeID  The id of the recipe
     * @param property  The property
     */
    public RecipePropertyEntity(final int recipeID, final RecipeProperty property)
    {
        this.recipeID = recipeID;
        this.property = property;
    }


    /**
     * Creates a new property entity
     * @param _id       The database id of the property
     * @param recipeID  The id of the recipe
     * @param property  The property
     */
    RecipePropertyEntity(int _id, int recipeID, RecipeProperty property)
    {
        this._id = _id;
        this.recipeID = recipeID;
        this.property = property;
    }



    /**
     * Gets the property
     * @return The property
     */
    public RecipeProperty getProperty()
    {
        return property;
    }


    /**
     * Gets the recipe id
     * @return The recipe id
     */
    public int getRecipeID()
    {
        return recipeID;
    }


    /**
     * Gets the database id
     * @return The database id
     */
    public int getId()
    {
        return _id;
    }


    /**
     * Sets the id
     * @param _id the id
     */
    void setId(int _id)
    {
        this._id = _id;
    }


    /**
     * Sets the recipe id
     * @param recipeID the recipe id
     */
    public void setRecipeID(int recipeID)
    {
        this.recipeID = recipeID;
    }
}