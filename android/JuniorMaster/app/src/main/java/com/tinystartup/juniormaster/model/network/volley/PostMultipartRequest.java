package com.tinystartup.juniormaster.model.network.volley;

import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostMultipartRequest extends Request<NetworkResponse> {
    private static Logger logger = Logger.getLogger(PostMultipartRequest.class);

    private static final String twoHyphens = "--";
    private static final String lineEnd = "\r\n";

    private final Response.Listener<NetworkResponse> mListener;
    private final Response.ErrorListener mErrorListener;
    private final Map<String, String> mHeaders = new HashMap<>();
    private final String mBoundary = "apiclient-" + System.currentTimeMillis();
    private final String mMimeType = "multipart/form-data;boundary=" + mBoundary;
    private byte[] mMultipartBody;

    public PostMultipartRequest(
            String url,
            List<Uri> images,
            HashMap<String, String> data,
            Response.Listener<NetworkResponse> listener,
            Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);

        mListener = listener;
        mErrorListener = errorListener;
        mHeaders.put("X-Requested-With", "XMLHttpRequest");

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

            for (Uri uri : images) {
                addImageAttachment(dataOutputStream, getByteArray(uri), (new File(uri.getPath())).getName());
            }

            for (String key: data.keySet()) {
                addFormdata(dataOutputStream, key, data.get(key));
            }

            dataOutputStream.writeBytes(twoHyphens + mBoundary + twoHyphens + lineEnd);
            mMultipartBody = byteArrayOutputStream.toByteArray();
        }
        catch (IOException exception) {
            errorListener.onErrorResponse(new VolleyError("failed to build the request body", exception));
        }
    }

    public void setApiToken(String api_token) {
        mHeaders.put("Authorization", "Bearer " + api_token);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders;
    }

    @Override
    public String getBodyContentType() {
        return mMimeType;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return mMultipartBody;
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(
                    response,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }

    private byte[] getByteArray(Uri uri) throws IOException {
        InputStream inputStream = new FileInputStream(uri.getPath());
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void addImageAttachment(DataOutputStream dataOutputStream, byte[] fileData, String fileName) throws IOException {
        logger.debug("addImageAttachment() - filename: " + fileName + ", size: " + String.valueOf(fileData.length));

        dataOutputStream.writeBytes(twoHyphens + mBoundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"files[]\"; filename=\"" + fileName + "\"" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileData);
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(lineEnd);
    }

    private void addFormdata(DataOutputStream dataOutputStream, String name, String value) throws IOException {
        logger.debug("addFormdata() - name: " + name + ", value: " + value);

        byte[] byte_array = value.getBytes("UTF-8");

        dataOutputStream.writeBytes(twoHyphens + mBoundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + lineEnd + lineEnd);

        dataOutputStream.write(byte_array, 0, byte_array.length);
        dataOutputStream.writeBytes(lineEnd);
    }
}
