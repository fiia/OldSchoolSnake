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
//TODO: HANDLE BOTH PLAYERS DYING AT THE SAME TIME

public class App extends Application {
    private int gridsize = 20;
    private int grids = 30;
    private Tools tools = new Tools(gridsize, grids);
    private boolean onePlayer = true;

    @Override
    public void start(Stage window) throws Exception {
	startForReal(window);
    }

    public void startForReal(Stage window) {
	window.setTitle("SNAKE");
        Canvas canvas = new Canvas(grids * gridsize, grids * gridsize/3*2);
        GraphicsContext gc = canvas.getGraphicsContext2D();
	tools.showText("SNAKE", gc, canvas, false);
        Snakegame snakegame = new Snakegame(grids, grids);
	Multisnake multisnake = new Multisnake(grids, grids);

	//TEXT SCENE
	BorderPane p = new BorderPane();
     	p.setCenter(canvas);
	Scene scene = new Scene(p);

	//MODE BUTTONS
	Button basic = new Button("BASIC");
	Button notBasic = new Button("NEW");
	Button multiBasic = new Button("TWO\nPLAYERS\nBASIC");
	Button multiNew = new Button("TWO\nPLAYERS\nNEW");
	ArrayList<Button> mbtns = new ArrayList<Button>();
	mbtns.add(basic);
	mbtns.add(notBasic);
	mbtns.add(multiBasic);
	mbtns.add(multiNew);
	HBox hboxFirst = new HBox();
	hboxFirst.setPrefHeight(grids * gridsize/3);
	hboxFirst.getChildren().addAll(basic, notBasic, multiBasic, multiNew);
	p.setBottom(hboxFirst);
	tools.buttonLayout(mbtns, 4, hboxFirst, canvas.getWidth());
	
	//SPEED BUTTONS
	Button fast = new Button("FAST");
	Button mid = new Button("MID");
	Button slow = new Button("SLOW");
	HBox hbox = new HBox();
	hbox.setPrefHeight(grids * gridsize/3);
	hbox.getChildren().addAll(fast, mid, slow);
	ArrayList<Button> sbtns = new ArrayList<Button>();
	sbtns.add(fast);
	sbtns.add(mid);
	sbtns.add(slow);
	tools.buttonLayout(sbtns, 3, hbox, canvas.getWidth());
	
        basic.setOnAction(actionEvent -> {
		window.setTitle("SNAKE BASIC");
		snakegame.setBasicMode(true);
		p.setBottom(hbox);
		onePlayer = true;
		tools.showText("SNAKE BASIC", gc, canvas, false);
       	});

        notBasic.setOnAction(actionEvent -> {
		window.setTitle("SNAKE NEW");
		snakegame.setBasicMode(false);
		p.setBottom(hbox);
		onePlayer = true;
		tools.showText("SNAKE NEW", gc, canvas, false);
       	});

	multiBasic.setOnAction(actionEvent -> {
		window.setTitle("SNAKE BASIC TWO PLAYERS");
		p.setBottom(hbox);
		tools.showText("SNAKE BASIC\nTWO PLAYERS", gc, canvas, false);
		onePlayer = false;
		multisnake.setBasicMode(true);
	    });

	multiNew.setOnAction(actionEvent -> {
		window.setTitle("SNAKE NEW TWO PLAYERS");
		p.setBottom(hbox);
		tools.showText("SNAKE NEW\nTWO PLAYERS", gc, canvas, false);
		onePlayer = false;
		multisnake.setBasicMode(false);
	    });
	
	fast.setOnAction(actionEvent -> {
		int speed = 12;
		if(onePlayer) { run(window, speed, snakegame, hbox); }
	       	else { runTwo(window, speed, multisnake, hbox); }
       	});

	mid.setOnAction(actionEvent -> {
		int speed = 8;
		if(onePlayer) { run(window, speed, snakegame, hbox); }
	       	else { runTwo(window, speed, multisnake, hbox); }
       	});

	slow.setOnAction(actionEvent -> {
		int speed = 4;
		if(onePlayer) { run(window, speed, snakegame, hbox); }
	       	else { runTwo(window, speed, multisnake, hbox); }
       	});
	
	window.setScene(scene);
	window.show();

    }
   
    public void run(Stage window, int speed, Snakegame snakegame, HBox hbox) {
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

		    	ArrayList<Button>ebtns = tools.createEndButtons();
			tools.endButtons(ebtns, ap);
			
			ebtns.get(0).setOnAction(actionEvent -> {
			    Snakegame newSnakegame = new Snakegame(grids, grids);
			    if(!snakegame.getMode()) { newSnakegame.setBasicMode(false); }
				run(window, speed, newSnakegame, hbox);
			    });
			
			ebtns.get(1).setOnAction(actionEvent -> {
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

    public void runTwo(Stage window, int speed, Multisnake multisnake, HBox hbox) {
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
			
			ArrayList<Button>ebtns = tools.createEndButtons();
			tools.endButtons(ebtns, ap);
			
			ebtns.get(0).setOnAction(actionEvent -> {
			    Multisnake newMultisnake = new Multisnake(grids, grids);
			    if(!multisnake.getMode()) { newMultisnake.setBasicMode(false); }
				runTwo(window, speed, newMultisnake, hbox);
			    });
			
			ebtns.get(1).setOnAction(actionEvent -> {
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
