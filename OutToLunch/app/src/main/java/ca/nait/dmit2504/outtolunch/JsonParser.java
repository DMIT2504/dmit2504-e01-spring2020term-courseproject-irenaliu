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
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";

        try {
            if(!jsonObject.isNull("name")) {
                //get name from object
                name = jsonObject.getString("name");
            }
            if(!jsonObject.isNull("vicinity")) {
                vicinity = jsonObject.getString("vicinity");
            }
            //get latitude & longitude
            latitude = jsonObject.getJSONObject("geometry")
                    .getJSONObject("location").getString("lat");
            longitude = jsonObject.getJSONObject("geometry")
                    .getJSONObject("location").getString("lng");

            reference = jsonObject.getString("reference");

            dataList.put("name", name);
            dataList.put("vicinity", vicinity);
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
                dataList.add(data);
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
