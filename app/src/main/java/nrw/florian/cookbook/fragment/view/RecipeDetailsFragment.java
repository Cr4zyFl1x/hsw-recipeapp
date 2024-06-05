package nrw.florian.cookbook.fragment.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nrw.florian.cookbook.R;
import nrw.florian.cookbook.databinding.FragmentRecipeDetailsBinding;
import nrw.florian.cookbook.db.ingredient.IngredientEntity;
import nrw.florian.cookbook.db.recipe.RecipeDatabaseOpenHelper;
import nrw.florian.cookbook.db.recipe.RecipeEntity;
import nrw.florian.cookbook.db.recipeproperty.RecipePropertyEntity;
import nrw.florian.cookbook.db.shoppinglist.ShoppingListItemDatabaseOpenHelper;
import nrw.florian.cookbook.db.shoppinglist.ShoppingListItemEntity;
import nrw.florian.cookbook.fragment.list.RecipeIngredientRecyclerViewAdapter;
import nrw.florian.cookbook.fragment.list.RecipeTagViewFragment;


/**
 * @author Florian J. Kleine-Vorholt
 */
public class RecipeDetailsFragment extends Fragment {

    /**
     * View Binding
     */
    private FragmentRecipeDetailsBinding binding;

    /**
     * Adapter for ingredients list
     */
    private RecipeIngredientRecyclerViewAdapter ingredientsAdapter;

    /**
     * Current recipe
     */
    private RecipeEntity recipe = null;

    /**
     * Current ingredients
     * <p>
     *     The current displayed copy. It's a copy of the recipe's ingredients.
     * </p>
     * @see RecipeEntity#getIngredients()
     */
    private List<IngredientEntity> currentIngredients = new ArrayList<>();

    /**
     * Saves the amount of current portions
     */
    private int currentPortions;



    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();

        initialize();
        initializeEditButton();
    }


    private void initializeEditButton()
    {
        if (recipe == null || recipe.getId() == 0) {
            Toast.makeText(requireContext(), getString(R.string.error_loading_recipe), Toast.LENGTH_LONG).show();
            return;
        }
        binding.floatingActionButton.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(RecipeDetailsFragmentDirections.actionRecipeDetailsFragmentToEditRecipeFragment(recipe)));
    }


    private void initialize()
    {
        // Initialize Recipe
        if (!initRecipeData())
            return;

        // Image
        initImage();

        // Portions
        initPortionsMenu();

        // Shopping List
        initShoppingList();

        // Init Tags (RecipeProperties)
        initTags();
    }

    /**
     * Initialize the recipe data object
     */
    private boolean initRecipeData()
    {
        try (final RecipeDatabaseOpenHelper dbHelper = new RecipeDatabaseOpenHelper(requireContext())) {
            this.recipe = dbHelper.findById(RecipeDetailsFragmentArgs
                            .fromBundle(getArguments()).getRecipeID())
                    .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));
            currentPortions = recipe.getDefaultServings();

            currentIngredients = recipe.getIngredients().stream()
                    .map(j -> new IngredientEntity(j.getId(), j.getRecipeID(), j.getName(), j.getQuantity(), j.getUnit()))
                    .collect(Collectors.toList());

            // Ingredients List
            ingredientsAdapter = new RecipeIngredientRecyclerViewAdapter(currentIngredients, requireContext());
            binding.recipeIngredientsRecyclerView.setAdapter(ingredientsAdapter);
            binding.recipeIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

            // Initialize all
            binding.recipeTitleTextView.setText(recipe.getTitle());
            binding.recipeEnergyTextView.setText(getString(R.string.recipe_kcalories, recipe.getCalories()));
            binding.recipeTimeTextView.setText(getString(R.string.recipe_workmin, recipe.getWorkTime()));
            binding.recipeDifficultyTextView.setText(recipe.getDifficulty().toString());
            binding.makingDescriptionTextView.setText(recipe.getInstructions());

            return true;
        } catch (IllegalArgumentException e) {
            Toast.makeText(requireContext(), getString(R.string.error_loading_recipe), Toast.LENGTH_LONG).show();
            NavHostFragment.findNavController(this).popBackStack();
            return false;
        }
    }

    private void initTags()
    {
        // If no tags set -> hide tags
        if (recipe.getRecipePropertyEntities() == null || recipe.getRecipePropertyEntities().isEmpty()) {
            binding.tagsContainer.setVisibility(View.GONE);
            binding.tagsTextView.setVisibility(View.GONE);
            return;
        }

        final FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
                .beginTransaction();
        for (RecipePropertyEntity tag : recipe.getRecipePropertyEntities()) {
            transaction.add(R.id.tagsLayoutContainer, RecipeTagViewFragment.newInstance(tag));
        }
        transaction.commit();
    }


    /**
     * Initialize all buttons for portions
     */
    private void initPortionsMenu()
    {
        binding.recipePortionsTextView.setText(getString(R.string.cnt_portions, currentPortions));
        binding.recipePortionsTextView.setOnClickListener(v -> setPortions(recipe.getDefaultServings()));
        binding.minusPortionImageView.setOnClickListener(v -> setPortions(currentPortions - 1));
        binding.plusPortionImageView.setOnClickListener(v -> setPortions(currentPortions + 1));
    }


    /**
     * Initialize image
     */
    private void initImage()
    {
        if (recipe.getImage() != null) {
            binding.recipePictureImageView.setImageBitmap(recipe.getImage());
        }
        else {
            binding.recipePictureImageView.getLayoutParams().height = 0;
        }
    }


    /**
     * Initialize button for adding ingredients to shopping list
     */
    private void initShoppingList()
    {
        binding.putOnShoppingListButton.setOnClickListener(v -> {
            try (final ShoppingListItemDatabaseOpenHelper shoppingList = new ShoppingListItemDatabaseOpenHelper(requireContext())) {
                for (IngredientEntity ingredient : recipe.getIngredients()) {
                    shoppingList.saveOrUpdate(new ShoppingListItemEntity(ingredient.getName(), false));
                }
            }
            binding.putOnShoppingListButton.setEnabled(false);
            Toast.makeText(requireContext(),
                    getString(R.string.ingredients_added_to_shopping_list,
                            recipe.getIngredients().size()), Toast.LENGTH_LONG).show();
        });
    }


    /**
     * Recalculate all ingredients to the desired amount of portions
     * @param portions Amount of portions
     */
    private void setPortions(int portions)
    {
        if (portions < 1 || portions == currentPortions)
            return;

        for (int i = 0; i < currentIngredients.size(); i++) {
            final IngredientEntity ingredient = currentIngredients.get(i);

            ingredient.setQuantity((double) Math.round((recipe.getIngredients().get(i).getQuantity() * (float) portions / (float) recipe.getDefaultServings()) * 100) / 100);
        }
        currentPortions = portions;
        binding.recipePortionsTextView.setText(getString(R.string.cnt_portions, portions));
        ingredientsAdapter.notifyItemRangeChanged(0, currentIngredients.size());
    }
}