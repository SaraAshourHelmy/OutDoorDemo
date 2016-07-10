package mediasci.com.Util;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by Bassem on 6/27/2016.
 */
public class SendImage {

    public static void SendData(String URL, String json, byte[] img) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(URL);

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            entityBuilder.addTextBody("input", json);

            if (img != null) {
                entityBuilder.addBinaryBody("attachment", img);
                Log.e("enter", "img");
            }

            HttpEntity entity = entityBuilder.build();
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            HttpEntity httpEntity = response.getEntity();
            String result = EntityUtils.toString(httpEntity);
            Log.v("result", result);
            Log.e("status", response.getStatusLine().getStatusCode() + "");

        } catch (Exception e) {
            Log.e("img_error", e + "");
        }
    }
}
