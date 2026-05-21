package game.gui.controllers;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import game.engine.Constants;
import game.engine.Game;
import game.engine.exceptions.InvalidMoveException;
import game.engine.exceptions.InvalidTurnException;
import game.engine.exceptions.OutOfEnergyException;
import game.engine.monsters.Dasher;
import game.engine.monsters.Dynamo;
import game.engine.monsters.Monster;
import game.engine.monsters.MultiTasker;
import game.engine.cards.Card;
import game.engine.cells.CardCell;
import game.engine.cells.TransportCell;
import game.gui.views.GameView;
import game.gui.views.WinView;
import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
public class GameControl {
    private Game game;
    private GameView view;

    public GameControl(Game game, GameView view) {
        this.game = game;
        this.view = view;
        attachEvents();
        attachKeyboardControls();
    }

    private void attachEvents() {

        System.out.println("Attaching to: " + view.getRollDiceButton());

        view.getRollDiceButton().setOnAction(e -> {
            handleRollDice();
            System.out.println("CLICKED!");
        });

        // Prevent NullPointerException
        if (view.getPower1() != null) {

            view.getPower1().setOnAction(e -> {
                handlePower1();
            });

        } else {
            System.out.println("Power button is NULL!");
        }
        view.getPower2().setOnAction(e -> {
            handlePower1();
        });
    }

    private void handlePower1() {

        displayPower(
            "Execute Power Effect",
            "You can " + typeEffect()
            + "\nThe cost is " + Constants.POWERUP_COST
        );
    }
    private String typeEffect(){
    	if(game.getCurrent() instanceof Dynamo)
    		return "Freeze your Opponent for 1 Turn!";  
    	if (game.getCurrent() instanceof Dasher)
    		return "Move with roll*3 for 3 Turns";
    	if(game.getCurrent() instanceof MultiTasker)
    		return "Move with Normal Speed for 2 Turns";
    	return "Steal Energy from all Monsters and Your Opponent";
    	}
    private void handleRollDice() {
    	System.out.println("dice rolled");
    	Stage alert = new Stage();
    	alert.setTitle("invalid move");
    	Card c= game.getBoard().getCards().get(0);
   
  //      game.getBoard().getCell(game.getCurrent().getPosition()).onLand(game.getCurrent(), oppMonster);
    	Monster currMonster = (Monster)(game.getCurrent());
    	int pos = currMonster.getPosition();
    	Boolean flag=currMonster.isShielded();
    	if(game.getCurrent().isFrozen())
    		displayAlert("Frozen", "You were Frozen , but now ou are Free!");
    		
        try {
        	
            int x= game.playTurn(); 
            view.getDiceResultLabel().setText("Dice Roll: " + x);
            if(flag==true&&currMonster.isShielded()==false)
            	displayAlert("SHIELD EFFECT",currMonster.getName()+" was saved from Energy Deduction");
            if (game.getBoard().getCell(currMonster.getPosition()) instanceof CardCell) {
            	displayAlert("A Card was Drawn",currMonster.getName()+" Landed on a Card Cell "+"\n"+" Card Drawn was "+c.getName()+"\n" + view.cardType(c));}
            if(game.getBoard().getCell(pos+s(currMonster,x))instanceof TransportCell)
            	displayAlert("Transport Cell!","You Landed on a Transport Cell"+" It had an effect with " +((TransportCell) game.getBoard().getCell(pos+s(currMonster,x))).getEffect());

        } catch (InvalidMoveException e) {
        	displayAlert("InvalidMOveException","Invalid Move! Your Opponent is there!" );
        }
        Monster oppMonster;
        if(currMonster.equals(game.getOpponent()))
        	oppMonster = (Monster)game.getPlayer();
        else
        	oppMonster = (Monster)game.getOpponent();
        
        // Handle Card Logic
       

        view.refreshBoard();

        // Handle Win Condition
        if (game.getWinner() != null) {
            WinView win = new WinView(view);
            Stage stage = (Stage) view.getRoot().getScene().getWindow();
            stage.setScene(win.createWinScene(stage));
        }
    }
    private void displayAlert(String title, String message) {
        Stage alertStage = new Stage();
        alertStage.setTitle(title);

        Label label = new Label(message);
        Button closeButton = new Button("Continue Playing");
        closeButton.setOnAction(event -> alertStage.close());

        BorderPane pane = new BorderPane();
        pane.setTop(label);
        pane.setCenter(closeButton);

        Scene scene = new Scene(pane, 500, 100);
        alertStage.setScene(scene);
        alertStage.show();
    }
    private void displayPower(String title, String message) {

        Stage alertStage = new Stage();
        alertStage.setTitle(title);

        Monster oppMonster;

        if (game.getCurrent().equals(game.getOpponent()))
            oppMonster = (Monster) game.getPlayer();
        else
            oppMonster = (Monster) game.getOpponent();

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-text-fill: gold;" +
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;"
        );

