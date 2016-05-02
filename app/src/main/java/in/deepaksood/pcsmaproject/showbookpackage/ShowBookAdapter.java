package in.deepaksood.pcsmaproject.showbookpackage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import in.deepaksood.pcsmaproject.R;
import in.deepaksood.pcsmaproject.datamodelpackage.BookObject;
import in.deepaksood.pcsmaproject.datamodelpackage.CardObject;
import in.deepaksood.pcsmaproject.datamodelpackage.UserObject;
import in.deepaksood.pcsmaproject.mainactivitypackage.MainActivity;
import in.deepaksood.pcsmaproject.mapactivitypackage.ShowBookMapsActivity;

/**
 * Created by deepak on 29/4/16.
 */
public class ShowBookAdapter extends RecyclerView.Adapter<ShowBookAdapter.BookViewHolder> {

    private static final String TAG = ShowBookAdapter.class.getSimpleName();

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView bookName;
        TextView bookAuthor;
        TextView bookIsbn;
        TextView bookRent;
        TextView userName;
        TextView userEmailId;
        TextView userContactNum;

        ImageView showBookPoster;
        ImageView userProfilePicture;

        Button contactBookOwner;
        Button showBookLocation;

        BookViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            bookName = (TextView)itemView.findViewById(R.id.tv_cv_show_book_name);
            bookAuthor = (TextView)itemView.findViewById(R.id.tv_cv_show_book_author);
            bookIsbn = (TextView)itemView.findViewById(R.id.tv_cv_show_book_isbn);
            userName = (TextView) itemView.findViewById(R.id.tv_user_name);
            bookRent = (TextView) itemView.findViewById(R.id.tv_cv_book_rent);
            userEmailId = (TextView) itemView.findViewById(R.id.tv_user_email_id);
            userContactNum = (TextView) itemView.findViewById(R.id.tv_user_contact_num);

            userProfilePicture = (ImageView) itemView.findViewById(R.id.iv_user_profile_pic);
            showBookPoster = (ImageView) itemView.findViewById(R.id.iv_show_book_poster);

            contactBookOwner = (Button) itemView.findViewById(R.id.btn_all_book_contact_owner);
            showBookLocation = (Button) itemView.findViewById(R.id.btn_all_book_show_location);
        }
    }

    List<CardObject> cardObjects;
    ArrayList<String> locations;
    ArrayList<String> bookNames;
    ArrayList<String> userNames;
    String mainUserEmailId;

    ShowBookAdapter(List<CardObject> cardObjects, String mainUserEmailId){
        this.cardObjects = cardObjects;
        this.mainUserEmailId = mainUserEmailId;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        locations = new ArrayList<>();
        bookNames = new ArrayList<>();
        userNames = new ArrayList<>();
        if(cardObjects != null) {
            for(CardObject object: cardObjects) {
                locations.add(object.getUserLocation());
                bookNames.add(object.getBookName());
                userNames.add(object.getUserName());
            }
        }
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
    public void onBindViewHolder(final BookViewHolder personViewHolder, final int position) {
        personViewHolder.bookName.setText(cardObjects.get(position).getBookName());
        personViewHolder.bookAuthor.setText(cardObjects.get(position).getBookAuthor());
        personViewHolder.bookIsbn.setText(cardObjects.get(position).getBookIsbn());
        if(cardObjects.get(position).getBookRent()) {
            personViewHolder.bookRent.setText("For Rent");
        }
        else {
            personViewHolder.bookRent.setText("For Sale");
        }

        personViewHolder.userName.setText(cardObjects.get(position).getUserName());
        personViewHolder.userName.setText(cardObjects.get(position).getUserName());
        personViewHolder.userEmailId.setText(cardObjects.get(position).getUserEmailId());
        personViewHolder.userContactNum.setText(cardObjects.get(position).getUserContactNum());

        Picasso.with(context).load(cardObjects.get(position).getBookPosterUrl()).into(personViewHolder.showBookPoster);
        Picasso.with(context).load(cardObjects.get(position).getUserProfilePictureUrl()).into(personViewHolder.userProfilePicture);

        personViewHolder.contactBookOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setData(Uri.parse("mailto:"));
                Log.v(TAG,"mailto: "+cardObjects.get(position).getUserEmailId());
                shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {cardObjects.get(position).getUserEmailId()});
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "BookXchange:"+mainUserEmailId+":"+cardObjects.get(position).getBookIsbn());
                shareIntent.putExtra(Intent.EXTRA_TEXT, "I want to have "+cardObjects.get(position).getBookName()+" book. Please lend me this book.");
                shareIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(shareIntent, "Send request with"));
            }
        });

        personViewHolder.showBookLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowBookMapsActivity.class);
                intent.putStringArrayListExtra("LOCATIONS", locations);
                intent.putStringArrayListExtra("BOOK_NAMES", bookNames);
                intent.putStringArrayListExtra("USER_NAMES", userNames);
                intent.putExtra("POSITION", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardObjects.size();
    }
}
