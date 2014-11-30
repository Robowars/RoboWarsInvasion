package com.robowars.core.entity.monster;

import com.robowars.core.RoboWarsMod;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import net.minecraft.util.DamageSource;



/**
 * Created by thomas on 17/11/14.
 * Modded  by Evydder
 */
public abstract class EntityBot extends EntityMob {

    private int explosionRadius = 1;
    static final int DEATH_TIME= 48;

    public EntityBot(World world) {
        super(world);
        setHealth(50F);
        this.setSize(0.9F, 1.8F);
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, false, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityAnimal.class, false, true));
        this.isImmuneToFire = true;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(.5D);
    }

    @Override
    protected String getLivingSound(){

        return "robowars:mob.bot.say";

    }

    @Override
    protected String getHurtSound()
    {
        return "robowars:mob.bot.hit";
    }

    @Override
    protected String getDeathSound()
    {
        return "robowars:mob.bot.death";
    }

    @Override
    public boolean getCanSpawnHere() {
        return true;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected float getSoundVolume()
    {
        return 0.4F;
    }

    @Override
    public void onDeath(DamageSource cause){
        super.onDeath(cause);
        deathTime-= DEATH_TIME-20;//20 is default time for all entityLivings
    }

    @Override
    public void setDead(){
        super.setDead();
        explode();
    }

    protected void explode(){

        float f = 2.0F;

        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float) this.explosionRadius * f, false);

    }

}
