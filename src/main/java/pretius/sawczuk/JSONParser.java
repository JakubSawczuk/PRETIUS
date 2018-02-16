package pretius.sawczuk;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;


public class JSONParser extends TimerTask {

    static Map<String, Double> askMap;
    static Map<String, Double> bidMap;

    @Override
    public void run() {
        JsonArray jsonArray = getJsonArrayFromLink("http://api.nbp.pl/api/exchangerates/tables/C/?format=json");
        addBidAndAskToMaps(jsonArray);
    }

    private JsonArray getJsonArrayFromLink(String path) {
        String sURL = path;
        URL url;
        HttpURLConnection requestHTTP;
        try {
            url = new URL(sURL);
            requestHTTP = (HttpURLConnection) url.openConnection();
            requestHTTP.connect();
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(new InputStreamReader((InputStream) requestHTTP.getContent()));
            return jsonElement.getAsJsonArray().get(0).getAsJsonObject().get("rates").getAsJsonArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addBidAndAskToMaps(JsonArray jsonArray) {
        askMap = new HashMap<>();
        bidMap = new HashMap<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            askMap.put(jsonArray.get(i).getAsJsonObject().get("code").getAsString(),
                    jsonArray.get(i).getAsJsonObject().get("ask").getAsDouble());

            bidMap.put(jsonArray.get(i).getAsJsonObject().get("code").getAsString(),
                    jsonArray.get(i).getAsJsonObject().get("bid").getAsDouble());
        }
    }
}
