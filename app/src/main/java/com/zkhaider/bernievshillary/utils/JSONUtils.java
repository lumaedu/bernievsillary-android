package com.zkhaider.bernievshillary.utils;

import android.content.res.Resources;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by ZkHaider on 6/3/16.
 */

public class JSONUtils {

    private static final String TAG = JSONUtils.class.getSimpleName();

    private static Gson sGson = new Gson();

    public static <T> T convertToModel(String json, Class<T> clazz) {
        try {
            return sGson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            Log.i(TAG, "convertToModel: error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Reader utilities
     */

    public static String readJSONFromResources(Resources resources, int id) {

        // Open an input stream to the file
        InputStream isReader = resources.openRawResource(id);
        Writer writer = new StringWriter();

        // Read the file using a buffered reader
        try {

            // Open a buffered reader and write to writer
            BufferedReader reader = new BufferedReader(new InputStreamReader(isReader, "UTF-8"));
            String line = reader.readLine();
            while (line != null) {
                writer.write(line);
                line = reader.readLine();
            }

        } catch (Exception e) {

            Log.i(TAG, "readJSONFromResources: error: " + e.getMessage());

        } finally {

            try {
                isReader.close();
            } catch (Exception e) {
                Log.i(TAG, "readJSONFromResources: error: " + e.getMessage());
            }
        }

        return writer.toString();
    }

}
