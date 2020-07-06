package ca.nait.dmit2504.outtolunch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadUrl {
    public String readUrl(String myUrl) throws IOException {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        try {
            //initialize url
            URL url = new URL(myUrl);
            //initialize connection
            urlConnection = (HttpURLConnection) url.openConnection();
            //connect connection
            urlConnection.connect();

            //READ DATA
            //initialize input stream
            inputStream = urlConnection.getInputStream();
            //initialize buffer reader
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            //initialize string builder
            StringBuilder builder = new StringBuilder();
            //initialize string variable
            String  line = "";
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }
            //get data
            data = builder.toString();
            //close reader
            reader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            //always gets executed
            inputStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
