package game.gui.views;

import game.engine.Game;
import game.engine.Role;
import game.engine.monsters.Monster;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.*;
import javafx.application.Platform;

public class WinView {

    private GameView view;

    public WinView(GameView game) {
        view = game;
    }

    public Scene createWinScene(Stage primaryStage) {

        Label title = new Label("🏆 GAME WON! 🏆");

        title.setFont(Font.font("Verdana", 38));

        title.setStyle(
                "-fx-text-fill: gold;" +
                "-fx-font-weight: bold;" +
                "-fx-effect: dropshadow(gaussian, rgba(255,215,0,0.7), 20, 0.5, 0, 0);"
        );

        // Logic unchanged
        Monster winner = (Monster) (view.getGame().getWinner());
        Monster current = (Monster) (view.getGame().getCurrent());
        Monster opponent = (Monster) (view.getGame().getOpponent());

        int winnerEnergy = 0;
        int opponentEnergy = 0;

        if (winner.equals(current)) {
            winnerEnergy = current.getEnergy();
            opponentEnergy = opponent.getEnergy();
        } else {
            winnerEnergy = opponent.getEnergy();
            opponentEnergy = current.getEnergy();
        }

        Label energy = new Label(
                "Winner Energy: " + winnerEnergy
                        + "     |     Opponent Energy: "
                        + opponentEnergy
        );

        energy.setFont(Font.font("Verdana", 20));

        energy.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;"
        );

        Role winnerRole = winner.getOriginalRole();

        Label playerWon = new Label(
                "Congratulations!\n"
                        + winner.getName()
                        + " "
                        + winnerRole
                        + " Wins!"
        );

        playerWon.setFont(Font.font("Verdana", 26));

        playerWon.setStyle(
                "-fx-text-fill: #e6e6e6;" +
                "-fx-font-weight: bold;"
        );

        Button replay = new Button("▶ Play Again");

        replay.setFont(Font.font("Verdana", 20));

        replay.setTextFill(Color.WHITE);

        replay.setStyle(
                "-fx-background-color: linear-gradient(#00c853, #009624);" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 18;" +
                "-fx-padding: 15 30 15 30;" +
                "-fx-cursor: hand;"
        );

        Button exit = new Button("✖ Exit Game");

        exit.setFont(Font.font("Verdana", 20));

        exit.setTextFill(Color.WHITE);

        exit.setStyle(
                "-fx-background-color: linear-gradient(#ff5252, #c62828);" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 18;" +
                "-fx-padding: 15 30 15 30;" +
                "-fx-cursor: hand;"
        );

        // LOGIC UNCHANGED
        replay.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {

                try {

                    StartView startView = new StartView();

                    Scene startScene =
                            startView.createStarterScene(primaryStage);

                    primaryStage.setScene(startScene);

                } catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });

        // LOGIC UNCHANGED
        exit.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {

                try {

                    Platform.exit();

                } catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });

        VBox layout = new VBox(
                30,
                title,
                playerWon,
                energy,
                replay,
                exit
        );

        layout.setAlignment(Pos.CENTER);

        layout.setStyle(
                "-fx-padding: 60;" +
                "-fx-background-color: linear-gradient(to bottom, #141e30, #243b55);"
        );

        return new Scene(layout, 800, 600);
    }
}