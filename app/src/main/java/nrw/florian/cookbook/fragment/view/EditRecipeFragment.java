package nrw.florian.cookbook.fragment.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import nrw.florian.cookbook.databinding.FragmentEditRecipeBinding;
import nrw.florian.cookbook.db.ingredient.IngredientEntity;
import nrw.florian.cookbook.db.ingredient.IngredientUnit;
import nrw.florian.cookbook.db.recipe.RecipeBaseCategory;
import nrw.florian.cookbook.db.recipe.RecipeDatabaseOpenHelper;
import nrw.florian.cookbook.db.recipe.RecipeDifficulty;
import nrw.florian.cookbook.db.recipe.RecipeEntity;
import nrw.florian.cookbook.db.recipeproperty.RecipeProperty;
import nrw.florian.cookbook.db.recipeproperty.RecipePropertyEntity;
import nrw.florian.cookbook.util.BitmapUtil;

/**
 * @author Sina Lindemann
 */
public class EditRecipeFragment extends Fragment {

    /**
     * Fragment view binding
     */
    private FragmentEditRecipeBinding binding;

    /**
     * List of ingredients
     */
    private List<IngredientViewHolder> ingredientsList = new ArrayList<>();

    private static final int REQUEST_IMAGE = 101;


    private RecipeEntity temporaryRecipe = null;


    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = FragmentEditRecipeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        initComboBoxes();

        if (EditRecipeFragmentArgs.fromBundle(getArguments()).getRecipe() != null) {
            temporaryRecipe = EditRecipeFragmentArgs.fromBundle(getArguments()).getRecipe();
            binding.recipeNameTextInput.setText(temporaryRecipe.getTitle());
            binding.servingsNumber.setText(Integer.toString(temporaryRecipe.getDefaultServings()));
            binding.recipeDescriptionTextInput.setText(temporaryRecipe.getInstructions());
            if (temporaryRecipe.getImage() != null) {
                binding.recipeImageView.setImageBitmap(temporaryRecipe.getImage());
                binding.recipeImageView.setVisibility(View.VISIBLE);
                binding.recipeImageButton.setVisibility(View.GONE);
            }
            if (temporaryRecipe.getWorkTime() != 0) {
                binding.workingHoursNumber.setText(Integer.toString(temporaryRecipe.getWorkTime()));
            }
            if (temporaryRecipe.getCalories() != 0) {
                binding.energyNumber.setText(Integer.toString(temporaryRecipe.getCalories()));
            }
            for (IngredientEntity ingredient : temporaryRecipe.getIngredients()) {
                addIngredient(ingredient);
            }
            {
                if (temporaryRecipe.getRecipeProperties().contains(RecipeProperty.MEAT))
                    binding.meatCheckBoxCreate.setChecked(true);
                if (temporaryRecipe.getRecipeProperties().contains(RecipeProperty.VEGETARIAN))
                    binding.vegetarianCheckBoxCreate.setChecked(true);
                if (temporaryRecipe.getRecipeProperties().contains(RecipeProperty.COLD))
                    binding.coldCheckBoxCreate.setChecked(true);
                if (temporaryRecipe.getRecipeProperties().contains(RecipeProperty.WARM))
                    binding.warmCheckBoxCreate.setChecked(true);
            }
            {
                if (temporaryRecipe.getCategory() != null)
                    binding.categorySpinnerCreate.setSelection(new ArrayList<>(Arrays.asList(RecipeBaseCategory.values())).indexOf(temporaryRecipe.getCategory()));
                if (temporaryRecipe.getDifficulty() !=  null)
                    binding.diffcultySpinnerCreate.setSelection(new ArrayList<>(Arrays.asList(RecipeDifficulty.values())).indexOf(temporaryRecipe.getDifficulty()));
            }
        }

        initPropertyCheckboxes();

        // Save ingredients to a list
        binding.addIngredientButton.setOnClickListener(v -> {
            if (validateIngredientInputs()) {
                String name = binding.ingredientTextInput.getText().toString().trim();
                double quantity = Double.parseDouble(binding.quantityTextInput.getText().toString().trim());
                IngredientUnit unit = (IngredientUnit) binding.typeOfQuantitySpinner.getSelectedItem();

                IngredientEntity ingredientEntity = new IngredientEntity(name, quantity, unit);
                addIngredient(ingredientEntity);
            }
        });

