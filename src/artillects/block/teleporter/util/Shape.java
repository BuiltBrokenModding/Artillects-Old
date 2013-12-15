package artillects.block.teleporter.util;

public enum Shape {
	NOTHING(null, 0),
	SQUARE("square", 1),
	PENTAGON("pentagon", 2),
	CIRCLE("circle", 3),
	CROSS("cross", 4);
	
	private String shapeName;
	private int shapeNumber;
	
	Shape(String name, int number) {
		shapeName = name;
		shapeNumber = number;
	}
}
