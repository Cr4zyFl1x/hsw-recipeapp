package nrw.florian.cookbook.fragment.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nrw.florian.cookbook.R;
import nrw.florian.cookbook.db.recipe.RecipeBaseCategory;
import nrw.florian.cookbook.db.recipe.RecipeDatabaseOpenHelper;
import nrw.florian.cookbook.db.recipe.RecipeDifficulty;
import nrw.florian.cookbook.db.recipe.RecipeEntity;
import nrw.florian.cookbook.db.recipeproperty.RecipeProperty;
import nrw.florian.cookbook.fragment.adapter.RecipeAdapter;

public class RecipeOverviewFragment extends Fragment {

    private List<RecipeEntity> recipes;
    private List<RecipeEntity> filteredRecipes;
    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private Spinner categorySpinner;
    private Spinner difficultySpinner;
    private CheckBox meatCheckBox;
    private CheckBox vegetarianCheckBox;
    private CheckBox coldCheckBox;
    private CheckBox warmCheckBox;
    private EditText searchText;
    private Button searchButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_overview, container, false);

        // Initialize RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter with an empty list initially
        adapter = new RecipeAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Initialize Spinners
        categorySpinner = rootView.findViewById(R.id.categorySpinner);
        difficultySpinner = rootView.findViewById(R.id.diffcultySpinner);

        // Initialize CheckBoxes
        meatCheckBox = rootView.findViewById(R.id.meatCheckBox);
        vegetarianCheckBox = rootView.findViewById(R.id.vegetrainCheckBox);
        coldCheckBox = rootView.findViewById(R.id.coldCheckBox);
        warmCheckBox = rootView.findViewById(R.id.warmCheckBox);

        // Initialize search text
        searchText = rootView.findViewById(R.id.searchText);

        // Initialize search button
        searchButton = rootView.findViewById(R.id.searchButton);

        // Set spinners for the category
        RecipeBaseCategory[] categoryOptions = RecipeBaseCategory.values();
        ArrayAdapter<RecipeBaseCategory> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoryOptions);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Set spinners for the difficulty
        RecipeDifficulty[] difficultyOptions = RecipeDifficulty.values();
        ArrayAdapter<RecipeDifficulty> difficultyAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, difficultyOptions);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(difficultyAdapter);

        // Load recipes from database
        loadRecipesFromDatabase();

        // Add listener for the search button
        searchButton.setOnClickListener(v -> filterRecipes());

        return rootView;
    }

    private void loadRecipesFromDatabase() {
        // Create an instance of RecipeDatabaseOpenHelper
        RecipeDatabaseOpenHelper dbHelper = new RecipeDatabaseOpenHelper(getContext());

        // Fetch recipes from the database using the findAll() method
        recipes = dbHelper.findAll();

        // Set the fetched recipes to the adapter and the filtered list
        filteredRecipes = new ArrayList<>(recipes);
        adapter.setRecipes(filteredRecipes);
    }

    private void filterRecipes() {
        RecipeBaseCategory selectedCategory = (RecipeBaseCategory) categorySpinner.getSelectedItem();
        RecipeDifficulty selectedDifficulty = (RecipeDifficulty) difficultySpinner.getSelectedItem();
        boolean isMeatChecked = meatCheckBox.isChecked();
        boolean isVegetarianChecked = vegetarianCheckBox.isChecked();
        boolean isColdChecked = coldCheckBox.isChecked();
        boolean isWarmChecked = warmCheckBox.isChecked();
        String searchTextString = searchText.getText().toString().trim().toLowerCase();

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
