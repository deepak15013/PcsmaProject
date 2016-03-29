package in.deepaksood.pcsmaproject.navigationdrawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.deepaksood.pcsmaproject.R;

/**
 * Created by deepak on 28/3/16.
 */
public class MyCollection extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.my_collection_fragment, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Collection");

        return rootView;

    }
}
