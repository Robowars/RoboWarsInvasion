package com.robowars.core.client.renderer.entity.monster;

import com.robowars.core.RoboWarsMod;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Created by thomas on 17/11/14.
 */
public class RenderBotShoot extends RenderLiving {

    public static final ResourceLocation texture = new ResourceLocation(RoboWarsMod.MODID, "textures/entity/botshoot/botshoot.png");

    public RenderBotShoot(ModelBase modelBase, float v) {
        super(modelBase, v);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }
}
