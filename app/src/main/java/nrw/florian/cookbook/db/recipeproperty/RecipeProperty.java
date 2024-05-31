package nrw.florian.cookbook.db.recipeproperty;

import android.content.Context;

import nrw.florian.cookbook.R;
import nrw.florian.cookbook.api.iface.StringResource;

/**
 * @author Florian J. Kleine-Vorholt
 */
public enum RecipeProperty implements StringResource {

    VEGETARIAN (R.string.rp_vegetarian),

    MEAT (R.string.rp_meat),

    WARM (R.string.rp_warm),

    COLD (R.string.rp_cold);


    /**
     * Resource identifier
     */
    private final int resourceId;



    RecipeProperty(int resourceId)
    {
        this.resourceId = resourceId;
    }



    /**
     * Gets the resource identifier
     */
    public int getId()
    {
        return resourceId;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(Context ctx)
    {
        return ctx.getString(getId());
    }
}