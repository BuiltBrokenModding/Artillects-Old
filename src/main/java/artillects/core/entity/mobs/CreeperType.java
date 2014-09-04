package artillects.core.entity.mobs;

public enum CreeperType
{
    //Classic creeper
    BASIC,
    //Spawns with a camo of a block (Ex. Cobble creeper, looks like cobble stone)
    DECOY,
    //Ghost of a creeper captured in gas
    GAS,
    //Ghost of a creeper captured in liquid (Ex. LAVA creeper, made of lava. Slime creeper, Water creeper)
    LIQUID;
    
    
    private CreeperType()
    {
        
    }
}
