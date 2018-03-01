package com.tinystartup.juniormaster.model.network;

import com.tinystartup.juniormaster.model.app.ChallengeListItem;
import com.tinystartup.juniormaster.model.app.ProfileItem;
import com.tinystartup.juniormaster.model.app.QuestionDetail;
import com.tinystartup.juniormaster.model.app.QuestionItem;

import org.json.JSONObject;

import java.util.List;

public class RequestListener {

    public interface OnReceivedSchoolListListener {
        void OnReceivedSchoolList(JSONObject jsonObject);
    }

    public interface OnLoginResultListener {
        void OnLoginSuccess();
        void OnLoginFailed(int code);
    }

    public interface OnReceivedMyProfileListener {
        void OnReceivedMyProfile(ProfileItem profileItem);
    }

    public interface OnPostQuestionResultListener {
        void OnPostQuestionSuccess();
    }

    public interface OnReceivedQuestionListListener {
        void OnReceivedQuestionList(int page, List<QuestionItem> items);
    }

    public interface OnReceivedQuestionDeatilListener {
        void OnReceivedQuestionDeatil(QuestionDetail detail);
    }

    public interface OnReceivedChallengeListListener {
        void OnReceivedChallengeList(List<ChallengeListItem> items);
    }
}
