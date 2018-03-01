package com.tinystartup.juniormaster.model.network;

public class RequestURL {

    public static final String URL_GETSCHOOLLIST = "https://data.junior-master.markchen.cc/api/v1/schools";
    public static final String URL_LOGIN = "https://data.junior-master.markchen.cc/api/v1/login";
    public static final String URL_GETMYPROFILE = "https://data.junior-master.markchen.cc/api/v1/me";
    public static final String URL_POSTQUESTION = "https://data.junior-master.markchen.cc/api/v1/questions";
    public static final String URL_GETQUESTIONLIST = "https://data.junior-master.markchen.cc/api/v1/questions";

    public static final String URL_GETCHALLENGELIST = "http://demo.junior-master.markchen.cc/challenges.json";

    public static String URL_GETQUESTIONDETAIL(int id) {
        return URL_GETQUESTIONLIST + "/" + String.valueOf(id);
    }
}
