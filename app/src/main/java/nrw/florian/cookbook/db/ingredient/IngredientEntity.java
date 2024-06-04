package nrw.florian.cookbook.db.ingredient;

import java.io.Serializable;

/**
 * @author Florian J. Kleine-Vorholt
 */
public class IngredientEntity implements Serializable {

    private static final long serialVersionUID = 29874567872234L;

    /**
     * The id of the ingredient
     */
    private int _id;

    /**
     * The id of the recipe
     */
    private int recipeID;

    /**
     * The name of the ingredient
     */
    private String name;

    /**
     * The quantity of the ingredient
     */
    private double quantity;

    /**
     * The unit of the ingredient
     */
    private IngredientUnit unit;



    /**
     * Constructs a new ingredient entity
     * @param name      the name of the ingredient
     * @param quantity  the quantity of the ingredient
     * @param unit      the unit of the ingredient
     */
    public IngredientEntity(final String name,
                            final double quantity,
                            final IngredientUnit unit)
    {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }


    /**
     * Constructs a new ingredient entity
     * @param recipeID  the id of the recipe
     * @param name      the name of the ingredient
     * @param quantity  the quantity of the ingredient
     * @param unit      the unit of the ingredient
     */
    public IngredientEntity(final int recipeID,
                            final String name,
                            final double quantity,
                            final IngredientUnit unit)
    {
        this.recipeID = recipeID;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }


    /**
     * Constructs a new ingredient entity
     * @param _id       the id of the ingredient
     * @param recipeID  the id of the recipe
     * @param name      the name of the ingredient
     * @param quantity  the quantity of the ingredient
     * @param unit      the unit of the ingredient
     */
    public IngredientEntity(final int _id,
                            final int recipeID,
                            final String name,
                            final double quantity,
                            final IngredientUnit unit)
    {
        this._id = _id;
        this.recipeID = recipeID;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }



    /**
     * Gets the id of the ingredient
     * @return the id of the ingredient
     */
    public int getId()
    {
        return _id;
    }


    /**
     * Gets the id of the recipe
     * @return the id of the recipe
     */
    public int getRecipeID()
    {
        return recipeID;
    }


    /**
     * Gets the name of the ingredient
     * @return the name of the ingredient
     */
    public String getName()
    {
        return name;
    }


    /**
     * Sets the name of the ingredient
     * @param name the name of the ingredient
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Gets the quantity of the ingredient
     * @return the quantity of the ingredient
     */
    public double getQuantity()
    {
        return quantity;
    }


    /**
     * Sets the quantity of the ingredient
     * @param quantity the quantity of the ingredient
     */
    public void setQuantity(double quantity)
    {
        this.quantity = quantity;
    }


    /**
     * Gets the unit of the ingredient
     * @return the unit of the ingredient
     */
    public IngredientUnit getUnit()
    {
        return unit;
    }


    /**
     * Sets the unit of the ingredient
     * @param unit the unit of the ingredient
     */
    public void setUnit(IngredientUnit unit)
    {
        this.unit = unit;
    }


    /**
     * Sets the id of the ingredient
     * @param _id the id of the ingredient
     */
    void setId(int _id)
    {
        this._id = _id;
    }


    /**
     * Sets the id of the recipe
     * @param recipeID the id of the recipe
     */
    public void setRecipeID(int recipeID)
    {
        this.recipeID = recipeID;
    }

    public String toString() { return quantity + " " + unit + " " + name; }
}