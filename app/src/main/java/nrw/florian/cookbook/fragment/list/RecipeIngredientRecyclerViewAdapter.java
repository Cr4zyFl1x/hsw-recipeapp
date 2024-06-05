package nrw.florian.cookbook.fragment.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nrw.florian.cookbook.databinding.FragmentRecipeIngredientItemBinding;
import nrw.florian.cookbook.db.ingredient.IngredientEntity;


public class RecipeIngredientRecyclerViewAdapter extends RecyclerView.Adapter<RecipeIngredientRecyclerViewAdapter.ViewHolder> {

    private final List<IngredientEntity> mValues;
    private final Context context;

    public RecipeIngredientRecyclerViewAdapter(List<IngredientEntity> items, Context context) {
        mValues = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentRecipeIngredientItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.quantityTextView.setText("" + mValues.get(position).getQuantity());
        holder.unitTextView.setText(mValues.get(position).getUnit().getString(context));
        holder.titleTextView.setText(mValues.get(position).getName());
    }

    @Override
    public int getItemCount()
    {
        return mValues.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView quantityTextView;
        public final TextView unitTextView;
        public final TextView titleTextView;

        public ViewHolder(FragmentRecipeIngredientItemBinding binding) {
            super(binding.getRoot());
            quantityTextView = binding.ingredientQuantity;
            unitTextView = binding.ingredientUnit;
            titleTextView = binding.ingredientTitle;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titleTextView.getText() + "'";
        }
    }
}