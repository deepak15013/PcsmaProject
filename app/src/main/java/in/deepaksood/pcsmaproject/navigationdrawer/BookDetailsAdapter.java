package in.deepaksood.pcsmaproject.navigationdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.deepaksood.pcsmaproject.R;

/**
 * Created by deepak on 30/3/16.
 */
public class BookDetailsAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private ArrayList<BookDetails> objects;

    private class ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;
    }

    public BookDetailsAdapter(Context context, ArrayList<BookDetails> objects) {
        inflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.display_book_list, null);
            holder.textView1 = (TextView) convertView.findViewById(R.id.titleAdapter);
            holder.textView2 = (TextView) convertView.findViewById(R.id.authorAdapter);
            holder.textView3 = (TextView) convertView.findViewById(R.id.isbnAdapter);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView1.setText(objects.get(position).title);
        holder.textView2.setText(objects.get(position).author);
        holder.textView3.setText(objects.get(position).isbn);
        return convertView;
    }
}
