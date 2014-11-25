package com.robowars.core.client.renderer.item;

import com.robowars.core.RoboWarsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

/**
 * Created by thomas on 25/11/2014.
 */
public class ItemRender implements IItemRenderer {

    private ModelBase model;
    private TextureManager manager;
    private ResourceLocation texture;

    public ItemRender(ModelBase model){
        this.model = model;
        this.manager = Minecraft.getMinecraft().renderEngine;
        this.texture = new ResourceLocation(RoboWarsMod.MODID, "textures/items/powercore.png");
    }

    @Override
    public boolean handleRenderType(ItemStack itemStack, ItemRenderType itemRenderType) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType itemRenderType, ItemStack itemStack, ItemRendererHelper itemRendererHelper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType itemRenderType, ItemStack itemStack, Object... objects) {
        GL11.glPushMatrix();
        manager.bindTexture(texture);
        if (itemRenderType == ItemRenderType.ENTITY)
        {
            GL11.glScalef(0.8F, -0.8F, -0.8F);
            GL11.glTranslatef(-0F, -2.75F, -0F);
            model.render(null, 0, 0, 0, 0, 0, 0.125F);
        }
        else if (itemRenderType == ItemRenderType.INVENTORY)
        {
            GL11.glScalef(1F, -1F, -1F);
            GL11.glTranslatef(-0F, -2.1F, -0F);
            model.render(null, 0, 0, 0, 0, 0, 0.125F);
        }
        else if (itemRenderType == ItemRenderType.EQUIPPED || itemRenderType == ItemRenderType.EQUIPPED_FIRST_PERSON )
        {
            GL11.glScalef(1F, -1F, -1F);
            GL11.glTranslatef(0.5F, -3.25F, -0.5F);
            model.render(null, 0, 0, 0, 0, 0, 0.125F);
        }
        GL11.glPopMatrix();
    }
}
