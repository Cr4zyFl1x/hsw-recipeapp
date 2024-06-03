package nrw.florian.cookbook.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Optional;

import nrw.florian.cookbook.db.shoppinglist.ShoppingListItemEntity;
import nrw.florian.cookbook.repositories.ShoppingListRepository;

public class ShoppingListViewModel extends ViewModel {
    private final ShoppingListRepository repository;

    public ShoppingListViewModel(ShoppingListRepository repository) {
        this.repository = repository;
    }

    public void upsert(ShoppingListItemEntity item) {
        repository.upsert(item);
    }

    public void delete(ShoppingListItemEntity item) {
        repository.delete(item);
    }

    public LiveData<List<ShoppingListItemEntity>> getAllEntriesOfTypeLive(boolean isDone) {
        return repository.getAllEntriesOfTypeLive(isDone);
    }

    public Optional<ShoppingListItemEntity> getItemById(int id) {
        return repository.getItemById(id);
    }
}
