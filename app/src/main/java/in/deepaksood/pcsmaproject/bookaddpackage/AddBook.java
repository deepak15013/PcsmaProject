package in.deepaksood.pcsmaproject.bookaddpackage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import in.deepaksood.pcsmaproject.R;
import in.deepaksood.pcsmaproject.datamodelpackage.BookFullDetailsObject;
import in.deepaksood.pcsmaproject.datamodelpackage.BookObject;
import in.deepaksood.pcsmaproject.datamodelpackage.UserObject;

public class AddBook extends AppCompatActivity {

    private static String TAG = AddBook.class.getSimpleName();

    String api_key = "AIzaSyB6uIztKiDwY7KAARNldqYEDXiMhm5DRhk";

    private TextView textScanFormat;
    private TextView textScanContent;
    private ImageView scanImage;
    private TextView textscanTime;

    private String scanFormat;
    private String scanContent;
    private String imagePath;
    private String emailId;

    private ImageView bookPoster;
    private TextView title;
    private TextView author;
    private TextView publisher;
    private TextView publicationDate;
    private TextView binding;
    private TextView productDescription;
    
    private Button addBookButton;

    private String bookName = "";
    private String bookAuthor = "";
    private String bookIsbn = "";
    private String bookPosterUrl = "";

    private RadioGroup rgRentSale;
    private RadioButton rbRentSaleId;
    private boolean bookRent = true;


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

        rgRentSale = (RadioGroup) findViewById(R.id.rg_rent_sale);
        addBookButton = (Button) findViewById(R.id.addBook); 


        Bundle bundle = getIntent().getExtras();
        scanFormat = bundle.getString("SCAN_FORMAT");
        scanContent = bundle.getString("SCAN_CONTENT");
        imagePath = bundle.getString("IMAGE_PATH");
        emailId = bundle.getString("USER_EMAIL_ID");
        bookIsbn = scanContent;

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
            amazonBookSearch();

        }
        else {
            Toast.makeText(AddBook.this, "Please Scan a ISBN Book to get Book details", Toast.LENGTH_SHORT).show();
        }
        
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(! title.getText().toString().equalsIgnoreCase("Title")) {

                    int selectedId = rgRentSale.getCheckedRadioButtonId();
                    Log.v(TAG,"selectedId: "+selectedId);

                    rbRentSaleId = (RadioButton) findViewById(selectedId);
                    Log.v(TAG,"rb: "+rbRentSaleId.getText());

                    if(rbRentSaleId.getText().toString().equals("For Sale")) {
                        bookRent = false;
                        Log.v(TAG,"for sale");
                    }

                    saveBookData();
                    finish();
                }
                else {
                    Toast.makeText(AddBook.this, "Please scan a correct book", Toast.LENGTH_SHORT).show();
                }
            }
        });
        

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

        Log.v(TAG,"response of scan: "+response);

        List<BookFullDetailsObject> items = null;

        XMLPullParserHandler xmlPullParserHandler = new XMLPullParserHandler();
        items = xmlPullParserHandler.parse(response);

        if(items != null) {
            try {
                bookName = items.get(0).getTitle();
                bookAuthor = items.get(0).getAuthor();
                bookPosterUrl = items.get(0).getMediumImageUrl();

                title.setText(items.get(0).getTitle());
                author.setText(items.get(0).getAuthor());
                publisher.setText(items.get(0).getPublisher());
                publicationDate.setText(items.get(0).getPublicationDate());
                binding.setText(items.get(0).getBinding());
                productDescription.setText(items.get(0).getProductDescription());

                Picasso.with(this).load(items.get(0).getMediumImageUrl()).into(bookPoster);
            } catch (Exception e) {
                Log.v(TAG,"exception getting book from amazon");
                Toast.makeText(AddBook.this, "Cannot find the book", Toast.LENGTH_SHORT).show();
            }

        }

    }

    BookObject bookObject;
    public void saveBookData() {

        bookObject = new BookObject(bookName, bookAuthor, bookIsbn, bookPosterUrl, bookRent, true);
        new db().execute();

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
                UserObject userObject = mapper.load(UserObject.class, emailId);

                if(userObject.getBookObjectSet() != null) {
                    if(userObject.getBookObjectSet().size() == 0) {
                        Log.v(TAG,"first book added");
                        List<BookObject> bookObjectSet = new ArrayList<>();
                        bookObjectSet.add(bookObject);
                        userObject.setBookObjectSet(bookObjectSet);
                        mapper.save(userObject);
                    }
                    else {
                        List<BookObject> bookObjectList = userObject.getBookObjectSet();
                        Log.v(TAG,"book: "+bookObjectList.get(0).getBookName());
                        if(bookObjectList.contains(bookObject)) {
                            Log.v(TAG,"Already Added");
                        }
                        else {
                            Log.v(TAG,"new book added to list");
                            bookObjectList.add(bookObject);
                            userObject.setBookObjectSet(bookObjectList);
                            mapper.save(userObject);
                        }
                    }
                }
                else {
                    Log.v(TAG,"first book added");
                    List<BookObject> bookObjectSet = new ArrayList<>();
                    bookObjectSet.add(bookObject);
                    userObject.setBookObjectSet(bookObjectSet);
                    mapper.save(userObject);
                }
            }

            else
                Log.v(TAG,"not saved");

            return "Executed";
        }
    }

}
