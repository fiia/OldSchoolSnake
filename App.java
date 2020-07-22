import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;

import java.util.ArrayList;

/**
 * @author josefiina
 *
 * CLASS HANDLES GRAPHICS & TIMING
 */

//TODO: SPACE PAUSES
//TODO: MOVE ALL STYLING TO TOOLS

public class App extends Application {
    private int gridsize = 20;
    private int grids = 30;
    private Tools tools = new Tools(gridsize, grids);
    private boolean onePlayer;
    private ArrayList<Button> mbtns = tools.createModeButtons();
    private ArrayList<Button> sbtns = tools.createSpeedButtons();
    private ArrayList<Button> ebtns = tools.createEndButtons();
    Button goBack = new Button("RETURN");
    private Snakegame snakegame = new Snakegame(grids, grids);
    private Multisnake multisnake = new Multisnake(grids, grids);

    @Override
    public void start(Stage window) throws Exception {
	window.setTitle("SNAKE");
	startForReal(window);
    }

    public void startForReal(Stage window) {
	//MENU CANVAS
        Canvas scanvas = new Canvas(grids * gridsize, grids * gridsize/3*2);
        GraphicsContext gc = scanvas.getGraphicsContext2D();
	tools.showText("SNAKE", gc, scanvas, false);

	//TEXT SCENE
	BorderPane p = new BorderPane();
     	p.setCenter(scanvas);
	p.setStyle("-fx-background-color:black;");
	Scene scene = new Scene(p);

	//MODE BUTTONS
	HBox hboxFirst = new HBox();
	hboxFirst.setPrefHeight(grids * gridsize/3);
	hboxFirst.getChildren().addAll(mbtns);
	//p.setBottom(hboxFirst);
	hboxFirst.setStyle("-fx-background-color:black;");
	hboxFirst.setAlignment(Pos.BOTTOM_CENTER);
	
	//SPEED BUTTONS
	HBox hbox = new HBox();
	hbox.setPrefHeight(grids * gridsize/3);
	hbox.getChildren().addAll(sbtns);
	hbox.setAlignment(Pos.BOTTOM_CENTER);

	//RETURN
	tools.buttonLayout(goBack);
	HBox scoreBoard = new HBox();
	scoreBoard.setPrefHeight(grids*gridsize/12);
	scoreBoard.setStyle("-fx-background-color:black;");
	
	VBox empty = new VBox();
	empty.getChildren().addAll(hboxFirst, scoreBoard);
	p.setBottom(empty);

	//KAHDEN ALAPALKIN JÄRJESTELY ENNEN NOPEUSNÄKYMÄÄ
	VBox vbox = new VBox();
	//vbox.getChildren().addAll(hbox, scoreBoard);
	

	//BUTTON ACTION	
        mbtns.get(0).setOnAction(actionEvent -> {
		chooseMode(window, "SNAKE BASIC", snakegame, true, true, p, hbox,
			   scoreBoard, vbox, "SNAKE BASIC", scanvas, gc);
	    });

        mbtns.get(1).setOnAction(actionEvent -> {
		chooseMode(window, "SNAKE NEW", snakegame, true, false, p, hbox,
			   scoreBoard, vbox, "SNAKE NEW", scanvas, gc);
	    });

	mbtns.get(2).setOnAction(actionEvent -> {
		chooseMode(window, "SNAKE BASIC TWO PLAYERS", multisnake, false,
			   true, p, hbox, scoreBoard, vbox, "SNAKE BASIC\nTWO PLAYERS",
			   scanvas, gc);
	    });

	mbtns.get(3).setOnAction(actionEvent -> {
		chooseMode(window, "SNAKE NEW TWO PLAYERS", multisnake, false,
			   false, p, hbox, scoreBoard, vbox, "SNAKE NEW\nTWO PLAYERS",
			   scanvas, gc);
	    });

	sbtns.get(0).setOnAction(actionEvent -> {
        	chooseSpeed(window, 12);
	    });

	sbtns.get(1).setOnAction(actionEvent -> {
		chooseSpeed(window, 8);
	    });

	sbtns.get(2).setOnAction(actionEvent -> {
        	chooseSpeed(window, 4);
	    });

	goBack.setOnAction(actionEvent -> {
		startForReal(window);
	    });
	
	window.setScene(scene);
	window.show();

    }

