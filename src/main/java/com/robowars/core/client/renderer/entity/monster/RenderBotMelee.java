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
public class RenderBotMelee extends RenderLiving {

    public static final ResourceLocation texture = new ResourceLocation(RoboWarsMod.MODID, "textures/entity/botmelee/botmelee.png");
    public static final ResourceLocation textureGlow = new ResourceLocation(RoboWarsMod.MODID, "textures/entity/botmelee/botmelee_glow.png");

    public RenderBotMelee(float v) {
        super(new ModelBotMelee(), v);
        setRenderPassModel(new ModelBotMelee());
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }

    @Override
    protected int shouldRenderPass(EntityLivingBase entity, int par1, float par2) {
        if (par1 != 0)
        {
            return -1;
        }
        else {
            this.bindTexture(textureGlow);
            float f1 = 1.0F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

            if (entity.isInvisible()) {
                GL11.glDepthMask(false);
            } else {
                GL11.glDepthMask(true);
            }

            char c0 = 61680;
            int j = c0 % 65536;
            int k = c0 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j / 1.0F, (float) k / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);
            return 1;
        }
    }
}
