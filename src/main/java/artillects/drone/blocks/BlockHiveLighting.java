package artillects.drone.blocks;

public class BlockHiveLighting extends BlockHiveBlock
{

    public BlockHiveLighting(int id)
    {
        super(id, "decorLight");
        this.setLightValue(1.0f);
    }

}
