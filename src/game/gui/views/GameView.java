package game.gui.views;

import java.io.IOException;

import javafx.scene.control.*;
import game.engine.*;
import game.engine.cells.*;
import game.engine.cells.Cell;
import game.engine.exceptions.InvalidMoveException;
import game.engine.monsters.*;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import game.engine.cards.*;
import game.gui.MainApp;
import game.gui.views.*;
//... imports (Keep your current imports) ...

public class GameView {
	private Game game;
    private StackPane[] cells;
    private BorderPane root;
    private Role playerRole;

    private GridPane boardGrid;

	private Label currentTurnLabel;
    private Label diceResultLabel;

    private Label monster1Info;
    private Label monster2Info;
    private Label drawCard;

    private Button rollDiceButton;
    private ImageView playerView;
    private ImageView opponentView;
    private Button power1;
    private Button power2;
    private Label info;
    private Image beigeImage;
    private Image doorImage;
    private Image cardImage;
    private Image sockImage;
    private Image conveyorImage;
    private Image monsterImage;

    public Button getPower1() {
		return power1;
	}
	public Button getPower2() {
		return power2;
		
	}
	
	public GameView(Game game, Role playerRole) {
        this.game = game;
        System.out.println(game.getBoard().getCards().size());
        this.playerRole=playerRole;
        // Create array for 100 cells
        cells = new StackPane[100];
        beigeImage =
                new Image(getClass().getResourceAsStream(
                        "/resources/images/beige.jpg"));

        doorImage =
                new Image(getClass().getResourceAsStream(
                        "/resources/images/door.jpg"));

        cardImage =
                new Image(getClass().getResourceAsStream(
                        "/resources/images/card.png"));

        sockImage =
                new Image(getClass().getResourceAsStream(
                        "/resources/images/sock.jpg"));

        conveyorImage =
                new Image(getClass().getResourceAsStream(
                        "/resources/images/conv.jpg"));

        monsterImage =
                new Image(getClass().getResourceAsStream(
                        "/resources/images/monster.jpg"));
        initializeMonsterViews();
    }
    private void initializeMonsterViews() {

        Image playerImage;
        Image opponentImage;
        
        if(playerRole ==Role.SCARER){
        	playerImage=new Image(getClass().getResourceAsStream("/resources/images/scarer.png"));
        	opponentImage=new Image( getClass().getResourceAsStream("/resources/images/laugh.png"));
        }
        else{
        	playerImage=new Image( getClass().getResourceAsStream("/resources/images/laugh.png"));
        	opponentImage=new Image(getClass().getResourceAsStream("/resources/images/scarer.png"));
        }
        
        opponentView = new ImageView(opponentImage);
        opponentView.setFitWidth(30);
        opponentView.setFitHeight(30);
        
        playerView = new ImageView(playerImage);
        playerView.setFitWidth(30);
        playerView.setFitHeight(30);


        
    }
        

 // GETTERS (Crucial so Controller can access the UI)
 public Button getRollDiceButton() { return rollDiceButton; }
 public BorderPane getRoot() { return root; }
 public StackPane[] getCells() { return cells; }
 public Game getGame(){return game;}
public GridPane getBoardGrid(){
	return boardGrid;
}
 public Scene createScene(Stage stage) {
     root = new BorderPane();
     root.setStyle(
    		    "-fx-background-color: linear-gradient(to bottom, #0b1020, #141d2f);"
    		);
     root.setPadding(new Insets(50));

     root.setTop(createTopBar());
     root.setCenter(createBoard());
     root.setLeft(createPlayerPanel());
     root.setRight(createOpponentPanel());
     //root.setBottom(createControls());
     root.setBottom(info);
     //cells[0].getChildren().addAll(playerView, opponentView);
     refreshBoard();
     
     return new Scene(root, 1200, 700);
 }

