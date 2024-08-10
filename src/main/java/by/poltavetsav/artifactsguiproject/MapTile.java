package by.poltavetsav.artifactsguiproject;

public class MapTile {

    private int x;
    private int y;
    private String content;
    private String cords;

    public String getCords() {
        return cords;
    }

    public void setCords(String cords) {
        this.cords = cords;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
        this.cords = x + " " + y;
    }
}
