package com.robowars.core.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderLaser extends Render
{
    private static final ResourceLocation arrowTextures = new ResourceLocation("textures/entity/arrow.png");

    public RenderLaser() {}

    public void doRender(Entity p_doRender_1_, double p_doRender_2_, double p_doRender_4_, double p_doRender_6_, float p_doRender_8_, float p_doRender_9_)
    {
        System.out.println("ya");

        bindEntityTexture(p_doRender_1_);

        GL11.glPushMatrix();

        GL11.glTranslatef((float)p_doRender_2_, (float)p_doRender_4_, (float)p_doRender_6_);
        GL11.glRotatef(p_doRender_1_.prevRotationYaw + (p_doRender_1_.rotationYaw - p_doRender_1_.prevRotationYaw) * p_doRender_9_ - 90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(p_doRender_1_.prevRotationPitch + (p_doRender_1_.rotationPitch - p_doRender_1_.prevRotationPitch) * p_doRender_9_, 0.0F, 0.0F, 1.0F);

        Tessellator localTessellator = Tessellator.instance;
        int i = 0;

        float f1 = 0.0F;
        float f2 = 0.5F;
        float f3 = (0 + i * 10) / 32.0F;
        float f4 = (5 + i * 10) / 32.0F;

        float f5 = 0.0F;
        float f6 = 0.15625F;
        float f7 = (5 + i * 10) / 32.0F;
        float f8 = (10 + i * 10) / 32.0F;
        float f9 = 0.05625F;
        GL11.glEnable(32826);
        GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(f9, f9, f9);

        GL11.glTranslatef(-4.0F, 0.0F, 0.0F);

        GL11.glNormal3f(f9, 0.0F, 0.0F);
        localTessellator.startDrawingQuads();
        localTessellator.addVertexWithUV(-7.0D, -2.0D, -2.0D, f5, f7);
        localTessellator.addVertexWithUV(-7.0D, -2.0D, 2.0D, f6, f7);
        localTessellator.addVertexWithUV(-7.0D, 2.0D, 2.0D, f6, f8);
        localTessellator.addVertexWithUV(-7.0D, 2.0D, -2.0D, f5, f8);
        localTessellator.draw();

        GL11.glNormal3f(-f9, 0.0F, 0.0F);
        localTessellator.startDrawingQuads();
        localTessellator.addVertexWithUV(-7.0D, 2.0D, -2.0D, f5, f7);
        localTessellator.addVertexWithUV(-7.0D, 2.0D, 2.0D, f6, f7);
        localTessellator.addVertexWithUV(-7.0D, -2.0D, 2.0D, f6, f8);
        localTessellator.addVertexWithUV(-7.0D, -2.0D, -2.0D, f5, f8);
        localTessellator.draw();
        for (int j = 0; j < 4; j++)
        {
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, f9);
            localTessellator.startDrawingQuads();
            localTessellator.addVertexWithUV(-8.0D, -2.0D, 0.0D, f1, f3);
            localTessellator.addVertexWithUV(8.0D, -2.0D, 0.0D, f2, f3);
            localTessellator.addVertexWithUV(8.0D, 2.0D, 0.0D, f2, f4);
            localTessellator.addVertexWithUV(-8.0D, 2.0D, 0.0D, f1, f4);
            localTessellator.draw();
        }
        GL11.glDisable(32826);
        GL11.glPopMatrix();
    }


    protected ResourceLocation getEntityTexture(Entity p_getEntityTexture_1_)
    {
        return arrowTextures;
    }
}
