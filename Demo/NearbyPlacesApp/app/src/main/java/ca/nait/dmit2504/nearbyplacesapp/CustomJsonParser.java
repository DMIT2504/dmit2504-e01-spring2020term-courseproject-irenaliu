package ca.nait.dmit2504.nearbyplacesapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomJsonParser {
    private HashMap<String, String> parseJsonObject(JSONObject jsonObject) {
        //initialize hash map
        HashMap<String, String> dataList = new HashMap<>();
        String name = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";

        try {
            if(!jsonObject.isNull("name")) {
                //get name from object
                name = jsonObject.getString("name");
            }

            //get latitude & longitude
            latitude = jsonObject.getJSONObject("geometry")
                    .getJSONObject("location").getString("lat");
            longitude = jsonObject.getJSONObject("geometry")
                    .getJSONObject("location").getString("lng");

            reference = jsonObject.getString("reference");


            dataList.put("name", name);
            dataList.put("lat", latitude);
            dataList.put("lng", longitude);
            dataList.put("reference", reference);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    private List<HashMap<String, String>> parseJsonArray(JSONArray jsonArray) {
        //initialize hash map list
        List<HashMap<String, String>> dataList = new ArrayList<>();
        for(int i = 0; i<jsonArray.length(); i++) {
            try {
                //initialize hash map
                HashMap<String, String> data = null;
                data = parseJsonObject((JSONObject) jsonArray.get(i));
                //add data to hash map list
                if (data.size() != 0) {
                    dataList.add(data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dataList;
    }


    public List<HashMap<String, String>> parseResult(JSONObject jsonObject) {
        //initialize json array
        JSONArray jsonArray = null;

        //get result array
        try {
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parseJsonArray(jsonArray);
    }
}
