package simplegamer003.flickrdemoapp;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.REST;
import com.googlecode.flickrjandroid.RequestContext;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Anirudh on 24-05-2017.
 */

public final class FlickrHelper {

    private static FlickrHelper instance = null;
    private static final String API_KEY = "ee3b2f1b3dc6dee8786f9e3d481315c9";
    public static final String API_SEC = "30178bebb81b9950";

    private FlickrHelper() {

    }

    public static FlickrHelper getInstance() {
        if (instance == null) {
            instance = new FlickrHelper();
        }

        return instance;
    }

    public Flickr getFlickr() {
        try {
            Flickr f = new Flickr(API_KEY, API_SEC, new REST());
            return f;
        } catch (ParserConfigurationException e) {
            return null;
        }
    }

    public Flickr getFlickrAuthed(String token, String secret) {
        Flickr f = getFlickr();
        RequestContext requestContext = RequestContext.getRequestContext();
        OAuth auth = new OAuth();
        auth.setToken(new OAuthToken(token, secret));
        requestContext.setOAuth(auth);
        return f;
    }

}
