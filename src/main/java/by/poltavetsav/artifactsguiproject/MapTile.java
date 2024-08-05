package by.poltavetsav.artifactsguiproject;

public class MapTile {

    private int x;
    private int y;
    private String content;
    private String coords;

    public String getCoords() {
        return coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public MapTile(int x, int y, String content) {
        this.x = x;
        this.y = y;
        this.content = content;
        this.coords = x + " " + y;
    }
}
