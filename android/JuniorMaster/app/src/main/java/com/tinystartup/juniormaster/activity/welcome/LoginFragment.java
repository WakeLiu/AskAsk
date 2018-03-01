package com.tinystartup.juniormaster.activity.welcome;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.tinystartup.juniormaster.R;
import com.tinystartup.juniormaster.AppController;
import com.tinystartup.juniormaster.model.network.RequestListener;

import org.apache.log4j.Logger;

public class LoginFragment extends Fragment implements RequestListener.OnLoginResultListener {
    private static final Logger logger = Logger.getLogger(LoginFragment.class);

    private EditText mEmailBox;
    private EditText mPasswordBox;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        mEmailBox = (EditText) rootView.findViewById(R.id.email);
        mPasswordBox = (EditText) rootView.findViewById(R.id.password);

        rootView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.getInstance().getRequestCenter().makeLoginRequest(mEmailBox.getText().toString(), mPasswordBox.getText().toString(), LoginFragment.this);
            }
        });

        rootView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragMgr = getFragmentManager();

                if (fragMgr.getBackStackEntryCount() > 0) {
                    fragMgr.popBackStack();
                } else {
                    getActivity().onBackPressed();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void OnLoginSuccess() {
        Toast.makeText(getContext(), "成功登入", Toast.LENGTH_LONG).show();
        getActivity().finish();
    }

    @Override
    public void OnLoginFailed(int code) {
        Toast.makeText(getContext(), "登入失敗", Toast.LENGTH_LONG).show();
    }
}
