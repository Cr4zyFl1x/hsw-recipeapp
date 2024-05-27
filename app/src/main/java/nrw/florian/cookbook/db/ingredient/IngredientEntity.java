package nrw.florian.cookbook.db.ingredient;


public class IngredientEntity {

    private int id;

    private String name;



    public IngredientEntity(String name)
    {
        this.name = name;
    }

    IngredientEntity(final int _id, String name)
    {
        this.id = _id;
        this.name = name;
    }


    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
