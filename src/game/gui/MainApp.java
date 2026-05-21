package game.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import game.gui.views.*;
import javafx.event.*;
import game.engine.*;


public class MainApp extends Application {

	@Override
	public void start(Stage stage)throws IOException {
        StartView startView = new StartView();
		Scene startScene = startView.createStarterScene(stage);
        startScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("DoorDash");
        //stage.setFullScreen(true);
        stage.setMaxHeight(1000);
        stage.setScene(startScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
