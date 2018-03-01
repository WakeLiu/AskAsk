package com.tinystartup.juniormaster.activity.welcome;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import com.tinystartup.juniormaster.R;
import com.tinystartup.juniormaster.activity.WelcomeActivity;
import com.tinystartup.juniormaster.adapter.common.SchoolFilterAdapter;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RegisterFragment extends Fragment {
    private static final Logger logger = Logger.getLogger(RegisterFragment.class);

    private ImageView mImageView;
    private List<String> mZoneList = new ArrayList<String>();
    private List<String> mSchoolList = new ArrayList<String>();

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            JSONObject dataset = loadSchoolDataset();

            JSONArray zones = dataset.getJSONArray("zones");
            for (int i = 0; i < zones.length(); i++) {
                mZoneList.add(zones.getString(i));
            }

            JSONArray schools = dataset.getJSONArray("schools");
            for (int i = 0; i < schools.length(); i++) {
                mSchoolList.add(schools.getString(i));
            }

        }
        catch (Exception e)
        {
            logger.error("RegisterFragment()", e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        final ArrayAdapter<String> zoneAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mZoneList);
        final AutoCompleteTextView zoneSelectView = (AutoCompleteTextView)rootView.findViewById(R.id.zone);
        zoneSelectView.setAdapter(zoneAdapter);

        final SchoolFilterAdapter schoolFilterAdapter = new SchoolFilterAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, zoneSelectView, mSchoolList);
        final AutoCompleteTextView schoolSelectView = (CustomAutoCompleteTextView)rootView.findViewById(R.id.school);
        schoolSelectView.setAdapter(schoolFilterAdapter);
        schoolSelectView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String school = schoolFilterAdapter.getItem(position).toString();
                zoneSelectView.setText(school.substring(0, 3));
                schoolSelectView.setText(school.substring(5));
            }
        });

        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, GRADES);
        AutoCompleteTextView gradeSelectView = (AutoCompleteTextView)rootView.findViewById(R.id.grade);
        gradeSelectView.setAdapter(gradeAdapter);

        mImageView = (ImageView) rootView.findViewById(R.id.card);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((WelcomeActivity)getActivity()).startCameraActivityForResult(RegisterFragment.this);
            }
        });

        ((Button) rootView.findViewById(R.id.register)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        rootView.findViewById(R.id.name).requestFocus();

        return rootView;
    }

    public void setStudentCardImageView(Uri uri) {
        mImageView.setImageDrawable(null);
        mImageView.setImageURI(uri);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private JSONObject loadSchoolDataset() {
        try
        {
            final InputStream stream = getActivity().getAssets().open("schools.json");
            final byte[] data = new byte[stream.available()];

            stream.read(data);
            stream.close();

            return new JSONObject(new String(data));
        }
        catch (Exception e)
        {
            logger.error("loadSchoolDataset()", e);
        }

        return null;
    }

    private static final String[] GRADES = new String[] {
            "國中ㄧ年級", "國中二年級", "國中三年級"
    };
}
