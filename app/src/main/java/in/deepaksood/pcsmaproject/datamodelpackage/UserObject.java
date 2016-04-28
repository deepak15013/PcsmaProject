package in.deepaksood.pcsmaproject.datamodelpackage;

/**
 * Created by deepak on 29/4/16.
 */
public class UserObject {
    private String userName;
    private String userEmailId;
    private String userProfilePictureUrl;
    private String userCoverPictureUrl;
    private String userContactNum;
    private String userLocation;

    public UserObject(String userName, String userEmailId, String userProfilePictureUrl, String userCoverPictureUrl, String userContactNum, String userLocation) {
        this.userName = userName;
        this.userEmailId = userEmailId;
        this.userProfilePictureUrl = userProfilePictureUrl;
        this.userCoverPictureUrl = userCoverPictureUrl;
        this.userContactNum = userContactNum;
        this.userLocation = userLocation;
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
