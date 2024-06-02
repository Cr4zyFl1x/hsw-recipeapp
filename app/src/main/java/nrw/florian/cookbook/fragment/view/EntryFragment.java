package nrw.florian.cookbook.fragment.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;

import nrw.florian.cookbook.R;
import nrw.florian.cookbook.api.weather.OpenWeatherMapAPIClient;
import nrw.florian.cookbook.api.weather.data.CurrentWeatherData;
import nrw.florian.cookbook.databinding.FragmentEntryBinding;
import nrw.florian.cookbook.db.ingredient.IngredientEntity;
import nrw.florian.cookbook.db.ingredient.IngredientUnit;
import nrw.florian.cookbook.db.recipe.RecipeBaseCategory;
import nrw.florian.cookbook.db.recipe.RecipeDifficulty;
import nrw.florian.cookbook.db.recipe.RecipeEntity;
import nrw.florian.cookbook.db.recipeproperty.RecipeProperty;
import nrw.florian.cookbook.db.recipeproperty.RecipePropertyEntity;

/**
 * @author Florian J. Kleine-Vorholt
 */
public class EntryFragment extends Fragment {

    /**
     * View binding
     */
    private FragmentEntryBinding binding;

    /**
     * Location manager
     */
    private LocationManager locationManager;

    /**
     * Location listener
     */
    private LocationListener locationListener;
    private Location currentLocation;

    /**
     * Last weather data
     */
    private CurrentWeatherData lastWeatherData;

    /**
     * Request permission launcher
     */
    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), e -> processResult());


    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = FragmentEntryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        initLocationListeners();

        initMenuButtons();

        initWeatherContainerClickListener();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume()
    {
        super.onResume();
        if (lastWeatherData != null)
            updateWeatherContainer(lastWeatherData);
        if (currentLocation != null)
            updateWeatherData(currentLocation);
    }


    /**
     * Initializes the location listeners
     */
    private void initLocationListeners()
    {
        if (this.requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                this.requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, getWeatherLocationListener());
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }


    /**
     * Processes the result of the location permission request
     */
    private void processResult() {

        requireActivity().runOnUiThread(() -> {
            // Permission granted
            if (this.requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    this.requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), R.string.location_permitted_message, Toast.LENGTH_SHORT).show();
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, getWeatherLocationListener());
                return;
            }

            // Permission denied
            Toast.makeText(requireActivity(), getString(R.string.location_denied_message), Toast.LENGTH_SHORT).show();
            lastWeatherData = new CurrentWeatherData();
            lastWeatherData.setCity(getString(R.string.unknown));
            lastWeatherData.setTempC(Double.NaN);
            updateWeatherContainer(lastWeatherData);
        });
    }


    /**
     * Initializes the menu buttons
     */
    private void initMenuButtons()
    {
        binding.recipeOverviewMenuButton.setOnClickListener((v) -> NavHostFragment.findNavController(this)
                .navigate(EntryFragmentDirections.actionEntryFragmentToRecipeOverviewFragment()));

        binding.addRecipeMenuButton.setOnClickListener(v -> NavHostFragment.findNavController(this)
                .navigate(EntryFragmentDirections.actionEntryFragmentToEditRecipeFragment()));

        binding.shoppingListMenuButton.setOnClickListener((v) -> NavHostFragment.findNavController(this)
                .navigate(EntryFragmentDirections.actionEntryFragmentToShoppingListFragment()));
    }

    private void initWeatherContainerClickListener()
    {
        // TODO: Dies ist nur übergangsweise für das Testen der Detailansicht
        RecipeEntity recipe;
        recipe = new RecipeEntity(1, "Toast", null, 2, "Test1234\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nTest1234Test1234", RecipeBaseCategory.MAIN, RecipeDifficulty.EASY, 20, 200,
                new ArrayList<>(), new ArrayList<>());
        recipe.getRecipePropertyEntities().add(new RecipePropertyEntity(RecipeProperty.COLD));
        recipe.getRecipePropertyEntities().add(new RecipePropertyEntity(RecipeProperty.VEGETARIAN));
        recipe.getIngredients().add(new IngredientEntity(1, "Toast", 1, IngredientUnit.PIECE));
        recipe.getIngredients().add(new IngredientEntity(1, "Cheese", 2.5, IngredientUnit.SLICE));
        recipe.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.exampleimage_recipe));


        binding.weatherContainer.setOnClickListener((v) -> {
            if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED &&
                    requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(requireActivity(), getString(R.string.location_denied_message), Toast.LENGTH_SHORT).show();
                return;
            }
            NavHostFragment.findNavController(this)
                    .navigate(EntryFragmentDirections.actionEntryFragmentToRecipeDetailsFragment(recipe));
        });
    }


    /**
     * Gets the LocationListener to update the weather data based on the position
     * @return The LocationListener singleton
     */
    private synchronized LocationListener getWeatherLocationListener()
    {
        if (locationListener == null) {
            locationListener =  loc -> {
                this.currentLocation = loc;
                updateWeatherData(loc);
            };
        }
        return locationListener;
    }


    /**
     * Updates the weather data based on the current location
     * @param loc the current location
     */
    private synchronized void updateWeatherData(final Location loc)
    {
        final OpenWeatherMapAPIClient client = OpenWeatherMapAPIClient.createAPIClient(getString(R.string.weather_api_key));
        new Thread(() -> {
            lastWeatherData = client.getCurrentWeather(loc.getLatitude(), loc.getLongitude());
            updateWeatherContainer(lastWeatherData);
        }).start();
    }


    /**
     * Updates the UI with the last weather data
     * @param lastWeatherData the last weather data
     */
    private void updateWeatherContainer(final CurrentWeatherData lastWeatherData)
    {
        if (lastWeatherData == null) {
            binding.menuWeatherLocationTextView.setText(getString(R.string.error));
            return;
        }
        requireActivity().runOnUiThread(() -> {
            if (lastWeatherData.getCity() != null) {
                binding.menuWeatherLocationTextView.setText(lastWeatherData.getCity());
                if (lastWeatherData.getImage() != null)
                    binding.menuWeatherIconImageView.setImageBitmap(lastWeatherData.getImage());
                if (lastWeatherData.getTempC() != null)
                    binding.menuWeatherTemperatureTextView.setText(
                            getString(R.string.entry_tempC_label,
                                    new DecimalFormat("#.0").format(lastWeatherData.getTempC())));
            }
        });
    }
}