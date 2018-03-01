package com.tinystartup.juniormaster.model.network.volley;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetRequest extends JsonObjectRequest {
    private static Logger logger = Logger.getLogger(GetRequest.class);

    protected Map<String, String> headers = new HashMap<>();

    protected String api_token;

    public GetRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Request.Method.GET, url, null, listener, errorListener);

        logger.info("new GetRequest url: " + url);
    }

    public GetRequest(String url, Map<String, String> params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Request.Method.GET, buildURL(url, params), null, listener, errorListener);

        logger.info("new GetRequest url: " + buildURL(url, params));
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setApiToken(String api_token) {
        this.api_token = api_token;
        updateAuthorizationHeader();
    }

    protected void updateAuthorizationHeader() {
        headers.put("Authorization", "Bearer " + api_token);
    }

    protected static String buildURL(String url, Map<String, String> params) {

        if (params == null || params.size() == 0) {
            return  url;
        }

        String result = url;
        for(Map.Entry<String, String> entry : params.entrySet()) {
            result += "&" + entry.getKey() + "=" + entry.getValue();
        }

        return result.replaceFirst("&", "?");
    }
}
