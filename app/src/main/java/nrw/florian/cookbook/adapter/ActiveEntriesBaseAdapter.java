package nrw.florian.cookbook.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import nrw.florian.cookbook.ShoppingListEntry;
import nrw.florian.cookbook.databinding.FragmentShoppingListItemBinding;

public class ActiveEntriesBaseAdapter extends BaseAdapter {

    private final Context context;
    private final LayoutInflater inflater;
    private List<ShoppingListEntry> entries;

    public ActiveEntriesBaseAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        entries = new ArrayList<>();
        entries.add(new ShoppingListEntry("Äpfel"));
        entries.add(new ShoppingListEntry("Bananen"));
        entries.add(new ShoppingListEntry("Brot"));
        entries.add(new ShoppingListEntry("Milch"));
        entries.add(new ShoppingListEntry("Wasser"));
    }
    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public Object getItem(int position) {
//        return ShoppingListEntries.values().length;
        return entries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        layoutInflater = LayoutInflater.from(this.context);

        final ShoppingListEntry entryString = entries.get(position);

        FragmentShoppingListItemBinding entry;
        if (null == convertView) {
            entry = FragmentShoppingListItemBinding.inflate(LayoutInflater.from(context));
            entry = FragmentShoppingListItemBinding.inflate(LayoutInflater.from(context));
        } else{
            entry = FragmentShoppingListItemBinding.bind(convertView);
        }

//        ShoppingListEntries currentEntry = (ShoppingListEntries) getItem(position);
        ShoppingListEntry currentEntry = (ShoppingListEntry) getItem(position);

//        entry.shoppingsListEntry.setText(currentEntry.getName());
        entry.shoppingsListEntry.setText(currentEntry.getName());

        String tag = ActiveEntriesBaseAdapter.class.getSimpleName();
        Log.i(tag, "Index: " + position + " : " + entry);
        return entry.getRoot();
    }
}
