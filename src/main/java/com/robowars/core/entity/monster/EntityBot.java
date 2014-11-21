package com.robowars.core.entity.monster;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;

/**
 * Created by thomas on 17/11/14.
 */
public abstract class EntityBot extends EntityMob {
    public EntityBot(World world) {
        super(world);
        setHealth(50F);
        this.setSize(0.9F, 1.3F);
    }

    @Override
    public boolean getCanSpawnHere() {
        return true;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }
}
