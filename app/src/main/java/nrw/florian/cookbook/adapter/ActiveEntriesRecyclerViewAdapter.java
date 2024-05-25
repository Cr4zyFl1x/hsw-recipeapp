package nrw.florian.cookbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nrw.florian.cookbook.R;
import nrw.florian.cookbook.ShoppingListEntry;

public class ActiveEntriesRecyclerViewAdapter extends RecyclerView.Adapter<ActiveEntriesRecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<ShoppingListEntry> entries;

    public ActiveEntriesRecyclerViewAdapter(Context context, ArrayList<ShoppingListEntry> entries) {
        this.context = context;
        this.entries = entries;
    }

    @NonNull
    @Override
    public ActiveEntriesRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you inflate the layout -> Giving a look to each of the rows
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_shopping_list_item, parent, false);
        return new ActiveEntriesRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveEntriesRecyclerViewAdapter.MyViewHolder holder, int position) {
        // assigning values to the views in framgnet_shopping_list_item
        // based on the position of the recycler view
        holder.checkBox.setText(entries.get(position).getName());
    }

    @Override
    public int getItemCount() {
        // the recycler view just wantes to know the number of items you want displayed
        return entries.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // grabbing all the view from our fragment_shopping_list_item layout file
        // Kinda like in the onCreate method

        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.shoppingsListEntry);
        }
    }
}
