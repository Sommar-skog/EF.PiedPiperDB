package com.example.piedpiperdb.View;

import com.example.piedpiperdb.Entities.Match;
import com.example.piedpiperdb.Entities.Player;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

//GEFP-16-AA
public class ConfirmBox {
    private static boolean answer;

    public static boolean display(String title, String message){
        Stage window = new Stage();

        window.setTitle(title);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(250);
        window.setMinHeight(200);

        Label label = new Label(message + "\n\n\n\n");

        Button yes = new Button("Yes");
        yes.getStyleClass().add("standardButton");
        Button no = new Button("No");
        no.getStyleClass().add("standardButton");

        yes.setOnAction(e -> {
            answer = true;
            window.close();
        });
        no.setOnAction(e -> {
            answer = false;
            window.close();
        });

        HBox buttonLayout = new HBox(10);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(yes, no);

        VBox layout = new VBox(label, buttonLayout);
        layout.getStyleClass().add("backgroundTeaGreen");
        layout.getStyleClass().add("standardLabel");
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        scene.getStylesheets().add("EscortFlasher.css");
        window.setScene(scene);
        window.showAndWait();

        return answer;
    };

    //GEFP-19-AA
    public static List<String> displayCheckBoxOptions(String title,  List<CheckBox> checkBoxes) {

        List<String> selectedOptions = new ArrayList<>();

        Stage window = new Stage();

        window.setTitle(title);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(350);
        window.setMinHeight(400);

        Label label = new Label("Please select one or several options");

        checkBoxes.forEach(checkBox -> {
                checkBox.setOnAction(event -> {
                    if (checkBox.isSelected()) {
                        selectedOptions.add(checkBox.getText());
                    } else {
                        selectedOptions.remove(checkBox.getText());
                    }
                });
            });

        Button confirm = new Button("Confirm options");
        confirm.getStyleClass().add("standardButton");
        confirm.setOnAction(e -> {
            checkBoxes.forEach(checkBox -> checkBox.setSelected(false));
            window.close();
        });

        HBox buttonLayout = new HBox(10);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().add(confirm);

        VBox layout = new VBox(10);
        layout.getStyleClass().add("backgroundTeaGreen");
        layout.getStyleClass().add("standardLabel");
        layout.setAlignment(Pos.TOP_LEFT);
        layout.getChildren().add(label);
        layout.getChildren().addAll(checkBoxes);
        layout.getChildren().add(buttonLayout);

        Scene scene = new Scene(layout);
        scene.getStylesheets().add("EscortFlasher.css");
        window.setScene(scene);
        window.showAndWait();


        return selectedOptions;

    }

    //GEFP-26-SA
    public static void noPlayersOrMatchesOfGame(String game, String message, String titel){
        Stage window = new Stage();
        window.setTitle(titel+" of "+game);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(300);
        window.setMinHeight(150);

        Label gameLabel = new Label(game);
        gameLabel.getStyleClass().add("titel");

        VBox layout = new VBox();
        layout.setSpacing(5);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setMaxWidth(100);
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("backgroundWhite");

        Label playersLabel = new Label(message);
        playersLabel.getStyleClass().add("standardLabelNoBorder");
        playersLabel.setLineSpacing(5);

        layout.getChildren().addAll(playersLabel);

        Button ok = new Button("Ok");
        ok.getStyleClass().add("standardButton");
        ok.setOnAction(e -> {window.close();});

        VBox vbox = new VBox();
        vbox.setSpacing(25);
        vbox.setPadding(new Insets(15,15,15,15));
        vbox.setAlignment(Pos.CENTER);
        vbox.getStyleClass().add("backgroundTeaGreen");
        vbox.getChildren().addAll(gameLabel, layout,ok);

        Scene scene = new Scene(vbox);
        scene.getStylesheets().add("EscortFlasher.css");
        window.setScene(scene);
        window.showAndWait();
    }

    //GEFP-26-SA
    //GEFP-34-SA, la till playersCount
    //GEFP-43-SA, ändra om från två olika med Player och Match till en
    public static <T> void displayItemsOnList(String game, ObservableList<T> items, String itemsCount, String type){
        Stage window = new Stage();
        window.setTitle(type+" of "+game);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(300);
        window.setMinHeight(150);

        Label gameLabel = new Label(game);
        gameLabel.getStyleClass().add("titel");

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(20));
        vBox.setMaxWidth(100);
        vBox.setAlignment(Pos.CENTER);
        vBox.getStyleClass().add("backgroundTeaGreen");

        Button ok = new Button("Ok");
        ok.getStyleClass().add("standardButton");
        ok.setOnAction(e -> {window.close();});

        Label label = new Label(type);
        label.getStyleClass().add("standardLabelNoBorder");

        Label countLabel = new Label("Number of "+type.toLowerCase()+": "+itemsCount);
        countLabel.getStyleClass().add("standardLabelNoBorder");

        ListView<String>itemsList = new ListView<>();
        itemsList.getStyleClass().add("list-cell");
        itemsList.setMinWidth(100);
        itemsList.setMinHeight(50);

        for(T item : items){//GEFP-43-SA, använder instanceof
            if(item instanceof Match){
                Match match = (Match) item;
                itemsList.getItems().add(match.getMatchName());
            } else if (item instanceof Player) {
                Player player = (Player) item;
                itemsList.getItems().add(player.getNickname());
            }
        }

        vBox.getChildren().addAll(gameLabel, countLabel,label,itemsList,ok);

        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("EscortFlasher.css");
        window.setScene(scene);
        window.showAndWait();
    }


}
