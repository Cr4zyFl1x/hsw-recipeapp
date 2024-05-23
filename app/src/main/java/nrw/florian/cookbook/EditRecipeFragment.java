package nrw.florian.cookbook;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import nrw.florian.cookbook.databinding.FragmentEditRecipeBinding;
import java.util.ArrayList;
import java.util.List;

public class EditRecipeFragment extends Fragment {

    private FragmentEditRecipeBinding binding;
    private List<Ingredient> ingredientsList = new ArrayList<>();
    private static final int REQUEST_IMAGE = 101;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditRecipeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Erstelle das Array und initialisiere es mit den Mengenoptionen
        String[] quantityOptions = {"kg", "g", "L", "ml", "Stücke", "EL", "TL"};

        // Erstelle den ArrayAdapter und setze die Optionen
        ArrayAdapter<String> quantityAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, quantityOptions);

        // Setze das Layout für die Dropdown-Liste
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Finde den Spinner
        Spinner typeOfQuantitySpinner = view.findViewById(R.id.typeOfQuantitySpinner);

        // Setze den ArrayAdapter auf den Spinner
        typeOfQuantitySpinner.setAdapter(quantityAdapter);

        // Erstelle das Array und initialisiere es mit den Mengenoptionen
        String[] categoryOptions = {"Suppe", "Soße", "Nachtisch", "Salat", "Hauptspeise", "Vorspeise"};

        // Erstelle den ArrayAdapter und setze die Optionen
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoryOptions);

        // Setze das Layout für die Dropdown-Liste
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Finde den Spinner
        Spinner categorySpinnerCreate = view.findViewById(R.id.categorySpinnerCreate);

        // Setze den ArrayAdapter auf den Spinner
        categorySpinnerCreate.setAdapter(categoryAdapter);

        // Erstelle das Array und initialisiere es mit den Mengenoptionen
        String[] difficultyOptions = {"Leicht", "Mittel", "Schwer"};

        // Erstelle den ArrayAdapter und setze die Optionen
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, difficultyOptions);

        // Setze das Layout für die Dropdown-Liste
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Finde den Spinner
        Spinner difficultySpinnerCreate = view.findViewById(R.id.diffcultySpinnerCreate);

        // Setze den ArrayAdapter auf den Spinner
        difficultySpinnerCreate.setAdapter(difficultyAdapter);

        binding.addIngredientButton.setOnClickListener(v -> {
            // Hole die eingegebenen Werte für Zutat, Menge und Maßeinheit
            String name = binding.ingredientTextInput.getText().toString();
            double quantity = Double.parseDouble(binding.quantityTextInput.getText().toString());
            String unit = typeOfQuantitySpinner.getSelectedItem().toString();

            // Erstelle ein Ingredient-Objekt mit den eingegebenen Werten
            Ingredient ingredient = new Ingredient(name, quantity, unit);

            // Füge das Ingredient-Objekt zur Liste hinzu
            ingredientsList.add(ingredient);

            // Erstelle ein TextView zur Anzeige der Zutat
            TextView textView = new TextView(requireContext());
            textView.setText(ingredient.toString());

            // Füge das TextView zum LinearLayout hinzu
            binding.safedIngriedientsLinearLayout.addView(textView);
        });

        binding.recipeImageButton.setOnClickListener(x -> {
            // Starte die Aktivität zum Hochladen eines Bildes
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_IMAGE, null);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            // Das ausgewählte Bild erhalten
            Uri selectedImageUri = data.getData();
            // Das Bild im ImageView anzeigen
            binding.recipeImageView.setImageURI(selectedImageUri);
            // Das ImageView sichtbar machen
            binding.recipeImageView.setVisibility(View.VISIBLE);
            //Button unsichtbar machen
            binding.recipeImageButton.setVisibility(View.GONE);
        }
    }
}
