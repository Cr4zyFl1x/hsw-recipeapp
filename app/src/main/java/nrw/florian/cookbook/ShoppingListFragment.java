package nrw.florian.cookbook;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nrw.florian.cookbook.adapter.ActiveEntriesBaseAdapter;
import nrw.florian.cookbook.adapter.ActiveEntriesRecyclerViewAdapter;
import nrw.florian.cookbook.databinding.FragmentShoppingListBinding;

public class ShoppingListFragment extends Fragment {
    private FragmentShoppingListBinding binding;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ActiveEntriesBaseAdapter adapter;
    private ArrayList<ShoppingListEntry> entries;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpEntries();

        RecyclerView recyclerView = container.findViewById(R.id.activeEntriesList);

        ActiveEntriesRecyclerViewAdapter adapter = new ActiveEntriesRecyclerViewAdapter(getContext(), entries);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        /*binding = FragmentShoppingListBinding.inflate(inflater, container, false);
        recyclerView = container.findViewById(R.id.activeEntriesList);
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);*/

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.addEntryButton.setOnClickListener(v -> {
            if (!binding.addEntryInput.getText().toString().isEmpty()) {
                // TODO: neuer Datenbankeintrag
            }

            binding.addEntryInput.setText("");
        });

        FragmentTransaction activeEntriesTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        activeEntriesTransaction.add(R.id.activeEntriesList, new ShoppingListEntriesListFragment());
        activeEntriesTransaction.commit();

        FragmentTransaction completedEntriesTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        completedEntriesTransaction.add(R.id.completedEntriesList, new ShoppingListEntriesListFragment());
        completedEntriesTransaction.commit();

        TextView text = binding.completedEntriesText;
        text.setPaintFlags(text.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
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
