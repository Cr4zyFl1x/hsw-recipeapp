package nrw.florian.cookbook.db.recipe;

import android.content.Context;

import nrw.florian.cookbook.R;

/**
 * @author Florian J. Kleine-Vorholt
 */
public enum RecipeBaseCategory {

    SOUP (R.string.rc_soup),

    SAUCE (R.string.rc_sauce),

    DESSERT (R.string.rc_dessert),

    SALAD (R.string.rc_salad),

    MAIN (R.string.rc_main),

    STARTER (R.string.rc_starter),

    SNACK (R.string.rc_snack),

    DRINK (R.string.rc_drink);



    /**
     * Resource ID (string) for this category.
     */
    private final int resourceId;


    /**
     * Constructor.
     * @param resourceId Resource ID (string) for this category.
     */
    RecipeBaseCategory(int resourceId)
    {
        this.resourceId = resourceId;
    }


    /**
     * Gets the resource ID.
     * @return Resource ID (string) for this category.
     */
    public int getResourceId()
    {
        return resourceId;
    }


    /**
     * Gets the string for this category.
     * @param context Context.
     * @return String for this category.
     */
    public String getString(final Context context)
    {
        return context.getString(getResourceId());
    }
}