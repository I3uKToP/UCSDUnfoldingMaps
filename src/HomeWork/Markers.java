package HomeWork;


import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;

import processing.core.PGraphics;

public class Markers extends CommonMarker {
	
	
	public int salary =10;
	


	
	public Markers(Location location) {
		super(location);
		
	}

	

	
	public Markers(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
		// Cities have properties: "name" (city name), "country" (country name)
		// and "population" (population, in millions)
	}
	
	
	
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		//System.out.println("Drawing a city");
		// Save previous drawing style
		pg.pushStyle();
		int salary = 10 + getSalary()/3000;
		//System.out.println(salary);

		pg.fill(0, 255, 255);
		pg.ellipse(x, y, salary, salary);
		
		// Restore previous drawing style
		pg.popStyle();
	}



	
	private int getSalary()
	{
		String salary  = getStringProperty("salary");
		if (!salary.equals("not information")) {
		float salaryFloat = Float.parseFloat(salary);
		return (int) salaryFloat;}
		//System.out.println(salary);
		else return 0;
	}

}
