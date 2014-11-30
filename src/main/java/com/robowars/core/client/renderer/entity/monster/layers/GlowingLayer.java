package com.robowars.core.client.renderer.entity.monster.layers;

import com.robowars.core.client.renderer.entity.monster.MonsterRender;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by thomas on 26/11/2014.
 */
public class GlowingLayer implements LayerRenderer {

    private ResourceLocation glowingTexture;
    private MonsterRender render;

    public GlowingLayer(ResourceLocation glowingTexture, MonsterRender render){
        this.glowingTexture = glowingTexture;
        this.render = render;
    }

    public void doRenderLayer(EntityLivingBase p_177148_1_, float p_177148_2_, float p_177148_3_, float p_177148_4_, float p_177148_5_, float p_177148_6_, float p_177148_7_, float p_177148_8_) {
        this.render.bindTexture(glowingTexture);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        if(p_177148_1_.isInvisible()) {
            GlStateManager.depthMask(false);
        } else {
            GlStateManager.depthMask(true);
        }

        char var9 = '\uf0f0';
        int var10 = var9 % 65536;
        int var11 = var9 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var10 / 1.0F, (float) var11 / 1.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.render.getMainModel().render(p_177148_1_, p_177148_2_, p_177148_3_, p_177148_5_, p_177148_6_, p_177148_7_, p_177148_8_);
        int var12 = p_177148_1_.getBrightnessForRender(p_177148_4_);
        var10 = var12 % 65536;
        var11 = var12 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var10 / 1.0F, (float) var11 / 1.0F);
        this.render.func_177105_a((EntityLiving) p_177148_1_, p_177148_4_);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
