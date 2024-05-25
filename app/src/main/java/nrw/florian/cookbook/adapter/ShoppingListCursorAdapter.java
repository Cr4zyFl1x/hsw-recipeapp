package nrw.florian.cookbook.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import nrw.florian.cookbook.databinding.FragmentShoppingListBinding;
import nrw.florian.cookbook.databinding.FragmentShoppingListItemBinding;

public class ShoppingListCursorAdapter extends CursorAdapter {
    public ShoppingListCursorAdapter(Context context) {
        super(context, null, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return FragmentShoppingListBinding.inflate(LayoutInflater.from(context)).getRoot();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        FragmentShoppingListItemBinding binding = FragmentShoppingListItemBinding.bind(view);
        // TODO: wenn Datenbank steht hier Cursor einfügen
        binding.shoppingListEntryText.setText("Äpfel");
    }
}
