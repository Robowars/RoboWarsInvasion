package com.robowars.core.client.renderer.entity.monster;

import com.robowars.core.RoboWarsMod;
import com.robowars.core.client.model.entity.ModelBotShoot;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Created by thomas on 17/11/14.
 */
public class RenderBotShoot extends RenderBot {

    public static final ResourceLocation texture = new ResourceLocation(RoboWarsMod.MODID, "textures/entity/botshoot/botshoot.png");
    public static final ResourceLocation textureGlow = new ResourceLocation(RoboWarsMod.MODID, "textures/entity/botshoot/botshoot_glow.png");

    public RenderBotShoot() {
        super(new ModelBotShoot(), 0.5F, textureGlow);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }
}
