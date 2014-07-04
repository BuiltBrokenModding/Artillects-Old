package test.common;

import universalelectricity.api.vector.Vector2;
import artillects.core.region.Plane;

public class PlaneTest
{
    public static void main(String... args)
    {
        System.out.println("Doing unit test of Plane.class\n");
        System.out.println("==Setup Test==");
        Vector2 start = new Vector2(-5, -5);
        Vector2 end = new Vector2(5, 5);
        Plane plane = new Plane(start, end);

        System.out.println("Start: " + start);
        System.out.println("End: " + end);
        System.out.println("Plane: " + plane);
        System.out.println("\n==Connection Test==");

        Plane centerPlane = new Plane(-1, -1, 1, 1);
        Plane leftPlane = new Plane(-2, -2, -1, -1);
        
        System.out.println("CenterPlane: " + centerPlane);
        System.out.println("LeftPlane: " + leftPlane);
        
        System.out.println("LeftToCenter: " + centerPlane.isConnected(leftPlane));

    }
}
