package nrw.florian.cookbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;

import java.util.Optional;

public class GPSTracker extends Activity {
    nrw.florian.cookbook.Location.LocationBuilder locationBuilder;
    private final FusedLocationProviderClient fusedLocationClient;

    private final Context context;

    public GPSTracker(Context context) {
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public Optional<nrw.florian.cookbook.Location> getGPSLocation(View view) {
        this.locationBuilder = new nrw.florian.cookbook.Location.LocationBuilder();
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            createSnackbarPermissionDenied(view);
        } else {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    locationBuilder.latitude(location.getLatitude());
                    locationBuilder.longitude(location.getLongitude());
                }
            });
        }
        return Optional.of(locationBuilder.build());
    }

    private void createSnackbarPermissionDenied(View view) {
        Snackbar.make(context, view, context.getString(R.string.no_permission_cannot_access_location),
                        Snackbar.LENGTH_LONG)
                .setAction(R.string.permission_edit, click -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .show();
    }
}
