package nrw.florian.cookbook.db.ingredient;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nrw.florian.cookbook.db.DBInfo;
import nrw.florian.cookbook.db.DatabaseOpenHelper;
import nrw.florian.cookbook.db.recipe.RecipeEntity;

/**
 * @author Florian J. Kleine-Vorholt
 */
public class IngredientDatabaseOpenHelper extends DatabaseOpenHelper<IngredientEntity> {


    public IngredientDatabaseOpenHelper(final Context context)
    {
        super(context, null, 1);
    }


    @Override
    public List<IngredientEntity> findAll()
    {
        try (final Cursor cursor = getReadableDatabase().query(
                DBInfo.TABLE_INGREDIENT,
                new String[]{"_id", "recipeID", "name", "quantity", "unit"},
                null,
                null,
                null,
                null,
                null)) {

            List<IngredientEntity> ingredientEntities = new ArrayList<>();
            while (cursor.moveToNext()) {
                ingredientEntities.add(new IngredientEntity(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        IngredientUnit.valueOf(cursor.getString(4))));
            }
            return ingredientEntities;
        }
    }


    @Override
    public Optional<IngredientEntity> findById(final int id)
    {
        try (final Cursor cursor = getReadableDatabase().query(
                DBInfo.TABLE_INGREDIENT,
                new String[]{"_id", "recipeID", "name", "quantity", "unit"},
                "_id = " + id,
                null,
                null,
                null,
                null)) {
            if (cursor.moveToNext()) {
                return Optional.of(new IngredientEntity(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        IngredientUnit.valueOf(cursor.getString(4))));
            }
            return Optional.empty();
        }
    }


    /**
     * Finds all ingredients for a recipe id
     * @param recipeID the id of the recipe
     * @return the ingredients
     */
    public List<IngredientEntity> findByRecipeId(final int recipeID)
    {
        try (final Cursor cursor = getReadableDatabase().query(DBInfo.TABLE_INGREDIENT,
                new String[]{"_id", "recipeID", "name", "quantity", "unit"},
                "recipeID = " + recipeID,
                null, null, null, null))
        {
            final List<IngredientEntity> ingredientEntities = new ArrayList<>();
            while (cursor.moveToNext()) {
                ingredientEntities.add(new IngredientEntity(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        IngredientUnit.valueOf(cursor.getString(4))));
            }
            return ingredientEntities;
        }
    }


    @Override
    public boolean saveOrUpdate(final IngredientEntity entity)
    {
        final ContentValues values = new ContentValues();
        values.put("recipeID", entity.getRecipeID());
        values.put("name", entity.getName());
        values.put("quantity", entity.getQuantity());
        values.put("unit", entity.getUnit().name());

        // UPDATE
        if (entity.getId() != 0 && exists(entity.getId())) {
            return getWritableDatabase()
                    .update(DBInfo.TABLE_INGREDIENT, values, "_id = " + entity.getId(),
                            null) == 1;
        }

        // INSERT
        long id = getWritableDatabase()
                .insert(DBInfo.TABLE_INGREDIENT, null, values);
        if (id != -1) {
            entity.setId((int) id);
            return true;
        }
        return false;
    }


    /**
     * Saves or updates a list of ingredients
     * @param entities the ingredients
     * @return true if all ingredients were saved or updated
     */
    public boolean saveOrUpdate(final List<IngredientEntity> entities) {
        int cnt = 0;
        for (IngredientEntity entity : entities) {
            if (saveOrUpdate(entity)) {
                cnt++;
            }
        }
        return cnt == entities.size();
    }


    @Override
    public boolean remove(final IngredientEntity entity)
    {
        return getWritableDatabase()
                .delete(DBInfo.TABLE_INGREDIENT, "_id = " + entity.getId(), null) == 1;
    }


    /**
     * Removes a list of ingredients
     * @param entities the ingredients
     * @return true if all ingredients were removed
     */
    public boolean remove(final List<IngredientEntity> entities) {
        int cnt = 0;
        for (IngredientEntity entity : entities) {
            if (remove(entity)) {
                cnt++;
            }
        }
        return cnt == entities.size();
    }


    /**
     * Removes all ingredients for a recipe
     * @param recipe the recipe
     */
    public void removeAllForRecipe(final RecipeEntity recipe)
    {
        getWritableDatabase().delete(DBInfo.TABLE_INGREDIENT,
                "recipeID = " + recipe.getId(), null);
    }


    @Override
    public boolean exists(final int id)
    {
        try (final Cursor cursor = getReadableDatabase().query(
                DBInfo.TABLE_INGREDIENT,
                new String[]{"_id"},
                "_id = " + id,
                null,
                null,
                null,
                null)) {
            return cursor.moveToNext();
        }
    }
}
