package in.deepaksood.pcsmaproject.datamodelpackage;

import java.io.Serializable;

/**
 * Created by deepak on 30/4/16.
 */
public class CardObject implements Serializable{

    private String bookName;
    private String bookAuthor;
    private String bookIsbn;
    private String bookPosterUrl;
    private Boolean bookRent;
    private String userName;
    private String userEmailId;
    private String userProfilePictureUrl;
    private String userCoverPictureUrl;
    private String userContactNum;
    private String userLocation;

    public CardObject(String bookName, String bookAuthor, String bookIsbn, String bookPosterUrl, Boolean bookRent, String userName, String userEmailId, String userProfilePictureUrl, String userCoverPictureUrl, String userContactNum, String userLocation) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookIsbn = bookIsbn;
        this.bookPosterUrl = bookPosterUrl;
        this.bookRent = bookRent;
        this.userName = userName;
        this.userEmailId = userEmailId;
        this.userProfilePictureUrl = userProfilePictureUrl;
        this.userCoverPictureUrl = userCoverPictureUrl;
        this.userContactNum = userContactNum;
        this.userLocation = userLocation;
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

    public String getBookPosterUrl() {
        return bookPosterUrl;
    }

    public void setBookPosterUrl(String bookPosterUrl) {
        this.bookPosterUrl = bookPosterUrl;
    }

    public Boolean getBookRent() {
        return bookRent;
    }

    public void setBookRent(Boolean bookRent) {
        this.bookRent = bookRent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public String getUserProfilePictureUrl() {
        return userProfilePictureUrl;
    }

    public void setUserProfilePictureUrl(String userProfilePictureUrl) {
        this.userProfilePictureUrl = userProfilePictureUrl;
    }

    public String getUserCoverPictureUrl() {
        return userCoverPictureUrl;
    }

    public void setUserCoverPictureUrl(String userCoverPictureUrl) {
        this.userCoverPictureUrl = userCoverPictureUrl;
    }

    public String getUserContactNum() {
        return userContactNum;
    }

    public void setUserContactNum(String userContactNum) {
        this.userContactNum = userContactNum;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }
}
