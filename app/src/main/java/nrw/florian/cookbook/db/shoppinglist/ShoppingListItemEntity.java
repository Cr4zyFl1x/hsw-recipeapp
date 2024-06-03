package nrw.florian.cookbook.db.shoppinglist;

/**
 * @author Florian J. Kleine-Vorholt
 */
public class ShoppingListItemEntity {

    /**
     * The id of the shopping list item.
     */
    private int _id;

    /**
     * The title of the shopping list item.
     */
    private String title;

    /**
     * Whether the shopping list item is done.
     */
    private boolean done;



    /**
     * Creates a new shopping list item entity.
     * @param title The title of the shopping list item.
     * @param done Whether the shopping list item is done.
     */
    public ShoppingListItemEntity(final String title, final boolean done)
    {
        this.title = title;
        this.done = done;
    }


    /**
     * Creates a new shopping list item entity.
     * @param _id The id of the shopping list item.
     * @param title The title of the shopping list item.
     * @param done Whether the shopping list item is done.
     */
    ShoppingListItemEntity(final int _id, final String title, final boolean done)
    {
        this._id = _id;
        this.title = title;
        this.done = done;
    }


    /**
     * Gets the id of the shopping list item.
     * @return The id of the shopping list item.
     */
    public int getId()
    {
        return _id;
    }


    /**
     * Gets the title of the shopping list item.
     * @return The title of the shopping list item.
     */
    public String getTitle()
    {
        return title;
    }


    /**
     * Gets whether the shopping list item is done.
     * @return Whether the shopping list item is done.
     */
    public boolean isDone()
    {
        return done;
    }


    /**
     * Sets the title of the shopping list item.
     * @param title The title of the shopping list item.
     */
    public void setTitle(String title)
    {
        this.title = title;
    }


    /**
     * Sets whether the shopping list item is done.
     * @param done Whether the shopping list item is done.
     */
    public void setDone(boolean done)
    {
        this.done = done;
    }


    /**
     * Sets the id of the shopping list item.
     * @param _id The id of the shopping list item.
     */
    void setId(int _id)
    {
        this._id = _id;
    }
}