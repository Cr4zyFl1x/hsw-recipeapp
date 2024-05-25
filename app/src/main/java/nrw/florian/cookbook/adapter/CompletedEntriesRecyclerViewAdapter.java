package nrw.florian.cookbook.adapter;

import android.content.Context;
import android.graphics.Paint;
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

public class CompletedEntriesRecyclerViewAdapter extends RecyclerView.Adapter<CompletedEntriesRecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<ShoppingListEntry> entries;

    public CompletedEntriesRecyclerViewAdapter(Context context, ArrayList<ShoppingListEntry> entries) {
        this.context = context;
        this.entries = entries;
    }

    @NonNull
    @Override
    public CompletedEntriesRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you inflate the layout -> Giving a look to each of the rows
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_shopping_list_item, parent, false);
        return new CompletedEntriesRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedEntriesRecyclerViewAdapter.MyViewHolder holder, int position) {
        // assigning values to the views in fragment_shopping_list_item
        // based on the position of the recycler view
        holder.checkBox.setChecked(true);
        holder.textView.setText(entries.get(position).getName());

        updateStrikeThrough(holder.textView);
    }

    @Override
    public int getItemCount() {
        // the recycler view just wants to know the number of items you want displayed
        return entries.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // grabbing all the view from our fragment_shopping_list_item layout file
        CheckBox checkBox;
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.shoppingsListEntryCheckBox);
            checkBox.setChecked(true);
            textView = itemView.findViewById(R.id.shoppingListEntryText);
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public TextView getTextView() {
            return textView;
        }
    }

    private void updateStrikeThrough(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }
}
