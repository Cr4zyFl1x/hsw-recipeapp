package nrw.florian.cookbook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nrw.florian.cookbook.db.recipe.RecipeEntity;
import nrw.florian.cookbook.db.recipeproperty.RecipeProperty;
import nrw.florian.cookbook.util.ByteStreamUtil;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<RecipeEntity> recipes;

    public RecipeAdapter(List<RecipeEntity> recipes) {
        this.recipes = recipes;
    }

    public void setRecipes(List<RecipeEntity> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged(); // Notify the adapter about the changes
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item_layout, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        RecipeEntity recipe = recipes.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private TextView recipeNameTextView;
        private TextView recipePropertiesTextView;
        private TextView recipeCategoryTextView;
        private TextView recipeDifficultyTextView;
        private ImageView recipeImageView; // ImageView hinzufügen

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeNameTextView = itemView.findViewById(R.id.recipeNameTextView);
            recipePropertiesTextView = itemView.findViewById(R.id.recipePropertiesTextView);
            recipeCategoryTextView = itemView.findViewById(R.id.recipeCategoryTextView);
            recipeDifficultyTextView = itemView.findViewById(R.id.recipeDifficultyTextView);
            recipeImageView = itemView.findViewById(R.id.recipeImageView); // ImageView initialisieren
        }

        public void bind(RecipeEntity recipe) {
            recipeNameTextView.setText(recipe.getTitle());
            recipePropertiesTextView.setText(formatProperties(recipe.getRecipeProperties()));
            recipeCategoryTextView.setText(recipe.getCategory().toString());
            recipeDifficultyTextView.setText(recipe.getDifficulty().toString());


            if (recipe.getImage() != null) {
                recipeImageView.setImageBitmap(recipe.getImage());
            } else {
                recipeImageView.setImageResource(R.drawable.recipe_list_placeholder); // Platzhalterbild anzeigen
            }
        }

        private String formatProperties(List<RecipeProperty> properties) {
            StringBuilder stringBuilder = new StringBuilder();
            for (RecipeProperty property : properties) {
                stringBuilder.append(property.toString()).append(", ");
            }
            if (stringBuilder.length() > 0) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 2); // Entferne das letzte ", "
            }
            return stringBuilder.toString();
        }
    }


}
