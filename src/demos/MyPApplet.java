package demos;

import processing.core.PApplet;
import processing.core.PImage;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

/** 
 * A class to illustrate some use of the PApplet class
 * Used in module 3 of the UC San Diego MOOC Object Oriented Programming in Java
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * 
 *
 */
public class MyPApplet extends PApplet{
	
	PImage backgroundImg = loadImage("https://vokrugsveta.ua/wp-content/uploads/2017/10/shutterstock_276228476.jpg", "jpg");
	
	public void setup() {
		//Add setup code for MyPApplet
		size(800,800);				//set canvas size
		background(255, 204, 0);			//set canvas color
		stroke(0);				//set pen color	
	
	}
	
	public void draw() {
		backgroundImg.resize(width, 0);
		image (backgroundImg, 0 ,0);
		fill (255, 204 , 0);
		float [] cords = setUpCords(millis());
		ellipse (cords[0], cords [1], 50 , 50);
		
		frameRate(60);
		
		

			}
		
	public float [] setUpCords(float millis) {
		float [] cord= new float [2];
	
		cord [0] =  (float) (200*Math.cos(millis/ 180 * PI /10) + width / 2);
		cord [1] =  (float) (200*Math.sin(millis / 180 * PI / 10) + height / 2 -100);
		return cord;

	}
	
	public int [] sunColorSec (float seconds) {
		int [] rgb =  new int [3];
		float different = Math.abs(30-seconds);
		float ratio =  different / 30;
		rgb[0] = (int) (255 * ratio);
		rgb [1] = (int) (255*ratio);
		rgb[2] = 0;
		return rgb;
		
	} 	
	}
	

