package nrw.florian.cookbook.repositories;

import java.util.List;

import nrw.florian.cookbook.db.listener.DatabaseChangeListener;
import nrw.florian.cookbook.db.shoppinglist.ShoppingListItemDatabaseOpenHelper;
import nrw.florian.cookbook.db.shoppinglist.ShoppingListItemEntity;

public class ShoppingListRepository {
    private DatabaseChangeListener databaseChangeListener;
    private final ShoppingListItemDatabaseOpenHelper db;
    public ShoppingListRepository(ShoppingListItemDatabaseOpenHelper db) {
        this.db = db;
    }

    public void upsert(ShoppingListItemEntity item) {
        db.saveOrUpdate(item);
        updateDatabaseListener();
    }

    public void delete(ShoppingListItemEntity item) {
        db.remove(item);
        updateDatabaseListener();
    }

    public List<ShoppingListItemEntity> getAllEntriesOfType(boolean isDone) {
        return db.getAllEntriesOfType(isDone);
    }

    public void updateDatabaseListener() {
        if (databaseChangeListener != null) {
            databaseChangeListener.onDataChanged();
        }
    }

    public void setDatabaseChangeListener(DatabaseChangeListener listener) {
        this.databaseChangeListener = listener;
    }
}