 public void setDiceResultLabel(Label diceResultLabel) {
	this.diceResultLabel = diceResultLabel;
}
private HBox createTopBar() {

     HBox topBar = new HBox(40); //40 is the spacing between the elements
     currentTurnLabel = new Label("Current Turn: "+game.getCurrent().getName());

     diceResultLabel = new Label("Dice Result: -" );//how should i add the value of the roll , its method is private should i make it public
     currentTurnLabel.setAlignment(Pos.CENTER);
     diceResultLabel.setAlignment(Pos.CENTER);
     topBar.setPrefHeight(10);
     topBar.setMaxHeight(10);
     topBar.setAlignment(Pos.CENTER);
     drawCard =new Label("Cards"+Board.cards.size());
     rollDiceButton = new Button("Roll Dice");
     currentTurnLabel.setId("hudLabel");
     diceResultLabel.setId("hudLabel");
     drawCard.setId("hudLabel");
     System.out.println("Button created: " + rollDiceButton); // Debug 1
     topBar.getChildren().addAll(rollDiceButton ,drawCard,
             currentTurnLabel,
             diceResultLabel
     );

     return topBar;
 }
 private GridPane createBoard() {

     GridPane grid = new GridPane();

     grid.setHgap(0);
     grid.setVgap(0);
    grid.setAlignment(Pos.CENTER);

     // Create 100 cells
     for (int i = 0; i < 100; i++) {

         // Create visual cell
         StackPane cell = createCell(i);
         System.out.println(cell);
         // Store it
         cells[i] = cell;

         // Convert index to row/column
         int row = i / 10;
 	    int col = i % 10;
 	    int r=10-1-row;
 	    if (row % 2 == 1){
 	    	//row=10-1-row;
 	        col = 10 - 1 - col;}
 	    cell.getStyleClass().add("game-cell");
         grid.add(cell, col, r);
     }
     
     return grid;
 }
 private StackPane createCell(int index) {

     StackPane cell = new StackPane();
     Rectangle bg = new Rectangle(55, 55);
     Cell data = game.getBoard().getCell(index);
     System.out.println(data.getName());
     
     Image image ;
     if(data.getName().equals("Normal Rest Corridor")){
     	bg.setFill(Color.BISQUE);
     	image = beigeImage;
     	ImageView imageView = new ImageView(image);
     	imageView.setFitWidth(55);
 		imageView.setFitHeight(55);
 		cell.getChildren().addAll(imageView);}
     else if(data instanceof DoorCell){
     	bg.setFill(Color.BISQUE);
     	image = doorImage;
     	cell.setOnMouseClicked(e->{displayAlert("Information","Type: Door Cell " + ((DoorCell) data).getRole() 
    		    + "\n"
    		    + "Energy: " + ((DoorCell) data).getEnergy()+"\n"+
    		    "M.type: "+getType(((DoorCell) data).getMonster()) 
    		    
    		    )
    		    ;}); 

     }else if(data instanceof CardCell){
     	bg.setFill(Color.RED);
     	image = cardImage;
     }else if(data instanceof ContaminationSock){
     	bg.setFill(Color.ORANGE);
     	image = sockImage;
     }else if(data instanceof ConveyorBelt){
     	bg.setFill(Color.GREEN);
     	image = conveyorImage;
     }else {
     	bg.setFill(Color.BLUE);
     	image = monsterImage;
     	cell.setOnMouseClicked(e->{displayAlert("Information", "Type: "+getType(data.getMonster())+"\n"+
                "Name: "+data.getName()+"\n" 
     			+ "Role: " +((MonsterCell)data).getCellMonster().getRole() + "\n"+
                "Energy: " + ((MonsterCell)data).getCellMonster().getEnergy()+"\n" 
    		    
    		    
    		    )
    		    ;}); 
     }


     // Cell number
     Label number = new Label(String.valueOf(index));
     number.setId("index");
     number.getStyleClass().add("index");
     StackPane.setAlignment(number, Pos.TOP_LEFT);
    

      ImageView imageView = new ImageView(image);

      imageView.setFitWidth(35);
      imageView.setFitHeight(35);
       if(!(data instanceof DoorCell))
         	cell.getChildren().addAll(bg, imageView, number);
       else{
         	if(((DoorCell) data).getRole()==Role.LAUGHER){
         		Label l = new Label("L");
         		l.setId("l_LS");
                 l.getStyleClass().add("l_LS");
         		StackPane.setAlignment(l, Pos.TOP_RIGHT);
         		cell.getChildren().addAll(bg, imageView, number,l);
         	}
         	else {
         		Label l = new Label("S");
         		l.setId("l_LS");
                 l.getStyleClass().add("l_LS");
         		StackPane.setAlignment(l, Pos.TOP_RIGHT);
         		cell.getChildren().addAll(bg, imageView, number,l);
         		}
         }
     return cell;
     }
 private VBox createPlayerPanel() {

     VBox panel = new VBox(20);
     power1=new Button("PowerUp");
     power1.setId("powerButton");
     power1.setMaxSize(200, 30);

     panel.setPadding(new Insets(10));
     VBox.setVgrow(panel, Priority.NEVER);
     panel.setPrefWidth(230);
     panel.setPrefHeight(400);
     Label l=new Label("Player");
     l.setAlignment(Pos.CENTER);
     // Monster info labels
     monster1Info = new Label(
             "Type: "+getType(game.getPlayer())+"\n"+
             "Name: "+game.getPlayer().getName()+"\n"+
             "OriginalRole: "+game.getPlayer().getOriginalRole()+"\n"+
             "CurrentRole: "+game.getPlayer().getRole()+"\n"+
             "Energy: " + game.getPlayer().getEnergy()+"\n" +
             "Position: "+game.getPlayer().getPosition()+"\n"+
             "Shielded: "+game.getPlayer().isShielded()+"\n"+
             "Frozen: "+game.getPlayer().isFrozen()+"\n"+
             "Confused: "+game.getPlayer().isConfused()+"\n"+
             "Confused Turns: " + game.getPlayer().getConfusionTurns()+"\n"+
             s(game.getPlayer())
     );
     monster1Info.setId("player");
     monster1Info.getStyleClass().add("player");
     l.getStyleClass().add("labelTitle");
     panel.setStyle(
    		    "-fx-background-color: rgba(20,30,48,0.92);" +
    		    	    "-fx-background-radius: 20;" +
    		    	    "-fx-padding: 15;" +
    		    	    "-fx-border-color: #00e5ff;" +
    		    	    "-fx-border-radius: 20;" +
    		    	    "-fx-border-width: 2;");
     panel.setAlignment(Pos.CENTER);
     monster1Info.setAlignment(Pos.CENTER);
     panel.setMaxHeight(Region.USE_PREF_SIZE);
     panel.getChildren().addAll(l,
             monster1Info,power1
     );

     return panel;
 }
 private String s(Monster m){
	 if(m instanceof Dasher)
		 return"Momentum Turns: "+((Dasher)m).getMomentumTurns();
	 if(m instanceof MultiTasker)
		 return "Normal Speed Turns: "+ ((MultiTasker)m).getNormalSpeedTurns();
	 return "";
	 
 }
 private String getType(Monster m) {
 	if(m instanceof Dynamo)
 		return "Dynamo";
 	else if(m instanceof Dasher)
 		return"Dasher";
 	else if(m instanceof Schemer)
 		return "Schemer";
 	return "MultiTasker";
 	
 }
 private VBox createOpponentPanel() {

     VBox panel = new VBox(20);
     power2 =new Button("PowerUp");
     power2.setId("powerButton");
     power2.setMaxSize(200, 30);
     panel.setPadding(new Insets(10));

     panel.setPrefWidth(230);
     panel.setPrefHeight(370);
     panel.setAlignment(Pos.CENTER);
     Label l = new Label("Opponent");
     l.setAlignment(Pos.CENTER);
     monster2Info = new Label(
                     "Type: "+getType(game.getOpponent())+"\n"+
                     "Name: "+game.getOpponent().getName()+"\n"+
                     "OriginalRole: "+game.getOpponent().getOriginalRole()+"\n"+
                     "CurrentRole: "+game.getOpponent().getRole()+"\n"+
                     "Energy: " + game.getOpponent().getEnergy()+"\n" +
                     "Position: "+game.getOpponent().getPosition()+"\n"+
                     "Shielded: "+game.getOpponent().isShielded()+"\n"+
                     "Frozen: "+game.getOpponent().isFrozen()+"\n"+
                     "Confused: "+game.getOpponent().isConfused()+"\n"+
                     "Confused Turns: "+game.getOpponent().getConfusionTurns()+"\n"+
                     s(game.getOpponent())
     );
     panel.setStyle(
    		    "-fx-background-color: rgba(20,30,48,0.92);" +
    		    "-fx-background-radius: 20;" +
    		    "-fx-padding: 15;" +
    		    "-fx-border-color: #00e5ff;" +
    		    "-fx-border-radius: 20;" +
    		    "-fx-border-width: 2;");
     monster2Info.getStyleClass().add("player");
     l.getStyleClass().add("labelTitle");
     panel.setAlignment(Pos.CENTER);
     monster1Info.setAlignment(Pos.CENTER);
     panel.setMaxHeight(Region.USE_PREF_SIZE);

     panel.getChildren().addAll(l,monster2Info,power2);
     return panel;
     
 
 }
	private void updateCellVisualState(int index) {
        Cell data = game.getBoard().getCell(index);
        StackPane cellPane = cells[index];

        if (data instanceof DoorCell) {
            DoorCell door = (DoorCell) data;
            if (door.isActivated()) {
                // Requirement: Distinguishable from non-activated doors 
                cellPane.setOpacity(0.4); 
                cellPane.setStyle("-fx-border-color: gray; -fx-border-style: dashed;");
            } else {
                cellPane.setOpacity(1.0);
                cellPane.setStyle("-fx-border-color: black;");
            }
        }
    }

 
 public String cardType(Card card) {
     
     if (card instanceof SwapperCard) return "Swap Positions";
     if (card instanceof ConfusionCard) return "Swap Roles";
     if (card instanceof EnergyStealCard) return "Steal Energy";
     if (card instanceof ShieldCard) return "You are Shielded Now";
     return "Start Over";
 }

