package nrw.florian.cookbook.repositories;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Optional;

import nrw.florian.cookbook.db.shoppinglist.ShoppingListItemDatabaseOpenHelper;
import nrw.florian.cookbook.db.shoppinglist.ShoppingListItemEntity;

public class ShoppingListRepository {
    private final ShoppingListItemDatabaseOpenHelper db;
    public ShoppingListRepository(ShoppingListItemDatabaseOpenHelper db) {
        this.db = db;
    }

    public void upsert(ShoppingListItemEntity item) {
        db.saveOrUpdate(item);
        db.updateDatabaseListener();
    }

    public void delete(ShoppingListItemEntity item) {
        db.remove(item);
        db.updateDatabaseListener();
    }

    public LiveData<List<ShoppingListItemEntity>> getAllEntriesOfTypeLive(boolean isDone) {
        return db.getAllEntriesOfTypeLive(isDone);
    }

    public Optional<ShoppingListItemEntity> getItemById(int id) {
        return db.findById(id);
    }
}
