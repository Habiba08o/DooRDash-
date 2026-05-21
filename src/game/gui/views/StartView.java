package game.gui.views;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import game.engine.Role;
import game.engine.Game;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import game.gui.views.GameView;
import game.gui.controllers.*;


public class StartView {
private Role selectedRole = Role.SCARER;


public Scene createStarterScene(Stage primaryStage){	
	 Label title = new Label("Choose Your Side");
	 title.setStyle(
		        "-fx-font-size: 32px;" +
		        "-fx-font-weight: bold;" +
		        "-fx-text-fill: #FFD700;" +
		        "-fx-effect: dropshadow(gaussian, rgba(255,215,0,0.5), 12, 0.3, 0, 0);"
		);
     Image scarerImage = new Image(getClass().getResourceAsStream("/resources/images/scarer.png"));
     ImageView scarerImageView = new ImageView(scarerImage);
     scarerImageView.setFitWidth(150);
     scarerImageView.setPreserveRatio(true);

     Image laugherImage = new Image(getClass().getResourceAsStream("/resources/images/laugh.png"));
     ImageView laugherImageView = new ImageView(laugherImage);
     laugherImageView.setFitWidth(150);
     laugherImageView.setPreserveRatio(true);

     Button scarerBtn = new Button("Play as SCARER");
     Button laugherBtn = new Button("Play as LAUGHER");
     Button continueBtn = new Button("Continue →");

     String scarerDefault = "-fx-background-color: #ff6666; -fx-font-size: 16px;";
     String scarerSelected = "-fx-background-color: #ff0000; -fx-font-size: 16px;";
     String laugherDefault = "-fx-background-color: #66b3ff; -fx-font-size: 16px;";
     String laugherSelected = "-fx-background-color: #0066cc; -fx-font-size: 16px;";

     
     
     scarerBtn.setStyle(scarerSelected);
     laugherBtn.setStyle(laugherDefault);
     continueBtn.setStyle("-fx-background-color: #66cc66; -fx-font-size: 16px;");

     scarerBtn.setOnAction(e -> {
         selectedRole = Role.SCARER;
         scarerBtn.setStyle(scarerSelected);
         laugherBtn.setStyle(laugherDefault);
         
         //experimenting something
         scarerBtn.setStyle(scarerSelected + " -fx-border-color: white; -fx-border-width: 2px;");
         javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.millis(100));
         pause.setOnFinished(ev -> scarerBtn.setStyle(scarerSelected));
         pause.play();
     });

