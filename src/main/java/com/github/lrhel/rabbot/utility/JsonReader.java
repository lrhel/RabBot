/**
 * Solution proposed by Roland Illig
 * from: https://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
 */

package com.github.lrhel.rabbot.utility;


import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class JsonReader {

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();

        try {
            JSONParser parser = new JSONParser();
            org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(new BufferedReader(new InputStreamReader(is)));
            return new JSONObject(json);
        } catch (Exception e) { e.printStackTrace();}

        finally {
            is.close();
        }
        return null;
    }

}
