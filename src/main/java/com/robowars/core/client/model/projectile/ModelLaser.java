package com.robowars.core.client.model.projectile;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Created by thomas on 23/11/2014.
 */
public class ModelLaser extends ModelBase
{
    //fields
    ModelRenderer beam;

    public ModelLaser()
    {
        textureWidth = 64;
        textureHeight = 32;

        beam = new ModelRenderer(this, 1, 1);
        beam.addBox(0F, -0.5F, -0.5F, 9, 1, 1);
        beam.setRotationPoint(-4F, 16F, 0F);
        beam.setTextureSize(64, 32);
        beam.mirror = true;
        setRotation(beam, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        beam.render(f5);
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

