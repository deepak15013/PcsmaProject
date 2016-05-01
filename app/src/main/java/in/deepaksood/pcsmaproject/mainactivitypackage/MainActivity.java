package in.deepaksood.pcsmaproject.mainactivitypackage;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import in.deepaksood.pcsmaproject.bookaddpackage.AddBook;
import in.deepaksood.pcsmaproject.bookaddpackage.CaptureActivityAnyOrientation;
import in.deepaksood.pcsmaproject.R;
import in.deepaksood.pcsmaproject.datamodelpackage.UserObject;
import in.deepaksood.pcsmaproject.loginpackage.LoginActivity;
import in.deepaksood.pcsmaproject.navigationdrawer.transactionspackage.TransactionsFragment;
import in.deepaksood.pcsmaproject.navigationdrawer.mycollectionpackage.MyCollection;
import in.deepaksood.pcsmaproject.navigationdrawer.searchbookpackage.SearchBook;
import in.deepaksood.pcsmaproject.preferencemanagerpackage.PrefManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private String userName = "";
    private String userEmailId = "";
    private String userProfilePictureUrl = "";
    private String userCoverPictureUrl = "";
    private String userContactNum = "";
    private String userLocation = "";

    TextView viewDisplayName;
    TextView viewEmailId;
    ImageView displayPic;
    ImageView coverPic;
    TextView contactNum;

    private GoogleApiClient mGoogleApiClient;

    public String getUserEmailId() {
        return userEmailId;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addBookByScan();
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        displayNavigationBarDetails();
        displayView(R.id.nav_my_collection);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    public void addBookByScan() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(intentResult != null && intentResult.getBarcodeImagePath() != null) {
            String scanFormat = intentResult.getFormatName();
            String scanContent = intentResult.getContents();
            String imagePath = intentResult.getBarcodeImagePath();

            Log.v(TAG,"scanFormat: "+scanFormat);
            Log.v(TAG,"scanContent: "+scanContent);
            Log.v(TAG,"imagePath: "+imagePath);

            Intent intent = new Intent(MainActivity.this,AddBook.class);
            Bundle bundle = new Bundle();
            bundle.putString("SCAN_FORMAT",scanFormat);
            bundle.putString("SCAN_CONTENT",scanContent);
            bundle.putString("IMAGE_PATH",imagePath);
            bundle.putString("USER_EMAIL_ID", userEmailId);
            intent.putExtras(bundle);
            startActivity(intent);

        }
        else {
            Toast.makeText(MainActivity.this, "No Scan Data Received", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG,"onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG,"onResume");
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        MyCollection myCollection = (MyCollection) getSupportFragmentManager().findFragmentByTag("MYCOLLECTIONFRAGMENT");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(myCollection != null && myCollection.isVisible()) {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);


        } else {
            displayView(R.id.nav_my_collection);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this, "Settings Coming Soon", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displayView(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Log.v(TAG,"onResumeFragements");
        displayView(R.id.nav_my_collection);
    }

    public void displayView(int viewId) {

        Fragment fragment = null;
        FragmentTransaction ft;
        ft = getSupportFragmentManager().beginTransaction();

        switch (viewId) {
            case R.id.nav_my_collection:
                Log.v(TAG,"nav_my_collection");
                fragment = new MyCollection();
                ft.replace(R.id.content_frame, fragment, "MYCOLLECTIONFRAGMENT");
                ft.commit();
                break;

            case R.id.nav_search_book:
                fragment = new SearchBook();
                ft.replace(R.id.content_frame, fragment, "SEARCHBOOKFRAGMENT");
                ft.commit();
                break;

            case R.id.nav_add_book:
                addBookByScan();
                break;

            case R.id.nav_transactions:
                fragment = new TransactionsFragment();
                ft.replace(R.id.content_frame, fragment, "TRANSACTIONSFRAGMENT");
                ft.commit();
                break;

            case R.id.nav_log_out:
                Toast.makeText(MainActivity.this, "Log_out", Toast.LENGTH_SHORT).show();

                googleSignOut();

                PrefManager prefManager = new PrefManager(this);
                prefManager.clearSession();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.nav_share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setData(Uri.parse("mailto:"));
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "HEY CHECK IT OUT");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey I got this great app called BookXchange. Great for getting books from other people. Download now from Google Play Store");
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, "Share with"));
                break;

            case R.id.nav_contact_us:
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setType("plain/text");
                sendIntent.setData(Uri.parse("deepak15013@iiitd.ac.in"));
                sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "deepak15013@iiitd.ac.in" });
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Write your text here - ");
                startActivity(sendIntent);
                break;
        }
    }

    private void googleSignOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.v(TAG,"Log out success: "+status);
                    }
                });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(MainActivity.this, "Error logging out", Toast.LENGTH_SHORT).show();
    }


    public void displayNavigationBarDetails() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        viewDisplayName = (TextView) header.findViewById(R.id.displayName);
        viewEmailId = (TextView) header.findViewById(R.id.displayEmailId);
        displayPic = (ImageView) header.findViewById(R.id.displayPic);
        coverPic = (ImageView) header.findViewById(R.id.coverPic);
        contactNum = (TextView) header.findViewById(R.id.tv_nav_header_contact_num);

        PrefManager prefManager = new PrefManager(this);
        userName = prefManager.getDisplayName();
        userEmailId = prefManager.getDisplayEmailId();
        userProfilePictureUrl = prefManager.getPhotoUrl();
        userCoverPictureUrl = prefManager.getCoverUrl();
        userContactNum = prefManager.getMobileNumber();
        userLocation = prefManager.getLocation();

        viewDisplayName.setText(userName);
        viewEmailId.setText(userEmailId);
        Picasso.with(this).load(userProfilePictureUrl).into(displayPic);
        Picasso.with(this).load(userCoverPictureUrl).fit().centerCrop().into(coverPic);
        contactNum.setText(userContactNum);

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
                UserObject userObject = mapper.load(UserObject.class, userEmailId);
                Log.v(TAG,"user: "+userObject.getUserName());
                Log.v(TAG,"email: "+userObject.getUserEmailId());
            }

            else
                Log.v(TAG,"not saved");

            return "Executed";
        }
    }

}