        Label messageLabel = new Label(message);

        messageLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;"
        );

        Button cancelButton = new Button("Cancel");

        cancelButton.setStyle(
                "-fx-background-color: #444444;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 10;"
        );

        cancelButton.setOnAction(e -> alertStage.close());

        Button applyButton = new Button("ACTIVATE POWER");

        applyButton.setStyle(
                "-fx-background-color: linear-gradient(#ffcc00, #ff6600);" +
                "-fx-text-fill: black;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 12;"
        );

        applyButton.setOnAction(e -> {

            try {

                game.usePowerup();

                //game.getCurrent().executePowerupEffect(oppMonster);

                view.refreshBoard();

                displayAlert(
                        "POWER ACTIVATED",
                        "Special ability executed successfully!"
                );

                alertStage.close();

            } catch (OutOfEnergyException ex) {

                displayAlert(
                        "OUT OF ENERGY",
                        "You need "
                        + Constants.POWERUP_COST
                        + " energy to use this power."
                );
            }
        });

        HBox buttons = new HBox(20);
        buttons.getChildren().addAll(cancelButton, applyButton);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20);

        layout.getChildren().addAll(
                titleLabel,
                messageLabel,
                buttons
        );

        layout.setAlignment(Pos.CENTER);

        layout.setStyle(
                "-fx-background-color: #1b1b1b;" +
                "-fx-padding: 25;" +
                "-fx-border-color: gold;" +
                "-fx-border-width: 3;"
        );

        Scene scene = new Scene(layout, 500, 220);

        alertStage.setScene(scene);
        alertStage.show();
    }
    private int s(Monster m,int x){
    	if(m instanceof Dasher)
    		return x*2;
    	if(m instanceof Dynamo)
    		return x;
    	if(m instanceof MultiTasker)
    		return x/2;
    	return x;
    }
    private void attachKeyboardControls() {

        view.getRoot().setOnKeyPressed(event -> {

            switch (event.getCode()) {

            case E:

                game.getCurrent().alterEnergy(
                       100
                );

                displayAlert(
                        "CHEAT ACTIVATED",
                        "Energy increased!"
                );

                view.refreshBoard();
                if (game.getWinner() != null) {
                    WinView win = new WinView(view);
                    Stage stage = (Stage) view.getRoot().getScene().getWindow();
                    stage.setScene(win.createWinScene(stage));
                }

                break;

            case W:

                game.getCurrent().setPosition(99);

                displayAlert(
                        "CHEAT ACTIVATED",
                        "Monster moved to position 99!"
                );

                view.refreshBoard();
                if (game.getWinner() != null) {
                    WinView win = new WinView(view);
                    Stage stage = (Stage) view.getRoot().getScene().getWindow();
                    stage.setScene(win.createWinScene(stage));
                }

                break;

            default:
                break;
            }
        });

        // important so keyboard input works
        view.getRoot().requestFocus();
    }
    /*private void showNotification(String message) {
    if (view == null || view.getRoot() == null) {
        System.out.println("Cannot show notification: View or Root is null!");
        return; 
    }

    Label notification = new Label(message);
    notification.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-text-fill: white; -fx-padding: 10;");

    // Use getRoot() but check if it exists
    notification.setMouseTransparent(true);
    view.getRoot().getChildren().add(notification);
    notification.toFront(); // Or add to children
    notification.setTranslateX(500); 
    notification.setTranslateY(350);
    // ... rest of your FadeTransition code ...

    FadeTransition fade = new FadeTransition(Duration.seconds(3), notification);
    fade.setFromValue(1.0);
    fade.setToValue(0.0);
    fade.setOnFinished(e -> {
        // Check view first
        if (view == null) return;
        
        // Check root next
        BorderPane rootNode = view.getRoot();
        if (rootNode == null) return;
        
        // Finally, check if the notification is still actually a child of the root
        if (rootNode.getChildren().contains(notification)) {
            rootNode.getChildren().remove(notification);
            System.out.println("Notification cleaned up successfully.");
        }
    });
    fade.play();
}*/
}
