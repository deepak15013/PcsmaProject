package in.deepaksood.pcsmaproject.navigationdrawer.mycollectionpackage;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import in.deepaksood.pcsmaproject.R;
import in.deepaksood.pcsmaproject.datamodelpackage.BookObject;

/**
 * Created by deepak on 29/4/16.
 */
public class ShowBookAdapter extends RecyclerView.Adapter<ShowBookAdapter.BookViewHolder> {

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView bookName;
        TextView bookAuthor;
        TextView bookIsbn;
        ImageView bookPoster;

        BookViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            bookName = (TextView)itemView.findViewById(R.id.tv_cv_book_name);
            bookAuthor = (TextView)itemView.findViewById(R.id.tv_cv_book_author);
            bookIsbn = (TextView)itemView.findViewById(R.id.tv_cv_book_isbn);
            bookPoster = (ImageView) itemView.findViewById(R.id.iv_cv_book_poster);
        }
    }

    List<BookObject> bookObjects;

    ShowBookAdapter(List<BookObject> bookObjects){
        this.bookObjects = bookObjects;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    Context context;
    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item, viewGroup, false);
        context = viewGroup.getContext();
        BookViewHolder bookViewHolder = new BookViewHolder(v);
        return bookViewHolder;
    }

    @Override
    public void onBindViewHolder(BookViewHolder personViewHolder, int position) {
        personViewHolder.bookName.setText(bookObjects.get(position).getBookName());
        personViewHolder.bookAuthor.setText(bookObjects.get(position).getBookAuthor());
        personViewHolder.bookIsbn.setText(bookObjects.get(position).getBookIsbn());

        Picasso.with(context).load(bookObjects.get(position).getBookPosterUrl()).into(personViewHolder.bookPoster);
    }

    @Override
    public int getItemCount() {
        if(bookObjects != null) {
            return bookObjects.size();
        }
        return 0;
    }
}
