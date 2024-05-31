package nrw.florian.cookbook.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nrw.florian.cookbook.R;
import nrw.florian.cookbook.ShoppingListEntry;
import nrw.florian.cookbook.db.shoppinglist.ShoppingListItemEntity;

public class ActiveEntriesRecyclerViewAdapter extends CursorRecyclerViewAdapter<ActiveEntriesRecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<ShoppingListEntry> entries;
    Cursor cursor;
    private DataSetObserver dataSetObserver;
    private boolean isDataValid;
    private int mRowIdColumn;

    private boolean activeEntries;

    public ActiveEntriesRecyclerViewAdapter(Context context, Cursor cursor, boolean activeEntries) {
        super(context, cursor, activeEntries);
        /*this.context = context;
        this.cursor = cursor;*/
        this.activeEntries = activeEntries;
    }

    @NonNull
    @Override
    public ActiveEntriesRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you inflate the layout -> Giving a look to each of the rows
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_shopping_list_item, parent, false);
        return new ActiveEntriesRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveEntriesRecyclerViewAdapter.MyViewHolder holder, Cursor cursor) {
        // assigning values to the views in fragment_shopping_list_item
        // based on the position of the recycler view
//        holder.textView.setText(entries.get(position).getName());
        if (activeEntries) {
            ShoppingListItemEntity item = ShoppingListItemEntity.getShoppingListItemEntity(cursor);
            holder.textView.setText(item.getTitle());
            holder.checkBox.setOnClickListener(v -> {
                /*if (holder.checkBox.isChecked()) {
                    CompletedEntriesRecyclerViewAdapter.makeStrikethrough(holder.textView, true);
                }*/
            });
        }
        if (!activeEntries) {
            ShoppingListItemEntity item = ShoppingListItemEntity.getShoppingListItemEntity(cursor);
            holder.textView.setText(item.getTitle());
            holder.textView.setTextColor(808080);
            holder.checkBox.setChecked(true);
            holder.checkBox.setOnClickListener(v -> {
                if (holder.checkBox.isChecked()) {
                    CompletedEntriesRecyclerViewAdapter.makeStrikethrough(holder.textView, false);
                }
            });
        }
        changeCursor(cursor);
    }

    /*@Override
    public int getItemCount() {
        // the recycler view just wants to know the number of items you want displayed
        if (isDataValid && cursor != null) {
            return cursor.getCount();
        }
        return 0;
    }*/

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // grabbing all the view from our fragment_shopping_list_item layout file
        CheckBox checkBox;
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.shoppingsListEntryCheckBox);
            textView = itemView.findViewById(R.id.shoppingListEntryText);
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public TextView getTextView() {
            return textView;
        }
    }
}
