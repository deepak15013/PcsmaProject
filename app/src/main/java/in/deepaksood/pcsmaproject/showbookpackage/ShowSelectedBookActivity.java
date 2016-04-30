package in.deepaksood.pcsmaproject.showbookpackage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import in.deepaksood.pcsmaproject.R;
import in.deepaksood.pcsmaproject.datamodelpackage.CardObject;

/**
 * Created by deepak on 30/4/16.
 */
public class ShowSelectedBookActivity extends AppCompatActivity {

    private static final String TAG = ShowSelectedBookActivity.class.getSimpleName();

    List<CardObject> cardObjects;

    ShowBookAdapter adapter;
    private RecyclerView rvShowBook;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book);

        Toolbar toolbar = (Toolbar) findViewById(R.id.show_book_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Books Found");

        cardObjects = new ArrayList<>();

        cardObjects = (List<CardObject>) getIntent().getSerializableExtra("HAVE_BOOK");

        rvShowBook = (RecyclerView)findViewById(R.id.rv_show_book);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvShowBook.setLayoutManager(llm);
        rvShowBook.setHasFixedSize(true);

        initializeAdapter();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void initializeAdapter(){
        adapter = new ShowBookAdapter(cardObjects);
        rvShowBook.setAdapter(adapter);
    }
}
