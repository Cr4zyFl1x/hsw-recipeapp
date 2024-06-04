package nrw.florian.cookbook.fragment.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nrw.florian.cookbook.databinding.FragmentRecipeOverviewBinding;
import nrw.florian.cookbook.db.recipe.RecipeBaseCategory;
import nrw.florian.cookbook.db.recipe.RecipeDatabaseOpenHelper;
import nrw.florian.cookbook.db.recipe.RecipeDifficulty;
import nrw.florian.cookbook.db.recipe.RecipeEntity;
import nrw.florian.cookbook.db.recipeproperty.RecipeProperty;
import nrw.florian.cookbook.fragment.adapter.RecipeAdapter;

public class RecipeOverviewFragment extends Fragment {

    private FragmentRecipeOverviewBinding binding;
    private List<RecipeEntity> recipes;
    private List<RecipeEntity> filteredRecipes;
    private RecipeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentRecipeOverviewBinding.inflate(inflater, container, false);

        // Initialize RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecipeAdapter(new ArrayList<>());
        adapter.setSrcFrame(this);
        binding.recyclerView.setAdapter(adapter);


        // Set spinners for the category
        RecipeBaseCategory[] categoryOptions = RecipeBaseCategory.values();
        ArrayAdapter<RecipeBaseCategory> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoryOptions);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categorySpinner.setAdapter(categoryAdapter);

        // Set spinners for the difficulty
        RecipeDifficulty[] difficultyOptions = RecipeDifficulty.values();
        ArrayAdapter<RecipeDifficulty> difficultyAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, difficultyOptions);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.diffcultySpinner.setAdapter(difficultyAdapter);

        // Load recipes from database
        loadRecipesFromDatabase();

        // Add listener for the search button
        binding.searchButton.setOnClickListener(v -> filterRecipes());
        binding.resetFilterButton.setOnClickListener(v -> loadRecipesFromDatabase());

        return binding.getRoot();
    }

    private void loadRecipesFromDatabase()
    {
        try (final RecipeDatabaseOpenHelper dbHelper = new RecipeDatabaseOpenHelper(getContext())) {
            recipes = dbHelper.findAll();
            filteredRecipes = new ArrayList<>(recipes);
            adapter.setRecipes(filteredRecipes);
        }
    }

    private void filterRecipes() {
        RecipeBaseCategory selectedCategory = (RecipeBaseCategory) binding.categorySpinner.getSelectedItem();
        RecipeDifficulty selectedDifficulty = (RecipeDifficulty) binding.diffcultySpinner.getSelectedItem();
        boolean isMeatChecked = binding.meatCheckBox.isChecked();
        boolean isVegetarianChecked = binding.vegetrainCheckBox.isChecked();
        boolean isColdChecked = binding.coldCheckBox.isChecked();
        boolean isWarmChecked = binding.warmCheckBox.isChecked();
        String searchTextString = binding.searchText.getText().toString().trim().toLowerCase();

        filteredRecipes = recipes.stream()
                .filter(recipe -> (selectedCategory == null || recipe.getCategory() == selectedCategory) &&
                        (selectedDifficulty == null || recipe.getDifficulty() == selectedDifficulty) &&
                        (!isMeatChecked || recipe.getRecipeProperties().contains(RecipeProperty.MEAT)) &&
                        (!isVegetarianChecked || recipe.getRecipeProperties().contains(RecipeProperty.VEGETARIAN)) &&
                        (!isColdChecked || recipe.getRecipeProperties().contains(RecipeProperty.COLD)) &&
                        (!isWarmChecked || recipe.getRecipeProperties().contains(RecipeProperty.WARM)) &&
                        (searchTextString.isEmpty() || recipe.getTitle().toLowerCase().contains(searchTextString)))
                .collect(Collectors.toList());

        adapter.setRecipes(filteredRecipes);
    }
}