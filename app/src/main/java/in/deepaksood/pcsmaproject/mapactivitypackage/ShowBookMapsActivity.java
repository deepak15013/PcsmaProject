package in.deepaksood.pcsmaproject.mapactivitypackage;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import in.deepaksood.pcsmaproject.R;

public class ShowBookMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = ShowBookMapsActivity.class.getSimpleName();

    private GoogleMap mMap;

    private ArrayList<String> locations;
    private ArrayList<String> bookNames;
    private ArrayList<String> userNames;
    private int position = 0;
    private ArrayList<LatLng> latLngArrayList;
    private ArrayList<Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locations = new ArrayList<>();
        bookNames = new ArrayList<>();
        userNames = new ArrayList<>();
        markers = new ArrayList<>();
        latLngArrayList = new ArrayList<>();

        Intent intent = getIntent();
        locations = intent.getStringArrayListExtra("LOCATIONS");
        bookNames = intent.getStringArrayListExtra("BOOK_NAMES");
        userNames = intent.getStringArrayListExtra("USER_NAMES");
        position = intent.getIntExtra("POSITION",0);

        for(String string: locations) {
            String[] strings = string.split(",");
            double lat = Double.parseDouble(strings[0]);
            double lon = Double.parseDouble(strings[1]);
            LatLng latLng = new LatLng(lat, lon);
            latLngArrayList.add(latLng);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        UiSettings uiSettings;
        uiSettings = mMap.getUiSettings();

        mMap.setMyLocationEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setMapToolbarEnabled(true);

        int i = 0;
        // Add markers
        for(LatLng latLng: latLngArrayList) {
            Log.v(TAG,"marker added");
            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
            if(bookNames.get(i) != null)
                marker.setTitle(bookNames.get(i));
            if(userNames.get(i) != null)
                marker.setSnippet(userNames.get(i));

            marker.setAlpha(0.8f);
            markers.add(marker);
            i++;
        }

        // Move camera to current book position
        markers.get(position).setAlpha(1f);
        markers.get(position).showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngArrayList.get(position), 17));

    }
}
