package jc;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by ½ð³É on 2015/10/16.
 */
public class ByteResponseHandler implements ResponseHandler<byte[]> {

    @Override
    public byte[] handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {

        int status = httpResponse.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
            HttpEntity entity = httpResponse.getEntity();
            return entity != null ? EntityUtils.toByteArray(entity) : null;
        } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
    }
}
