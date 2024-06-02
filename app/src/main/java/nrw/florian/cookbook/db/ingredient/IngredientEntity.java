package nrw.florian.cookbook.db.ingredient;

/**
 * @author Florian J. Kleine-Vorholt
 */
public class IngredientEntity {

    private int _id;

    private int recipeID;

    private String name;

    private double quantity;

    private IngredientUnit unit;


    public IngredientEntity(final String name,
                            final double quantity,
                            final IngredientUnit unit)
    {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

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


    IngredientEntity(final int _id,
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


    public int getId()
    {
        return _id;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public double getQuantity()
    {
        return quantity;
    }

    public void setQuantity(double quantity)
    {
        this.quantity = quantity;
    }

    public IngredientUnit getUnit()
    {
        return unit;
    }

    public void setUnit(IngredientUnit unit)
    {
        this.unit = unit;
    }

    void setId(int _id) {
        this._id = _id;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public String toString() { return quantity + " " + unit + " " + name; }
}