package in.deepaksood.pcsmaproject.navigationdrawer.transactionspackage;

import android.support.v4.app.Fragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

import in.deepaksood.pcsmaproject.R;

public class TransactionsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.transactions_fragment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Transactions");
        return rootView;

    }
}