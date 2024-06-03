package nrw.florian.cookbook.db.recipeproperty;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nrw.florian.cookbook.db.DBInfo;
import nrw.florian.cookbook.db.DatabaseOpenHelper;
import nrw.florian.cookbook.db.RecipeFindable;
import nrw.florian.cookbook.db.recipe.RecipeEntity;


/**
 * @author Florian J. Kleine-Vorholt
 */
public class RecipePropertyDatabaseOpenHelper
        extends DatabaseOpenHelper<RecipePropertyEntity>
        implements RecipeFindable<RecipePropertyEntity> {

    /**
     * Creates a new instance of RecipePropertyDatabaseOpenHelper
     * @param context the context
     */
    public RecipePropertyDatabaseOpenHelper(final Context context)
    {
        super(context, null, 1);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public List<RecipePropertyEntity> findAll()
    {
        try (final Cursor cursor = getReadableDatabase().query(DBInfo.TABLE_RECIPE_RECIPEPROPERTY,
                new String[] { "_id", "recipeId", "property" },
                null, null, null, null, null))
        {
            final List<RecipePropertyEntity> list = new ArrayList<>();
            while (cursor.moveToNext())
                list.add(new RecipePropertyEntity(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        RecipeProperty.valueOf(cursor.getString(2))));
            return list;
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<RecipePropertyEntity> findById(int id)
    {
        try (final Cursor cursor = getReadableDatabase().query(DBInfo.TABLE_RECIPE_RECIPEPROPERTY,
                new String[] { "_id", "recipeId", "property" },
                "_id = ?",
                new String[] { String.valueOf(id) },
                null, null, null))
        {
            if (cursor.moveToFirst())
                return Optional.of(new RecipePropertyEntity(cursor.getInt(0),
                        cursor.getInt(1),
                        RecipeProperty.valueOf(cursor.getString(2))));
        }
        return Optional.empty();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<RecipePropertyEntity> findByRecipeId(final int recipeID)
    {
        try (final Cursor cursor = getReadableDatabase().query(DBInfo.TABLE_RECIPE_RECIPEPROPERTY,
                new String[]{"_id", "recipeId", "property"},
                "recipeId = " + recipeID,
                null, null, null, null))
        {
            final List<RecipePropertyEntity> propertyEntities = new ArrayList<>();
            while (cursor.moveToNext()) {
                propertyEntities.add(new RecipePropertyEntity(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        RecipeProperty.valueOf(cursor.getString(2))
                ));
            }
            return propertyEntities;
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveOrUpdate(RecipePropertyEntity entity)
    {
        final ContentValues values = new ContentValues();
        values.put("recipeId", entity.getRecipeID());
        values.put("property", entity.getProperty().name());

        // UPDATE
        if (entity.getId() != 0 && exists(entity.getId())) {
            return getWritableDatabase().update(DBInfo.TABLE_RECIPE_RECIPEPROPERTY,
                    values, "_id = " + entity.getId(), null) == 1;
        }

        // INSERT
        final long _id = getWritableDatabase()
                .insert(DBInfo.TABLE_RECIPE_RECIPEPROPERTY, null, values);
        if (_id == -1) {
            entity.setId((int) _id);
            return true;
        }
        return false;
    }


    /**
     * Saves or updates a list of properties
     * @param entities the properties
     * @return true if all properties were saved or updated
     */
    public boolean saveOrUpdate(final List<RecipePropertyEntity> entities) {
        int cnt = 0;
        for (RecipePropertyEntity entity : entities) {
            if (saveOrUpdate(entity)) {
                cnt++;
            }
        }
        return cnt == entities.size();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(RecipePropertyEntity entity)
    {
        return getWritableDatabase()
                .delete(DBInfo.TABLE_RECIPE_RECIPEPROPERTY,
                        "_id = " + entity.getId(), null) == 1;
    }


    /**
     * Removes all properties for a recipe
     * @param recipe the recipe
     */
    public void removeAllForRecipe(final RecipeEntity recipe)
    {
        getWritableDatabase().delete(DBInfo.TABLE_RECIPE_RECIPEPROPERTY,
                "recipeID = " + recipe.getId(), null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(int id)
    {
        try (final Cursor cursor = getReadableDatabase().query(DBInfo.TABLE_RECIPE_RECIPEPROPERTY,
                new String[] { "_id" },
                "_id = " + id,
                null, null, null, null))
        {
            return cursor.moveToNext();
        }
    }
}