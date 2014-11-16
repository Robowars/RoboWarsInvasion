package com.robowars.core.Entity;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.World;

/**
 * Created by thomas on 16/11/14.
 */
public class EntitylHeavyBotMelee extends EntityAnimal {

    public EntitylHeavyBotMelee(World world) {
        super(world);
        setHealth(10);
    }

    @Override
    protected boolean isAIEnabled() {
        return true;
    }

    @Override
    public EntityAgeable createChild(EntityAgeable entityAgeable) {
        return null;
    }
}
