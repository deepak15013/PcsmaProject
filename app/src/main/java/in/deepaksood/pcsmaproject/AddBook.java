package in.deepaksood.pcsmaproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class AddBook extends AppCompatActivity {

    private static String TAG = AddBook.class.getSimpleName();

    String api_key = "AIzaSyB6uIztKiDwY7KAARNldqYEDXiMhm5DRhk";

    private TextView textScanFormat;
    private TextView textScanContent;
    private ImageView scanImage;
    private TextView textscanTime;

    private String url = "";
    private String scanFormat;
    private String scanContent;
    private String imagePath;
    private String emailId="";

    private ImageView bookPoster;
    private TextView title;
    private TextView author;
    private TextView publisher;
    private TextView publicationDate;
    private TextView binding;
    private TextView productDescription;
    
    private Button addBookButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        textScanFormat = (TextView) findViewById(R.id.scanFormat);
        textScanContent = (TextView) findViewById(R.id.scanContent);
        scanImage = (ImageView) findViewById(R.id.scanImage);
        textscanTime = (TextView) findViewById(R.id.scanTime);

        bookPoster = (ImageView) findViewById(R.id.bookPoster);
        title = (TextView) findViewById(R.id.title);
        author = (TextView) findViewById(R.id.author);
        publisher = (TextView) findViewById(R.id.publisher);
        publicationDate = (TextView) findViewById(R.id.publicationDate);
        binding = (TextView) findViewById(R.id.binding);
        productDescription = (TextView) findViewById(R.id.productDescription);
        
        addBookButton = (Button) findViewById(R.id.addBook); 


        Bundle bundle = getIntent().getExtras();
        scanFormat = bundle.getString("SCAN_FORMAT");
        scanContent = bundle.getString("SCAN_CONTENT");
        imagePath = bundle.getString("IMAGE_PATH");
        emailId = bundle.getString("EMAIL_ID");

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
        
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v(TAG,"title: "+title.getText());
                if(! title.getText().toString().equalsIgnoreCase("Title")) {
                    saveBookData();
                }
                else {
                    Toast.makeText(AddBook.this, "Please scan a correct book", Toast.LENGTH_SHORT).show();
                }


            }
        });
        

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
//                        result.setText(response);
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
                            parseAmazonData(response);
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

    public void parseAmazonData(String response) {

        List<ItemObject> items = null;

        XMLPullParserHandler xmlPullParserHandler = new XMLPullParserHandler();
        items = xmlPullParserHandler.parse(response);

        title.setText(items.get(0).getTitle());
        author.setText(items.get(0).getAuthor());
        publisher.setText(items.get(0).getPublisher());
        publicationDate.setText(items.get(0).getPublicationDate());
        binding.setText(items.get(0).getBinding());
        productDescription.setText(items.get(0).getProductDescription());

        Picasso.with(this).load(items.get(0).getMediumImageUrl()).into(bookPoster);

    }

    public void saveBookData() {
        Toast.makeText(AddBook.this, "Add this book", Toast.LENGTH_SHORT).show();

        emailId = emailId.replaceAll("@","");
        emailId = emailId.replaceAll("\\.","");
        Log.v(TAG,"newEmail: "+emailId);

        ParseObject testObject = new ParseObject(emailId);
        testObject.put("isbn",scanContent);
        testObject.put("title", title.getText());
        testObject.put("author",author.getText());
        testObject.saveInBackground();
    }
}
