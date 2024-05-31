package nrw.florian.cookbook.db.ingredient;

import android.content.Context;

import nrw.florian.cookbook.R;
import nrw.florian.cookbook.api.iface.StringResource;

/**
 * @author Florian J. Kleine-Vorholt
 */
public enum IngredientUnit implements StringResource {

    GRAMS (R.string.us_grams),
    KILOGRAMS (R.string.us_kilograms),
    SLICE (R.string.us_slice),
    TEASPOON (R.string.us_teaspoon),
    TABLESPOON (R.string.us_tablespoon),
    PIECE (R.string.us_piece),
    CUP (R.string.us_cup),
    PRIZE (R.string.us_prize),
    BOTTLE (R.string.us_bottle),
    MILLILITERS (R.string.us_milliliters),
    LITER (R.string.us_liter);

    /**
     * String resource id representing the value for the string
     */
    private final int stringResId;



    IngredientUnit(final int stringResId)
    {
        this.stringResId = stringResId;
    }


    /**
     * Gets the resource id which represents the string value
     * @return resource id
     */
    public int getId()
    {
        return this.stringResId;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(Context context)
    {
        return context.getString(getId());
    }
}