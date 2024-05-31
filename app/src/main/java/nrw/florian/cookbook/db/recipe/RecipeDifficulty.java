package nrw.florian.cookbook.db.recipe;

import android.content.Context;

import nrw.florian.cookbook.R;
import nrw.florian.cookbook.api.iface.StringResource;

/**
 * @author Florian J. Kleine-Vorholt
 */
public enum RecipeDifficulty implements StringResource {

    EASY (R.string.dfc_easy),

    MEDIUM (R.string.dfc_medium),

    HARD (R.string.dfc_hard);


    /**
     * The resource id of the enum value
     */
    private final int resourceId;



    RecipeDifficulty(int resourceId)
    {
        this.resourceId = resourceId;
    }



    /**
     * Gets the resource id of the enum value
     * @return the resource id
     */
    public int getResourceId()
    {
        return resourceId;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(Context context) {
        return context.getString(getResourceId());
    }
}