package by.poltavetsav.artifactsguiproject;

import java.util.HashMap;

public class MapTile {

    private int x;
    private int y;
    private Object content;
    private String cords;
    private String name;
    private boolean playerTile;

    public String getCords() {
        return cords;
    }

    public String getName() {
        HashMap content = getContent();
        if (content.size() > 1){
            return name + "(" + content.get("code") + ")";
        }
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isPlayerTile() {
        return playerTile;
    }

    public HashMap getContent() {
        if (content == "null") {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("type", "nothing interesting");
            return hashMap;
        }
        return (HashMap) content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public MapTile(int x, int y, Object content, String name, boolean playerTile) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.content = content;
        this.cords = x + " " + y;
        this.playerTile = playerTile;
    }
}
