package by.poltavetsav.artifactsguiproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.http.HttpResponse;

public class HelloController {

    CharacterController characterController;

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
        for (int i = 0; i < 16; i++) {
            TableColumn<MapTile, String> column = new TableColumn<>(String.valueOf(i));
            column.setCellValueFactory(new PropertyValueFactory<>("coords"));
          //  worldMapTable.getColumns().add(column);
        }
        do {
            HttpResponse<String> response = characterController.getMap(page);
            page++;
            JSONObject jsonResult = (JSONObject) new JSONObject(response.body());
            JSONArray jsonArray = (JSONArray) new JSONObject(response.body()).get("data");
            totalTiles = (int) jsonResult.get("total");
            processedTiles += jsonArray.length();
            int counter = 0;



            for ( Object object : jsonArray){
                MapTile tile = new MapTile((int)((JSONObject) object).get("x"),
                        (int)((JSONObject) object).get("y"),
                        ((JSONObject) object).get("x").toString());
                       // worldMapTable.getItems().add(tile);
            }
        }
        while (processedTiles < totalTiles);


    }

}