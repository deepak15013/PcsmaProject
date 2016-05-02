package in.deepaksood.pcsmaproject.mailpackage;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.ArrayList;
import java.util.List;

import in.deepaksood.pcsmaproject.R;
import in.deepaksood.pcsmaproject.datamodelpackage.BookObject;
import in.deepaksood.pcsmaproject.datamodelpackage.UserObject;
import in.deepaksood.pcsmaproject.preferencemanagerpackage.PrefManager;

public class LendBook extends AppCompatActivity {

    private static final String TAG = LendBook.class.getSimpleName();

    TextView userEmailId;
    TextView userBookIsbn;

    String emailId = "";
    String isbn = "";

    String mainUserEmailId = "";

    boolean bookFound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend_book);

        userEmailId = (TextView) findViewById(R.id.tv_renter_emailId);
        userBookIsbn = (TextView) findViewById(R.id.tv_renter_book_isbn);

        Intent intent = getIntent();
        emailId = intent.getStringExtra("EMAILID");
        isbn = intent.getStringExtra("ISBN");

        userEmailId.setText(emailId);
        userBookIsbn.setText(isbn);

        PrefManager prefManager = new PrefManager(this);
        mainUserEmailId = prefManager.getDisplayEmailId();

        final Button button = (Button) findViewById(R.id.btn_yes_give);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(LendBook.this, "yes", Toast.LENGTH_SHORT).show();
                new db().execute();

            }
        });

        final Button button1 = (Button) findViewById(R.id.btn_exit_give);
        assert button1 != null;
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendRejectMail();
                finish();
                moveTaskToBack(true);

            }
        });

    }

    UserObject userObject;
    List<BookObject> bookObjects = new ArrayList<>();
    private class db extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

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

            try {
                if(mainUserEmailId != null && !mainUserEmailId.equals("")) {
                    userObject = mapper.load(UserObject.class, mainUserEmailId);
                    bookObjects = userObject.getBookObjectSet();
                    for(BookObject i: bookObjects) {
                        if(i.getBookIsbn().equals(isbn)) {
                            i.setHaveBook(false);
                            bookFound = true;
                        }
                    }
                    userObject.setBookObjectSet(bookObjects);
                    mapper.save(userObject);
                }
            } catch (Exception e) {
                Log.v(TAG,"Exception e: "+e);
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            sendConfirmationMail();
            finish();
        }
    }

    public void sendConfirmationMail() {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setType("plain/text");
        sendIntent.setData(Uri.parse(emailId));
        sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { emailId });
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Confirmation for book");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Your request for book isbn: "+isbn+" has been accepted.");
        startActivity(sendIntent);
    }


    public void sendRejectMail() {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setType("plain/text");
        sendIntent.setData(Uri.parse(emailId));
        sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { emailId });
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Book request rejected");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Your request for book isbn: "+isbn+" has been rejected. Sorry but I need it now.");
        startActivity(sendIntent);
    }

}
