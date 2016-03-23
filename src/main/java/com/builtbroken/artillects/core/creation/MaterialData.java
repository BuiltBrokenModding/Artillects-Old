package com.builtbroken.artillects.core.creation;

import net.minecraft.block.material.Material;

/** Simple enum of meterials from both MC and artillects
 * 
 * @author Darkguardsman */
public enum MaterialData
{
    AIR(Material.air),
    GRASS(Material.grass),
    GROUND(Material.ground),
    WOOD(Material.wood),
    ROCK(Material.rock),
    IRON(Material.iron),
    ANVIL(Material.anvil),
    WATER(Material.water),
    LAVA(Material.lava),
    LEAVES(Material.leaves),
    PLANTS(Material.plants),
    VINE(Material.vine),
    SPONGE(Material.sponge),
    CLOTH(Material.cloth),
    FIRE(Material.fire),
    SAND(Material.sand),
    CIRCUITS(Material.circuits),
    GLASS(Material.glass),
    CLAY(Material.clay),
    PISTON(Material.piston);

    public final Material material;

    MaterialData(Material material)
    {
        this.material = material;
    }
}
