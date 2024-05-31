package nrw.florian.cookbook.fragment.view;

import android.database.Cursor;
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

import java.util.ArrayList;

import nrw.florian.cookbook.R;
import nrw.florian.cookbook.ShoppingListEntry;
import nrw.florian.cookbook.adapter.ActiveEntriesRecyclerViewAdapter;
import nrw.florian.cookbook.databinding.FragmentShoppingListBinding;
import nrw.florian.cookbook.db.shoppinglist.ShoppingListItemDatabaseOpenHelper;
import nrw.florian.cookbook.db.shoppinglist.ShoppingListItemEntity;

public class ShoppingListFragment extends Fragment {
    private FragmentShoppingListBinding binding;
    private ArrayList<ShoppingListEntry> entries;
    private RecyclerView activeEntriesRecyclerView;
    private RecyclerView completedEntriesRecyclerView;
    private ActiveEntriesRecyclerViewAdapter activeEntriesAdapter;
    private ActiveEntriesRecyclerViewAdapter completedEntriesAdapter;

    private ShoppingListItemDatabaseOpenHelper databaseOpenHelper;
    private Cursor cursor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpEntries();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentShoppingListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseOpenHelper = new ShoppingListItemDatabaseOpenHelper(requireContext());

        LinearLayoutManager activeEntriesManager = new LinearLayoutManager(getContext());
        activeEntriesRecyclerView = view.findViewById(R.id.activeEntriesList);
        activeEntriesRecyclerView.setLayoutManager(activeEntriesManager);
       /* activeEntriesAdapter = new ActiveEntriesRecyclerViewAdapter(this.getContext(), activeEntriesAdapter.getCursor(), true);
        activeEntriesRecyclerView.setAdapter(activeEntriesAdapter);*/

        LinearLayoutManager completedEntriesManager = new LinearLayoutManager(getContext());
        completedEntriesRecyclerView = view.findViewById(R.id.completedEntriesList);
        completedEntriesRecyclerView.setLayoutManager(completedEntriesManager);
       /* completedEntriesAdapter = new ActiveEntriesRecyclerViewAdapter(this.getContext(), activeEntriesAdapter.getCursor(), true);
        completedEntriesRecyclerView.setAdapter(completedEntriesAdapter);*/

        updateAdapter();

        binding.addEntryButton.setOnClickListener(v -> {
            if (!isFieldFilled(binding.addEntryInput)) {
                binding.addEntryInput.setError("Bitte Eintrag eingeben");
                return;
            }

            // TODO: neuer Datenbankeintrag
            insert(binding.addEntryInput.getText().toString());
            System.out.println("success!");

            binding.addEntryInput.setText("");
        });
    }

    private boolean isFieldFilled(EditText field) {
        return !field.getText().toString().isEmpty();
    }

    private void updateAdapter() {
        if (databaseOpenHelper.selectCursor() != null || databaseOpenHelper.selectCursor().getCount() > 0) {
            activeEntriesAdapter = new ActiveEntriesRecyclerViewAdapter(this.getContext(), cursor, true);
            activeEntriesRecyclerView.setAdapter(activeEntriesAdapter);

            completedEntriesAdapter = new ActiveEntriesRecyclerViewAdapter(this.getContext(), cursor, false);
            completedEntriesRecyclerView.setAdapter(completedEntriesAdapter);
        }
    }

    private void insert(String title) {
        databaseOpenHelper.saveOrUpdate(new ShoppingListItemEntity(title, false));
    }

    public void setUpEntries() {
        this.entries = new ArrayList<>();
        entries.add(new ShoppingListEntry("Äpfel"));
        entries.add(new ShoppingListEntry("Bananen"));
        entries.add(new ShoppingListEntry("Brot"));
        entries.add(new ShoppingListEntry("Milch"));
        entries.add(new ShoppingListEntry("Wasser"));
    }
}
