package nrw.florian.cookbook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nrw.florian.cookbook.adapter.ActiveEntriesRecyclerViewAdapter;
import nrw.florian.cookbook.databinding.FragmentShoppingList2Binding;

public class ShoppingListFragment2 extends Fragment {
    private FragmentShoppingList2Binding binding;
    private ArrayList<ShoppingListEntry> entries;
    private RecyclerView activeEntriesRecyclerView;
    private RecyclerView completedEntriesRecyclerView;
    private ActiveEntriesRecyclerViewAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpEntries();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentShoppingList2Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activeEntriesRecyclerView = view.findViewById(R.id.activeEntriesList2);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        activeEntriesRecyclerView.setLayoutManager(manager);

        adapter = new ActiveEntriesRecyclerViewAdapter(this.getContext(), entries);

        activeEntriesRecyclerView.setAdapter(adapter);

        FragmentTransaction activeEntriesTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        ShoppingListEntriesListFragment fragment = new ShoppingListEntriesListFragment();
        activeEntriesTransaction.replace(R.id.activeEntriesList2, fragment); // TODO add oder replace?
        activeEntriesTransaction.commit();

        binding.addEntryButton2.setOnClickListener(v -> {
            if (!binding.addEntryInput2.getText().toString().isEmpty()) {
                // TODO: neuer Datenbankeintrag
            }

            binding.addEntryInput2.setText("");
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
