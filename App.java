import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;

import java.util.ArrayList;

/**
 * @author josefiina
 *
 * CLASS HANDLES GRAPHICS & TIMING
 */

//TODO: SPACE PAUSES
//TODO: PREV BUTTON

public class App extends Application {
    private int gridsize = 20;
    private int grids = 30;
    private Tools tools = new Tools(gridsize, grids);
    private boolean onePlayer = true;
    private ArrayList<Button> mbtns = tools.createModeButtons();
    private ArrayList<Button> sbtns = tools.createSpeedButtons();
    private ArrayList<Button> ebtns = tools.createEndButtons();
    private Snakegame snakegame = new Snakegame(grids, grids);
    private Multisnake multisnake = new Multisnake(grids, grids);

    @Override
    public void start(Stage window) throws Exception {
	window.setTitle("SNAKE");
	startForReal(window);
    }

    public void startForReal(Stage window) {
        Canvas canvas = new Canvas(grids * gridsize, grids * gridsize/3*2);
        GraphicsContext gc = canvas.getGraphicsContext2D();
	tools.showText("SNAKE", gc, canvas, false);

	//TEXT SCENE
	BorderPane p = new BorderPane();
     	p.setCenter(canvas);
	Scene scene = new Scene(p);

	//MODE BUTTONS
	HBox hboxFirst = new HBox();
	hboxFirst.setPrefHeight(grids * gridsize/3);
	hboxFirst.getChildren().addAll(mbtns);
	p.setBottom(hboxFirst);
	tools.buttonLayout(mbtns, 4, hboxFirst, canvas.getWidth());
	
	//SPEED BUTTONS
	HBox hbox = new HBox();
	hbox.setPrefHeight(grids * gridsize/3);
	hbox.getChildren().addAll(sbtns);
	tools.buttonLayout(sbtns, 3, hbox, canvas.getWidth());
	
        mbtns.get(0).setOnAction(actionEvent -> {
		chooseMode(window, "SNAKE BASIC", true, true, p, hbox,
			   "SNAKE BASIC", canvas, gc);
	    });

        mbtns.get(1).setOnAction(actionEvent -> {
		chooseMode(window, "SNAKE NEW", true, false, p, hbox,
			   "SNAKE NEW", canvas, gc);
       	});

	mbtns.get(2).setOnAction(actionEvent -> {
		chooseMode(window, "SNAKE BASIC TWO PLAYERS", false, true, p, hbox,
			   "SNAKE BASIC\nTWO PLAYERS", canvas, gc);
       	});

	mbtns.get(3).setOnAction(actionEvent -> {
		chooseMode(window, "SNAKE NEW TWO PLAYERS", false, false, p, hbox,
			   "SNAKE NEW\nTWO PLAYERS", canvas, gc);
	    });
	
	sbtns.get(0).setOnAction(actionEvent -> {
        	chooseSpeed(window, 12, hbox);
       	});

	sbtns.get(1).setOnAction(actionEvent -> {
		chooseSpeed(window, 8, hbox);
       	});

	sbtns.get(2).setOnAction(actionEvent -> {
        	chooseSpeed(window, 4, hbox);
       	});
	
	window.setScene(scene);
	window.show();

    }

    public void chooseMode(Stage window, String label, boolean onePlayer, boolean basicMode,
			   BorderPane p, HBox hbox,
			   String canvasText, Canvas canvas, GraphicsContext gc) {
	window.setTitle(label);
	if(onePlayer) { snakegame.setBasicMode(basicMode); }
	else { multisnake.setBasicMode(basicMode); }
	p.setBottom(hbox);
	this.onePlayer = onePlayer;
	tools.showText(canvasText, gc, canvas, false);
    }

    public void chooseSpeed(Stage window, int speed, HBox hbox) {
	if(onePlayer) { run(window, speed, hbox); }
    	else { runTwo(window, speed, hbox); }
    }
   
    public void run(Stage window, int speed, HBox hbox) {
	Canvas canvas = new Canvas(grids * gridsize, grids * gridsize);
	GraphicsContext gc = canvas.getGraphicsContext2D();
	tools.showText("GAME STARTS", gc, canvas, false);

	//GAME SCENE
	AnchorPane ap = new AnchorPane();
	ap.setPrefSize(grids * gridsize, grids * gridsize);
	ap.setTopAnchor(canvas, 0.0);
	ap.getChildren().addAll(canvas);
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
		
		    if (snakegame.end()) {
			tools.drawSnake(Color.GRAY, snakegame.getSnake(), gc);
			String endScore = "GAME OVER\nSCORE: " + snakegame.getScore();
			tools.showText(endScore, gc, canvas, true);			
			tools.endButtons(ebtns, ap);
			
			ebtns.get(0).setOnAction(actionEvent -> {
			    snakegame.reset();
			    if(!snakegame.getMode()) { snakegame.setBasicMode(false); }
			    run(window, speed, hbox);
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

    public void runTwo(Stage window, int speed, HBox hbox) {
	Canvas canvas = new Canvas(grids * gridsize, grids * gridsize);
	GraphicsContext gc = canvas.getGraphicsContext2D();
	tools.showText("GAME STARTS\nYELLOW USE WASD\nBLUE USE ARROWS", gc, canvas, false);

	//GAME SCENE
	AnchorPane ap = new AnchorPane();
	ap.setPrefSize(grids * gridsize, grids * gridsize);
	ap.setTopAnchor(canvas, 0.0);
	ap.getChildren().addAll(canvas);
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
		
		    if (multisnake.end()) {
			tools.drawDeadSnakes(multisnake, gc, canvas);
			tools.endButtons(ebtns, ap);
			
			ebtns.get(0).setOnAction(actionEvent -> {
			    multisnake.reset();
			    if(!multisnake.getMode()) { multisnake.setBasicMode(false); }
			    runTwo(window, speed, hbox);
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
