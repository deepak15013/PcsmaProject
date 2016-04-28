package in.deepaksood.pcsmaproject.loginpackage;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import in.deepaksood.pcsmaproject.mainactivitypackage.MainActivity;
import in.deepaksood.pcsmaproject.R;
import in.deepaksood.pcsmaproject.preferencemanagerpackage.PrefManager;

public class Registration extends AppCompatActivity {

    public static final String TAG = Registration.class.getSimpleName();

    Button next;
    EditText contact_num;
    TextView location;

    private String userName = "";
    private String userEmailId = "";
    private String userLocation="";
    private String userProfilePictureUrl="";
    private String userCoverPictureUrl="";
    private String userContactNum = "";

    LocationManager locationManager;
    LocationListener locationListener;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        location = (TextView) findViewById(R.id.location);
        next = (Button) findViewById(R.id.next);
        contact_num = (EditText) findViewById(R.id.contact_num);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location loc) {
                double lat = loc.getLatitude();
                double lon = loc.getLongitude();

                userLocation = lat + "," +lon;

                location.setText(userLocation);
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        Bundle bundle = getIntent().getExtras();
        userName = bundle.getString("DISPLAY_NAME");
        userEmailId = bundle.getString("DISPLAY_EMAIL_ID");
        userProfilePictureUrl = bundle.getString("");
        userCoverPictureUrl = bundle.getString("");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(location.getText().toString().equalsIgnoreCase("Getting Device Coordinates")) {
                    Toast.makeText(Registration.this, "Please Wait Getting Device Location", Toast.LENGTH_SHORT).show();
                }
                else {
                    storeUserData();

                    Intent intent = new Intent(Registration.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    public void storeUserData() {
        userContactNum = contact_num.getText().toString();

        PrefManager prefManager = new PrefManager(this);
        prefManager.saveUserData(userName, userEmailId, userProfilePictureUrl, userCoverPictureUrl, userContactNum, userLocation);
        prefManager.createLogin();
    }

    @Override
    protected void onPause() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(locationListener);
        super.onPause();
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}