package com.robowars.core.client.renderer.entity.projectile;

import com.robowars.core.RoboWarsMod;
import com.robowars.core.client.model.projectile.ModelLaser;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderLaser extends Render
{
    private static final ResourceLocation texture = new ResourceLocation(RoboWarsMod.MODID, "textures/entity/laserbeam.png");
    private static final ModelBase model = new ModelLaser();
    public RenderLaser() {}

    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTick)
    {
        GL11.glPushMatrix();
        bindTexture(texture);
        GL11.glTranslated(x, y, z);
        model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }


    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return texture;
    }
}
