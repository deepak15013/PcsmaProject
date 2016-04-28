package in.deepaksood.pcsmaproject.datamodelpackage;

/**
 * Created by deepak on 23/3/16.
 */
public class BookFullDetailsObject {

    private String author;
    private String binding;
    private String publicationDate;
    private String title;
    private String mediumImageUrl;
    private String publisher;
    private String productDescription;

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getMediumImageUrl() {
        return mediumImageUrl;
    }

    public void setMediumImageUrl(String mediumImageUrl) {
        this.mediumImageUrl = mediumImageUrl;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    @Override
    public String toString() {
        return "hello: "+author + "\n" +binding + "\n" +publicationDate +"\n" +title + "\n" + mediumImageUrl +"\n"+publisher+"\n"+productDescription;
    }
}
