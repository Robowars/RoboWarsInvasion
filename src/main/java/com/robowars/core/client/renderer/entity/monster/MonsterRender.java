package com.robowars.core.client.renderer.entity.monster;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by thomas on 26/11/2014.
 */
public abstract class MonsterRender extends RenderLiving {

    private ResourceLocation glowingTexture;

    public MonsterRender(ModelBase modelBase, float v) {
        super(modelBase, v);
    }

    public void setGlowingTexture(ResourceLocation texture){
        this.glowingTexture = texture;
    }

    @Override
    protected int shouldRenderPass(EntityLivingBase entity, int par1, float par2) {
        if (par1 != 0)
        {
            return -1;
        }
        else {
            this.bindTexture(glowingTexture);
            float f1 = 1.0F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            //GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

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