     laugherBtn.setOnAction(e -> {
         selectedRole = Role.LAUGHER;
         laugherBtn.setStyle(laugherSelected);
         scarerBtn.setStyle(scarerDefault);
         
         //here too
         laugherBtn.setStyle(laugherSelected + " -fx-border-color: white; -fx-border-width: 2px;");
         javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.millis(100));
         pause.setOnFinished(ev -> laugherBtn.setStyle(laugherSelected));
         pause.play();
     });

     VBox scarerBox = new VBox(10, scarerImageView, scarerBtn);
     scarerBox.setAlignment(Pos.CENTER);
     VBox laugherBox = new VBox(10, laugherImageView, laugherBtn);
     laugherBox.setAlignment(Pos.CENTER);

     HBox choices = new HBox(40, scarerBox, laugherBox);
     choices.setAlignment(Pos.CENTER);

     VBox selectionContent = new VBox(30, title, choices, continueBtn);
     selectionContent.setAlignment(Pos.CENTER);
     //selectionContent.setStyle("-fx-padding: 50px; -fx-background-color: #f0f0f0;");
     selectionContent.setStyle(
    		    "-fx-padding: 50px;" +
    		    "-fx-background-color: linear-gradient(to bottom, #0b1020, #141d2f);"
    		);
     // Make selection panel fill entire StackPane
     StackPane selectionPanel = new StackPane(selectionContent);
     selectionPanel.setStyle(
    		    "-fx-background-color: linear-gradient(to bottom, #0b1020, #141d2f);"
    		);

     // ==================== INSTRUCTIONS PANEL (full screen, top-aligned) ====================
     Label instructionsTitle = new Label("Game Instructions");
     instructionsTitle.setStyle(
    		    "-fx-font-size: 26px;" +
    		    "-fx-font-weight: bold;" +
    		    "-fx-text-fill: #00e5ff;"
    		);

     String instructionsText =
         "• Reach cell 99 (Boo's Door) with at least 1000 energy to win.\n\n" +
         "• Roll the dice to move forward.\n\n" +
         "• Land on Doors: same role = gain energy for whole team; opposite role = lose energy (shield blocks).\n\n" +
         "• Monster Cells: same role = free power-up; opposite = swap energy if you have more.\n\n" +
         "• Conveyor Belts (green): move forward.\n\n" +
         "• Contamination Socks (orange): move backward + lose 100 energy.\n\n" +
         "• Card Cells (red): draw a random card (swap, steal, shield, confusion, start over).\n\n" +
         "• Shields block one negative energy effect.\n\n" +
         "• Confusion swaps roles for several turns.\n\n" +
         "• Power-up costs 500 energy (optional).";

     Label instructionsLabel = new Label(instructionsText);
     instructionsLabel.setStyle(
    		    "-fx-font-size: 17px;" +
    		    "-fx-padding: 20px;" +
    		    "-fx-font-weight: bold;" +
    		    "-fx-text-fill: #0b1020;"
    		);
     instructionsLabel.setWrapText(true);
     instructionsLabel.setMaxWidth(Double.MAX_VALUE);

     ScrollPane scrollPane = new ScrollPane(instructionsLabel);
     scrollPane.setFitToWidth(true);
     scrollPane.setStyle(
    		    "-fx-background: #d9e2f2;" +
    		    "-fx-background-color: #d9e2f2;" +
    		    "-fx-border-color: #00e5ff;" +
    		    "-fx-border-width: 2;" +
    		    "-fx-background-radius: 15;" +
    		    "-fx-border-radius: 15;"
    		);
     scrollPane.setMaxHeight(Double.MAX_VALUE);

     Button startBtn = new Button("Start Game");
     startBtn.setStyle("-fx-background-color: #ffaa00; -fx-font-size: 18px; -fx-font-weight: bold;");
     startBtn.setOnAction(e -> {
         try {
             Game game = new Game(selectedRole);
             GameView gameView = new  GameView(game, selectedRole);
             Scene gameScene = gameView.createScene(primaryStage);
             //primaryStage.setFullScreen(true);
             primaryStage.setScene(gameScene);
             primaryStage.setMaxHeight(1000);
             //primaryStage.setFullScreen(true);
             new GameControl(game, gameView);
             gameScene.getStylesheets().add(getClass().getResource("/game/gui/style.css").toExternalForm());
         } catch (Exception ex) {
             ex.printStackTrace();
         }
     });

     // BorderPane to keep instructions title at top, scrollable center, button at bottom
     BorderPane instructionsRoot = new BorderPane();
     instructionsRoot.setTop(instructionsTitle);
     instructionsRoot.setCenter(scrollPane);
     instructionsRoot.setBottom(startBtn);
     BorderPane.setAlignment(instructionsTitle, Pos.CENTER);
     BorderPane.setAlignment(startBtn, Pos.CENTER);
     instructionsRoot.setStyle(
    		    "-fx-padding: 30px;" +
    		    "-fx-background-color: linear-gradient(to bottom, #0b1020, #141d2f);"
    		);
     StackPane instructionsPanel = new StackPane(instructionsRoot);
     instructionsPanel.setStyle(
    		    "-fx-background-color: linear-gradient(to bottom, #0b1020, #141d2f);"
    		);
     instructionsPanel.setVisible(false);

     // ==================== MAIN STACKPANE TO SWITCH PANELS ====================
     StackPane root = new StackPane();
     root.getChildren().addAll(selectionPanel, instructionsPanel);

     continueBtn.setOnAction(e -> {
         selectionPanel.setVisible(false);
         instructionsPanel.setVisible(true);
     });

     Scene scene = new Scene(root, 800, 600);
     return scene;
 


    
}
}
