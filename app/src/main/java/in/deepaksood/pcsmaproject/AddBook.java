package in.deepaksood.pcsmaproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class AddBook extends AppCompatActivity {

    private static String TAG = AddBook.class.getSimpleName();

    String api_key = "AIzaSyB6uIztKiDwY7KAARNldqYEDXiMhm5DRhk";

    private TextView textScanFormat;
    private TextView textScanContent;
    private ImageView scanImage;
    private TextView textscanTime;
    private TextView result;
    private TextView awsResult;

    private String url = "";
    private String scanFormat;
    private String scanContent;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        textScanFormat = (TextView) findViewById(R.id.scanFormat);
        textScanContent = (TextView) findViewById(R.id.scanContent);
        scanImage = (ImageView) findViewById(R.id.scanImage);
        textscanTime = (TextView) findViewById(R.id.scanTime);
        result = (TextView) findViewById(R.id.result);
        awsResult = (TextView) findViewById(R.id.awsResult);

        Bundle bundle = getIntent().getExtras();
        scanFormat = bundle.getString("SCAN_FORMAT");
        scanContent = bundle.getString("SCAN_CONTENT");
        imagePath = bundle.getString("IMAGE_PATH");

        String timestamp = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd'  'HH:mm:ss");
        dfm.setTimeZone(TimeZone.getTimeZone("GMT"));
        timestamp = dfm.format(cal.getTime());

        textScanFormat.setText(scanFormat);
        textScanContent.setText(scanContent);
        textscanTime.setText(timestamp);

        Log.v(TAG,"imagePathAddBook: "+imagePath);

        File imageFile = new File(imagePath);
        if(imageFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap,80,80,true);
            scanImage.setImageBitmap(scaleBitmap);
        }

        if(scanContent != null && scanFormat != null && scanFormat.equalsIgnoreCase("EAN_13")) {

            //googleBookSearch();
            amazonBookSearch();
        }
        else {
            Toast.makeText(AddBook.this, "Please Scan a ISBN Book to get Book details", Toast.LENGTH_SHORT).show();
        }

    }

    public void googleBookSearch() {
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

    public void amazonBookSearch() {
        //book Search in Amazon aws

        Map<String, String> map = new HashMap<String, String>();
        map.put("Service","AWSECommerceService");
        map.put("Operation","ItemLookup");
        map.put("ResponseGroup","Medium");
        map.put("SearchIndex","All");
        map.put("IdType","ISBN");
        map.put("ItemId",scanContent);
        map.put("AssociateTag","deepaksood-21");

        SignedRequestHelperAWS signedRequestHelperAWS = new SignedRequestHelperAWS();
        String amazonUrl = signedRequestHelperAWS.sign(map);
        Log.v(TAG,"amazonUrl: "+amazonUrl);

        // Instantiate the RequestQueue.
        RequestQueue awsQueue = Volley.newRequestQueue(this);
        StringRequest awsStringRequest = new StringRequest(Request.Method.GET, amazonUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response != null) {
                            Log.v(TAG,"awsResponse: "+response);
                            awsResult.setText(response);
                            //parseData(response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v(TAG,"Error: "+error);
            }
        });
        awsQueue.add(awsStringRequest);
    }

    public void parseData(String response) {

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setInput(new StringReader(response));
            int eventType = xmlPullParser.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT) {
                String name = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if(name.equalsIgnoreCase("Title")) {

                            break;
                        }
                    case XmlPullParser.TEXT: {
                            String temp = xmlPullParser.getText();
                            Log.v(TAG,"temp: "+temp);
                            break;
                        }
                }
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }
}
