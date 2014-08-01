package artillects.core.creation;

/** Metadata sub version of a block
 * 
 * @author Darkguardsman */
public class Subblock
{
    int meta = 0;
    float hardness = 1;
    float resistance = 1;
    String unlocalizedName;
    String iconName;

    public Subblock()
    {

    }

    public Subblock(int meta, String name)
    {
        this.meta = meta;
        this.unlocalizedName = name;
        this.iconName = name;
    }

}