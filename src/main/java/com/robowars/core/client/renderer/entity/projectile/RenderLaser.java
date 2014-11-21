package com.robowars.core.client.renderer.entity.projectile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderLaser extends Render
{
    private static final ResourceLocation textures = new ResourceLocation("textures/entity/laser.png");

    public RenderLaser() {}

    public void doRender(Entity p_doRender_1_, double p_doRender_2_, double p_doRender_4_, double p_doRender_6_, float p_doRender_8_, float p_doRender_9_)
    {
        //dosomething
    }


    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return textures;
    }
}
