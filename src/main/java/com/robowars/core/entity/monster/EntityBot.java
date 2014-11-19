package com.robowars.core.entity.monster;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;

/**
 * Created by thomas on 17/11/14.
 */
public abstract class EntityBot extends EntityMob {
    public EntityBot(World p_i1738_1_) {
        super(p_i1738_1_);
        setHealth(20F);
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
