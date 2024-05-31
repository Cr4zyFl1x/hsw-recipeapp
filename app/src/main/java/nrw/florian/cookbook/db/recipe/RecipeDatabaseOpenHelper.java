package nrw.florian.cookbook.db.recipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nrw.florian.cookbook.db.DBInfo;
import nrw.florian.cookbook.db.DatabaseOpenHelper;
import nrw.florian.cookbook.db.ingredient.IngredientDatabaseOpenHelper;
import nrw.florian.cookbook.db.ingredient.IngredientEntity;
import nrw.florian.cookbook.db.recipeproperty.RecipePropertyDatabaseOpenHelper;
import nrw.florian.cookbook.db.recipeproperty.RecipePropertyEntity;
import nrw.florian.cookbook.util.ByteStreamUtil;

/**
 * @author Florian J. Kleine-Vorholt
 */
public class RecipeDatabaseOpenHelper extends DatabaseOpenHelper<RecipeEntity> {


    /**
     * Creates a new database helper
     * @param context Context
     */
    public RecipeDatabaseOpenHelper(Context context)
    {
        super(context, null, 1);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public List<RecipeEntity> findAll()
    {
        try (final Cursor cursor = getReadableDatabase().query(
                DBInfo.TABLE_RECIPE,
                new String[]{"_id", "title", "image", "defaultServings", "instructions", "category", "difficulty", "workTime", "calories"},
                null, null, null, null, null))
        {
            final List<RecipeEntity> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                list.add(objectFromCursor(cursor));
            }
            return list;
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<RecipeEntity> findById(int id)
    {
        try (final Cursor cursor = getReadableDatabase().query(
                DBInfo.TABLE_RECIPE,
                new String[]{"_id", "title", "image", "defaultServings", "instructions", "category", "difficulty", "workTime", "calories"},
                "_id = " + id,
                null, null, null, null))
        {
            if (cursor.moveToNext()) {
                return Optional.of(objectFromCursor(cursor));
            }
        }
        return Optional.empty();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveOrUpdate(RecipeEntity entity)
    {
        // DATA
        final ContentValues values = new ContentValues();
        values.put("title", entity.getTitle());
        values.put("image", ByteStreamUtil.bitmap2Byte(entity.getImage()));
        values.put("defaultServings", entity.getDefaultServings());
        values.put("instructions", entity.getInstructions());
        values.put("category", entity.getCategory().name());
        values.put("difficulty", entity.getDifficulty().name());
        values.put("workTime", entity.getWorkTime());
        values.put("calories", entity.getCalories());

        // UPDATE
        if (entity.getId() != 0 && exists(entity.getId())) {
            // UPDATE RECIPEDATA
            final int rows = getWritableDatabase().update(DBInfo.TABLE_RECIPE, values,
                    "_id = " + entity.getId(), null);

            // UPDATE INGREDIENTS
            try (final IngredientDatabaseOpenHelper idoh = new IngredientDatabaseOpenHelper(getContext())) {
                idoh.removeAllForRecipe(entity);
                entity.getIngredients().forEach(j -> j.setRecipeID(entity.getId()));
                idoh.saveOrUpdate(entity.getIngredients());
            }
            // UPDATE PROPS
            try (final RecipePropertyDatabaseOpenHelper rcdoh = new RecipePropertyDatabaseOpenHelper(getContext())) {
                rcdoh.removeAllForRecipe(entity);
                entity.getRecipePropertyEntities().forEach(j -> j.setRecipeID(entity.getId()));
                rcdoh.saveOrUpdate(entity.getRecipePropertyEntities());
            }

            return rows == 1;
        }

        // INSERT
        final long id = getWritableDatabase().insert(DBInfo.TABLE_RECIPE, null, values);
        entity.setId((int) id);

        // INSERT INGREDIENTS
        try (final IngredientDatabaseOpenHelper idoh = new IngredientDatabaseOpenHelper(getContext())) {
            entity.getIngredients().forEach(eh -> {
                eh.setRecipeID((int) id);
                idoh.saveOrUpdate(eh);
            });
        }

        // INSERT PROPERTIES
        try (final RecipePropertyDatabaseOpenHelper rcdoh = new RecipePropertyDatabaseOpenHelper(getContext())) {
            entity.getRecipePropertyEntities().forEach(eh -> {
                eh.setRecipeID((int) id);
                rcdoh.saveOrUpdate(eh);
            });
        }
        return id != -1;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(RecipeEntity entity)
    {
        try (RecipePropertyDatabaseOpenHelper rp = new RecipePropertyDatabaseOpenHelper(getContext());
        IngredientDatabaseOpenHelper ig = new IngredientDatabaseOpenHelper(getContext())) {
            rp.removeAllForRecipe(entity);
            ig.removeAllForRecipe(entity);
        }
        return getWritableDatabase()
                .delete(DBInfo.TABLE_RECIPE,
                        "_id = " + entity.getId(), null) == 1;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(int id)
    {
        try (final Cursor cursor = getReadableDatabase().query(
                DBInfo.TABLE_RECIPE,
                new String[]{"_id"},
                "_id = " + id,
                null,
                null,
                null,
                null)) {
            return cursor.moveToNext();
        }
    }


    /**
     * Creates a recipe entity from a cursor pointer
     * @param cursor Cursor with a pointer on a row
     * @return the recipe entity object
     */
    private RecipeEntity objectFromCursor(final Cursor cursor)
    {
        final int recipeID = cursor.getInt(0);
        final List<IngredientEntity> ingredientEntities;
        final List<RecipePropertyEntity> propertyEntities;

        // INGREDIENTS
        try (IngredientDatabaseOpenHelper ingredientDB = new IngredientDatabaseOpenHelper(getContext())) {
            ingredientEntities = ingredientDB.findByRecipeId(recipeID);
        }
        // RECIPE PROPERTIES
        try (RecipePropertyDatabaseOpenHelper recipePropDB = new RecipePropertyDatabaseOpenHelper(getContext())) {
            propertyEntities = recipePropDB.findByRecipeId(recipeID);
        }

        return new RecipeEntity(
                cursor.getInt(0),
                cursor.getString(1),
                ByteStreamUtil.byte2Bitmap(cursor.getBlob(2)),
                cursor.getInt(3),
                cursor.getString(4),
                RecipeBaseCategory.valueOf(cursor.getString(5)),
                RecipeDifficulty.valueOf(cursor.getString(6)),
                cursor.getInt(7),
                cursor.getInt(8),
                propertyEntities,
                ingredientEntities);
    }
}