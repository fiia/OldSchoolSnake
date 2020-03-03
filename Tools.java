//import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.input.KeyCode;
//import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
//import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
//import javafx.stage.Stage;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;

import java.util.ArrayList;

/**
 * @author josefiina
 *
 * CLASS CONTAINS VISUAL TOOLS
 */

public class Tools {
    private int gridsize;
    private int grids;
    
    public Tools(int gridsize, int grids) {
	this.gridsize = gridsize;
	this.grids = grids;
    }

 public void buttonLayout(ArrayList<Button> btns, int numButtons, HBox hbox, double width) {
	btns.stream().forEach(b -> {
		b.setMinHeight(hbox.getPrefHeight());
		b.setMinWidth(width/numButtons);
		b.setFont(new Font("Impact", 50));
		b.setStyle("-fx-background-color:black; -fx-text-fill:white; -fx-border-style:solid;");
	    });
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
}
