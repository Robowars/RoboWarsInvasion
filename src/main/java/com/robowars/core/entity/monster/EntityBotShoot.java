package com.robowars.core.entity.monster;

import com.google.common.base.Predicate;
import com.robowars.core.entity.projectile.EntityLaser;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.World;

/**
 * Created by thomas on 17/11/14.
 */
public class EntityBotShoot extends EntityBot implements IRangedAttackMob {
    private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 2.0D,50,50,15f);
    private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityLiving.class, 5D, false);

    public EntityBotShoot(World p_i1738_1_) {
        super(p_i1738_1_);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 30, 10.0F));
        this.tasks.addTask(3, new EntityAIWander(this, 0.40D));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityAgeable.class, 8.0F));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityMob.class, 16.0F));
        this.targetTasks.addTask(3, new AINearestAttackableTargetNonCreeper(this, EntityLiving.class, 10, false, true, IMob.field_175450_e));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.tasks.addTask(8, this.aiArrowAttack);
        //this.tasks.addTask(7, this.aiAttackOnCollide);
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityLiving.class,16.0F));


    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_)
    {

        EntityArrow entityarrow = new EntityArrow(this.worldObj, this, p_82196_1_, 1.6F, (float)(14 - this.worldObj.getDifficulty().getDifficultyId() * 4));
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
        entityarrow.setDamage((double)(p_82196_2_ * 2.0F) + this.rand.nextGaussian() * 0.25D + (double)((float)this.worldObj.getDifficulty().getDifficultyId() * 0.11F));

        if (i > 0)
        {
            entityarrow.setDamage(entityarrow.getDamage() + (double)i * 0.5D + 0.5D);
        }

        if (j > 0)
        {
            entityarrow.setKnockbackStrength(j);
        }

        entityarrow.setDamage(entityarrow.getDamage() + (double)i * 10D + 0.5D);
        this.playSound("robowars:mob.bot.shoot", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.worldObj.spawnEntityInWorld(entityarrow);
    }

/*
    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase entity, float v) {
       /* EntityLaser var3 = new EntityLaser(this.worldObj, this, entity , 1.6F, (float)(14 - this.worldObj.getDifficulty().getDifficultyId()* 4));
        var3.setDamage(10F);

        this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.worldObj.spawnEntityInWorld(var3);

}
*/


    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(50F);
    }

    @Override
    public boolean attackEntityAsMob(Entity p_attackEntityAsMob_1_) {
        return false;
    }

    @Override
    public float getEyeHeight() {
        return super.getEyeHeight();
    }

    static class AINearestAttackableTargetNonCreeper extends EntityAINearestAttackableTarget
    {


        public AINearestAttackableTargetNonCreeper(final EntityCreature p_i45858_1_, Class p_i45858_2_, int p_i45858_3_, boolean p_i45858_4_, boolean p_i45858_5_, final Predicate p_i45858_6_)
        {
            super(p_i45858_1_, p_i45858_2_, p_i45858_3_, p_i45858_4_, p_i45858_5_, p_i45858_6_);
            this.targetEntitySelector = new Predicate()
            {

                public boolean func_180096_a(EntityLivingBase p_180096_1_)
                {
                    if (p_i45858_6_ != null && !p_i45858_6_.apply(p_180096_1_))
                    {
                        return false;
                    }
                    else if (p_180096_1_ instanceof EntityCreeper)
                    {
                        return false;
                    }
                    else
                    {
                        if (p_180096_1_ instanceof EntityPlayer)
                        {
                            double d0 = AINearestAttackableTargetNonCreeper.this.getTargetDistance();

                            if (p_180096_1_.isSneaking())
                            {
                                d0 *= 0.800000011920929D;
                            }

                            if (p_180096_1_.isInvisible())
                            {
                                float f = ((EntityPlayer)p_180096_1_).getArmorVisibility();

                                if (f < 0.1F)
                                {
                                    f = 0.1F;
                                }

                                d0 *= (double)(0.7F * f);
                            }

                            if ((double)p_180096_1_.getDistanceToEntity(p_i45858_1_) > d0)
                            {
                                return false;
                            }
                        }

                        return AINearestAttackableTargetNonCreeper.this.isSuitableTarget(p_180096_1_, false);
                    }
                }
                public boolean apply(Object p_apply_1_)
                {
                    return this.func_180096_a((EntityLivingBase)p_apply_1_);
                }
            };
        }
    }

}
