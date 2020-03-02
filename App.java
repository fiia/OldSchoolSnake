import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
        public static int gridsize = 20;
        public static int grids = 30;

    @Override
    public void start(Stage window) throws Exception {

	window.setTitle("SNAKE");
        Canvas canvas = new Canvas(grids * gridsize, (grids * gridsize)-200);
        GraphicsContext gc = canvas.getGraphicsContext2D();
	showText("SNAKE", gc, canvas);

        Snakegame snakegame = new Snakegame(grids, grids);

	//TEXT GRAPHICS
	BorderPane p = new BorderPane();
     	p.setCenter(canvas);
	Scene scene = new Scene(p);
	
	//BUTTONS
	Button fast = new Button("FAST");
	Button mid = new Button("MID");
	Button slow = new Button("SLOW");
	HBox hbox = new HBox();
	hbox.setPrefHeight(100);
	hbox.getChildren().addAll(fast, mid, slow);
	ArrayList<Button> btns = new ArrayList<Button>();
	btns.add(fast);
	btns.add(mid);
	btns.add(slow);
	p.setBottom(hbox);

	btns.stream().forEach(b -> {
		b.setMinHeight(hbox.getPrefHeight());
		b.setMinWidth(canvas.getWidth()/3);
		b.setFont(new Font("Impact", 50));
		b.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-border-style: solid;");
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

    public void showText(String text, GraphicsContext gc, Canvas canvas) {
	gc.setFill(Color.BLACK);
	gc.fillRect(0, 0, grids * gridsize, grids * gridsize);
	gc.setFill(Color.WHITE);
	gc.setTextAlign(TextAlignment.CENTER);
	gc.setFont(new Font("Impact", 100));
	gc.fillText(text, canvas.getWidth()/2, canvas.getHeight()/2);

    }

    public void drawBackround(Color color, GraphicsContext gc) {
		gc.setFill(color);
	        gc.fillRect(0, 0, grids * gridsize, grids * gridsize);
	    }

    public void drawSnake(Color color, Snakegame snakegame, GraphicsContext gc) {
		gc.setFill(color);
		snakegame.getSnake().getParts().stream()
		    .forEach(part -> {
			gc.fillRect(part.getX() * gridsize,
				    part.getY() * gridsize,
				    gridsize, gridsize);
			});
      }

    public void drawApple(Color color, Snakegame snakegame, GraphicsContext gc) {
		 gc.setFill(color);
		 Apple apple = snakegame.getApple();
	         gc.fillRect(apple.getX() * gridsize,
			     apple.getY() * gridsize,
			     gridsize, gridsize);
     }

    public void run(Stage window, int speed, Snakegame snakegame) {
	
	Canvas canvas = new Canvas(grids * gridsize, grids * gridsize);
	GraphicsContext gc = canvas.getGraphicsContext2D();
	showText("GAME STARTS", gc, canvas);
	
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
		drawBackround(Color.BLACK, gc);
	        drawApple(Color.RED, snakegame, gc);
		
		if (snakegame.end()) {
		    drawSnake(Color.GRAY, snakegame, gc);
		    gc.setFill(Color.WHITE);
		    gc.setTextAlign(TextAlignment.CENTER);
		    gc.setFont(new Font("Impact", 100));
		    gc.fillText("GAME OVER\nscore: " + snakegame.getScore(),
				    canvas.getWidth()/2, canvas.getHeight()/2);

		    return;
		}

		drawSnake(Color.WHITE, snakegame, gc);
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

	//GAME GRAPHICS
	BorderPane snakebp = new BorderPane();
       	snakebp.setCenter(canvas);
	Scene snakeScene = new Scene(snakebp);
      	window.setScene(snakeScene);
	window.show();
		
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
