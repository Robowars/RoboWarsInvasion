package com.robowars.core.client.renderer.entity.monster;

import com.robowars.core.RoboWarsMod;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Created by thomas on 17/11/14.
 */
public class RenderBotMelee extends RenderLiving {

    public static final ResourceLocation texture = new ResourceLocation(RoboWarsMod.MODID, "textures/entity/botmelee.png");

    public RenderBotMelee(ModelBase p_i1262_1_, float p_i1262_2_) {
        super(p_i1262_1_, p_i1262_2_);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }
}
