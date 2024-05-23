package nrw.florian.cookbook.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * Helper class encapsulating default db methods.
 *
 * @param <T> The EntityType
 *
 * @author Florian J. Kleine-Vorholt
 */
public abstract class DatabaseOpenHelper<T> extends SQLiteOpenHelper {


    public DatabaseOpenHelper(@Nullable Context context,
                              @Nullable SQLiteDatabase.CursorFactory factory,
                              int version)
    {
        super(context, DBInfo.DATABASE_NAME, factory, version);
    }

    public DatabaseOpenHelper(@Nullable Context context,
                              @Nullable SQLiteDatabase.CursorFactory factory,
                              int version, @Nullable DatabaseErrorHandler errorHandler)
    {
        super(context, DBInfo.DATABASE_NAME, factory, version, errorHandler);
    }

    public DatabaseOpenHelper(@Nullable Context context,
                              int version,
                              @NonNull SQLiteDatabase.OpenParams openParams)
    {
        super(context, DBInfo.DATABASE_NAME, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DBInfo.SCHEMA_INGREDIENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.d(getClass().getSimpleName(), "DB-Schema update is not implemented!");
    }

    public abstract List<T> findAll();

    public abstract Optional<T> findById(final int id);

    public abstract void saveOrUpdate(final T entity);

    public abstract void remove(final T entity);
}
