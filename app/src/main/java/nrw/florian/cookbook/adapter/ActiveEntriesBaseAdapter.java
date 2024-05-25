package nrw.florian.cookbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import nrw.florian.cookbook.databinding.FragmentShoppingListItemBinding;
import nrw.florian.cookbook.enums.ShoppingListEntries;

public class ActiveEntriesBaseAdapter extends BaseAdapter {

    private final Context context;
    private final LayoutInflater inflater;

    public ActiveEntriesBaseAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return ShoppingListEntries.values().length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FragmentShoppingListItemBinding entry;
        if (null == convertView) {
            entry = FragmentShoppingListItemBinding.inflate(LayoutInflater.from(context));
            entry = FragmentShoppingListItemBinding.inflate(LayoutInflater.from(context));
        } else{
            entry = FragmentShoppingListItemBinding.bind(convertView);
        }

        ShoppingListEntries currentEntry = (ShoppingListEntries) getItem(position);

        entry.shoppingsListEntry.setText(currentEntry.getName());

        return entry.getRoot();
    }
}
