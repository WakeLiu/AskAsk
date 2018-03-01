package com.tinystartup.juniormaster;

import com.tinystartup.juniormaster.model.network.RequestCenter;
import com.tinystartup.juniormaster.model.network.RequestListener;
import android.app.Application;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.mindpipe.android.logging.log4j.LogConfigurator;

public class AppController extends Application implements RequestListener.OnReceivedSchoolListListener {
    private static final Logger logger = Logger.getLogger(AppController.class);

    private List<String> mChapterList = null;
    private List<String> mSchoolList = null;
    private List<String> mSourceList = null;
    private HashMap<String, String> mChapterHashMap = null;
    private HashMap<String, String> mSchoolHashMap = null;
    private HashMap<String, String> mSourceHashMap = null;

    private List<Integer> mStarredItems;

    private RequestCenter mRequestCenter;

    private static AppController mInstance;
    private static final String SCHOOLSJSONFILE = "schoolList.json";

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        LogConfigurator logConfigurator = new LogConfigurator();
        logConfigurator.setFileName(getCacheDir() + File.separator + "juniormaster.log");
        logConfigurator.setRootLevel(Level.DEBUG);
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
        logConfigurator.setMaxFileSize(1024 * 1024 * 5);
        logConfigurator.setImmediateFlush(true);
        logConfigurator.configure();

        mRequestCenter = new RequestCenter(getApplicationContext());

        if (!loadStarredItems()) {
            mStarredItems = new ArrayList<Integer>();
        }

        asyncLoadSchoolsHashMap();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestCenter getRequestCenter() {
        return mRequestCenter;
    }

    private void loadChaptersResource() {

        mChapterList = new ArrayList<String>();
        mChapterHashMap = new HashMap<String, String>();

        for (String entry: getResources().getStringArray(R.array.chapters_array)) {
            String[] data = entry.split("\\|", 2);

            mChapterList.add(data[0]);
            mChapterHashMap.put(data[0], data[1]);
        }
    }

    private void loadSchoolsResource() {

        mSchoolList = new ArrayList<String>();

        for (String entry: getResources().getStringArray(R.array.schools_array)) {
            String[] data = entry.split("\\|", 2);

            mSchoolList.add(data[0]);
        }
    }

    private void loadSourcesResource() {

        mSourceList = new ArrayList<String>();
        mSourceHashMap = new HashMap<String, String>();

        for (String entry: getResources().getStringArray(R.array.sources_array)) {
            String[] data = entry.split("\\|", 2);

            mSourceList.add(data[0]);
            mSourceHashMap.put(data[0], data[1]);
        }
    }

    private void asyncLoadSchoolsHashMap() {

        mSchoolHashMap = new HashMap<String, String>();

        File file = getFileStreamPath(SCHOOLSJSONFILE);
        if (file == null || !file.exists()) {
            mRequestCenter.makeGetSchoolListRequest(this);
            return;
        }

        JSONObject jsonObject = loadSchoolListResponse();
        if (jsonObject == null) {
            logger.error("loadSchoolsResource(): failed to load data from json file)");
            return;
        }

        loadSchoolsHashMap(jsonObject);
    }

    private void loadSchoolsHashMap(JSONObject jsonObject) {
        try
        {
            JSONArray schools = jsonObject.getJSONArray("data");
            for (int i = 0; i < schools.length(); i++) {
                JSONObject school = schools.getJSONObject(i);

                mSchoolHashMap.put(school.getString("fullname"), school.getString("id"));
            }

            logger.debug("mSchoolHashMap(fullname => id): " + String.valueOf(schools.length()) + " items load.");
        }
        catch (JSONException exception)
        {
            logger.error("loadSchoolsResource(): exception", exception);
        }


    }

    public List<String> getChapterList() {

        if (mChapterList == null) {
            loadChaptersResource();
        }

        return mChapterList;
    }

    public List<String> getSchoolList() {

        if (mSchoolList == null) {
            loadSchoolsResource();
        }

        return mSchoolList;
    }

    public List<String> getSourceList() {

        if (mSourceList == null) {
            loadSourcesResource();
        }

        return mSourceList;
    }

    public HashMap<String, String> getChapterHashMap() {

        if (mChapterHashMap == null) {
            loadChaptersResource();
        }

        return mChapterHashMap;
    }

    public HashMap<String, String> getSchoolHashMap() {

        if (mSchoolHashMap == null) {
            loadSchoolsResource();
        }

        return mSchoolHashMap;
    }

    public HashMap<String, String> getSourceHashMap() {

        if (mSourceHashMap == null) {
            loadSourcesResource();
        }

        return mSourceHashMap;
    }

    public boolean isStarredItem(Integer id) {
        return mStarredItems.contains(id);
    }

    public void addStarredItem(Integer id) {

        mStarredItems.add(id);
        saveStarredItems();
    }

    public void removeStarredItem(Integer id) {

        mStarredItems.remove(id);
        saveStarredItems();
    }

    public boolean loadStarredItems() {

        try
        {
            final FileInputStream stream = openFileInput("starredItems.dat");
            final ObjectInputStream reader = new ObjectInputStream(stream);

            mStarredItems = (List<Integer>) reader.readObject();
            reader.close();
            stream.close();

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public void saveStarredItems() {

        try
        {
            final FileOutputStream stream = openFileOutput("starredItems.dat", Context.MODE_PRIVATE);
            final ObjectOutputStream writer = new ObjectOutputStream(stream);

            writer.writeObject(mStarredItems);
            writer.close();
            stream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void saveSchoolListResponse(JSONObject response) {
        try
        {
            final FileOutputStream stream = openFileOutput(SCHOOLSJSONFILE, Context.MODE_PRIVATE);
            final OutputStreamWriter writer = new OutputStreamWriter(stream);

            writer.write(response.toString());
            writer.close();
        }
        catch (Exception e)
        {
            logger.error("saveSchoolListResponse()", e);
        }
    }

    private JSONObject loadSchoolListResponse() {
        try
        {
            final FileInputStream stream = openFileInput(SCHOOLSJSONFILE);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            final StringBuilder builder = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            reader.close();

            return new JSONObject(builder.toString());
        }
        catch (Exception e)
        {
            logger.error("loadSchoolListResponse()", e);
        }

        return null;
    }

    @Override
    public void OnReceivedSchoolList(JSONObject jsonObject) {
        saveSchoolListResponse(jsonObject);
        loadSchoolsHashMap(jsonObject);
    }
}