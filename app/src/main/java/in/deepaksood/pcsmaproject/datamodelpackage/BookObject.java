package in.deepaksood.pcsmaproject.datamodelpackage;

/**
 * Created by deepak on 29/4/16.
 */
public class BookObject {
    private String bookName;
    private String bookAuthor;
    private String bookIsbn;

    public BookObject(String bookName, String bookAuthor, String bookIsbn) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookIsbn = bookIsbn;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }
}
