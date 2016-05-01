package in.deepaksood.pcsmaproject.navigationdrawer.mycollectionpackage;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import in.deepaksood.pcsmaproject.R;
import in.deepaksood.pcsmaproject.datamodelpackage.BookObject;
import in.deepaksood.pcsmaproject.datamodelpackage.UserObject;

/**
 * Created by deepak on 29/4/16.
 */
public class ShowBookAdapter extends RecyclerView.Adapter<ShowBookAdapter.BookViewHolder> {

    private static final String TAG = in.deepaksood.pcsmaproject.showbookpackage.ShowBookAdapter.class.getSimpleName();

    private static String emailId;
    private String deleteBookIsbn = "";

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView bookName;
        TextView bookAuthor;
        TextView bookIsbn;
        ImageView bookPoster;
        TextView bookRentSale;

        Button btnRemove;

        BookViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            bookName = (TextView)itemView.findViewById(R.id.tv_cv_book_name);
            bookAuthor = (TextView)itemView.findViewById(R.id.tv_cv_book_author);
            bookIsbn = (TextView)itemView.findViewById(R.id.tv_cv_book_isbn);
            bookPoster = (ImageView) itemView.findViewById(R.id.iv_cv_book_poster);
            bookRentSale = (TextView) itemView.findViewById(R.id.tv_rent_sale);
            btnRemove =(Button) itemView.findViewById(R.id.btn_remove_from_collection);
        }
    }

    List<BookObject> bookObjects = new ArrayList<>();

    public ShowBookAdapter(List<BookObject> bookObjects, String emailId){

        this.bookObjects = bookObjects;
        this.emailId = emailId;
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
    public void onBindViewHolder(BookViewHolder personViewHolder, final int position) {
        personViewHolder.bookName.setText(bookObjects.get(position).getBookName());
        personViewHolder.bookAuthor.setText(bookObjects.get(position).getBookAuthor());
        personViewHolder.bookIsbn.setText(bookObjects.get(position).getBookIsbn());
        Log.v(TAG,"bool: "+bookObjects.get(position).isBookRent());
        if(bookObjects.get(position).isBookRent()) {
            personViewHolder.bookRentSale.setText("For Rent");
        }
        else {
            personViewHolder.bookRentSale.setText("For Sale");
        }
        Picasso.with(context).load(bookObjects.get(position).getBookPosterUrl()).into(personViewHolder.bookPoster);

        personViewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBookIsbn = bookObjects.get(position).getBookIsbn();
                bookObjects.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, bookObjects.size());
                new db().execute();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(bookObjects != null) {
            return bookObjects.size();
        }
        return 0;
    }


    UserObject userObject;
    private class db extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            CognitoCachingCredentialsProvider credentialsProvider;

            credentialsProvider = new CognitoCachingCredentialsProvider(
                    context,
                    "us-east-1:25c78fbe-abb8-4655-9309-8442c610ffd0", // Identity Pool ID
                    Regions.US_EAST_1 // Region
            );

            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);

            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

            try {
                if(mapper != null) {
                    if(emailId != null && !emailId.equals("")) {
                        userObject = mapper.load(UserObject.class, emailId);
                        bookObjects = userObject.getBookObjectSet();
                        if(bookObjects != null) {
                            for(BookObject i: bookObjects) {
                                if(i.getBookIsbn().equals(deleteBookIsbn)) {
                                    bookObjects.remove(i);

                                }
                            }
                        }

                        userObject.setBookObjectSet(bookObjects);
                        mapper.save(userObject);
                    }
                }
                else
                    Log.v(TAG,"not saved");
            } catch (Exception e) {
                Log.v(TAG,"Exception e: "+e);
            }

            return "Executed";
        }
    }

}