    public void chooseMode(Stage window, String label, Snakegame game, boolean oneP,
			   boolean basicMode, BorderPane p, HBox hbox, HBox scoreB,
			   VBox vbox, 
			   String canvasText, Canvas scanvas, GraphicsContext gc) {
	window.setTitle(label);
	this.onePlayer = oneP;
        game.setBasicMode(basicMode);
	scoreB.getChildren().add(goBack);
	vbox.getChildren().addAll(hbox, scoreB);
	p.setBottom(vbox);
	this.onePlayer = onePlayer;
	tools.showText(canvasText, gc, scanvas, false);
    }

    public void chooseSpeed(Stage window, int speed) {
	if(onePlayer) { run(window, speed); }
    	else { runTwo(window, speed); }
    }

    //RUN ONE PLAYER GAME
    public void run(Stage window, int speed) {
	Canvas canvas = new Canvas(grids * gridsize, grids * gridsize);
	GraphicsContext gc = canvas.getGraphicsContext2D();
	tools.showText("GAME STARTS", gc, canvas, false);

	//SCORE AT BOTTOM OF PAGE
	HBox scoreBoard = new HBox();
	scoreBoard.setPrefHeight(grids*gridsize/12);
	scoreBoard.setStyle("-fx-background-color:black;");
	Text score = tools.startScoreText(Color.WHITE);
	scoreBoard.getChildren().add(score);
	scoreBoard.setAlignment(Pos.CENTER);

	//GAME SCENE
	AnchorPane ap = new AnchorPane();
	ap.setPrefSize(grids * gridsize, grids * gridsize + (grids*gridsize/12));
	VBox gameBox = new VBox(2);
	gameBox.getChildren().addAll(canvas, scoreBoard);
	ap.getChildren().addAll(gameBox);

       	Scene snakeScene = new Scene(ap);
      	window.setScene(snakeScene);
	window.show();
	
	new AnimationTimer() {
	    private long prev;
	    int count = 0;

	    @Override
	    public void handle(long now) {
		if(count>70) {
		    if (now - prev < 1_000_000_000 / 30) {
			return;
		    }
		    prev = now;
		    tools.drawBackround(Color.BLACK, gc);
		    tools.drawApple(Color.ORANGERED, snakegame, gc);
		    score.setText("SCORE: " + snakegame.getScore());
		
		    if (snakegame.end()) {
			score.setText("");
			tools.drawSnake(Color.GRAY, snakegame.getSnake(), gc);
			String endScore = "GAME OVER\nSCORE: " + snakegame.getScore();
			tools.showText(endScore, gc, canvas, true);			
			tools.endButtons(ebtns, ap);
			
			ebtns.get(0).setOnAction(actionEvent -> {
			    snakegame.reset();
			    run(window, speed);
			    });
			
			ebtns.get(1).setOnAction(actionEvent -> {
				snakegame.reset();
				startForReal(window);
			    });
			
			return;
		    }
		    tools.drawSnake(Color.WHITE, snakegame.getSnake(), gc);
		}
		count = (count<71) ? count+1 : count;
	    }
	}.start();
        
        new AnimationTimer() {
            private long prev;

            @Override
            public void handle(long now) {
                if (now - prev < 1_000_000_000 / speed) {
                    return;
                }
                prev = now;
                snakegame.refresh();
                if (snakegame.end()) {
                    stop();
                }
            }
	}.start();

        //KEYBOARD LISTENER
        snakeScene.setOnKeyPressed((event) -> {
            KeyCode kc = event.getCode();
	    switch(kc) {
       	        case UP: snakegame.getSnake().setDirection(Direction.UP); break;
	        case DOWN: snakegame.getSnake().setDirection(Direction.DOWN); break;
       	        case RIGHT: snakegame.getSnake().setDirection(Direction.RIGHT); break;
	        case LEFT: snakegame.getSnake().setDirection(Direction.LEFT); break;
	        default: break;
	    }
        });
	
    }

