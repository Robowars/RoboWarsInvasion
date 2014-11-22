package com.robowars.core.entity.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

/**
 * Created by thomas on 16/11/14.
 */
public class EntityHeavyBotMelee extends EntityBotMelee {

    public EntityHeavyBotMelee(World world) {
        super(world);
        setHealth(80F);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(15.0F);
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(80F);
    }
}
