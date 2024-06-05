package nrw.florian.cookbook.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nrw.florian.cookbook.R;
import nrw.florian.cookbook.db.shoppinglist.ShoppingListItemEntity;
import nrw.florian.cookbook.repositories.ShoppingListRepository;

public class ShoppingListRecyclerViewAdapter extends RecyclerView.Adapter<ShoppingListRecyclerViewAdapter.ShoppingListViewHolder> {
    private List<ShoppingListItemEntity> items;
    private final boolean isDone;
    private final ShoppingListRepository repository;

    public ShoppingListRecyclerViewAdapter(List<ShoppingListItemEntity> items, ShoppingListRepository repository, boolean isDone) {
        super();
        this.items = items;
        this.repository = repository;
        this.isDone = isDone;
    }

    @NonNull
    @Override
    public ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you inflate the layout -> Giving a look to each of the rows
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_shopping_list_item, parent, false);
        return new ShoppingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListViewHolder holder, int position) {
        // assigning values to the views in fragment_shopping_list_item
        // based on the position of the recycler view
        ShoppingListItemEntity item = items.get(position);
        if (isDone) {
            holder.textView.setText(item.getTitle());
            holder.checkBox.setChecked(true);
            makeStrikethrough(holder.textView, isDone);
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.setDone(false);
                repository.upsert(item);
            });
        }
        if (!isDone) {
            holder.textView.setText(item.getTitle());
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.setDone(true);
                repository.upsert(item);
            });
        }
        holder.imageButton.setOnClickListener(v -> repository.delete(item));
    }

    @Override
    public int getItemCount() {
        // the recycler view just wants to know the number of items you want displayed
        if (!items.isEmpty()) {
            return items.size();
        }
        return 0;
    }

    public static void makeStrikethrough(TextView textView, boolean isChecked) {
        if (isChecked) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            textView.setPaintFlags(textView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    public void setItems(List<ShoppingListItemEntity> items) {
        this.items = items;
    }

    public static class ShoppingListViewHolder extends RecyclerView.ViewHolder {
        // grabbing all the view from our fragment_shopping_list_item layout file
        CheckBox checkBox;
        TextView textView;
        ImageButton imageButton;

        public ShoppingListViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.shoppingsListEntryCheckBox);
            textView = itemView.findViewById(R.id.shoppingListEntryText);
            imageButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
