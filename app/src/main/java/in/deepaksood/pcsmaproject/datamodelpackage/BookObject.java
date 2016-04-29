package in.deepaksood.pcsmaproject.datamodelpackage;

import android.util.Log;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBDocument;

/**
 * Created by deepak on 29/4/16.
 */

@DynamoDBDocument
public class BookObject {

    private static final String TAG = BookObject.class.getSimpleName();

    private String bookName;
    private String bookAuthor;
    private String bookIsbn;
    private String bookPosterUrl;

    public BookObject() {}

    public BookObject(String bookName, String bookAuthor, String bookIsbn, String bookPosterUrl) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookIsbn = bookIsbn;
        this.bookPosterUrl = bookPosterUrl;
    }

    @DynamoDBAttribute(attributeName = "BookName")
    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @DynamoDBAttribute(attributeName = "BookAuthor")
    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    @DynamoDBAttribute(attributeName = "BookIsbn")
    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    @DynamoDBAttribute(attributeName = "BookPosterUrl")
    public String getBookPosterUrl() {
        return bookPosterUrl;
    }

    public void setBookPosterUrl(String bookPosterUrl) {
        this.bookPosterUrl = bookPosterUrl;
    }

    @Override
    public boolean equals(Object o) {
        Log.v(TAG,"checking");
        boolean retVal = false;
        if(o instanceof BookObject) {
            BookObject ptr = (BookObject) o;
            Log.v(TAG,"this: "+this.bookIsbn);
            Log.v(TAG,"o: "+((BookObject) o).bookIsbn);
            retVal = this.bookIsbn.equals(ptr.bookIsbn);
        }
        Log.v(TAG,"retVal: "+retVal);
        return retVal;
    }
}
