package com.robowars.core.entity.projectile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

public class EntityLaser
        extends Entity
        implements IProjectile
{
    private int field_145791_d = -1;
    private int field_145792_e = -1;
    private int field_145789_f = -1;
    private Block field_145790_g;
    private int inData;
    private boolean inGround;
    public int canBePickedUp;
    public int arrowShake;
    public Entity shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    private double damage = 2.0D;
    private int knockbackStrength;

    public EntityLaser(World p_i1753_1_)
    {
        super(p_i1753_1_);
        this.renderDistanceWeight = 10.0D;
        setSize(0.5F, 0.5F);
    }

    public EntityLaser(World p_i1754_1_, double p_i1754_2_, double p_i1754_4_, double p_i1754_6_)
    {
        super(p_i1754_1_);
        this.renderDistanceWeight = 10.0D;

        setSize(0.5F, 0.5F);

        setPosition(p_i1754_2_, p_i1754_4_, p_i1754_6_);
        this.yOffset = 0.0F;
    }

    public EntityLaser(World p_i1755_1_, EntityLivingBase p_i1755_2_, EntityLivingBase p_i1755_3_, float p_i1755_4_, float p_i1755_5_)
    {
        super(p_i1755_1_);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = p_i1755_2_;
        if ((p_i1755_2_ instanceof EntityPlayer)) {
            this.canBePickedUp = 1;
        }
        this.posY = (p_i1755_2_.posY + p_i1755_2_.getEyeHeight() - 0.10000000149011612D);

        double d1 = p_i1755_3_.posX - p_i1755_2_.posX;
        double d2 = p_i1755_3_.boundingBox.minY + p_i1755_3_.height / 3.0F - this.posY;
        double d3 = p_i1755_3_.posZ - p_i1755_2_.posZ;
        double d4 = MathHelper.sqrt_double(d1 * d1 + d3 * d3);
        if (d4 < 1.0E-7D) {
            return;
        }
        float f1 = (float)(Math.atan2(d3, d1) * 180.0D / 3.1415927410125732D) - 90.0F;
        float f2 = (float)-(Math.atan2(d2, d4) * 180.0D / 3.1415927410125732D);

        double d5 = d1 / d4;
        double d6 = d3 / d4;
        setLocationAndAngles(p_i1755_2_.posX + d5, this.posY, p_i1755_2_.posZ + d6, f1, f2);
        this.yOffset = 0.0F;

        float f3 = (float)d4 * 0.2F;
        setThrowableHeading(d1, d2 + f3, d3, p_i1755_4_, p_i1755_5_);
    }

    public EntityLaser(World p_i1756_1_, EntityLivingBase p_i1756_2_, float p_i1756_3_)
    {
        super(p_i1756_1_);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = p_i1756_2_;
        if ((p_i1756_2_ instanceof EntityPlayer)) {
            this.canBePickedUp = 1;
        }
        setSize(0.5F, 0.5F);

        setLocationAndAngles(p_i1756_2_.posX, p_i1756_2_.posY + p_i1756_2_.getEyeHeight(), p_i1756_2_.posZ, p_i1756_2_.rotationYaw, p_i1756_2_.rotationPitch);

        this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F;
        this.posY -= 0.10000000149011612D;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F;
        setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;

        this.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F));
        this.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F));
        this.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * 3.1415927F));

        setThrowableHeading(this.motionX, this.motionY, this.motionZ, p_i1756_3_ * 1.5F, 1.0F);
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
    }

    public void setThrowableHeading(double p_setThrowableHeading_1_, double p_setThrowableHeading_3_, double p_setThrowableHeading_5_, float p_setThrowableHeading_7_, float p_setThrowableHeading_8_)
    {
        float f1 = MathHelper.sqrt_double(p_setThrowableHeading_1_ * p_setThrowableHeading_1_ + p_setThrowableHeading_3_ * p_setThrowableHeading_3_ + p_setThrowableHeading_5_ * p_setThrowableHeading_5_);

        p_setThrowableHeading_1_ /= f1;
        p_setThrowableHeading_3_ /= f1;
        p_setThrowableHeading_5_ /= f1;

        p_setThrowableHeading_1_ += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * p_setThrowableHeading_8_;
        p_setThrowableHeading_3_ += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * p_setThrowableHeading_8_;
        p_setThrowableHeading_5_ += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * p_setThrowableHeading_8_;

        p_setThrowableHeading_1_ *= p_setThrowableHeading_7_;
        p_setThrowableHeading_3_ *= p_setThrowableHeading_7_;
        p_setThrowableHeading_5_ *= p_setThrowableHeading_7_;

        this.motionX = p_setThrowableHeading_1_;
        this.motionY = p_setThrowableHeading_3_;
        this.motionZ = p_setThrowableHeading_5_;

        float f2 = MathHelper.sqrt_double(p_setThrowableHeading_1_ * p_setThrowableHeading_1_ + p_setThrowableHeading_5_ * p_setThrowableHeading_5_);

        this.prevRotationYaw = (this.rotationYaw = (float)(Math.atan2(p_setThrowableHeading_1_, p_setThrowableHeading_5_) * 180.0D / 3.1415927410125732D));
        this.prevRotationPitch = (this.rotationPitch = (float)(Math.atan2(p_setThrowableHeading_3_, f2) * 180.0D / 3.1415927410125732D));
        this.ticksInGround = 0;
    }

    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double p_setPositionAndRotation2_1_, double p_setPositionAndRotation2_3_, double p_setPositionAndRotation2_5_, float p_setPositionAndRotation2_7_, float p_setPositionAndRotation2_8_, int p_setPositionAndRotation2_9_)
    {
        setPosition(p_setPositionAndRotation2_1_, p_setPositionAndRotation2_3_, p_setPositionAndRotation2_5_);
        setRotation(p_setPositionAndRotation2_7_, p_setPositionAndRotation2_8_);
    }

    @SideOnly(Side.CLIENT)
    public void setVelocity(double p_setVelocity_1_, double p_setVelocity_3_, double p_setVelocity_5_)
    {
        this.motionX = p_setVelocity_1_;
        this.motionY = p_setVelocity_3_;
        this.motionZ = p_setVelocity_5_;
        if ((this.prevRotationPitch == 0.0F) && (this.prevRotationYaw == 0.0F))
        {
            float f = MathHelper.sqrt_double(p_setVelocity_1_ * p_setVelocity_1_ + p_setVelocity_5_ * p_setVelocity_5_);
            this.prevRotationYaw = (this.rotationYaw = (float)(Math.atan2(p_setVelocity_1_, p_setVelocity_5_) * 180.0D / 3.1415927410125732D));
            this.prevRotationPitch = (this.rotationPitch = (float)(Math.atan2(p_setVelocity_3_, f) * 180.0D / 3.1415927410125732D));
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    public void onUpdate()
    {
        super.onUpdate();
        if ((this.prevRotationPitch == 0.0F) && (this.prevRotationYaw == 0.0F))
        {
            float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = (this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / 3.1415927410125732D));
            this.prevRotationPitch = (this.rotationPitch = (float)(Math.atan2(this.motionY, f1) * 180.0D / 3.1415927410125732D));
        }
        Block localBlock = this.worldObj.getBlock(this.field_145791_d, this.field_145792_e, this.field_145789_f);
        if (localBlock.getMaterial() != Material.air)
        {
            localBlock.setBlockBoundsBasedOnState(this.worldObj, this.field_145791_d, this.field_145792_e, this.field_145789_f);
            AxisAlignedBB localAxisAlignedBB = localBlock.getCollisionBoundingBoxFromPool(this.worldObj, this.field_145791_d, this.field_145792_e, this.field_145789_f);
            if ((localAxisAlignedBB != null) && (localAxisAlignedBB.isVecInside(Vec3.createVectorHelper(this.posX, this.posY, this.posZ)))) {
                this.inGround = true;
            }
        }
        if (this.arrowShake > 0) {
            this.arrowShake -= 1;
        }
        if (this.inGround)
        {
            int i = this.worldObj.getBlockMetadata(this.field_145791_d, this.field_145792_e, this.field_145789_f);
            if ((localBlock != this.field_145790_g) || (i != this.inData))
            {
                this.inGround = false;

                this.motionX *= this.rand.nextFloat() * 0.2F;
                this.motionY *= this.rand.nextFloat() * 0.2F;
                this.motionZ *= this.rand.nextFloat() * 0.2F;
                this.ticksInGround = 0;
                this.ticksInAir = 0;
                return;
            }
            this.ticksInGround += 1;
            if (this.ticksInGround == 1200) {
                setDead();
            }
            return;
        }
        this.ticksInAir += 1;


        Vec3 localVec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 localVec32 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition localMovingObjectPosition1 = this.worldObj.func_147447_a(localVec31, localVec32, false, true, false);

        localVec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        localVec32 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        if (localMovingObjectPosition1 != null) {
            localVec32 = Vec3.createVectorHelper(localMovingObjectPosition1.hitVec.xCoord, localMovingObjectPosition1.hitVec.yCoord, localMovingObjectPosition1.hitVec.zCoord);
        }
        Object localObject1 = null;
        List localList = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
        double d1 = 0.0D;
        Object localObject2;
        for (int j = 0; j < localList.size(); j++)
        {
            Entity localEntity = (Entity)localList.get(j);
            if ((localEntity.canBeCollidedWith()) && ((localEntity != this.shootingEntity) || (this.ticksInAir >= 5)))
            {
                float f5 = 0.3F;
                localObject2 = localEntity.boundingBox.expand(f5, f5, f5);
                MovingObjectPosition localMovingObjectPosition2 = ((AxisAlignedBB)localObject2).calculateIntercept(localVec31, localVec32);
                if (localMovingObjectPosition2 != null)
                {
                    double d2 = localVec31.distanceTo(localMovingObjectPosition2.hitVec);
                    if ((d2 < d1) || (d1 == 0.0D))
                    {
                        localObject1 = localEntity;
                        d1 = d2;
                    }
                }
            }
        }
        if (localObject1 != null) {
            localMovingObjectPosition1 = new MovingObjectPosition((Entity) localObject1);
        }
        if ((localMovingObjectPosition1 != null) && (localMovingObjectPosition1.entityHit != null) && ((localMovingObjectPosition1.entityHit instanceof EntityPlayer)))
        {
            EntityPlayer localEntityPlayer = (EntityPlayer)localMovingObjectPosition1.entityHit;
            if ((localEntityPlayer.capabilities.disableDamage) || (((this.shootingEntity instanceof EntityPlayer)) && (!((EntityPlayer)this.shootingEntity).canAttackPlayer(localEntityPlayer)))) {
                localMovingObjectPosition1 = null;
            }
        }
        float f7;
        if (localMovingObjectPosition1 != null)
        {
            float f2;
            if (localMovingObjectPosition1.entityHit != null)
            {
                f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                int m = MathHelper.ceiling_double_int(f2 * this.damage);
                if (getIsCritical()) {
                    m += this.rand.nextInt(m / 2 + 2);
                }
                if ((isBurning()) && (!(localMovingObjectPosition1.entityHit instanceof EntityEnderman))) {
                    localMovingObjectPosition1.entityHit.setFire(5);
                }
                if (localMovingObjectPosition1.entityHit.attackEntityFrom(new DamageSource("EntityLaser"), m))
                {
                    if ((localMovingObjectPosition1.entityHit instanceof EntityLivingBase))
                    {
                        localObject2 = (EntityLivingBase)localMovingObjectPosition1.entityHit;
                        if (!this.worldObj.isRemote) {
                            ((EntityLivingBase)localObject2).setArrowCountInEntity(((EntityLivingBase)localObject2).getArrowCountInEntity() + 1);
                        }
                        if (this.knockbackStrength > 0)
                        {
                            f7 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
                            if (f7 > 0.0F) {
                                localMovingObjectPosition1.entityHit.addVelocity(this.motionX * this.knockbackStrength * 0.6000000238418579D / f7, 0.1D, this.motionZ * this.knockbackStrength * 0.6000000238418579D / f7);
                            }
                        }
                        if ((this.shootingEntity != null) && ((this.shootingEntity instanceof EntityLivingBase)))
                        {
                            EnchantmentHelper.func_151384_a((EntityLivingBase)localObject2, this.shootingEntity);
                            EnchantmentHelper.func_151385_b((EntityLivingBase)this.shootingEntity, (Entity)localObject2);
                        }
                        if ((this.shootingEntity != null) && (localMovingObjectPosition1.entityHit != this.shootingEntity) && ((localMovingObjectPosition1.entityHit instanceof EntityPlayer)) && ((this.shootingEntity instanceof EntityPlayerMP))) {
                            ((EntityPlayerMP)this.shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
                        }
                    }
                    playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    if (!(localMovingObjectPosition1.entityHit instanceof EntityEnderman)) {
                        setDead();
                    }
                }
                else
                {
                    this.motionX *= -0.10000000149011612D;
                    this.motionY *= -0.10000000149011612D;
                    this.motionZ *= -0.10000000149011612D;
                    this.rotationYaw += 180.0F;
                    this.prevRotationYaw += 180.0F;
                    this.ticksInAir = 0;
                }
            }
            else
            {
                this.field_145791_d = localMovingObjectPosition1.blockX;
                this.field_145792_e = localMovingObjectPosition1.blockY;
                this.field_145789_f = localMovingObjectPosition1.blockZ;
                this.field_145790_g = this.worldObj.getBlock(this.field_145791_d, this.field_145792_e, this.field_145789_f);
                this.inData = this.worldObj.getBlockMetadata(this.field_145791_d, this.field_145792_e, this.field_145789_f);
                this.motionX = ((float)(localMovingObjectPosition1.hitVec.xCoord - this.posX));
                this.motionY = ((float)(localMovingObjectPosition1.hitVec.yCoord - this.posY));
                this.motionZ = ((float)(localMovingObjectPosition1.hitVec.zCoord - this.posZ));
                f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                this.posX -= this.motionX / f2 * 0.05000000074505806D;
                this.posY -= this.motionY / f2 * 0.05000000074505806D;
                this.posZ -= this.motionZ / f2 * 0.05000000074505806D;

                playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                this.inGround = true;
                this.arrowShake = 7;
                setIsCritical(false);
                if (this.field_145790_g.getMaterial() != Material.air) {
                    this.field_145790_g.onEntityCollidedWithBlock(this.worldObj, this.field_145791_d, this.field_145792_e, this.field_145789_f, this);
                }
            }
        }
        if (getIsCritical()) {
            for (int k = 0; k < 4; k++) {
                this.worldObj.spawnParticle("crit", this.posX + this.motionX * k / 4.0D, this.posY + this.motionY * k / 4.0D, this.posZ + this.motionZ * k / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ);
            }
        }
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;

        float f3 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = ((float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / 3.1415927410125732D));
        this.rotationPitch = ((float)(Math.atan2(this.motionY, f3) * 180.0D / 3.1415927410125732D));
        while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
            this.prevRotationPitch -= 360.0F;
        }
        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }
        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }
        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }
        this.rotationPitch = (this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F);
        this.rotationYaw = (this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F);

        float f4 = 0.99F;
        float f6 = 0.05F;
        if (isInWater())
        {
            for (int n = 0; n < 4; n++)
            {
                f7 = 0.25F;
                this.worldObj.spawnParticle("bubble", this.posX - this.motionX * f7, this.posY - this.motionY * f7, this.posZ - this.motionZ * f7, this.motionX, this.motionY, this.motionZ);
            }
            f4 = 0.8F;
        }
        if (isWet()) {
            extinguish();
        }
        this.motionX *= f4;
        this.motionY *= f4;
        this.motionZ *= f4;
        this.motionY -= f6;

        setPosition(this.posX, this.posY, this.posZ);

        func_145775_I();
    }

    public void writeEntityToNBT(NBTTagCompound p_writeEntityToNBT_1_)
    {
        p_writeEntityToNBT_1_.setShort("xTile", (short)this.field_145791_d);
        p_writeEntityToNBT_1_.setShort("yTile", (short)this.field_145792_e);
        p_writeEntityToNBT_1_.setShort("zTile", (short)this.field_145789_f);
        p_writeEntityToNBT_1_.setShort("life", (short)this.ticksInGround);
        p_writeEntityToNBT_1_.setByte("inTile", (byte)Block.getIdFromBlock(this.field_145790_g));
        p_writeEntityToNBT_1_.setByte("inData", (byte)this.inData);
        p_writeEntityToNBT_1_.setByte("shake", (byte)this.arrowShake);
        p_writeEntityToNBT_1_.setByte("inGround", (byte)(this.inGround ? 1 : 0));
        p_writeEntityToNBT_1_.setByte("pickup", (byte)this.canBePickedUp);
        p_writeEntityToNBT_1_.setDouble("damage", this.damage);
    }

    public void readEntityFromNBT(NBTTagCompound p_readEntityFromNBT_1_)
    {
        this.field_145791_d = p_readEntityFromNBT_1_.getShort("xTile");
        this.field_145792_e = p_readEntityFromNBT_1_.getShort("yTile");
        this.field_145789_f = p_readEntityFromNBT_1_.getShort("zTile");
        this.ticksInGround = p_readEntityFromNBT_1_.getShort("life");
        this.field_145790_g = Block.getBlockById(p_readEntityFromNBT_1_.getByte("inTile") & 0xFF);
        this.inData = (p_readEntityFromNBT_1_.getByte("inData") & 0xFF);
        this.arrowShake = (p_readEntityFromNBT_1_.getByte("shake") & 0xFF);
        this.inGround = (p_readEntityFromNBT_1_.getByte("inGround") == 1);
        if (p_readEntityFromNBT_1_.hasKey("damage", 99)) {
            this.damage = p_readEntityFromNBT_1_.getDouble("damage");
        }
        if (p_readEntityFromNBT_1_.hasKey("pickup", 99)) {
            this.canBePickedUp = p_readEntityFromNBT_1_.getByte("pickup");
        } else if (p_readEntityFromNBT_1_.hasKey("player", 99)) {
            this.canBePickedUp = (p_readEntityFromNBT_1_.getBoolean("player") ? 1 : 0);
        }
    }

    public void onCollideWithPlayer(EntityPlayer p_onCollideWithPlayer_1_)
    {
        if ((this.worldObj.isRemote) || (!this.inGround) || (this.arrowShake > 0)) {
            return;
        }
        int i = (this.canBePickedUp == 1) || ((this.canBePickedUp == 2) && (p_onCollideWithPlayer_1_.capabilities.isCreativeMode)) ? 1 : 0;
        if ((this.canBePickedUp == 1) &&
            (!p_onCollideWithPlayer_1_.inventory.addItemStackToInventory(new ItemStack(Items.arrow, 1)))) {
            i = 0;
        }
        if (i != 0)
        {
            playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            p_onCollideWithPlayer_1_.onItemPickup(this, 1);
            setDead();
        }
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    public void setDamage(double p_setDamage_1_)
    {
        this.damage = p_setDamage_1_;
    }

    public double getDamage()
    {
        return this.damage;
    }

    public void setKnockbackStrength(int p_setKnockbackStrength_1_)
    {
        this.knockbackStrength = p_setKnockbackStrength_1_;
    }

    public boolean canAttackWithItem()
    {
        return false;
    }

    public void setIsCritical(boolean p_setIsCritical_1_)
    {
        int i = this.dataWatcher.getWatchableObjectByte(16);
        if (p_setIsCritical_1_) {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(i | 0x1)));
        } else {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(i & 0xFFFFFFFE)));
        }
    }

    public boolean getIsCritical()
    {
        int i = this.dataWatcher.getWatchableObjectByte(16);
        return (i & 0x1) != 0;
    }
}
