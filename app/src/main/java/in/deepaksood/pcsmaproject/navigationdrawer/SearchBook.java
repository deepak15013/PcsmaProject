package in.deepaksood.pcsmaproject.navigationdrawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.deepaksood.pcsmaproject.R;

/**
 * Created by deepak on 29/3/16.
 */
public class SearchBook extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_book_fragment, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Search Book");

        return rootView;
    }
}
