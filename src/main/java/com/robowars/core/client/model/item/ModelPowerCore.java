package com.robowars.core.client.model.item;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Created by thomas on 25/11/2014.
 */
public class ModelPowerCore extends ModelBase
{
    //fields
    ModelRenderer plasmachamber;
    ModelRenderer top;
    ModelRenderer middle;
    ModelRenderer bottom;

    public ModelPowerCore()
    {
        textureWidth = 64;
        textureHeight = 32;

        plasmachamber = new ModelRenderer(this, 17, 1);
        plasmachamber.addBox(-1.5F, -1.5F, -1.5F, 3, 5, 3);
        plasmachamber.setRotationPoint(0F, 16F, 0F);
        plasmachamber.setTextureSize(64, 32);
        plasmachamber.mirror = true;
        setRotation(plasmachamber, 0F, 0F, 0F);
        top = new ModelRenderer(this, 0, 8);
        top.addBox(-2F, -1.5F, -2F, 4, 3, 4);
        top.setRotationPoint(0F, 13F, 0F);
        top.setTextureSize(64, 32);
        top.mirror = true;
        setRotation(top, 0F, 0F, 0F);
        middle = new ModelRenderer(this, 0, 16);
        middle.addBox(-2F, -0.5F, -2F, 4, 1, 4);
        middle.setRotationPoint(0F, 17F, 0F);
        middle.setTextureSize(64, 32);
        middle.mirror = true;
        setRotation(middle, 0F, 0F, 0F);
        bottom = new ModelRenderer(this, 0, 0);
        bottom.addBox(-2F, -1.5F, -2F, 4, 3, 4);
        bottom.setRotationPoint(0F, 21F, 0F);
        bottom.setTextureSize(64, 32);
        bottom.mirror = true;
        setRotation(bottom, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        plasmachamber.render(f5);
        top.render(f5);
        middle.render(f5);
        bottom.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }

}
