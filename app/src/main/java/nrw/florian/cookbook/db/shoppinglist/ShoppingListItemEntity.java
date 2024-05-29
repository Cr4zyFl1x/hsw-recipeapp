package nrw.florian.cookbook.db.shoppinglist;

/**
 * @author Florian J. Kleine-Vorholt
 */
public class ShoppingListItemEntity {

    private int _id;

    private String title;

    private boolean done;

    public ShoppingListItemEntity(final String title, final boolean done)
    {
        this.title = title;
        this.done = done;
    }

    ShoppingListItemEntity(final int _id, final String title, final boolean done)
    {
        this._id = _id;
        this.title = title;
        this.done = done;
    }

    public int getId() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDone() {
        return done;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    void setId(int _id) {
        this._id = _id;
    }
}
