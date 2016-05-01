package in.deepaksood.pcsmaproject.navigationdrawer.transactionspackage;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.ArrayList;
import java.util.List;

import in.deepaksood.pcsmaproject.R;
import in.deepaksood.pcsmaproject.datamodelpackage.BookObject;
import in.deepaksood.pcsmaproject.datamodelpackage.UserObject;
import in.deepaksood.pcsmaproject.mainactivitypackage.MainActivity;
import in.deepaksood.pcsmaproject.navigationdrawer.mycollectionpackage.ShowBookAdapter;

public class TransactionsFragment extends Fragment {

    private static final String TAG = TransactionsFragment.class.getSimpleName();

    ShowBookAdapter adapter;

    UserObject userObject;

    boolean collectionEmpty = false;
    private static String emailId;

    private List<BookObject> bookObjects;
    private List<BookObject> transactionBookObjects;
    LinearLayout emptyCart;

    private RecyclerView rv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity mainActivity = (MainActivity) getActivity();
        emailId = mainActivity.getUserEmailId();
        Log.v(TAG,"emailId in My collection: "+emailId);

        bookObjects = new ArrayList<>();
        transactionBookObjects = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.transactions_fragment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Transactions");

        rv=(RecyclerView)rootView.findViewById(R.id.rv_transactions);
        emptyCart = (LinearLayout) rootView.findViewById(R.id.ll_no_transactions);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        new db().execute();

        return rootView;

    }

    private class db extends AsyncTask<String, Void, String> {

        Context context;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            context = getActivity().getApplicationContext();
            if(context == null) {
                return;
            }
        }

        @Override
        protected String doInBackground(String... params) {

            CognitoCachingCredentialsProvider credentialsProvider;

            credentialsProvider = new CognitoCachingCredentialsProvider(
                    context,
                    "us-east-1:25c78fbe-abb8-4655-9309-8442c610ffd0", // Identity Pool ID
                    Regions.US_EAST_1 // Region
            );

            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);

            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

            if(mapper != null) {
                if(emailId != null) {
                    userObject = mapper.load(UserObject.class, emailId);
                    Log.v(TAG,"user: "+userObject.getUserName());
                    bookObjects = userObject.getBookObjectSet();
                    if(bookObjects == null) {
                        Log.v(TAG,"Empty no books in collection");
                        collectionEmpty = true;
                    }
                }
            }

            else
                Log.v(TAG,"not saved");

            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(collectionEmpty) {
                Log.v(TAG,"no books in collection");
                emptyCart.setVisibility(View.VISIBLE);
            } else {
                emptyCart.setVisibility(View.INVISIBLE);
                initializeAdapter();
            }
        }
    }

    private void initializeAdapter(){

        for(BookObject i: bookObjects) {
            if(!i.isHaveBook()) {
                transactionBookObjects.add(i);
            }
        }

        if(transactionBookObjects.size() > 0) {
            adapter = new ShowBookAdapter(transactionBookObjects, emailId);
            rv.setAdapter(adapter);
        }
        else {
            emptyCart.setVisibility(View.VISIBLE);
        }


    }

}