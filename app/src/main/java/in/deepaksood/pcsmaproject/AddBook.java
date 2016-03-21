package in.deepaksood.pcsmaproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;

public class AddBook extends AppCompatActivity {

    private static String TAG = AddBook.class.getSimpleName();

    String api_key = "AIzaSyB6uIztKiDwY7KAARNldqYEDXiMhm5DRhk";

    TextView textScanFormat;
    TextView textScanContent;
    ImageView scanImage;
    TextView result;

    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        textScanFormat = (TextView) findViewById(R.id.scanFormat);
        textScanContent = (TextView) findViewById(R.id.scanContent);
        scanImage = (ImageView) findViewById(R.id.scanImage);
        result = (TextView) findViewById(R.id.result);

        Bundle bundle = getIntent().getExtras();
        String scanFormat = bundle.getString("SCAN_FORMAT");
        String scanContent = bundle.getString("SCAN_CONTENT");
        String imagePath = bundle.getString("IMAGE_PATH");

        textScanFormat.setText(scanFormat);
        textScanContent.setText(scanContent);
        Log.v(TAG,"imagePathAddBook: "+imagePath);

        File imageFile = new File(imagePath);
        if(imageFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            scanImage.setImageBitmap(bitmap);
        }

        if(scanContent != null && scanFormat != null && scanFormat.equalsIgnoreCase("EAN_13")) {
            //book Search in google Books

            url = "https://www.googleapis.com/books/v1/volumes?"+
                    "q=isbn:"+scanContent+"&key="+api_key;

            Log.v(TAG,"url: "+url);

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.v(TAG,"response: "+response);
                            result.setText(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v(TAG,"Error: "+error);
                }
            });
            queue.add(stringRequest);

        }
        else {
            Toast.makeText(AddBook.this, "Please Scan a ISBN Book to get Book details", Toast.LENGTH_SHORT).show();
        }

    }
}
