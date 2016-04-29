package in.deepaksood.pcsmaproject.navigationdrawer.searchbookpackage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import in.deepaksood.pcsmaproject.R;

/**
 * Created by deepak on 29/3/16.
 */

public class SearchBook extends Fragment implements View.OnClickListener{

    EditText etBookIsbn;
    EditText etBookAuthor;
    EditText etBookName;

    Button btnIsbn;
    Button btnAuthor;
    Button btnName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_book_fragment, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Search Book");

        etBookIsbn = (EditText) rootView.findViewById(R.id.et_book_isbn);
        etBookAuthor = (EditText) rootView.findViewById(R.id.et_book_author);
        etBookName = (EditText) rootView.findViewById(R.id.et_book_name);

        btnIsbn = (Button) rootView.findViewById(R.id.btn_search_isbn);
        btnAuthor = (Button) rootView.findViewById(R.id.btn_search_author);
        btnName = (Button) rootView.findViewById(R.id.btn_search_name);

        btnIsbn.setOnClickListener(this);
        btnAuthor.setOnClickListener(this);
        btnName.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search_isbn:
                Toast.makeText(getActivity(), "search by isbn", Toast.LENGTH_SHORT).show();
                dbSearch(etBookIsbn.getText().toString());
                break;

            case R.id.btn_search_author:
                Toast.makeText(getActivity(), "search by author", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_search_name:
                Toast.makeText(getActivity(), "search by name", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void dbSearch(String search) {

    }
}
