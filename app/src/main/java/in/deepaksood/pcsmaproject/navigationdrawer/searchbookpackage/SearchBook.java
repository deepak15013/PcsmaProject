package in.deepaksood.pcsmaproject.navigationdrawer.searchbookpackage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

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
import in.deepaksood.pcsmaproject.datamodelpackage.UserObject;
import in.deepaksood.pcsmaproject.showbookpackage.ShowBookActivity;

/**
 * Created by deepak on 29/3/16.
 */

public class SearchBook extends Fragment implements View.OnClickListener{

    private static final String TAG = SearchBook.class.getSimpleName();

    AutoCompleteTextView etBookIsbn;
    AutoCompleteTextView etBookAuthor;
    AutoCompleteTextView etBookName;

    Button btnSearch;
    Button btnGetAll;

    PaginatedScanList<UserObject> result;

    static List<UserObject> haveBook;

    static List<String> isbnList;
    static List<String> authorList;
    static List<String> nameList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new db().execute();
        haveBook = new ArrayList<>();

        isbnList = new ArrayList<>();
        authorList = new ArrayList<>();
        nameList = new ArrayList<>();
    }

    public PaginatedScanList<UserObject> getResult() {
        return result;
    }

    public void setResult(PaginatedScanList<UserObject> result) {
        this.result = result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_book_fragment, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Search Book");

        etBookIsbn = (AutoCompleteTextView) rootView.findViewById(R.id.et_book_isbn);
        etBookAuthor = (AutoCompleteTextView) rootView.findViewById(R.id.et_book_author);
        etBookName = (AutoCompleteTextView) rootView.findViewById(R.id.et_book_name);

        btnSearch = (Button) rootView.findViewById(R.id.btn_search);
        btnGetAll = (Button) rootView.findViewById(R.id.btn_get_all);

        btnSearch.setOnClickListener(this);
        btnGetAll.setOnClickListener(this);

        ArrayAdapter<String> isbnAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, isbnList);
        ArrayAdapter<String> authorAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, authorList);
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, nameList);

        etBookIsbn.setThreshold(2);
        etBookIsbn.setAdapter(isbnAdapter);

        etBookAuthor.setThreshold(2);
        etBookAuthor.setAdapter(authorAdapter);

        etBookName.setThreshold(2);
        etBookName.setAdapter(nameAdapter);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_search:
                Toast.makeText(getActivity(), "search by author", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_get_all:
                Toast.makeText(getActivity(), "Get all Books", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ShowBookActivity.class);
                startActivity(intent);
                break;

        }
    }


    private class db extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            CognitoCachingCredentialsProvider credentialsProvider;

            credentialsProvider = new CognitoCachingCredentialsProvider(
                    getActivity(),
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
            for(UserObject userObject: result) {
                for(BookObject bookObject: userObject.getBookObjectSet()) {
                    isbnList.add(bookObject.getBookIsbn());
                    authorList.add(bookObject.getBookAuthor());
                    nameList.add(bookObject.getBookName());
                }
            }
            for(String i: isbnList) {
                Log.v(TAG,"i: "+i);
            }
            Log.v(TAG,"Completed");
        }
    }

}
