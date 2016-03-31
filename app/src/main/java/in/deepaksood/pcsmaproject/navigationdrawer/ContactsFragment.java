package in.deepaksood.pcsmaproject.navigationdrawer;

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

public class ContactsFragment extends Fragment {

    String phoneNumber;
    ListView lv;
    ArrayList <String> aa= new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.contact_fragment, container, false);
        lv = (ListView) rootView.findViewById(R.id.lv);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Contacts");

        getNumber(this.getActivity().getContentResolver());


        return rootView;

    }

    public void getNumber(ContentResolver cr)
    {
        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            System.out.println(".................."+phoneNumber);
            aa.add(name+"\n"+phoneNumber);
        }
        phones.close();
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_list_item_1,aa);
        lv.setAdapter(adapter);
        //display contact numbers in the list
    }
}