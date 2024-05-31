package nrw.florian.cookbook.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Helper class encapsulating default db methods.
 *
 * @param <T> The EntityType
 *
 * @author Florian J. Kleine-Vorholt
 */
public abstract class DatabaseOpenHelper<T>
        extends SQLiteOpenHelper
        implements BasicDatabaseOperations<T> {

    /**
     * Context to use for locating paths to the the database.
     */
    private final Context context;


    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use for locating paths to the the database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *     {@link #onUpgrade} will be used to upgrade the database; if the database is
     *     newer, {@link #onDowngrade} will be used to downgrade the database
     */
    public DatabaseOpenHelper(@Nullable Context context,
                              @Nullable SQLiteDatabase.CursorFactory factory,
                              int version)
    {
        super(context, DBInfo.DATABASE_NAME, factory, version);
        this.context = context;
    }


    /**
     * Create a helper object to create, open, and/or manage a database.
     * The database is not actually created or opened until one of
     * {@link #getWritableDatabase} or {@link #getReadableDatabase} is called.
     *
     * <p>Accepts input param: a concrete instance of {@link DatabaseErrorHandler} to be
     * used to handle corruption when sqlite reports database corruption.</p>
     *
     * @param context to use for locating paths to the the database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *     {@link #onUpgrade} will be used to upgrade the database; if the database is
     *     newer, {@link #onDowngrade} will be used to downgrade the database
     * @param errorHandler the {@link DatabaseErrorHandler} to be used when sqlite reports database
     * corruption, or null to use the default error handler.
     */
    public DatabaseOpenHelper(@Nullable Context context,
                              @Nullable SQLiteDatabase.CursorFactory factory,
                              int version, @Nullable DatabaseErrorHandler errorHandler)
    {
        super(context, DBInfo.DATABASE_NAME, factory, version, errorHandler);
        this.context = context;
    }


    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use for locating paths to the the database
     * @param version number of the database (starting at 1); if the database is older,
     *     {@link #onUpgrade} will be used to upgrade the database; if the database is
     *     newer, {@link #onDowngrade} will be used to downgrade the database
     * @param openParams configuration parameters that are used for opening {@link SQLiteDatabase}.
     *        Please note that {@link SQLiteDatabase#CREATE_IF_NECESSARY} flag will always be
     *        set when the helper opens the database
     */
    public DatabaseOpenHelper(@Nullable Context context,
                              int version,
                              @NonNull SQLiteDatabase.OpenParams openParams)
    {
        super(context, DBInfo.DATABASE_NAME, version, openParams);
        this.context = context;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DBInfo.SCHEMA_RECIPE);
        db.execSQL(DBInfo.SCHEMA_INGREDIENT);
        db.execSQL(DBInfo.SCHEMA_RECIPE_RECIPEPROPERTY);
        db.execSQL(DBInfo.SCHEMA_SHOPPINGLISTITEM);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.d(getClass().getSimpleName(), "DB-Schema update is not implemented!");
    }

    public Context getContext()
    {
        return context;
    }
}