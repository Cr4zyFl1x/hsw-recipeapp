package nrw.florian.cookbook.fragment.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import nrw.florian.cookbook.Ingredient;
import nrw.florian.cookbook.databinding.FragmentEditRecipeBinding;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditRecipeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Set spinners for the quantities
        String[] quantityOptions = {"kg", "g", "L", "ml", "Stücke", "EL", "TL"};
        ArrayAdapter<String> quantityAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, quantityOptions);
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner typeOfQuantitySpinner = binding.typeOfQuantitySpinner;
        typeOfQuantitySpinner.setAdapter(quantityAdapter);

        //Set spinners for the category
        String[] categoryOptions = {"Suppe", "Soße", "Nachtisch", "Salat", "Hauptspeise", "Vorspeise"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoryOptions);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner categorySpinnerCreate = binding.categorySpinnerCreate;
        categorySpinnerCreate.setAdapter(categoryAdapter);

        //Set spinners for the difficulty
        String[] difficultyOptions = {"Leicht", "Mittel", "Schwer"};
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, difficultyOptions);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner difficultySpinnerCreate = binding.diffcultySpinnerCreate;
        difficultySpinnerCreate.setAdapter(difficultyAdapter);


        //Save ingredients to a list
        binding.addIngredientButton.setOnClickListener(v -> {
            if (validateIngredientInputs()) {
                String name = binding.ingredientTextInput.getText().toString().trim();
                double quantity = Double.parseDouble(binding.quantityTextInput.getText().toString().trim());
                String unit = binding.typeOfQuantitySpinner.getSelectedItem().toString();

                Ingredient ingredient = new Ingredient(name, quantity, unit);
                TextView textView = new TextView(requireContext());
                textView.setText(ingredient.toString());
                textView.setTextSize(20);
                textView.setOnClickListener(v1 -> removeIngredient(textView));
                IngredientViewHolder holder = new IngredientViewHolder(ingredient, textView);
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
                Toast.makeText(getContext(), "Rezept gespeichert!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
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

    private static class IngredientViewHolder {
        private final Ingredient ingredient;
        private final TextView textView;

        IngredientViewHolder(Ingredient ingredient, TextView textView) {
            this.ingredient = ingredient;
            this.textView = textView;
        }

        public Ingredient getIngredient() {
            return ingredient;
        }

        public TextView getTextView() {
            return textView;
        }
    }
}
