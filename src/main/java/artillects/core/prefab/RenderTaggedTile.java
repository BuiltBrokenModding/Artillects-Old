package artillects.core.prefab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import resonant.lib.transform.vector.Vector3;

import java.util.ArrayList;
import java.util.List;

/** @author Rseifert & Calclavia */
@SideOnly(Side.CLIENT)
public abstract class RenderTaggedTile extends TileEntitySpecialRenderer
{
    float tagRenderHeight = 1.5f;

    @Override
    public void renderTileEntityAt(TileEntity t, double x, double y, double z, float f)
    {
        double distance = this.getPlayer().getDistance(t.xCoord, t.yCoord, t.zCoord);
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ;
        if (distance <= RendererLivingEntity.NAME_TAG_RANGE && player.ridingEntity == null)
        {
            MovingObjectPosition objectPosition = player.rayTrace(8, 1);

            if (objectPosition != null)
            {
                boolean isLooking = false;

                for (int h = 0; h < tagRenderHeight; h++)
                {
                    if (objectPosition.blockX == t.xCoord && objectPosition.blockY == t.yCoord + h && objectPosition.blockZ == t.zCoord)
                    {
                        isLooking = true;
                    }
                }

                if (isLooking)
                {
                    int i = 0;
                    List<RenderText> list = new ArrayList<RenderText>();
                    getRenderTags(t, list);

                    for (RenderText text : list)
                    {
                        text.render(new Vector3(x, y, z).add(0.5, i * 0.25f + tagRenderHeight, 0.5f));
                        i++;
                    }
                }
            }
        }
    }

    protected abstract void getRenderTags(TileEntity tile, List<RenderText> list);

    /** gets the player linked with the renderer */
    public EntityPlayer getPlayer()
    {
        EntityLivingBase entity = this.field_147501_a.field_147551_g;

        if (entity instanceof EntityPlayer)
        {
            return (EntityPlayer) entity;
        }

        return null;
    }
}
