package com.tinystartup.juniormaster.model.network.volley;

import android.net.Uri;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

// should be removed
public class RegisterRequest extends PostMultipartRequest {

    private static final String url = "https://demo.junior-master.markchen.cc/api/v1/register";

    private static HashMap<String, String> buildFormData(String name, int school_id, int grade, int gender) {
        HashMap<String, String> result = new HashMap<String, String>();

        result.put("name", name);
        result.put("school_id", String.valueOf(school_id));
        result.put("grade", String.valueOf(grade));
        result.put("gender", String.valueOf(gender));

        return result;
    }

    protected RegisterRequest(
            String name,
            int school_id,
            int grade,
            int gender,
            Uri image,
            Response.Listener<NetworkResponse>listener,
            Response.ErrorListener errorListener) throws IOException {
        super(url, Arrays.asList(image), buildFormData(name, school_id, grade, gender), listener, errorListener);
    }
}
