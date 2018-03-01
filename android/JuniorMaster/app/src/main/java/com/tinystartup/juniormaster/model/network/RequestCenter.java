package com.tinystartup.juniormaster.model.network;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.tinystartup.juniormaster.model.app.ChallengeListItem;
import com.tinystartup.juniormaster.model.app.ProfileItem;
import com.tinystartup.juniormaster.model.app.QuestionDetail;
import com.tinystartup.juniormaster.model.app.QuestionItem;
import com.tinystartup.juniormaster.model.network.volley.GetRequest;
import com.tinystartup.juniormaster.model.network.volley.PostRequest;
import com.tinystartup.juniormaster.model.network.volley.LruBitmapCache;
import com.tinystartup.juniormaster.model.network.volley.PostMultipartRequest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestCenter {
    private static final Logger logger = Logger.getLogger(RequestCenter.class);
    private static final String TAG = RequestCenter.class.getSimpleName();

    private Context mContext;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private LruBitmapCache mLruBitmapCache;

    private String mAuthToken;

    public RequestCenter(Context context) {
        mContext = context;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();

        if (mImageLoader == null) {
            getLruBitmapCache();
            mImageLoader = new ImageLoader(this.mRequestQueue, mLruBitmapCache);
        }

        return this.mImageLoader;
    }

    public LruBitmapCache getLruBitmapCache() {
        if (mLruBitmapCache == null) {
            mLruBitmapCache = new LruBitmapCache();
        }

        return this.mLruBitmapCache;
    }

    private <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    private <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    private String getAuthToken() {

        if (mAuthToken == null) {
            logger.debug("getApiToken() - no logon found, return hard-code api token.");
            mAuthToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2RhdGEuanVuaW9yLW1hc3Rlci5tYXJrY2hlbi5jYy9hcGkvdjEvbG9naW4iLCJpYXQiOjE0NjgxNTI3NDMsImV4cCI6MTQ5OTY4ODc0MywibmJmIjoxNDY4MTUyNzQzLCJqdGkiOiJmODdiZmYyNDc3NzgzYWVmOTg0YmI5MDA5ZGZmMGM2NiIsInN1YiI6MjB9.RWjWYaGOrXPdkTOFbyb_aCsde_ORZyYgsenhn1-DQmk";
        }

        return mAuthToken;
    }

    public void makeGetSchoolListRequest(final RequestListener.OnReceivedSchoolListListener listener) {

        GetRequest getRequest = new GetRequest(
            RequestURL.URL_GETSCHOOLLIST,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    listener.OnReceivedSchoolList(jsonObject);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    String message = (volleyError.getMessage() != null) ? volleyError.getMessage() : "(empty)";
                    logger.error("makeGetSchoolListRequest.onErrorResponse() - " + message, volleyError);
                }
            }
        );

        addToRequestQueue(getRequest);
    }

    public void makeLoginRequest(final String email, final String password, final RequestListener.OnLoginResultListener listener) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);

        PostRequest postRequest = new PostRequest(
            RequestURL.URL_LOGIN,
            params,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        mAuthToken = new JSONObject(s).getString("token");
                        logger.debug("makeLoginRequest() - auth token changed to " + mAuthToken);
                        if (listener != null) {
                            listener.OnLoginSuccess();
                        }
                    } catch (JSONException jsonException) {
                        logger.error("makeLoginRequest.JSONException: ", jsonException);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    String message = (volleyError.getMessage() != null) ? volleyError.getMessage() : "(empty)";
                    logger.error("makeLoginRequest.onErrorResponse() - " + message, volleyError);
                }
            }
        );

        addToRequestQueue(postRequest);
    }

    public void makeGetMyProfileRequest(final RequestListener.OnReceivedMyProfileListener listener) {

        GetRequest getRequest = new GetRequest(
            RequestURL.URL_GETMYPROFILE,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        listener.OnReceivedMyProfile(new ProfileItem(jsonObject));
                    } catch (JSONException jsonException) {
                        logger.error("makeGetMyProfileRequest.JSONException", jsonException);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    String message = (volleyError.getMessage() != null) ? volleyError.getMessage() : "(empty)";
                    logger.error("makeGetMyProfileRequest.onErrorResponse() - " + message, volleyError);
                }
            }
        );

        getRequest.setApiToken(getAuthToken());
        addToRequestQueue(getRequest);
    }

    public void makePostQuestion(final String content, final List<Uri> images, final RequestListener.OnPostQuestionResultListener listener) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("content", content);
        params.put("user_id", "21");

        PostMultipartRequest multipartRequest = new PostMultipartRequest(
            RequestURL.URL_POSTQUESTION,
            images,
            params,
            new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    logger.debug("makePostQuestion.onResponse() - Status Code = " + String.valueOf(response.statusCode));

                    if (response.statusCode == 200) {
                        listener.OnPostQuestionSuccess();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    String message = (volleyError.getMessage() != null) ? volleyError.getMessage() : "(empty)";
                    logger.error("makePostQuestion.onErrorResponse() - " + message, volleyError);
                }
            }
        );

        multipartRequest.setApiToken(mAuthToken);
        addToRequestQueue(multipartRequest);
    }

    public void makeGetQuestionListRequest(final Map<String, String> params, final RequestListener.OnReceivedQuestionListListener listener) {

        GetRequest getRequest = new GetRequest(
            RequestURL.URL_GETQUESTIONLIST,
            params,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        List<QuestionItem> list = new ArrayList<QuestionItem>();
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++)
                        {
                            list.add(new QuestionItem(array.getJSONObject(i)));
                        }
                        listener.OnReceivedQuestionList(jsonObject.getInt("current_page"), list);

                    } catch (JSONException jsonException) {
                        logger.error("makeGetQuestionListRequest.JSONException()", jsonException);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    String message = (volleyError.getMessage() != null) ? volleyError.getMessage() : "(empty)";
                    logger.error("makeGetQuestionListRequest.onErrorResponse() - " + message, volleyError);
                }
            }
        );

        getRequest.setApiToken(mAuthToken);
        addToRequestQueue(getRequest);
    }

    public void makeGetQuestionDetailRequest(int id, final RequestListener.OnReceivedQuestionDeatilListener listener) {

        GetRequest getRequest = new GetRequest(
           RequestURL.URL_GETQUESTIONDETAIL(id),
           new Response.Listener<JSONObject>() {
               @Override
               public void onResponse(JSONObject jsonObject) {
                   try {
                       QuestionDetail detail = new QuestionDetail(jsonObject);
                       listener.OnReceivedQuestionDeatil(detail);
                   } catch (JSONException jsonException) {
                       logger.error("makeGetQuestionDetailRequest.JSONException()", jsonException);
                   }
               }
           }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    String message = (volleyError.getMessage() != null) ? volleyError.getMessage() : "(empty)";
                    logger.error("makeGetQuestionDetailRequest.onErrorResponse() - " + message, volleyError);
                }
            }
        );

        getRequest.setApiToken(mAuthToken);
        addToRequestQueue(getRequest);
    }

    public void makeGetChallengeListRequest(final RequestListener.OnReceivedChallengeListListener listener) {

        GetRequest getRequest = new GetRequest(
            RequestURL.URL_GETCHALLENGELIST,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        List<ChallengeListItem> list = new ArrayList<ChallengeListItem>();
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++)
                        {
                            list.add(new ChallengeListItem(array.getJSONObject(i)));
                        }
                        listener.OnReceivedChallengeList(list);

                    } catch (JSONException jsonException) {
                        logger.error("makeGetChallengeListRequest.JSONException()", jsonException);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    String message = (volleyError.getMessage() != null) ? volleyError.getMessage() : "(empty)";
                    logger.error("makeGetChallengeListRequest.onErrorResponse() - " + message, volleyError);
                }
            }
        );

        addToRequestQueue(getRequest);
    }
}
