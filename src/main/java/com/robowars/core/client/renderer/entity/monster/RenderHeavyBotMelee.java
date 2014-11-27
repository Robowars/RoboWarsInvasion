package com.robowars.core.client.renderer.entity.monster;

import com.robowars.core.RoboWarsMod;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Created by thomas on 16/11/14.
 */
public class RenderHeavyBotMelee extends MonsterRender {

    public static final ResourceLocation texture = new ResourceLocation(RoboWarsMod.MODID, "textures/entity/heavybotmelee/heavybotmelee.png");
    public static final ResourceLocation textureGlow = new ResourceLocation(RoboWarsMod.MODID, "textures/entity/heavybotmelee/heavybotmelee_glow.png");


    public RenderHeavyBotMelee(ModelBase modelBase, float v) {
        super(modelBase, v, textureGlow);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }

}
