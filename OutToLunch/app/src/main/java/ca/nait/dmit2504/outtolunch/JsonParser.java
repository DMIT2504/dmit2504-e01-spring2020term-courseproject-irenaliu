package ca.nait.dmit2504.outtolunch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParser {
    private HashMap<String, String> parseJsonObject(JSONObject jsonObject) {
        //initialize hash map
        HashMap<String, String> dataList = new HashMap<>();
        String name = "-NA-";
        String price_level = "-NA-";
        String rating = "-NA-";
        String user_ratings_total = "-NA-";
        String open_now = "";
        String latitude = "";
        String longitude = "";
        String reference = "";

        try {
            if(!jsonObject.isNull("name")) {
                //get name from object
                name = jsonObject.getString("name");
            }
            if(!jsonObject.isNull("price_level")) {
                price_level = jsonObject.getString("price_level").equals("1") ? price_level = "$"
                        : jsonObject.getString("price_level").equals("2") ? price_level = "$$"
                        : jsonObject.getString("price_level").equals("3") ? price_level = "$$$"
                        : "$$$$";
            }
            if (!jsonObject.isNull("rating")) {
                rating = jsonObject.getString("rating");
            }
            if (!jsonObject.isNull("user_ratings_total")) {
                user_ratings_total = jsonObject.getString("user_ratings_total");
            }
            open_now = jsonObject.getJSONObject("opening_hours")
                    .getString("open_now");

            //get latitude & longitude
            latitude = jsonObject.getJSONObject("geometry")
                    .getJSONObject("location").getString("lat");
            longitude = jsonObject.getJSONObject("geometry")
                    .getJSONObject("location").getString("lng");

            reference = jsonObject.getString("reference");

            //check if open now
            if (!open_now.equals("false")) {
                dataList.put("name", name);
                dataList.put("price_level", price_level);
                dataList.put("rating", rating);
                dataList.put("user_ratings_total", user_ratings_total);
                dataList.put("open_now", open_now);
                dataList.put("lat", latitude);
                dataList.put("lng", longitude);
                dataList.put("reference", reference);
            }
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


    public List<HashMap<String, String>> parseResult(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        //get result array
        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parseJsonArray(jsonArray);
    }
}
