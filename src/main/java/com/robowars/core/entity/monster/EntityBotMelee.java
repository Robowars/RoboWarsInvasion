package com.robowars.core.entity.monster;

import net.minecraft.world.World;

/**
 * Created by thomas on 17/11/14.
 */
public class EntityBotMelee extends EntityBot {
    public EntityBotMelee(World p_i1738_1_) {
        super(p_i1738_1_);
        setAIMoveSpeed(10F);
    }
}
