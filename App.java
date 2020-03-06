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

public class App extends Application {
    private int gridsize = 20;
    private int grids = 30;
    private Tools tools = new Tools(gridsize, grids);

    @Override
    public void start(Stage window) throws Exception {
	window.setTitle("SNAKE");
        Canvas canvas = new Canvas(grids * gridsize, grids * gridsize/3*2);
        GraphicsContext gc = canvas.getGraphicsContext2D();
	tools.showText("SNAKE", gc, canvas, false);
        Snakegame snakegame = new Snakegame(grids, grids);

	//TEXT SCENE
	BorderPane p = new BorderPane();
     	p.setCenter(canvas);
	Scene scene = new Scene(p);

	//MODE BUTTONS
	Button basic = new Button("BASIC");
	Button notBasic = new Button("NEW");
	ArrayList<Button> mbtns = new ArrayList<Button>();
	mbtns.add(basic);
	mbtns.add(notBasic);
	HBox hboxFirst = new HBox();
	hboxFirst.setPrefHeight(grids * gridsize/3);
	hboxFirst.getChildren().addAll(basic, notBasic);
	p.setBottom(hboxFirst);
	tools.buttonLayout(mbtns, 2, hboxFirst, canvas.getWidth());
	
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
	       snakegame.setBasicMode(true);
	       p.setBottom(hbox);
	       tools.showText("SNAKE BASIC", gc, canvas, false);
       	});

        notBasic.setOnAction(actionEvent -> {
		snakegame.setBasicMode(false);
		p.setBottom(hbox);
		tools.showText("SNAKE NEW", gc, canvas, false);
       	});
	
	fast.setOnAction(actionEvent -> {
		int speed = 12;
		run(window, speed, snakegame, hbox);
       	});

	mid.setOnAction(actionEvent -> {
		int speed = 8;
		run(window, speed, snakegame, hbox);
       	});

	slow.setOnAction(actionEvent -> {
		int speed = 4;
		run(window, speed, snakegame, hbox);
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
		    tools.drawApple(Color.RED, snakegame, gc);
		
		    if (snakegame.end()) {
			tools.drawSnake(Color.GRAY, snakegame, gc);
			String endScore = "GAME OVER\nSCORE: " + snakegame.getScore();
			tools.showText(endScore, gc, canvas, true);			
	
			Button playAgain = new Button("NEW GAME");
			tools.endButton(playAgain, ap);
			playAgain.setOnAction(actionEvent -> {
			    Snakegame newSnakegame = new Snakegame(grids, grids);
			    if(!snakegame.getMode()) { newSnakegame.setBasicMode(false); }
				run(window, speed, newSnakegame, hbox);
			    });
			
			return;
		    }
		    tools.drawSnake(Color.WHITE, snakegame, gc);
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

    public static void main(String[] args) {
        launch(App.class);
    }

}
