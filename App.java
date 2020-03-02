import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;

/**
 * @author josefiina
 *
 * CLASS HANDLES GRAPHICS & TIMING
 */


public class App extends Application {

    @Override
    public void start(Stage window) throws Exception {
        int gridsize = 20;
        int grids = 30;
        int speed = 7;

        Canvas canvas = new Canvas(grids * gridsize, grids * gridsize);
        GraphicsContext gc = canvas.getGraphicsContext2D();

	gc.setFill(Color.BLACK);
	gc.fillRect(0, 0, grids * gridsize, grids * gridsize);
	gc.setFill(Color.WHITE);
	gc.setTextAlign(TextAlignment.CENTER);
	gc.setFont(new Font("Impact", 100));
	gc.fillText("GAME STARTS", canvas.getWidth()/2, canvas.getHeight()/2);
	
        Snakegame snakegame = new Snakegame(grids, grids);
        
        new AnimationTimer() {
            private long prev;
	    int count = 0;

	    public void drawBackround(Color color) {
		gc.setFill(color);
	        gc.fillRect(0, 0, grids * gridsize, grids * gridsize);
	    }

	    public void drawSnake(Color color) {
		gc.setFill(color);
		snakegame.getSnake().getParts().stream()
		    .forEach(part -> {
			gc.fillRect(part.getX() * gridsize,
				    part.getY() * gridsize,
				    gridsize, gridsize);
			});
	    }

	    public void drawApple(Color color) {
		 gc.setFill(color);
		 Apple apple = snakegame.getApple();
	         gc.fillRect(apple.getX() * gridsize,
			     apple.getY() * gridsize,
			     gridsize, gridsize);
	    }

            @Override
            public void handle(long now) {

		if(count>70) {
		    if (now - prev < 1_000_000_000 / 30) {
			return;
		    }
		    prev = now;

		    drawBackround(Color.BLACK);
		    drawApple(Color.RED);
		
		    if (snakegame.end()) {
			drawSnake(Color.GRAY);
			gc.setFill(Color.WHITE);
			gc.fillText("GAME OVER\nscore: " + snakegame.getScore(),
				    canvas.getWidth()/2, canvas.getHeight()/2);

			return;
		    }
		    drawSnake(Color.WHITE);
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

        BorderPane p = new BorderPane();
        p.setCenter(canvas);

        Scene scene = new Scene(p);

        //KEYBOARD LISTENER
        scene.setOnKeyPressed((event) -> {
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

	window.setScene(scene);
	window.show();

    }

    public static void main(String[] args) {
        launch(App.class);
    }

}
