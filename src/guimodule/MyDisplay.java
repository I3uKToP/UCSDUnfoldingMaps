package guimodule;

import processing.core.PApplet;

public class MyDisplay extends PApplet{
	
	public void setup () {
		
		size (400, 400); 
		background(200, 200, 200);
		
	}
	
	public void draw () {
		
		fill(255, 255, 0);
		ellipse (width / 2, height /2 , 390, 390);
		fill (0, 0, 0);
		ellipse (100,150,50,70);
		ellipse (300,150,50,70);
		
		arc(200, 280, 100, 100, 0, PI);
	}

}
