package nrw.florian.cookbook;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

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

import nrw.florian.cookbook.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationPermissionRequest.launch(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION});
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonFirst.setOnClickListener(v ->
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_weatherFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    ActivityResultLauncher<String[]> locationPermissionRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
        Boolean fineLocationGranted = result.getOrDefault(ACCESS_FINE_LOCATION, false);
        Boolean coarseLocationGranted = result.getOrDefault(ACCESS_COARSE_LOCATION, false); // TODO muss hier auch noch Background-Permission hin?
        if (fineLocationGranted != null && fineLocationGranted) {
            // Precise location access granted.
            Toast.makeText(requireActivity(), R.string.permission_granted, Toast.LENGTH_SHORT).show();
        } else if (coarseLocationGranted != null && coarseLocationGranted) {
            // Only approximate location access granted.
            Toast.makeText(requireActivity(), R.string.permission_granted, Toast.LENGTH_SHORT).show();
        } else {
            // No location access granted.
            Toast.makeText(requireActivity(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
        }
    });
}