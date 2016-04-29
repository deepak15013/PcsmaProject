package in.deepaksood.pcsmaproject.datamodelpackage;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMarshaller;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMarshalling;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.JsonMarshaller;

import java.util.List;
import java.util.Set;

/**
 * Created by deepak on 29/4/16.
 */

@DynamoDBTable(tableName = "BookUserObject")
public class UserObject {
    private String userName;
    private String userEmailId;
    private String userProfilePictureUrl;
    private String userCoverPictureUrl;
    private String userContactNum;
    private String userLocation;
    private List<BookObject> bookObjectSet;

    public UserObject() {}

    public UserObject(String userName, String userEmailId, String userProfilePictureUrl, String userCoverPictureUrl, String userContactNum, String userLocation) {
        this.userName = userName;
        this.userEmailId = userEmailId;
        this.userProfilePictureUrl = userProfilePictureUrl;
        this.userCoverPictureUrl = userCoverPictureUrl;
        this.userContactNum = userContactNum;
        this.userLocation = userLocation;
    }

    @DynamoDBAttribute(attributeName = "UserName")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @DynamoDBHashKey(attributeName = "EmailId")
    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    @DynamoDBAttribute(attributeName = "UserProfilePictureUrl")
    public String getUserProfilePictureUrl() {
        return userProfilePictureUrl;
    }

    public void setUserProfilePictureUrl(String userProfilePictureUrl) {
        this.userProfilePictureUrl = userProfilePictureUrl;
    }

    @DynamoDBAttribute(attributeName = "UserCoverPictureUrl")
    public String getUserCoverPictureUrl() {
        return userCoverPictureUrl;
    }

    public void setUserCoverPictureUrl(String userCoverPictureUrl) {
        this.userCoverPictureUrl = userCoverPictureUrl;
    }

    @DynamoDBAttribute(attributeName = "UserContactNum")
    public String getUserContactNum() {
        return userContactNum;
    }

    public void setUserContactNum(String userContactNum) {
        this.userContactNum = userContactNum;
    }

    @DynamoDBAttribute(attributeName = "UserLocation")
    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    @DynamoDBAttribute(attributeName = "BookObjectSet")
    public List<BookObject> getBookObjectSet() {
        return bookObjectSet;
    }

    public void setBookObjectSet(List<BookObject> bookObjectSet) {
        this.bookObjectSet = bookObjectSet;
    }

}
