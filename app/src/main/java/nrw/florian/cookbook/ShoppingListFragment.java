package nrw.florian.cookbook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nrw.florian.cookbook.adapter.ActiveEntriesRecyclerViewAdapter;
import nrw.florian.cookbook.adapter.CompletedEntriesRecyclerViewAdapter;
import nrw.florian.cookbook.databinding.FragmentShoppingListBinding;

public class ShoppingListFragment extends Fragment {
    private FragmentShoppingListBinding binding;
    private ArrayList<ShoppingListEntry> entries;
    private RecyclerView activeEntriesRecyclerView;
    private RecyclerView completedEntriesRecyclerView;
    private ActiveEntriesRecyclerViewAdapter activeEntriesAdapter;
    private CompletedEntriesRecyclerViewAdapter completedEntriesAdapter;

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

        LinearLayoutManager activeEntriesManager = new LinearLayoutManager(getContext());
        activeEntriesRecyclerView = view.findViewById(R.id.activeEntriesList);
        activeEntriesRecyclerView.setLayoutManager(activeEntriesManager);
        activeEntriesAdapter = new ActiveEntriesRecyclerViewAdapter(this.getContext(), entries);
        activeEntriesRecyclerView.setAdapter(activeEntriesAdapter);

        LinearLayoutManager completedEntriesManager = new LinearLayoutManager(getContext());
        completedEntriesRecyclerView = view.findViewById(R.id.completedEntriesList);
        completedEntriesRecyclerView.setLayoutManager(completedEntriesManager);
        completedEntriesAdapter = new CompletedEntriesRecyclerViewAdapter(this.getContext(), entries);
        completedEntriesRecyclerView.setAdapter(completedEntriesAdapter);

        binding.addEntryButton.setOnClickListener(v -> {
            if (!binding.addEntryInput.getText().toString().isEmpty()) {
                // TODO: neuer Datenbankeintrag
            }

            binding.addEntryInput.setText("");
        });
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
