package com.example.myapplication;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class ApiManagerJ {

    private Context context;

    public ApiManagerJ(Context context) {
        this.context = context;
    }


    public JSONObject readStations() throws IOException, JSONException {
        InputStream is = context.getResources().openRawResource(R.raw.ecobici);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        int n;
        while ((n = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, n);
        }

        is.close();

        return new JSONObject(writer.toString());
    }
}
