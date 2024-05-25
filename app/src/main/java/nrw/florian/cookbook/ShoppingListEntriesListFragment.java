package nrw.florian.cookbook;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nrw.florian.cookbook.adapter.ActiveEntriesRecyclerViewAdapter;
import nrw.florian.cookbook.databinding.FragmentShoppingListItemBinding;

public class ShoppingListEntriesListFragment extends ListFragment {
    //    private ShoppingListCursorAdapter adapter;
    private Cursor cursor;
    private ArrayList<ShoppingListEntry> entries;
    private FragmentShoppingListItemBinding binding;
    private RecyclerView activeEntriesRecyclerView;
    private ActiveEntriesRecyclerViewAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setListAdapter(new ActiveEntriesBaseAdapter(getContext()));
        setUpEntries();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentShoppingListItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
