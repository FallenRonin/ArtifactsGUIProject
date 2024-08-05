package by.poltavetsav.artifactsguiproject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class CharacterController {


    HashMap<String, Integer> characterInfo;
    HttpClient client;
    String characterName;
    final static String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VybmFtZSI6IkZhbGxlblJvbmluIiwicGFzc3dvc" +
            "mRfY2hhbmdlZCI6IiJ9.oYz7SV6s2y_WBnk-v8y2LpbVRAO6v2LVSHNyAAf-4nQ";
    boolean coolDown = false;

    public CharacterController(String characterName) throws IOException, InterruptedException {
        this.characterName = characterName;
        this.client = HttpClient.newBuilder().build();
        characterInfo = refreshCharacterInfo();
    }

    public boolean checkAPI() throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create("https://api.artifactsmmo.com"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return true;
        }
        return false;
    }

    public void move() throws IOException, InterruptedException {

        JSONObject jsonObject = new JSONObject();
        //jsonObject.put("x", x);
        // jsonObject.put("y", y);

        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create("https://api.artifactsmmo.com" +
                        "/my/" + characterName + "/action/move"))
                .headers("Content-Type", "application/json", "Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(jsonObject.toString()))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse getMap(int page) throws IOException, InterruptedException {

        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create("https://api.artifactsmmo.com/maps/?size=100" + "&page=" + page))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        return response;
    }

    public HashMap refreshCharacterInfo() throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create("https://api.artifactsmmo.com" +
                        "/characters/" + characterName))
                .headers("Content-Type", "application/json", "Authorization", "Bearer " + token)
                .GET()
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());


        JSONObject jsonResult = (JSONObject) new JSONObject(response.body()).get("data");
        return (HashMap) jsonResult.toMap();
    }

}
