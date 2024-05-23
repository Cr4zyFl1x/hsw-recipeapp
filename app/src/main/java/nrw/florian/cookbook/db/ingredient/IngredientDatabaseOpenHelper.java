package nrw.florian.cookbook.db.ingredient;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nrw.florian.cookbook.db.DBInfo;
import nrw.florian.cookbook.db.DatabaseOpenHelper;

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
                new String[]{"_id", "name"},
                null,
                null,
                null,
                null,
                null)) {

            List<IngredientEntity> ingredientEntities = new ArrayList<>();
            while (cursor.moveToNext()) {
                ingredientEntities.add(new IngredientEntity(cursor.getInt(0), cursor.getString(1)));
            }
            return ingredientEntities;
        }
    }


    @Override
    public Optional<IngredientEntity> findById(final int id)
    {
        try (final Cursor cursor = getReadableDatabase().query(
                DBInfo.TABLE_INGREDIENT,
                new String[]{"_id", "name"},
                "_id = " + id,
                null,
                null,
                null,
                null)) {
            if (cursor.moveToNext()) {
                return Optional.of(new IngredientEntity(cursor.getInt(0), cursor.getString(1)));
            }
            return Optional.empty();
        }
    }


    @Override
    public void saveOrUpdate(final IngredientEntity entity)
    {

    }


    @Override
    public void remove(final IngredientEntity entity)
    {

    }
}
