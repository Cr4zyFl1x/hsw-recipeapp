package nrw.florian.cookbook.db.ingredient;

import android.content.Context;

import nrw.florian.cookbook.R;

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



    private final int stringResId;

    IngredientUnit(final int stringResId)
    {
        this.stringResId = stringResId;
    }

    public int getId()
    {
        return this.stringResId;
    }

    public String getUnit(final Context ctx)
    {
        return ctx.getString(getId());
    }
}