 public void refreshBoard() {
	 
	 drawCard.setText("Cards:"+ game.getBoard().getCards().size());
	 
     for (StackPane cell : cells) {
         cell.getChildren().removeIf(node -> node.getStyleClass().contains("monster-marker"));
     }
     drawMonster(game.getOpponent(), opponentView);
     drawMonster(game.getPlayer(), playerView);
    
     
     for(int i=0; i<100; i++)
     {
    	 updateCellVisualState(i);
     }
     
     updateStatusPanels();
     //updating the card count
     
     
 }
 public Label getDiceResultLabel() {
	return diceResultLabel;
}
private void drawMonster(Monster m, ImageView view) {
	    // 1. Safety check: Remove the view from its CURRENT parent (if any)
	    if (view.getParent() != null) {
	        ((Pane) view.getParent()).getChildren().remove(view);
	    }

	    // 2. Add the marker class and styling
	    view.getStyleClass().add("monster-marker"); 
	    // 3. Now it is safe to add it to the NEW cell
	    int pos = m.getPosition();
	    cells[pos].getChildren().add(view);
	    
	    
	}
 public void updateStatusPanels() {
     // Update Player Info [cite: 35, 40, 43]
     monster1Info.setText(
    	     		 "Type: "+getType(game.getPlayer())+"\n"+
    	             "Name: "+game.getPlayer().getName()+"\n"+
    	             "OriginalRole: "+game.getPlayer().getOriginalRole()+"\n"+
    	             "CurrentRole: "+game.getPlayer().getRole()+"\n"+
    	             "Energy: " + game.getPlayer().getEnergy()+"\n" +
    	             "Position: "+game.getPlayer().getPosition()+"\n"+
    	             "Shielded: "+game.getPlayer().isShielded()+"\n"+
    	             "Frozen: "+game.getPlayer().isFrozen()+"\n"+
    	             "Confused: "+game.getPlayer().isConfused()+"\n"+
    	             "Confused Turns: "+game.getPlayer().getConfusionTurns()+"\n"+
    	             s(game.getPlayer())
     );

     monster2Info.setText(
             "Type: "+getType(game.getOpponent())+"\n"+
             "Name: "+game.getOpponent().getName()+"\n"+
             "OriginalRole: "+game.getOpponent().getOriginalRole()+"\n"+
             "CurrentRole: "+game.getOpponent().getRole()+"\n" +
             "Energy: " + game.getOpponent().getEnergy()+"\n" +
             "Position: "+game.getOpponent().getPosition()+"\n"+
             "Shielded: "+game.getOpponent().isShielded()+"\n"+
             "Frozen: "+game.getOpponent().isFrozen()+"\n"+
             "Confused: "+game.getOpponent().isConfused()+"\n"+
             "Confused Turns: "+game.getOpponent().getConfusionTurns()+"\n"+
             s(game.getOpponent())
);

     // Requirement: Current turn indication [cite: 12, 36]
     currentTurnLabel.setText("Current Turn: " + game.getCurrent().getName());
 }
 private void displayAlert(String title, String message) {

	    Stage alertStage = new Stage();

	    alertStage.setTitle(title);


	    Label titleLabel = new Label(title);

	    titleLabel.setStyle(
	        "-fx-font-size: 22px;"
	        + "-fx-font-weight: bold;"
	        + "-fx-text-fill: gold;"
	    );


	    Label label = new Label(message);

	    label.setStyle(
	        "-fx-font-size: 16px;"
	        + "-fx-text-fill: white;"
	    );


	    Button closeButton =
	            new Button("Continue");

	    closeButton.setStyle(

	        "-fx-background-color: darkred;"
	        + "-fx-text-fill: white;"
	        + "-fx-font-size: 14px;"
	        + "-fx-font-weight: bold;"
	        + "-fx-background-radius: 10;"
	    );

	    closeButton.setOnAction(
	            event -> alertStage.close()
	    );


	    VBox root = new VBox(20);

	    root.getChildren().addAll(
	            titleLabel,
	            label,
	            closeButton
	    );

	    root.setAlignment(Pos.CENTER);

	    root.setPadding(new Insets(20));



	    root.setStyle(
	        "-fx-background-color: #2b2b2b;"
	        + "-fx-border-color: gold;"
	        + "-fx-border-width: 3;"
	    );


	    Scene scene =
	            new Scene(root, 450, 250);

	    alertStage.setScene(scene);

	    alertStage.show();
	}
 
 
 
}
