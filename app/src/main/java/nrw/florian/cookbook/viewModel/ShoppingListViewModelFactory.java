package nrw.florian.cookbook.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import nrw.florian.cookbook.repositories.ShoppingListRepository;

public class ShoppingListViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final ShoppingListRepository repository;
    public ShoppingListViewModelFactory(ShoppingListRepository repository) {
        this.repository = repository;
    }

    /** @noinspection unchecked*/
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ShoppingListViewModel(repository);
    }
}
