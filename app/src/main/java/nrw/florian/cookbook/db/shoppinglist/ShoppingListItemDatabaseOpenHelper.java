package nrw.florian.cookbook.db.shoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nrw.florian.cookbook.db.DBInfo;
import nrw.florian.cookbook.db.DatabaseOpenHelper;
import nrw.florian.cookbook.db.listener.DatabaseChangeListener;

/**
 * @author Florian J. Kleine-Vorholt
 */
public class ShoppingListItemDatabaseOpenHelper extends DatabaseOpenHelper<ShoppingListItemEntity> {
    private DatabaseChangeListener databaseChangeListener;

    /**
     * Creates a new instance of {@link ShoppingListItemDatabaseOpenHelper}.
     * @param context the context in which to open the database
     */
    private final MutableLiveData<List<ShoppingListItemEntity>> liveDataList = new MutableLiveData<>();

    public ShoppingListItemDatabaseOpenHelper(Context context)
    {
        super(context, null, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ShoppingListItemEntity> findAll()
    {
        try (final Cursor cursor = getReadableDatabase().query(DBInfo.TABLE_SHOPPINGLISTITEM,
                new String[]{"_id", "title", "done"},
                null,
                null,
                null,
                null,
                null)) {
            final List<ShoppingListItemEntity> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                list.add(new ShoppingListItemEntity(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2) == 1));
            }
            return list;
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ShoppingListItemEntity> findById(int id)
    {
        try (final Cursor cursor = getReadableDatabase().query(DBInfo.TABLE_SHOPPINGLISTITEM,
                new String[]{"_id", "title", "done"},
                "_id = " + id,
                null,
                null,
                null,
                null)) {
            if (cursor.moveToNext()) {
                return Optional.of(new ShoppingListItemEntity(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2) == 1
                ));
            }
            return Optional.empty();
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveOrUpdate(ShoppingListItemEntity entity)
    {
        final ContentValues values = new ContentValues();
        values.put("title", entity.getTitle());
        values.put("done", entity.isDone());

        // UPDATE
        if(entity.getId() != 0 && exists(entity.getId())) {
            return getWritableDatabase()
                    .update(DBInfo.TABLE_SHOPPINGLISTITEM, values,
                            "_id = " + entity.getId(), null) == 1;
        }

        // INSERT
        long id = getWritableDatabase().insert(DBInfo.TABLE_SHOPPINGLISTITEM, null, values);
        if (id != -1) {
            entity.setId((int) id);
            return true;
        }
        return false;
    }

    public void updateDatabaseListener() {
        if (databaseChangeListener != null) {
            databaseChangeListener.onDataChanged();
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(ShoppingListItemEntity entity)
    {
        updateDatabaseListener();
        return getWritableDatabase().delete(DBInfo.TABLE_SHOPPINGLISTITEM,
                "_id = " + entity.getId(), null) == 1;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(int id) {
        try (final Cursor cursor = getReadableDatabase().query(DBInfo.TABLE_SHOPPINGLISTITEM,
                new String[]{"_id"},
                "_id = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null)) {
            return cursor.moveToNext();
        }
    }

    public List<ShoppingListItemEntity> getAllEntriesOfType (boolean isDone) {
        int activeNum = isDone ? 1 : 0;

        try (final Cursor cursor = getReadableDatabase().query(DBInfo.TABLE_SHOPPINGLISTITEM,
                new String[]{"_id", "title", "done"},
                "done = ?",
                new String[]{String.valueOf(activeNum)},
                null,
                null,
                null)) {
            final List<ShoppingListItemEntity> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                list.add(new ShoppingListItemEntity(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2) == 1));
            }
            return list;
        }
    }

    public LiveData<List<ShoppingListItemEntity>> getAllEntriesOfTypeLive(boolean active) {
        int activeNum = active ? 1 : 0;

        try (final Cursor cursor = getReadableDatabase().query(DBInfo.TABLE_SHOPPINGLISTITEM,
                new String[]{"_id", "title", "done"},
                "done = ?",
                new String[]{String.valueOf(activeNum)},
                null,
                null,
                null)) {
            final List<ShoppingListItemEntity> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                list.add(new ShoppingListItemEntity(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2) == 1));
            }
            liveDataList.setValue(list);
            return liveDataList;
        }
    }

    public void setDatabaseChangeListener(DatabaseChangeListener listener) {
        this.databaseChangeListener = listener;
    }
}