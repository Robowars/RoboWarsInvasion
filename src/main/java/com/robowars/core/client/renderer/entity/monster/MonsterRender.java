package com.robowars.core.client.renderer.entity.monster;

import com.robowars.core.client.renderer.entity.monster.layers.GlowingLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

/**
 * Created by thomas on 26/11/2014.
 */
public abstract class MonsterRender extends RenderLiving {

    private ResourceLocation glowingTexture;

    public MonsterRender(ModelBase modelBase, float v, ResourceLocation glowingTexture) {
        super(Minecraft.getMinecraft().getRenderManager(), modelBase, v);
        addLayer(new GlowingLayer(glowingTexture, this));
    }

    protected int shouldRenderPass(EntityLivingBase entity, int par1, float par2) {
        return 0;
    }
}
