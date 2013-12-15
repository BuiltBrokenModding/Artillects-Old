package artillects.block.teleporter.util;

public enum Shape {
	NOTHING(null, 0),
	SQUARE("square", 1),
	PENTAGON("pentagon", 3),
	CIRCLE("circle", 4),
	CROSS("cross", 5);
	
	private String shapeName;
	private int shapeNumber;
	
	Shape(String name, int number) {
		shapeName = name;
		shapeNumber = number;
	}
}
