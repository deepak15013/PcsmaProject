package in.deepaksood.pcsmaproject.navigationdrawer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import in.deepaksood.pcsmaproject.R;

/**
 * Created by deepak on 28/3/16.
 */
public class MyCollection extends Fragment {

    public static final String TAG = MyCollection.class.getSimpleName();

    List<BookDetails> list = new ArrayList<>();
    ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.my_collection_fragment, container, false);
        listView = (ListView) rootView.findViewById(R.id.displayBooksList);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Collection");

        getDataFromParse();

        BookDetailsAdapter customAdapter = new BookDetailsAdapter(this.getContext(), (ArrayList<BookDetails>) list);
        listView.setAdapter(customAdapter);

        return rootView;

    }

    public void getDataFromParse() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("deepaksood619gmailcom");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> bookList, ParseException e) {
                if (e == null) {
                    if(bookList.size() > 0) {
                        for(int i = 0; i < bookList.size(); i++) {
                            ParseObject parseObject = bookList.get(i);
                            String isbn = String.valueOf(parseObject.get("isbn"));
                            String author = String.valueOf(parseObject.get("author"));
                            String title = String.valueOf(parseObject.get("title"));
                            Log.v(TAG,"isbn: "+isbn+" author: "+author+" title: "+title);
                            BookDetails bookDetails = new BookDetails(isbn, author, title);
                            list.add(bookDetails);

                        }
                        updateUi();
                    }
                }
                else {
                    Log.d("score", "Error: " + e.getMessage());
                    // Alert.alertOneBtn(getActivity(),"Something went wrong!");
                }
            }
        });
    }

    public void updateUi() {
        Log.v(TAG,"Update UI");
        BookDetailsAdapter customAdapter = new BookDetailsAdapter(this.getContext(), (ArrayList<BookDetails>) list);
        listView.setAdapter(customAdapter);
    }
}
