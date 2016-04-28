package in.deepaksood.pcsmaproject.bookaddpackage;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import in.deepaksood.pcsmaproject.datamodelpackage.BookFullDetailsObject;

/**
 * Created by deepak on 23/3/16.
 */
public class XMLPullParserHandler {

    private final static String TAG = XMLPullParserHandler.class.getSimpleName();

    List<BookFullDetailsObject> items;
    private BookFullDetailsObject item;
    private String text;

    public XMLPullParserHandler() {
        items = new ArrayList<>();
    }

    public List<BookFullDetailsObject> getItems() {
        return items;
    }

    public List<BookFullDetailsObject> parse(String response) {

        boolean flagForProductDescription = false;

        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            parser = factory.newPullParser();

            parser.setInput(new StringReader(response));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase("item")) {
                            item = new BookFullDetailsObject();
                        }
                        else if(tagName.equalsIgnoreCase("LargeImage")) {
                            String temp="";
                            int nextEventType = parser.next();
                            while(nextEventType != XmlPullParser.END_TAG) {
                                switch (nextEventType) {
                                    case XmlPullParser.START_TAG:
                                        break;
                                    case XmlPullParser.TEXT:
                                        temp = parser.getText();
                                        item.setMediumImageUrl(temp);
                                        break;
                                    case XmlPullParser.END_TAG:
                                        break;

                                    default:
                                        break;

                                }
                                nextEventType=parser.next();
                            }
                        }
                        else if(tagName.equalsIgnoreCase("source")) {
                            String temp="";
                            int nextEventType = parser.next();
                            while(nextEventType != XmlPullParser.END_TAG) {
                                switch (nextEventType) {
                                    case XmlPullParser.START_TAG:
                                        break;
                                    case XmlPullParser.TEXT:
                                        temp = parser.getText();
                                        if(temp.contains("Description")) {
                                            flagForProductDescription = true;
                                        }
                                        break;
                                    case XmlPullParser.END_TAG:
                                        break;

                                    default:
                                        break;

                                }
                                nextEventType=parser.next();
                            }
                        }
                        break;

                    case XmlPullParser.TEXT: {
                        text = parser.getText();
                        break;
                    }

                    case XmlPullParser.END_TAG: {
                        if(tagName.equalsIgnoreCase("Item")) {
                            items.add(item);
                        }
                        else if(tagName.equalsIgnoreCase("Author")) {
                            item.setAuthor(text);
                        }
                        else if(tagName.equalsIgnoreCase("Binding")) {
                            item.setBinding(text);
                        }
                        else if(tagName.equalsIgnoreCase("PublicationDate")) {
                            item.setPublicationDate(text);
                        }
                        else if(tagName.equalsIgnoreCase("Title")) {
                            item.setTitle(text);
                        }
                        else if(tagName.equalsIgnoreCase("Publisher")) {
                            item.setPublisher(text);
                        }
                        else if(tagName.equalsIgnoreCase("Content")) {
                            if(flagForProductDescription) {
                                text = android.text.Html.fromHtml(text).toString();
                                item.setProductDescription(text);
                                flagForProductDescription= false;
                            }
                        }
                        break;
                    }

                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }
}
