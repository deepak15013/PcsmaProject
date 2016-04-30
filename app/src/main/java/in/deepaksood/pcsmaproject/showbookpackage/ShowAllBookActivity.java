package in.deepaksood.pcsmaproject.showbookpackage;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.ArrayList;
import java.util.List;

import in.deepaksood.pcsmaproject.R;
import in.deepaksood.pcsmaproject.datamodelpackage.BookObject;
import in.deepaksood.pcsmaproject.datamodelpackage.CardObject;
import in.deepaksood.pcsmaproject.datamodelpackage.UserObject;

public class ShowAllBookActivity extends AppCompatActivity {

    private static final String TAG = ShowAllBookActivity.class.getSimpleName();

    PaginatedScanList<UserObject> result;
    private List<CardObject> cardObjects;

    ShowBookAdapter adapter;
    private RecyclerView rvShowBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book);

        Toolbar toolbar = (Toolbar) findViewById(R.id.show_book_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("All Books");

        rvShowBook = (RecyclerView)findViewById(R.id.rv_show_book);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvShowBook.setLayoutManager(llm);
        rvShowBook.setHasFixedSize(true);

        cardObjects = new ArrayList<>();
        new db().execute();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
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
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                result = mapper.scan(UserObject.class, scanExpression);
            }

            else
                Log.v(TAG,"not saved");

            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.v(TAG,"result: "+result);

            try {
                if(result != null) {
                    for(UserObject userObject: result) {
                        for(BookObject bookObject: userObject.getBookObjectSet()) {
                            CardObject cardObject = new CardObject(bookObject.getBookName(), bookObject.getBookAuthor(), bookObject.getBookIsbn(), bookObject.getBookPosterUrl(), userObject.getUserName(), userObject.getUserEmailId(), userObject.getUserProfilePictureUrl(), userObject.getUserCoverPictureUrl(), userObject.getUserContactNum(), userObject.getUserLocation());
                            cardObjects.add(cardObject);
                        }
                    }
                }
                initializeAdapter();
            } catch (Exception e) {
                Log.v(TAG,"Exception e: "+e);
            }
            Log.v(TAG,"Completed");
        }
    }

    private void initializeAdapter(){
        adapter = new ShowBookAdapter(cardObjects);
        rvShowBook.setAdapter(adapter);
    }

}
