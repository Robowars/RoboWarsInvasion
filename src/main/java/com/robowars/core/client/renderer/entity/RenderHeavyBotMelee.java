package com.robowars.core.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Created by thomas on 16/11/14.
 */
public class RenderHeavyBotMelee extends RenderLiving {
    
    public RenderHeavyBotMelee(ModelBase modelBase, float v) {
        super(modelBase, v);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
