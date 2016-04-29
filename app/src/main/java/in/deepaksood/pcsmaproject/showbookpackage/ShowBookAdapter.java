package in.deepaksood.pcsmaproject.showbookpackage;

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
import in.deepaksood.pcsmaproject.datamodelpackage.CardObject;
import in.deepaksood.pcsmaproject.datamodelpackage.UserObject;

/**
 * Created by deepak on 29/4/16.
 */
public class ShowBookAdapter extends RecyclerView.Adapter<ShowBookAdapter.BookViewHolder> {

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView bookName;
        TextView bookAuthor;
        TextView bookIsbn;
        TextView userName;
        TextView userEmailId;
        TextView userContactNum;

        ImageView showBookPoster;
        ImageView userProfilePicture;

        BookViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            bookName = (TextView)itemView.findViewById(R.id.tv_cv_show_book_name);
            bookAuthor = (TextView)itemView.findViewById(R.id.tv_cv_show_book_author);
            bookIsbn = (TextView)itemView.findViewById(R.id.tv_cv_show_book_isbn);
            userName = (TextView) itemView.findViewById(R.id.tv_user_name);
            userEmailId = (TextView) itemView.findViewById(R.id.tv_user_email_id);
            userContactNum = (TextView) itemView.findViewById(R.id.tv_user_contact_num);

            userProfilePicture = (ImageView) itemView.findViewById(R.id.iv_user_profile_pic);
            showBookPoster = (ImageView) itemView.findViewById(R.id.iv_show_book_poster);
        }
    }

    List<CardObject> cardObjects;

    ShowBookAdapter(List<CardObject> cardObjects){
        this.cardObjects = cardObjects;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    Context context;
    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.show_book_card_item, viewGroup, false);
        context = viewGroup.getContext();
        BookViewHolder bookViewHolder = new BookViewHolder(v);
        return bookViewHolder;
    }

    @Override
    public void onBindViewHolder(BookViewHolder personViewHolder, int position) {
        personViewHolder.bookName.setText(cardObjects.get(position).getBookName());
        personViewHolder.bookAuthor.setText(cardObjects.get(position).getBookAuthor());
        personViewHolder.bookIsbn.setText(cardObjects.get(position).getBookIsbn());
        personViewHolder.userName.setText(cardObjects.get(position).getUserName());
        personViewHolder.userName.setText(cardObjects.get(position).getUserName());
        personViewHolder.userEmailId.setText(cardObjects.get(position).getUserEmailId());
        personViewHolder.userContactNum.setText(cardObjects.get(position).getUserContactNum());

        Picasso.with(context).load(cardObjects.get(position).getBookPosterUrl()).into(personViewHolder.showBookPoster);
        Picasso.with(context).load(cardObjects.get(position).getUserProfilePictureUrl()).into(personViewHolder.userProfilePicture);
    }

    @Override
    public int getItemCount() {
        return cardObjects.size();
    }
}
