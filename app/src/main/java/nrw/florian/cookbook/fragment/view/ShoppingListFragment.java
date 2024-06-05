package nrw.florian.cookbook.fragment.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import nrw.florian.cookbook.R;
import nrw.florian.cookbook.adapter.ShoppingListRecyclerViewAdapter;
import nrw.florian.cookbook.databinding.FragmentShoppingListBinding;
import nrw.florian.cookbook.db.listener.DatabaseChangeListener;
import nrw.florian.cookbook.db.shoppinglist.ShoppingListItemDatabaseOpenHelper;
import nrw.florian.cookbook.db.shoppinglist.ShoppingListItemEntity;
import nrw.florian.cookbook.repositories.ShoppingListRepository;

public class ShoppingListFragment extends Fragment implements DatabaseChangeListener {

    private FragmentShoppingListBinding binding;
    private RecyclerView activeEntriesRecyclerView;
    private RecyclerView completedEntriesRecyclerView;
    private ShoppingListRecyclerViewAdapter activeEntriesAdapter;
    private ShoppingListRecyclerViewAdapter completedEntriesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentShoppingListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeActiveEntriesRecyclerView();
        initializeCompletedEntriesRecyclerView();

        binding.addEntryButton.setOnClickListener(v -> {
            if (!isFieldFilled(binding.addEntryInput)) {
                binding.addEntryInput.setError(getString(R.string.please_insert_entry));
                return;
            }
            try (ShoppingListItemDatabaseOpenHelper databaseOpenHelper = new ShoppingListItemDatabaseOpenHelper(requireContext())) {
                ShoppingListRepository repository = new ShoppingListRepository(databaseOpenHelper);
                repository.setDatabaseChangeListener(this);
                repository.upsert(new ShoppingListItemEntity(binding.addEntryInput.getText().toString(), false));
            }
            binding.addEntryInput.setText("");
        });

        showCompletedEntriesHeaderWhenThereAreCompletedEntries();
    }

    private void initializeCompletedEntriesRecyclerView() {
        LinearLayoutManager completedEntriesManager = new LinearLayoutManager(getContext());
        completedEntriesRecyclerView = requireView().findViewById(R.id.completedEntriesList);
        completedEntriesRecyclerView.setLayoutManager(completedEntriesManager);
        try (ShoppingListItemDatabaseOpenHelper databaseOpenHelper = new ShoppingListItemDatabaseOpenHelper(requireContext())) {
            ShoppingListRepository repository = new ShoppingListRepository(databaseOpenHelper);
            repository.setDatabaseChangeListener(this);
            completedEntriesAdapter = new ShoppingListRecyclerViewAdapter(databaseOpenHelper.getAllEntriesOfType(true), repository, true);
            completedEntriesRecyclerView.setAdapter(completedEntriesAdapter);
        }
    }

    private void initializeActiveEntriesRecyclerView() {
        LinearLayoutManager activeEntriesManager = new LinearLayoutManager(getContext());
        activeEntriesRecyclerView = requireView().findViewById(R.id.activeEntriesList);
        activeEntriesRecyclerView.setLayoutManager(activeEntriesManager);
        try (ShoppingListItemDatabaseOpenHelper databaseOpenHelper = new ShoppingListItemDatabaseOpenHelper(requireContext())) {
            ShoppingListRepository repository = new ShoppingListRepository(databaseOpenHelper);
            repository.setDatabaseChangeListener(this);
            activeEntriesAdapter = new ShoppingListRecyclerViewAdapter(databaseOpenHelper.getAllEntriesOfType(false) ,repository, false);
            activeEntriesRecyclerView.setAdapter(activeEntriesAdapter);
        }
    }

    private boolean isFieldFilled(EditText field) {
        return !field.getText().toString().isEmpty();
    }

    @Override
    public void onDataChanged() {
        try (ShoppingListItemDatabaseOpenHelper databaseOpenHelper = new ShoppingListItemDatabaseOpenHelper(requireContext())) {
            ShoppingListRepository repository = new ShoppingListRepository(databaseOpenHelper);
            repository.setDatabaseChangeListener(this);
            activeEntriesAdapter.setItems(repository.getAllEntriesOfType(false));
            activeEntriesRecyclerView.setAdapter(activeEntriesAdapter);

            completedEntriesAdapter.setItems(repository.getAllEntriesOfType(true));
            completedEntriesRecyclerView.setAdapter(completedEntriesAdapter);

            showCompletedEntriesHeaderWhenThereAreCompletedEntries();
        }
    }

    private void showCompletedEntriesHeaderWhenThereAreCompletedEntries() {
        if (completedEntriesAdapter.getItemCount() > 0) {
            binding.completedEntriesText.setVisibility(View.VISIBLE);
        } else {
            binding.completedEntriesText.setVisibility(View.GONE);
        }
    }
}
