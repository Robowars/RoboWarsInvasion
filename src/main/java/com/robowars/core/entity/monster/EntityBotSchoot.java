package com.robowars.core.entity.monster;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.World;

/**
 * Created by thomas on 17/11/14.
 */
public class EntityBotSchoot extends EntityBot implements IRangedAttackMob {
    public EntityBotSchoot(World p_i1738_1_) {
        super(p_i1738_1_);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 60, 10.0F));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(3, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
    }

    @Override
    protected boolean isAIEnabled() {
        return true;
    }

    @Override
    public void onEntityUpdate() {

        super.onEntityUpdate();
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase entityLivingBase, float v) {
        EntityArrow var3 = new EntityArrow(this.worldObj, this, entityLivingBase , 1.6F, (float)(14 - this.worldObj.difficultySetting.getDifficultyId() * 4));
        int var4 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
        int var5 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
        var3.setDamage((double)(v * 2.0F) + this.rand.nextGaussian() * 0.25D + (double)((float)this.worldObj.difficultySetting.getDifficultyId() * 0.11F));
        if(var4 > 0) {
            var3.setDamage(var3.getDamage() + (double)var4 * 0.5D + 0.5D);
        }

        if(var5 > 0) {
            var3.setKnockbackStrength(var5);
        }

        this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.worldObj.spawnEntityInWorld(var3);
    }

    @Override
    public boolean attackEntityAsMob(Entity p_attackEntityAsMob_1_) {
        return false;
    }
}
