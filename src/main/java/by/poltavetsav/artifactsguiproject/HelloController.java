package by.poltavetsav.artifactsguiproject;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;

public class HelloController {

    public static MapTile[][] tiles;

    CharacterController characterController;

    @FXML
    private TableView worldMapTable;

    @FXML
    private Label checkApiLabel, hpLabel, levelLabel, xLabel, yLabel;

    @FXML
    void initialize() throws IOException, InterruptedException {
        characterController = new CharacterController("Alexandr");
        hpLabel.setText("HP: " + characterController.characterInfo.get("hp").toString());
        levelLabel.setText("Level: " + characterController.characterInfo.get("level").toString());
        xLabel.setText("X: " + characterController.characterInfo.get("x").toString());
        yLabel.setText("Y: " + characterController.characterInfo.get("y").toString());
        renderMap();
    }

    @FXML
    public void onCheckApiButtonClick() throws IOException, InterruptedException {
        if (characterController.checkAPI()) {
            checkApiLabel.setTextFill(Paint.valueOf("#2a8a12"));
            checkApiLabel.setText("OK");
        } else {
            checkApiLabel.setTextFill(Paint.valueOf("#fc0000"));
            checkApiLabel.setText("ERROR");
        }
    }

    void renderMap() throws IOException, InterruptedException {
        int processedTiles = 0;
        int page = 1;
        int totalTiles;
        int colCount = 17;
        int rowCount = 21;
        tiles = new MapTile[rowCount][colCount];
        ArrayList<Object> result = new ArrayList<>();

        do {
            HttpResponse<String> response = characterController.getMap(page);
            page++;
            JSONObject jsonResult = new JSONObject(response.body());
            JSONArray jsonArray = (JSONArray) new JSONObject(response.body()).get("data");
            result.addAll(jsonArray.toList());
            totalTiles = (int) jsonResult.get("total");
            processedTiles += jsonArray.length();
        }
        while (processedTiles < totalTiles);

        for (Object element : result) {
            String content;
            HashMap hashMap = (HashMap) element;
            if (hashMap.get("content") == null) {
                content = "null";
            } else {
                content = hashMap.get("content").toString();
            }
            MapTile mapTile = new MapTile((int) hashMap.get("x"), (int) hashMap.get("y"), content);
            tiles[mapTile.getY() + 5][mapTile.getX() + 5] = mapTile;
        }

        for (int y = 0; y < rowCount; y++) {
            MapTile[] row = new MapTile[colCount];
            System.arraycopy(tiles[y], 0, row, 0, colCount);
            worldMapTable.getItems().add(row);
        }

        for (int x = 0; x < colCount; x++) {
            final int colIndex = x;
            TableColumn<MapTile[], String> newColumn = new TableColumn<>(String.valueOf(x - 5));
            newColumn.setCellValueFactory(param -> {
                MapTile[] rowData = param.getValue();
                if (rowData[colIndex] != null) {
                    return new SimpleStringProperty(rowData[colIndex].getCords());
                } else {
                    return new SimpleStringProperty("");
                }

            });

//            newColumn.setCellFactory(column -> new TableCell<MapTile[], String>() {
//                @Override
//                protected void updateItem(Integer item, boolean empty) {
//                    super.updateItem(item, empty);
//
//                    if (item == null || empty) {
//                        setText(null);
//                        setStyle(""); // Reset cell style
//                    } else {
//                        setText(item.toString());
//
//                        // Set cell color based on a condition (e.g., age > 30)
//                        if (item > 5) {
//                            setTextFill(Color.RED);
//                        } else {
//                            setTextFill(Color.BLACK);
//                        }
//                    }
//                }
//            });
            worldMapTable.getColumns().add(newColumn);
        }

        worldMapTable.setOnMouseClicked((MouseEvent event) -> {
            TablePosition pos = worldMapTable.getFocusModel().getFocusedCell();
            if (pos.getColumn() >= 0) {
                int row = pos.getRow();
                int columnIndex = pos.getColumn();
                MapTile chosenTile = tiles[row][columnIndex];
                System.out.println(chosenTile.getContent());
            }
        });

    }


}