package simplegamer003.flickrdemoapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * Created by Anirudh on 24-05-2017.
 */

class getAccessToken extends AsyncTask<String,Void,String>{

    String flickrsecret = "30178bebb81b9950";
    String requestToken = "https://www.flickr.com/services/oauth/request_token?";

    @Override
    protected String doInBackground(String... params) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;

        String callback = "&oauth_callback=http%3A%2F%2Fwww.flickr.com%2Fservices%2Foauth%2Frequest_token?";
        String consumerkey = "&oauth_consumer_key=ee3b2f1b3dc6dee8786f9e3d481315c9";
        int rand = (int)(Math.random()*10000000);
        String n = Integer.toString(rand);
        String nonce = "oauth_nonce="+n;
        String sigmethod = "&oauth_signature_method=HMAC-SHA1";
        int time = (int) (System.currentTimeMillis()/1000);
        String ts = Integer.toString(time);
        String timestamp = "&oauth_timestamp="+ts;
        String version = "&oauth_version=1.0";

        String op = requestToken+nonce+timestamp+consumerkey+sigmethod+version+callback;
        String base = "GET&https%3A%2F%2Fwww.flickr.com%2Fservices%2Foauth%2Frequest_token" +
                "&oauth_callback%3Dhttp%253A%252F%252Fwww.flickr.com%252Fservices%252Foauth%252Frequest_token%253F%26" +
                "oauth_consumer_key%3Dee3b2f1b3dc6dee8786f9e3d481315c9%26" +
                "oauth_nonce%3D"+n+"%26" +
                "oauth_signature_method%3DHMAC-SHA1%26" +
                "oauth_timestamp%3D"+ts+"%26" +
                "oauth_version%3D1.0";
        String sigcallback = null;
        try {
            sigcallback = HmacSha1Signature.calculateRFC2104HMAC(base,flickrsecret);
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String currurl = op+"&oauth_signature="+sigcallback;
        Log.d("url",currurl);
        try {
            URL url = new URL(currurl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        }catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }

        String authtoken = result.toString();
        Log.d("tokens",authtoken);
        return null;
    }

    /*protected String getSignature(String op, String secret) throws NoSuchAlgorithmException {
        String key = op;
        key.replaceAll("&","");
        key.replaceAll("=","");
        key = secret + key;
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(key.getBytes(),0,key.length());
        String sign = new BigInteger(1,md.digest()).toString(16);
        return sign;
    }*/

}
