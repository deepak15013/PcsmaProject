package in.deepaksood.pcsmaproject.loginpackage;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
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

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import in.deepaksood.pcsmaproject.datamodelpackage.UserObject;
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

    UserObject userObject;

    boolean locationFetched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Log.v(TAG,"onCreate");

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
                locationFetched = true;

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
        userProfilePictureUrl = bundle.getString("PHOTO_URL");
        userCoverPictureUrl = bundle.getString("COVER_URL");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(locationFetched) {
                    if(contact_num.getText().toString().matches("[0-9]+") && contact_num.getText().toString().length() == 10) {
                        userContactNum = contact_num.getText().toString();
                        storeUserDataDynamoDb();
                        storePrefManagerData();
                        Intent intent = new Intent(Registration.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(Registration.this, "Enter a valid Mobile Number", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(Registration.this, "Please Wait Getting Device Location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void storeUserDataDynamoDb() {
        Log.v(TAG,"StoreUserDataDynamoDb");
        userObject = new UserObject(userName, userEmailId, userProfilePictureUrl, userCoverPictureUrl, userContactNum, userLocation);
        new db().execute();
    }

    public void storePrefManagerData() {
        Log.v(TAG,"storePrefManagerData");
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

    private class db extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            CognitoCachingCredentialsProvider credentialsProvider;

            credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "us-east-1:25c78fbe-abb8-4655-9309-8442c610ffd0", // Identity Pool ID
                    Regions.US_EAST_1 // Region
            );

            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);

            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

            if(mapper != null) {
                mapper.save(userObject);
                Log.v(TAG,"userName: "+userObject.getUserName());
                Log.v(TAG,"EmailId "+userObject.getUserEmailId());
                Log.v(TAG,"ProfilePicture: "+userObject.getUserProfilePictureUrl());
                Log.v(TAG,"CoverPicture: "+userObject.getUserCoverPictureUrl());
                Log.v(TAG,"ContactNum: "+userObject.getUserContactNum());
                Log.v(TAG,"UserLocation: "+userObject.getUserLocation());
            }

            else
                Log.v(TAG,"not saved");

            return "Executed";
        }
    }

}