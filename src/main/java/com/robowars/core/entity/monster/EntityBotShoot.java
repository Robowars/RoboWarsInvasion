package com.robowars.core.entity.monster;

import com.robowars.core.entity.projectile.EntityLaser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by thomas on 17/11/14.
 */
public class EntityBotShoot extends EntityBot implements IRangedAttackMob {
    public EntityBotShoot(World p_i1738_1_) {
        super(p_i1738_1_);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 30, 10.0F));
        this.tasks.addTask(2, new EntityAIWander(this, 0.40D));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(3, new EntityAILookIdle(this));
    }

    @Override
    protected boolean isAIEnabled() {
        return true;
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase entity, float v) {
        EntityLaser var3 = new EntityLaser(this.worldObj, this, entity , 1.6F, (float)(14 - this.worldObj.difficultySetting.getDifficultyId() * 4));
        var3.setDamage(10F);

        this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.worldObj.spawnEntityInWorld(var3);

    }

    @Override
    public boolean attackEntityAsMob(Entity p_attackEntityAsMob_1_) {
        return false;
    }
}
