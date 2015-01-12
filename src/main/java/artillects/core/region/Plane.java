package artillects.core.region;


import com.builtbroken.jlib.data.vector.IPos2D;
import com.builtbroken.mc.lib.transform.vector.Point;
import net.minecraftforge.common.util.ForgeDirection;

/** 2D area of space
 * 
 * @author Darkguardsman */
public class Plane
{
    /** -x -z */
    public Point negNegCorner;
    /** -x +z */
    public Point negPosCorner;

    /** +x +z */
    public Point posPosCorner;
    /** +x -z */
    public Point posNegCorner;

    /** Area covered by the plane */
    private double area = -1;

    /** Width on the x axis */
    private double deltaX = -1;

    /** Length on the z axis */
    private double deltaZ = -1;

    public Plane(double nx, double nz, double px, double pz)
    {
        this(new Point(nx, nz), new Point(px, pz));
    }

    public Plane(Point start, Point end)
    {
        this.negNegCorner = start;
        this.posPosCorner = end;
        update();
    }

    protected void update()
    {
        this.deltaX = negNegCorner.x() - posPosCorner.x();
        this.deltaZ = negNegCorner.y() - posPosCorner.y();
        this.area = deltaX * deltaZ;
        negPosCorner = new Point(negNegCorner.x(), posPosCorner.y());
        posNegCorner = new Point(posPosCorner.x(), negNegCorner.y());
    }

    public Plane expand(ForgeDirection direction, double by)
    {
        if (by > 0)
        {
            contract(direction, -by);
            update();
        }
        return this;
    }

    public Plane contract(ForgeDirection direction, double by)
    {
        if (by > 0)
        {
            switch (direction)
            {
                case EAST:
                    posPosCorner.add(-by, 0);
                    break;
                case NORTH:
                    negNegCorner.add(0, by);
                    break;
                case SOUTH:
                    posPosCorner.add(0, -by);
                    break;
                case WEST:
                    negNegCorner.add(by, 0);
                    break;
                case UNKNOWN:
                    posPosCorner.add(-by, 0);
                    negNegCorner.add(0, by);
                    posPosCorner.add(0, -by);
                    negNegCorner.add(by, 0);
                    break;
            }
            update();
        }
        return this;
    }

    /** Checks to see if the plane is connected by an edge or by overlapping parts */
    public boolean isConnected(Plane other)
    {
        //Edge only connecton test
        if (other.posPosCorner.x() == negNegCorner.x())
        {
            if (inside(posPosCorner.y(), negNegCorner.y(), other.posPosCorner.y()))
                return true;
        }
        if (other.posPosCorner.y() == negNegCorner.y())
        {
            if (inside(posPosCorner.x(), negNegCorner.x(), other.posPosCorner.x()))
                return true;
        }
        if (other.negNegCorner.x() == posPosCorner.x())
        {
            if (inside(posPosCorner.y(), negNegCorner.y(), other.negNegCorner.y()))
                return true;
        }
        if (other.negNegCorner.y() == posPosCorner.y())
        {
            if (inside(posPosCorner.x(), negNegCorner.x(), other.negNegCorner.x()))
                return true;
        }
        //overlap test
        return doesOverlap(other);
    }

    public boolean doesOverlap(Plane other)
    {
        return other.contains(negNegCorner) || other.contains(negPosCorner) || other.contains(posPosCorner) || other.contains(posNegCorner);
    }

    /** Checks to see if this plane can be added with the other */
    public boolean canAddTo(Plane other)
    {
        if (isConnected(other))
        {
            if (isGeoWidth(other) || isGeoLength(other))
            {
                return true;
            }
        }
        return false;
    }

    /** Adds the two planes together, does a canAddTo() check */
    public Plane add(Plane other)
    {
        if (canAddTo(other))
        {
            Point newStart = new Point(other.negNegCorner.x() < negNegCorner.x() ? other.negNegCorner.x() : negNegCorner.x(), other.negNegCorner.y() < negNegCorner.y() ? other.negNegCorner.y() : negNegCorner.y());
            Point newEnd = new Point(other.posPosCorner.x() > posPosCorner.x() ? other.posPosCorner.x() : posPosCorner.x(), other.posPosCorner.y() > posPosCorner.y() ? other.posPosCorner.y() : posPosCorner.y());
            this.negNegCorner = newStart;
            this.posPosCorner = newEnd;
        }
        return this;
    }

    /** Is this plane the same size as the other plane */
    public boolean isSameSize(Plane other)
    {
        return isSameLength(other) && isSameWidth(other);
    }

    /** Is this plane the same width as the other */
    public boolean isSameWidth(Plane other)
    {
        return this.deltaX == other.deltaX;
    }

    /** Is this plane the same length as the other */
    public boolean isSameLength(Plane other)
    {
        return this.deltaZ == other.deltaZ;
    }

    /** Is this plane the same width as the other. Matches using x start and end points */
    public boolean isGeoWidth(Plane other)
    {
        return other.getStart().x() == getStart().x() && other.getEnd().x() == getEnd().x();
    }

    /** Is this plane the same length as the other. Matches using z start and end points */
    public boolean isGeoLength(Plane other)
    {
        return other.getStart().y() == getStart().y() && other.getEnd().y() == getEnd().y();
    }

    /** Is the point contained with in this area */
    public boolean contains(IPos2D vec)
    {
        return contains(vec.x(), vec.y());
    }

    /** Is the point contained with in this area */
    public boolean contains(double x, double z)
    {
        return inside(posPosCorner.x(), negNegCorner.x(), x) && inside(posPosCorner.y(), negNegCorner.y(), z);
    }

    /** Is inside min and max point */
    protected boolean inside(double max, double min, double point)
    {
        return point <= max && point >= min;
    }

    public double getArea()
    {
        return area;
    }

    /** Gets the starting point */
    public Point getStart()
    {
        return negNegCorner;
    }

    /** sets starting point */
    public void setStart(Point start)
    {
        this.negNegCorner = start;
        update();
    }

    /** Gets the ending point */
    public Point getEnd()
    {
        return posPosCorner;
    }

    /** Sets the ending point */
    public void setEnd(Point end)
    {
        this.posPosCorner = end;
        update();
    }

    @Override
    public String toString()
    {
        return "Plane[(" + negPosCorner.x() + "," + negPosCorner.y() + "),(" + posPosCorner.x() + "," + posPosCorner.y() + "),(" + posNegCorner.x() + "," + posNegCorner.y() + "),(" + negNegCorner.x() + "," + negNegCorner.y() + ")]";
    }
}