        // Initialize image inputs
        initImageInputs();

        // Save button click listener
        initSaveButton();
    }

    private void addIngredient(IngredientEntity ingredientEntity)
    {
        TextView textView = new TextView(requireContext());
        textView.setText(ingredientEntity.toString());  // Ensure toString() is used
        textView.setTextSize(20);
        textView.setOnClickListener(v1 -> removeIngredient(textView));
        IngredientViewHolder holder = new IngredientViewHolder(ingredientEntity, textView);
        ingredientsList.add(holder);
        binding.safedIngriedientsLinearLayout.addView(textView);
    }


    /**
     * Initializes the spinners/comboboxes
     */
    private void initComboBoxes()
    {
        // INGREDIENTS
        IngredientUnit[] quantityOptions = IngredientUnit.values();
        ArrayAdapter<IngredientUnit> quantityAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, quantityOptions);
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner typeOfQuantitySpinner = binding.typeOfQuantitySpinner;
        typeOfQuantitySpinner.setAdapter(quantityAdapter);

        // RECIPE CATEGORY
        RecipeBaseCategory[] categoryOptions = RecipeBaseCategory.values();
        ArrayAdapter<RecipeBaseCategory> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoryOptions);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner categorySpinnerCreate = binding.categorySpinnerCreate;
        categorySpinnerCreate.setAdapter(categoryAdapter);

        // DIFFICULTY
        RecipeDifficulty[] difficultyOptions = RecipeDifficulty.values();
        ArrayAdapter<RecipeDifficulty> difficultyAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, difficultyOptions);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner difficultySpinnerCreate = binding.diffcultySpinnerCreate;
        difficultySpinnerCreate.setAdapter(difficultyAdapter);
    }


    /**
     * Initializes the property checkboxes
     */
    private void initPropertyCheckboxes()
    {
        binding.warmCheckBoxCreate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.coldCheckBoxCreate.setChecked(false);
            }
        });
        binding.coldCheckBoxCreate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.warmCheckBoxCreate.setChecked(false);
            }
        });
        binding.meatCheckBoxCreate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.vegetarianCheckBoxCreate.setChecked(false);
            }
        });
        binding.vegetarianCheckBoxCreate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.meatCheckBoxCreate.setChecked(false);
            }
        });
    }


    /**
     * Initializes the save button
     */
    private void initSaveButton()
    {
        binding.saveButton.setOnClickListener(v -> {
            if (validateInput()) {
                if (saveRecipeToDatabase()) {
                    Toast.makeText(getContext(), "Rezept gespeichert!", Toast.LENGTH_LONG).show();
                    NavHostFragment.findNavController(this).popBackStack();
                } else {
                    Toast.makeText(getContext(), "Rezept konnte nicht gespeichert werden!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getContext(), "Bitte Eingaben prüfen!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Initializes the image inputs
     */
    private void initImageInputs()
    {
        View.OnClickListener imageClickListener = x -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_IMAGE, null);
        };
        binding.recipeImageButton.setOnClickListener(imageClickListener);
        binding.recipeImageView.setOnClickListener(imageClickListener);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            binding.recipeImageView.setImageURI(selectedImageUri);
            binding.recipeImageView.setVisibility(View.VISIBLE);
            binding.recipeImageButton.setVisibility(View.GONE);
        }
    }


    /**
     * Validates the input fields
     * @return true if all fields are valid, false otherwise
     */
    private boolean validateIngredientInputs() {
        // EMPTY INGREDIENT NAME
        String name = Objects.requireNonNull(binding.ingredientTextInput.getText()).toString().trim();
        if (name.isEmpty()) {
            setError(binding.ingredientTextInput, "Zutat darf nicht leer sein");
            return false;
        }

        // EMPTY QUANTITY
        String quantityText = Objects.requireNonNull(binding.quantityTextInput.getText()).toString().trim();
        if (quantityText.isEmpty()) {
            setError(binding.quantityTextInput, "Menge darf nicht leer sein");
            return false;
        }

        // INVALID QUANTITY
        try {
            if (Double.parseDouble(quantityText) == 0) {
                setError(binding.quantityTextInput, "Menge darf nicht null sein");
                return false;
            }
        } catch (NumberFormatException e) {
            setError(binding.quantityTextInput, "Ungültige Menge");
            return false;
        }

        // IS VALID
        return true;
    }


    /**
     * Validates the input fields
     * @return true if all fields are valid, false otherwise
     */
    private boolean validateInput() {

        // EMPTY RECIPE NAME
        String recipeName = Objects.requireNonNull(binding.recipeNameTextInput.getText()).toString().trim();
        if (recipeName.isEmpty()) {
            setError(binding.recipeNameTextInput, "Rezeptname darf nicht leer sein");
            return false;
        }

        // EMPTY AMOUNT OF DEFAULT SERVINGS
        String servingsNumbers = binding.servingsNumber.getText().toString().trim();
        if (servingsNumbers.isEmpty()) {
            setError(binding.servingsNumber, "Anzahl der Portionen darf nicht leer sein");
            return false;
        }

        // INVALID AMOUNT OF DEFAULT SERVINGS
        try {
            if (Integer.parseInt(servingsNumbers) == 0) {
                setError(binding.servingsNumber, "Anzahl der Portionen darf nicht 0 sein");
                return false;
            }
        } catch (NumberFormatException e) {
            setError(binding.servingsNumber, "Ungültige Anzahl an Portionen");
            return false;
        }

        // NO INGREDIENTS
        if (ingredientsList == null || ingredientsList.isEmpty()) {
            setError(binding.ingredientTextInput, "Bitte Zutaten angeben");
            return false;
        }

        // RECIPE DESCRIPTION EMPTY
        if (Objects.requireNonNull(binding.recipeDescriptionTextInput.getText()).toString().trim().isEmpty()) {
            setError(binding.recipeDescriptionTextInput, "Rezeptbeschreibung darf nicht leer sein");
            return false;
        }

        // IS VALID
        return true;
    }


    /**
     * Removes an ingredient from the list (by the View)
     * @param textView the View of the ingredient to remove
     */
    private void removeIngredient(TextView textView) {
        for (IngredientViewHolder holder : ingredientsList) {
            if (holder.getTextView() == textView) {
                binding.safedIngriedientsLinearLayout.removeView(textView);
                ingredientsList.remove(holder);
                break;
            }
        }
    }


    /**
     * Saves the currently entered recipe details to the database
     */
    private boolean saveRecipeToDatabase()
    {
        if (temporaryRecipe != null && temporaryRecipe.getId() != 0) {
            temporaryRecipe.setTitle(Objects.requireNonNull(binding.recipeNameTextInput.getText()).toString().trim());
            if (!binding.servingsNumber.getText().toString().trim().isEmpty())
                temporaryRecipe.setDefaultServings(Integer.parseInt(binding.servingsNumber.getText().toString().trim()));
            if (!binding.energyNumber.getText().toString().trim().isEmpty())
                temporaryRecipe.setCalories(Integer.parseInt(binding.energyNumber.getText().toString().trim()));
            temporaryRecipe.setInstructions(Objects.requireNonNull(binding.recipeDescriptionTextInput.getText()).toString().trim());
            if (!binding.workingHoursNumber.getText().toString().trim().isEmpty())
                temporaryRecipe.setWorkTime(Integer.parseInt(binding.workingHoursNumber.getText().toString().trim()));
            temporaryRecipe.setCategory((RecipeBaseCategory) binding.categorySpinnerCreate.getSelectedItem());
            temporaryRecipe.setDifficulty((RecipeDifficulty) binding.diffcultySpinnerCreate.getSelectedItem());
            temporaryRecipe.setIngredients(ingredientsList.stream().map(IngredientViewHolder::getIngredient).collect(Collectors.toList()));

            final List<RecipePropertyEntity> properties = new ArrayList<>();
            if (binding.meatCheckBoxCreate.isChecked())
                properties.add(new RecipePropertyEntity(RecipeProperty.MEAT));
            if (binding.vegetarianCheckBoxCreate.isChecked())
                properties.add(new RecipePropertyEntity(RecipeProperty.VEGETARIAN));
            if (binding.coldCheckBoxCreate.isChecked())
                properties.add(new RecipePropertyEntity(RecipeProperty.COLD));
            if (binding.warmCheckBoxCreate.isChecked())
                properties.add(new RecipePropertyEntity(RecipeProperty.WARM));
            temporaryRecipe.setRecipeProperties(properties);

            if (binding.recipeImageView.getDrawable() != null)
                temporaryRecipe.setImage(BitmapUtil.compressBitmap(((BitmapDrawable) binding.recipeImageView.getDrawable()).getBitmap(), 400, 90));

            // Save recipe
            try (RecipeDatabaseOpenHelper helper = new RecipeDatabaseOpenHelper(getContext())) {
                return helper.saveOrUpdate(temporaryRecipe);
            }
        }

        // RECIPE TITLE
        final String title = binding.recipeNameTextInput.getText().toString().trim();

        // RECIPE IMAGE
        Bitmap compressedBitmap = null;
        if (binding.recipeImageView.getDrawable() != null) {
            final Bitmap imageBitmap = ((BitmapDrawable) binding.recipeImageView.getDrawable()).getBitmap();
            compressedBitmap = BitmapUtil.compressBitmap(imageBitmap, 400, 90);
        }

        int servings = Integer.parseInt(binding.servingsNumber.getText().toString().trim());
        String instructions = Objects.requireNonNull(binding.recipeDescriptionTextInput.getText()).toString().trim();
        RecipeBaseCategory category = (RecipeBaseCategory) binding.categorySpinnerCreate.getSelectedItem();
        RecipeDifficulty difficulty = (RecipeDifficulty) binding.diffcultySpinnerCreate.getSelectedItem();

        // WORK-TIME
        String workTimeString = binding.workingHoursNumber.getText().toString().trim();
        int workTime = 0;
        if (!workTimeString.isEmpty()) {
            workTime = Integer.parseInt(workTimeString);
        }

        // CALORIES
        final String caloriesString = binding.energyNumber.getText().toString().trim();
        int calories = 0;
        if (!caloriesString.isEmpty()) {
            calories = Integer.parseInt(caloriesString);
        }

        // RECIPE PROPERTIES / TAGS
        final List<RecipePropertyEntity> properties = new ArrayList<>();
        if (binding.meatCheckBoxCreate.isChecked())
            properties.add(new RecipePropertyEntity(RecipeProperty.MEAT));
        if (binding.vegetarianCheckBoxCreate.isChecked())
            properties.add(new RecipePropertyEntity(RecipeProperty.VEGETARIAN));
        if (binding.coldCheckBoxCreate.isChecked())
            properties.add(new RecipePropertyEntity(RecipeProperty.COLD));
        if (binding.warmCheckBoxCreate.isChecked())
            properties.add(new RecipePropertyEntity(RecipeProperty.WARM));

        // INGREDIENTS
        final List<IngredientEntity> ingredientEntities = new ArrayList<>();
        for (IngredientViewHolder holder : ingredientsList) {
            IngredientEntity ingredientEntity = holder.getIngredient();
            ingredientEntities.add(ingredientEntity);
        }

        // Create recipe
        final RecipeEntity recipe = new RecipeEntity(
                title,
                compressedBitmap,
                servings,
                instructions,
                category,
                difficulty,
                workTime,
                calories,
                properties,
                ingredientEntities);

        // Save recipe
        try (RecipeDatabaseOpenHelper helper = new RecipeDatabaseOpenHelper(getContext())) {
            return helper.saveOrUpdate(recipe);
        }
    }


    /**
     * Shows an error pane for a text field with the given error message
     * @param editText the text field to show the error in
     * @param errorMessage the error message to show
     */
    private void setError(final EditText editText, final String errorMessage)
    {
        SpannableString spannableString = new SpannableString(errorMessage);
        spannableString.setSpan(new RelativeSizeSpan(0.8f), 0, errorMessage.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setError(spannableString);
    }


    /**
     * IngredientViewHolder
     */
    private static class IngredientViewHolder {
        private final IngredientEntity ingredient;
        private final TextView textView;

        IngredientViewHolder(IngredientEntity ingredient, TextView textView) {
            this.ingredient = ingredient;
            this.textView = textView;
        }

        public IngredientEntity getIngredient() {
            return ingredient;
        }

        public TextView getTextView() {
            return textView;
        }
    }
}
