package by.poltavetsav.artifactsguiproject;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;

public class HelloController {

    private int startingX, startingY;
    public static MapTile[][] tiles;
    CharacterController characterController;

    @FXML
    private TableView worldMapTable;

    @FXML
    private Label checkApiLabel, hpLabel, levelLabel, xLabel, yLabel, currentPositionLabel, chosenPositionLabel, cooldownLabel;

    @FXML
    void initialize() throws IOException, InterruptedException {
        characterController = new CharacterController("Alexandr");
        startingX = characterController.characterInfo.get("x");
        startingY = characterController.characterInfo.get("y");
        refreshLabels();
        renderMap();
    }

    @FXML
    public void onMoveButtonClick() throws IOException, InterruptedException {
        characterController.move(startingX, startingY);
       // CooldownThread cooldownThread = new CooldownThread(characterController.move(startingX, startingY), cooldownLabel);
     //   cooldownThread.setDaemon(true);
    //   // Platform.runLater(cooldownThread);
      //  cooldownThread.start();
        renderMap();
        refreshLabels();
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

    void refreshLabels() {
        hpLabel.setText("HP: " + characterController.characterInfo.get("hp").toString());
        levelLabel.setText("Level: " + characterController.characterInfo.get("level").toString());
        xLabel.setText("X: " + startingX);
        yLabel.setText("Y: " + startingY);
    }

    void renderMap() throws IOException, InterruptedException {
        characterController.refreshCharacterInfo();
        worldMapTable.getColumns().clear();
        worldMapTable.getItems().clear();
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
            Object content;
            HashMap hashMap = (HashMap) element;
            if (hashMap.get("content") == null) {
                content = "null";
            } else {
                content = hashMap.get("content");
            }
            int xCord = (int) hashMap.get("x");
            int yCord = (int) hashMap.get("y");
            String mapName;
            boolean playerPos;
            mapName = (String) hashMap.get("name");
            if (xCord == characterController.characterInfo.get("x")
                    && yCord == characterController.characterInfo.get("y")) {
                mapName = "Player";
                playerPos = true;
            } else {
                playerPos = false;
            }
            MapTile mapTile = new MapTile(xCord, yCord, content, mapName, playerPos);
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
                    return new SimpleStringProperty(rowData[colIndex].getName());
                } else {
                    return new SimpleStringProperty("");
                }
            });

            newColumn.setCellFactory(new Callback<TableColumn<MapTile[], String>, TableCell<MapTile[], String>>() {
                @Override
                public TableCell<MapTile[], String> call(TableColumn<MapTile[], String> param) {
                    return new TableCell<MapTile[], String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setText(null);
                                setStyle("");
                            } else {
                                setText(item);
                                switch (item) {
                                    case "Forest(red_slime)":
                                    case "Forest(green_slime)":
                                    case "Forest(blue_slime)":
                                    case "Forest(wolf)":
                                    case "Forest(owlbear)":
                                    case "Forest(mushmush)":
                                    case "Forest(flying_serpent)":
                                    case "Forest(ogre)":
                                    case "Forest(yellow_slime)":
                                    case "Forest(pig)":
                                    case "Forest(bandit_lizard)":
                                    case "Forest(demon)":
                                    case "Graveyard(skeleton)":
                                    case "Graveyard(vampire)":
                                    case "Graveyard(death_knight)":
                                    case "Graveyard(lich)":
                                        setStyle("-fx-background-color: lightCoral;");
                                        break;
                                    case "Forest(woodcutting)":
                                    case "Forest(spruce_tree)":
                                    case "Forest(birch_tree)":
                                    case "Forest(ash_tree)":
                                    case "Graveyard(dead_tree)":
                                        setStyle("-fx-background-color: Peru;");
                                        break;
                                    case "Forest":
                                        setStyle("-fx-background-color: lightgreen;");
                                        break;
                                    case "City(cow)":
                                    case "City(chicken)":
                                    case "City":
                                        setStyle("-fx-background-color: lightgray;");
                                        break;
                                    case "City(gearcrafting)":
                                    case "City(weaponcrafting)":
                                    case "City(cooking)":
                                    case "City(monsters)":
                                    case "City(jewelrycrafting)":
                                    case "Forest (Forge)(mining)":
                                        setStyle("-fx-background-color: mediumSeaGreen;");
                                        break;
                                    case "City(grand_exchange)":
                                    case "City(bank)":
                                        setStyle("-fx-background-color: gold;");
                                        break;
                                    case "Lake(trout_fishing_spot)":
                                    case "Lake(gudgeon_fishing_spot)":
                                    case "Lake(shrimp_fishing_spot)":
                                    case "Lake(bass_fishing_spot)":
                                        setStyle("-fx-background-color: lightblue;");
                                        break;
                                    case "Forest(coal_rocks)":
                                    case "Forest(iron_rocks)":
                                    case "Forest(copper_rocks)":
                                    case "Forest(gold_rocks)":
                                        setStyle("-fx-background-color: goldenRod;");
                                        break;
                                    case "Graveyard":
                                        setStyle("-fx-background-color: darkgray;");
                                        break;
                                    case "Spawn":
                                        setStyle("-fx-background-color: green;");
                                        break;
                                    case "Player":
                                        setStyle("-fx-background-color: lightyellow;");
                                        break;
                                }
                            }
                        }
                    };
                }
            });
            worldMapTable.getColumns().add(newColumn);
        }
        int playerX = characterController.characterInfo.get("x");
        int playerY = characterController.characterInfo.get("y");
        currentPositionLabel.setText("Right now you are on tile with " + tiles[playerY + 5][playerX + 5].getContent().get("type")
                + "(" + tiles[playerY + 5][playerX + 5].getContent().get("code") + ")");

        worldMapTable.setOnMouseClicked((MouseEvent event) -> {
            TablePosition pos = worldMapTable.getFocusModel().getFocusedCell();
            if (pos.getColumn() >= 0) {
                int row = pos.getRow();
                int columnIndex = pos.getColumn();
                MapTile chosenTile = tiles[row][columnIndex];
                HashMap content = (HashMap) chosenTile.getContent();
                chosenPositionLabel.setText("You have chosen a tile with " + content.get("type")
                        + "(" + content.get("code") + ")" + " at x = " + (columnIndex - 5) + " and y = " + (row - 5));
                startingX = columnIndex - 5;
                startingY = row - 5;
            }
        });
    }
}