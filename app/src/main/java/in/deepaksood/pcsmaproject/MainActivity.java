package in.deepaksood.pcsmaproject;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private String displayName="";
    private String displayEmailId="";
    private String photoUrl="";
    private String coverUrl="";

    TextView viewDisplayName;
    TextView viewEmailId;

    ImageView displayPic;
    ImageView coverPic;

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
                    /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();*/
                    IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                    intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    intentIntegrator.setBarcodeImageEnabled(true);
                    intentIntegrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
                    intentIntegrator.setOrientationLocked(true);
                    intentIntegrator.initiateScan();
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        displayNavigationBarDetails();

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
            intent.putExtras(bundle);
            startActivity(intent);

        }
        else {
            Toast.makeText(MainActivity.this, "No Scan Data Received", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void displayNavigationBarDetails() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        viewDisplayName = (TextView) header.findViewById(R.id.displayName);
        viewEmailId = (TextView) header.findViewById(R.id.displayEmailId);
        displayPic = (ImageView) header.findViewById(R.id.displayPic);
        coverPic = (ImageView) header.findViewById(R.id.coverPic);

        Bundle bundle = getIntent().getExtras();
        displayName = bundle.getString("DISPLAY_NAME");
        viewDisplayName.setText(displayName);

        displayEmailId = bundle.getString("DISPLAY_EMAIL_ID");
        viewEmailId.setText(displayEmailId);

        photoUrl = bundle.getString("PHOTO_URL");
        if(!photoUrl.contentEquals(""))
            Picasso.with(this).load(photoUrl).into(displayPic);


        coverUrl = bundle.getString("COVER_URL");
        if(coverUrl != null)
            Picasso.with(this).load(coverUrl).fit().centerCrop().into(coverPic);
    }
}
