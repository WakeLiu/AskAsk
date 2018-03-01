package com.tinystartup.juniormaster.model.network.volley;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PostRequest extends StringRequest {
    private Map<String, String> mParams;

    public PostRequest(String url, Map<String, String> params, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, url, listener, errorListener);

        mParams = params;
    }

    @Override
    public Map<String, String> getParams() {
        return mParams;
    }
}
