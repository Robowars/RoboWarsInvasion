package com.robowars.core.entity.monster;

import com.robowars.core.RoboWarsMod;
import com.robowars.core.client.renderer.entity.monster.RenderBot.Sparks;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



/**
 * Created by thomas on 17/11/14.
 * Modded  by Evydder
 */
public abstract class EntityBot extends EntityMob {

    private int explosionRadius = 1;
    static final int DEATH_TIME= 48;
    private int deathTicks = DEATH_TIME;

    public EntityBot(World world) {
        super(world);
        setHealth(50F);
        this.setSize(0.9F, 1.8F);
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, false, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityAnimal.class, false, true));
        this.isImmuneToFire = true;
    }
    
    @SideOnly(Side.CLIENT)
    public Sparks sparks= new Sparks();
    float lasthp;
    @Override
    public void onUpdate(){
    	super.onUpdate();
    	this.hurtTime= 0;
    	if(worldObj.isRemote){//client only spark fx
    		float curhp= getHealth();
    		float dHealth= curhp-lasthp;
    		lasthp= curhp;
    		if(dHealth<0){
    			sparks.make((int)-dHealth*5);
    		}
    		sparks.update();
    	}
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
        //deathTime-= DEATH_TIME-20;//20 is default time for all entityLivings
    }

    @Override
    public void setDead(){
        explode();
        super.setDead();
    }

    @Override
    protected void onDeathUpdate() {
        deathTicks--;
        if(ticksExisted%3==0)//less frequent
        	smokeSigare();
        if (deathTicks<=-20)
            setDead();

    	if(worldObj.isRemote){
    		sparks.make(1);
    	}
    }

    protected void smokeSigare(){
        this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0.0D, 0.0D, 0.0D);
    }

    protected void explode(){
        float f = 2.0F;
        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float) this.explosionRadius * f, false);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!onGround && this.motionY < 0.0D)
            motionY *=0.6;
    }
}
