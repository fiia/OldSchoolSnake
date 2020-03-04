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
        Canvas canvas = new Canvas(grids * gridsize, (grids * gridsize)-200);
        GraphicsContext gc = canvas.getGraphicsContext2D();
	tools.showText("SNAKE", gc, canvas);

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
	hboxFirst.setPrefHeight(200);
	hboxFirst.getChildren().addAll(basic, notBasic);
	p.setBottom(hboxFirst);
	tools.buttonLayout(mbtns, 2, hboxFirst, canvas.getWidth());
	
	//SPEED BUTTONS
	Button fast = new Button("FAST");
	Button mid = new Button("MID");
	Button slow = new Button("SLOW");
	HBox hbox = new HBox();
	hbox.setPrefHeight(200);
	hbox.getChildren().addAll(fast, mid, slow);
	ArrayList<Button> sbtns = new ArrayList<Button>();
	sbtns.add(fast);
	sbtns.add(mid);
	sbtns.add(slow);
	tools.buttonLayout(sbtns, 3, hbox, canvas.getWidth());
	
	
        basic.setOnAction(actionEvent -> {
	       snakegame.setBasicMode(true);
	       p.setBottom(hbox);
	       tools.showText("SNAKE BASIC", gc, canvas);
       	});

        notBasic.setOnAction(actionEvent -> {
		snakegame.setBasicMode(false);
		p.setBottom(hbox);
		tools.showText("SNAKE NEW", gc, canvas);
       	});
	
	fast.setOnAction(actionEvent -> {
		int speed = 12;
		run(window, speed, snakegame);
       	});

	mid.setOnAction(actionEvent -> {
		int speed = 8;
		run(window, speed, snakegame);
       	});

	slow.setOnAction(actionEvent -> {
		int speed = 3;
		run(window, speed, snakegame);
       	});
	
	window.setScene(scene);
	window.show();

    }
   
    public void run(Stage window, int speed, Snakegame snakegame) {
	Canvas canvas = new Canvas(grids * gridsize, grids * gridsize);
	GraphicsContext gc = canvas.getGraphicsContext2D();
	tools.showText("GAME STARTS", gc, canvas);

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
			gc.setFill(Color.WHITE);
			gc.setTextAlign(TextAlignment.CENTER);
			gc.setFont(new Font("Impact", 100));
			gc.fillText("GAME OVER\nSCORE: " + snakegame.getScore(),
				    canvas.getWidth()/2, canvas.getHeight()/2);

			Button playAgain = new Button("PLAY AGAIN");
			playAgain.setFont(new Font("Impact", 50));
			playAgain.setStyle("-fx-background-color:black; -fx-text-fill:white; -fx-border-style:solid;");
			ap.setBottomAnchor(playAgain, 10.0);
			ap.getChildren().addAll(playAgain);

			playAgain.setOnAction(actionEvent -> {
				Snakegame newSnakegame = new Snakegame(grids, grids);
				if(!snakegame.getMode()) { newSnakegame.setBasicMode(false); }
				run(window, speed, newSnakegame);

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
            if (event.getCode().equals(KeyCode.UP)) {
                snakegame.getSnake().setDirection(Direction.UP);
            } else if (event.getCode().equals(KeyCode.DOWN)) {
                snakegame.getSnake().setDirection(Direction.DOWN);
            } else if (event.getCode().equals(KeyCode.RIGHT)) {
                snakegame.getSnake().setDirection(Direction.RIGHT);
            } else if (event.getCode().equals(KeyCode.LEFT)) {
                snakegame.getSnake().setDirection(Direction.LEFT);
            }
        });
	
	}

    public static void main(String[] args) {
        launch(App.class);
    }

}
