package in.deepaksood.pcsmaproject.navigationdrawer.mycollectionpackage;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.deepaksood.pcsmaproject.R;
import in.deepaksood.pcsmaproject.datamodelpackage.BookObject;

/**
 * Created by deepak on 29/4/16.
 */
public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.BookViewHolder> {

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView bookName;
        TextView bookAuthor;
        TextView bookIsbn;

        BookViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            bookName = (TextView)itemView.findViewById(R.id.tv_cv_book_name);
            bookAuthor = (TextView)itemView.findViewById(R.id.tv_cv_book_author);
            bookIsbn = (TextView)itemView.findViewById(R.id.tv_cv_book_isbn);
        }
    }

    List<BookObject> bookObjects;

    CardViewAdapter(List<BookObject> bookObjects){
        this.bookObjects = bookObjects;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item, viewGroup, false);
        BookViewHolder bookViewHolder = new BookViewHolder(v);
        return bookViewHolder;
    }

    @Override
    public void onBindViewHolder(BookViewHolder personViewHolder, int position) {
        personViewHolder.bookName.setText(bookObjects.get(position).getBookName());
        personViewHolder.bookAuthor.setText(bookObjects.get(position).getBookAuthor());
        personViewHolder.bookIsbn.setText(bookObjects.get(position).getBookIsbn());
    }

    @Override
    public int getItemCount() {
        return bookObjects.size();
    }
}
