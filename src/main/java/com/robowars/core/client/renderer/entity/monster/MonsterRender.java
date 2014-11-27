package com.robowars.core.client.renderer.entity.monster;

import com.robowars.core.client.renderer.entity.monster.layers.MonsterLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by thomas on 26/11/2014.
 */
public abstract class MonsterRender extends RenderLiving {

    private ResourceLocation glowingTexture;

    public MonsterRender(ModelBase modelBase, float v, ResourceLocation glowingTexture) {
        super(Minecraft.getMinecraft().getRenderManager(), modelBase, v);
        addLayer(new MonsterLayer(glowingTexture, this));
    }

    protected int shouldRenderPass(EntityLivingBase entity, int par1, float par2) {
        return 0;
    }
}
