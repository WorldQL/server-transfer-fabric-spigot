package com.nftworlds.portal_transfer.data;

import com.google.gson.*;
import com.nftworlds.portal_transfer.models.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ServerLoader {

    public static List<Server> getServerList() {
        List<Server> serverList = new ArrayList<>();

        try {
            JsonArray jsonObject = JsonParser.parseString(readUrl()).getAsJsonArray();
            for (JsonElement element : jsonObject) {
                JsonObject connection = element.getAsJsonObject().getAsJsonObject("connection");
                serverList.add(new Server(((JsonObject) element).get("name").getAsString(),
                        connection.get("address").getAsString(),
                        ((JsonObject) element).get("description").getAsString(),
                        ((JsonObject) element).get("playersOnline").getAsInt())); }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return serverList;
    }

    private static String readUrl() throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL("https://status-api.nftworlds.com/latest");
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
}
