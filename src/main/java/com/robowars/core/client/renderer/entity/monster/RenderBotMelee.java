package com.robowars.core.client.renderer.entity.monster;

import com.robowars.core.RoboWarsMod;
import com.robowars.core.client.model.entity.ModelBotMelee;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by thomas on 17/11/14.
 */
public class RenderBotMelee extends RenderBot{

    public static final ResourceLocation texture = new ResourceLocation(RoboWarsMod.MODID, "textures/entity/botmelee/botmelee.png");
    public static final ResourceLocation textureGlow = new ResourceLocation(RoboWarsMod.MODID, "textures/entity/botmelee/botmelee_glow.png");

    public RenderBotMelee() {
        super(new ModelBotMelee(), 0.5F, textureGlow);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }
}
