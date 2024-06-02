package nrw.florian.cookbook.fragment.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Base64;

import nrw.florian.cookbook.databinding.FragmentEditRecipeBinding;
import nrw.florian.cookbook.db.ingredient.IngredientDatabaseOpenHelper;
import nrw.florian.cookbook.db.ingredient.IngredientEntity;
import nrw.florian.cookbook.db.ingredient.IngredientUnit;
import nrw.florian.cookbook.db.recipe.RecipeBaseCategory;
import nrw.florian.cookbook.db.recipe.RecipeDatabaseOpenHelper;
import nrw.florian.cookbook.db.recipe.RecipeDifficulty;
import nrw.florian.cookbook.db.recipe.RecipeEntity;
import nrw.florian.cookbook.db.recipeproperty.RecipeProperty;
import nrw.florian.cookbook.db.recipeproperty.RecipePropertyEntity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class EditRecipeFragment extends Fragment {

    private void setError(EditText editText, String errorMessage) {
        SpannableString spannableString = new SpannableString(errorMessage);
        spannableString.setSpan(new RelativeSizeSpan(0.8f), 0, errorMessage.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setError(spannableString);
    }

    private FragmentEditRecipeBinding binding;
    private List<IngredientViewHolder> ingredientsList = new ArrayList<>();
    private static final int REQUEST_IMAGE = 101;
    private RecipeDatabaseOpenHelper dbHelperRecipe;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditRecipeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDatabaseHelpers();
    }

    private void initializeDatabaseHelpers() {
        dbHelperRecipe = new RecipeDatabaseOpenHelper(requireContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set spinners for the quantities
        IngredientUnit[] quantityOptions = IngredientUnit.values();
        ArrayAdapter<IngredientUnit> quantityAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, quantityOptions);
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner typeOfQuantitySpinner = binding.typeOfQuantitySpinner;
        typeOfQuantitySpinner.setAdapter(quantityAdapter);

        // Set spinners for the category
        RecipeBaseCategory[] categoryOptions = RecipeBaseCategory.values();
        ArrayAdapter<RecipeBaseCategory> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoryOptions);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner categorySpinnerCreate = binding.categorySpinnerCreate;
        categorySpinnerCreate.setAdapter(categoryAdapter);

        // Set spinners for the difficulty
        RecipeDifficulty[] difficultyOptions = RecipeDifficulty.values();
        ArrayAdapter<RecipeDifficulty> difficultyAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, difficultyOptions);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner difficultySpinnerCreate = binding.diffcultySpinnerCreate;
        difficultySpinnerCreate.setAdapter(difficultyAdapter);

        CheckBox warmCheckBox = binding.warmCheckBoxCreate;
        CheckBox coldCheckBox = binding.coldCheckBoxCreate;
        CheckBox meatCheckBox = binding.meatCheckBoxCreate;
        CheckBox vegetarianCheckBox = binding.vegetarianCheckBoxCreate;

        warmCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                coldCheckBox.setChecked(false);
            }
        });

        coldCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                warmCheckBox.setChecked(false);
            }
        });

        meatCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                vegetarianCheckBox.setChecked(false);
            }
        });

        vegetarianCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                meatCheckBox.setChecked(false);
            }
        });

        // Save ingredients to a list
        binding.addIngredientButton.setOnClickListener(v -> {
            if (validateIngredientInputs()) {
                String name = binding.ingredientTextInput.getText().toString().trim();
                double quantity = Double.parseDouble(binding.quantityTextInput.getText().toString().trim());
                IngredientUnit unit = (IngredientUnit) binding.typeOfQuantitySpinner.getSelectedItem();

                IngredientEntity ingredientEntity = new IngredientEntity(name, quantity, unit);
                TextView textView = new TextView(requireContext());
                textView.setText(ingredientEntity.toString());  // Ensure toString() is used
                textView.setTextSize(20);
                textView.setOnClickListener(v1 -> removeIngredient(textView));
                IngredientViewHolder holder = new IngredientViewHolder(ingredientEntity, textView);
                ingredientsList.add(holder);
                binding.safedIngriedientsLinearLayout.addView(textView);
            }
        });

        View.OnClickListener imageClickListener = x -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_IMAGE, null);
        };
        binding.recipeImageButton.setOnClickListener(imageClickListener);
        binding.recipeImageView.setOnClickListener(imageClickListener);

        // Save button click listener
        binding.saveButton.setOnClickListener(v -> {
            if (validateInput()) {
                saveIngredientsToDatabase();
                saveRecipeToDatabase();
                Toast.makeText(getContext(), "Rezept gespeichert!", Toast.LENGTH_SHORT).show();
            }
        });
    }

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

    private boolean validateIngredientInputs() {
        boolean isValid = true;

        // Check if the ingredient name is empty
        String name = binding.ingredientTextInput.getText().toString().trim();
        if (name.isEmpty()) {
            setError(binding.ingredientTextInput, "Zutat darf nicht leer sein");
            isValid = false;
        }

        // Check if the quantity is empty or invalid
        String quantityText = binding.quantityTextInput.getText().toString().trim();
        double quantity = 0;
        if (quantityText.isEmpty()) {
            setError(binding.quantityTextInput, "Menge darf nicht leer sein");
            isValid = false;
        } else {
            try {
                quantity = Double.parseDouble(quantityText);
                if (quantity == 0) {
                    setError(binding.quantityTextInput, "Menge darf nicht null sein");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                setError(binding.quantityTextInput, "Ungültige Menge");
                isValid = false;
            }
        }

        return isValid;
    }

    private boolean validateInput() {
        boolean isValid = true;

        String recipeName = binding.recipeNameTextInput.getText().toString().trim();
        if (recipeName.isEmpty()) {
            setError(binding.recipeNameTextInput, "Rezeptname darf nicht leer sein");
            isValid = false;
        }

        String servingsNumbers = binding.servingsNumber.getText().toString().trim();
        if (servingsNumbers.isEmpty()) {
            setError(binding.servingsNumber, "Anzahl der Portionen darf nicht leer sein");
            isValid = false;
        } else {
            try {
                int servingsNumber = Integer.parseInt(servingsNumbers);
                if (servingsNumber == 0) {
                    setError(binding.servingsNumber, "Anzahl der Portionen darf nicht 0 sein");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                setError(binding.servingsNumber, "Ungültige Anzahl an Portionen");
                isValid = false;
            }
        }
        return isValid;
    }

    private void removeIngredient(TextView textView) {
        for (IngredientViewHolder holder : ingredientsList) {
            if (holder.getTextView() == textView) {
                binding.safedIngriedientsLinearLayout.removeView(textView);
                ingredientsList.remove(holder);
                break;
            }
        }
    }

    private void saveIngredientsToDatabase() {
    }

    private void saveRecipeToDatabase() {
        String title = binding.recipeNameTextInput.getText().toString().trim();

        Bitmap compressedBitmap = null;
        if (binding.recipeImageView.getDrawable() != null) {
            Bitmap imageBitmap = ((BitmapDrawable) binding.recipeImageView.getDrawable()).getBitmap();
            // Compress the bitmap
            compressedBitmap = getCompressedBitmap(imageBitmap, 256);  // Adjust width and height as needed
        }

        int servings = Integer.parseInt(binding.servingsNumber.getText().toString().trim());
        String instructions = binding.recipeDescriptionTextInput.getText().toString().trim();
        RecipeBaseCategory category = (RecipeBaseCategory) binding.categorySpinnerCreate.getSelectedItem();
        RecipeDifficulty difficulty = (RecipeDifficulty) binding.diffcultySpinnerCreate.getSelectedItem();
        int workTime = 0;
        int calories = 0;

        String workTimeString = binding.workingHoursNumber.getText().toString().trim();
        if (!workTimeString.isEmpty()) {
            workTime = Integer.parseInt(workTimeString);
        }

        String caloriesString = binding.energyNumber.getText().toString().trim();
        if (!caloriesString.isEmpty()) {
            calories = Integer.parseInt(caloriesString);
        }

        List<RecipePropertyEntity> properties = new ArrayList<>();
        if (binding.meatCheckBoxCreate.isChecked())
            properties.add(new RecipePropertyEntity(RecipeProperty.MEAT));
        if (binding.vegetarianCheckBoxCreate.isChecked())
            properties.add(new RecipePropertyEntity(RecipeProperty.VEGETARIAN));
        if (binding.coldCheckBoxCreate.isChecked())
            properties.add(new RecipePropertyEntity(RecipeProperty.COLD));
        if (binding.warmCheckBoxCreate.isChecked())
            properties.add(new RecipePropertyEntity(RecipeProperty.WARM));

        List<IngredientEntity> ingredientEntities = new ArrayList<>();
        for (IngredientViewHolder holder : ingredientsList) {
            IngredientEntity ingredientEntity = holder.getIngredient();
            ingredientEntities.add(ingredientEntity);
        }

        RecipeEntity recipe = new RecipeEntity(title, compressedBitmap, servings, instructions, category, difficulty, workTime, calories, properties, ingredientEntities);

        dbHelperRecipe.saveOrUpdate(recipe);
    }


    private Bitmap getCompressedBitmap(Bitmap bitmap, int newHeight) {
        // Scale the bitmap
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (newHeight*bitmap.getWidth()/bitmap.getHeight()), newHeight, true);

        // Compress the bitmap to JPEG format
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream); // 80 is the quality, adjust as needed
        byte[] byteArray = outputStream.toByteArray();

        // Convert byte array back to Bitmap
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

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