    //RUN TWO PLAYER GAME
    public void runTwo(Stage window, int speed) {
	Canvas canvas = new Canvas(grids * gridsize, grids * gridsize);
	GraphicsContext gc = canvas.getGraphicsContext2D();
	tools.showText("GAME STARTS\nYELLOW USE WASD\nBLUE USE ARROWS",
		       gc, canvas, false);

	//SCORE AT BOTTOM OF PAGE
        HBox scoreBoard = new HBox(40);
	scoreBoard.setPrefHeight(grids*gridsize/12);
	scoreBoard.setStyle("-fx-background-color:black;");
	Text scoreMario = tools.startScoreText(multisnake.getMario().getColor());
	Text scoreLuigi = tools.startScoreText(multisnake.getLuigi().getColor());
	scoreBoard.getChildren().addAll(scoreLuigi, scoreMario);
	scoreBoard.setAlignment(Pos.CENTER);

	//GAME SCENE
	AnchorPane ap = new AnchorPane();
	ap.setPrefSize(grids * gridsize, grids * gridsize);
        VBox gameBox = new VBox(2);
	gameBox.getChildren().addAll(canvas, scoreBoard);
	ap.getChildren().addAll(gameBox);

       	Scene snakeScene = new Scene(ap);
      	window.setScene(snakeScene);
	window.show();

	new AnimationTimer() {
	    private long prev;
	    int count = 0;

	    @Override
	    public void handle(long now) {
	       	if(count>70) {
		    if (now - prev < 1_000_000_000 / 30) {
			return;
		    }
		    prev = now;
		    tools.drawBackround(Color.BLACK, gc);
		    tools.drawApple(Color.ORANGERED, multisnake, gc);
		    scoreMario.setText("SCORE: " + multisnake.getMario().getScore());
		    scoreLuigi.setText("SCORE: " + multisnake.getLuigi().getScore());
		
		    if (multisnake.end()) {
			scoreMario.setText("");
			scoreLuigi.setText("");
			tools.drawDeadSnakes(multisnake, gc, canvas);
			tools.endButtons(ebtns, ap);
			
			ebtns.get(0).setOnAction(actionEvent -> {
			    multisnake.reset();
			    runTwo(window, speed);
			    });
			
			ebtns.get(1).setOnAction(actionEvent -> {
				multisnake.reset();
				startForReal(window);
			    });
			
			return;
		    }
		    tools.drawSnake(multisnake.getLuigi().getColor(),
				    multisnake.getLuigi().getSnake(), gc);
		    tools.drawSnake(multisnake.getMario().getColor(),
				    multisnake.getMario().getSnake(), gc);
		}
	    count = (count<71) ? count+1 : count;
	    }
	}.start();

	new AnimationTimer() {
            private long prev;

            @Override
            public void handle(long now) {
                if (now - prev < 1_000_000_000 / speed) {
                    return;
                }
                prev = now;
                multisnake.refresh();
                if (multisnake.end()) {
                    stop();
                }
            }
	}.start();

	//KEYBOARD LISTENER
        snakeScene.setOnKeyPressed((event) -> {
            KeyCode kc = event.getCode();
	    switch(kc) {
	    case UP: multisnake.getLuigi().getSnake().setDirection(Direction.UP); break;
	    case DOWN: multisnake.getLuigi().getSnake().setDirection(Direction.DOWN); break;
	    case RIGHT: multisnake.getLuigi().getSnake().setDirection(Direction.RIGHT); break;
	    case LEFT: multisnake.getLuigi().getSnake().setDirection(Direction.LEFT); break;
       	    case W: multisnake.getMario().getSnake().setDirection(Direction.UP); break;
	    case S: multisnake.getMario().getSnake().setDirection(Direction.DOWN); break;
	    case D: multisnake.getMario().getSnake().setDirection(Direction.RIGHT); break;
	    case A: multisnake.getMario().getSnake().setDirection(Direction.LEFT); break;
	        default: break;
	    }
        });
    }

    public static void main(String[] args) {
        launch(App.class);
    }
}
