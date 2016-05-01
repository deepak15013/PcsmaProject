package in.deepaksood.pcsmaproject.datamodelpackage;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBDocument;

/**
 * Created by deepak on 1/5/16.
 */

@DynamoDBDocument
public class RentUserModel {
    private String rentUserName;
    private String rentEmailId;
    private String rentContactNum;
    private String rentPictureUrl;
    private String rentDeadline;

    @DynamoDBAttribute(attributeName = "RentUserName")
    public String getRentUserName() {
        return rentUserName;
    }

    public void setRentUserName(String rentUserName) {
        this.rentUserName = rentUserName;
    }

    @DynamoDBAttribute(attributeName = "RentEmailId")
    public String getRentEmailId() {
        return rentEmailId;
    }

    public void setRentEmailId(String rentEmailId) {
        this.rentEmailId = rentEmailId;
    }

    @DynamoDBAttribute(attributeName = "RentContactNum")
    public String getRentContactNum() {
        return rentContactNum;
    }

    public void setRentContactNum(String rentContactNum) {
        this.rentContactNum = rentContactNum;
    }

    @DynamoDBAttribute(attributeName = "RentPictureUrl")
    public String getRentPictureUrl() {
        return rentPictureUrl;
    }

    public void setRentPictureUrl(String rentPictureUrl) {
        this.rentPictureUrl = rentPictureUrl;
    }

    @DynamoDBAttribute(attributeName = "RentDeadline")
    public String getRentDeadline() {
        return rentDeadline;
    }

    public void setRentDeadline(String rentDeadline) {
        this.rentDeadline = rentDeadline;
    }
}
