package nrw.florian.cookbook.db.ingredient;

import android.content.Context;

import androidx.annotation.NonNull;

import nrw.florian.cookbook.MainActivity;
import nrw.florian.cookbook.R;

/**
 * @author Florian J. Kleine-Vorholt
 */
public enum IngredientUnit {

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
     * Resolves the string value for the resource id
     * @param ctx Context to get the string value from
     * @return The String value for the unit
     */
    public String getUnit(final Context ctx)
    {
        return ctx.getString(getId());
    }

    @NonNull
    @Override
    public String toString() {
        return getUnit(MainActivity.getContext());
    }
}