package com.robowars.core.entity.projectile;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import static java.lang.Math.*;

public class EntityLaser extends Entity {

	/**synched between c-s so bullets fly the same direction*/
	private Random r;

	public float dmg;
	public double speed0=0;
	/**For not adding to v[1] when on the ground*/
	public boolean wasCollided=false;
	public boolean isFirstCollision=true;

	public Entity owner;
	public boolean firstTimeSpawning=true;

	public double[] v= new double[3];


    public EntityLaser(World worldIn) {
        super(worldIn);
        r= worldIn.rand;
    }
	public EntityLaser(Entity aimer,/*Barrel barrel,*/double s, double deviation, float dmg){
		this(aimer.worldObj);
		this.ignoreFrustumCheck= true;
		this.preventEntitySpawning = false;	
		this.owner= aimer;
		//barrel.calculatePosAndLook();
		//setPos(barrel.muzzleEndTransformed.x, barrel.muzzleEndTransformed.y, barrel.muzzleEndTransformed.z);
		setPos(aimer.posX, aimer.posY, aimer.posZ);
//		this.rotationYaw= barrel.aimer.rotationYaw;
//		this.rotationPitch= -barrel.aimer.rotationPitch;
		this.rotationYaw= aimer.rotationYaw;
		this.rotationPitch= -aimer.rotationPitch;

		float cosp= MathHelper.cos(rotationPitch);
		Vector3f look= new Vector3f(MathHelper.cos(rotationYaw)*cosp, MathHelper.sin(rotationPitch), MathHelper.sin(rotationYaw)*cosp );
		this.v[0]= look.x*s;
		this.v[1]= look.y*s;
		this.v[2]= look.z*s;
		v[0]+= aimer.motionX;
		v[1]+= aimer.motionX;
		v[2]+= aimer.motionZ;
		this.deviate(deviation);
//		this.v[0]= barrel.lookdir.x*s;
//		this.v[1]= barrel.lookdir.y*s;
//		this.v[2]= barrel.lookdir.z*s;
//		v[0]+= barrel.getEntityMountedTo().motionX;
//		v[1]+= barrel.getEntityMountedTo().motionX;
//		v[2]+= barrel.getEntityMountedTo().motionZ;

		this.speed0= s;

		this.dmg=dmg;

//		if(this.getClass().equals(EntityProjectile.class))
//			worldObj.spawnEntityInWorld(this);

//		if(worldObj.isRemote)
//			posY-=1.75;//client has the wrong position for some reason
		
        this.playSound("robowars:mob.bot.shoot", 1.0F, 1.0F / ((float)Math.random() * 0.4F + 0.8F));
	}

    @Override
    protected void entityInit(){}

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbtTagCompound) {

    }
    @Override
    protected void writeEntityToNBT(NBTTagCompound nbtTagCompound) {

    }

	@Override
	public boolean isInRangeToRenderDist(double par1){return par1<200*200;}


//	@Override
//	@Deprecated
//	public void setPosition(double x, double y, double z){
//		super.setPosition(x, y, z);
//	}
//	@Override
//	@Deprecated
//	public void moveEntity(double vx, double vy, double vz){}

	public void setPos(double x, double y, double z){
//		this.prevPosX= this.posX;
//		this.prevPosY= this.posY;
//		this.prevPosZ= this.posZ;
//		this.posX= x;
//		this.posY= y;
//		this.posZ= z;
		setPosition(x, y, z);
		//this.getBoundingBox()...setBounds(-.25, -.25, -.25, .25, .25, .25);
		//this.getBoundingBox().offset(posX, posY, posZ);
	}
	@Override
	public boolean canBeCollidedWith(){
		return false;
	}
	
	
	
	

	/**The block ID this is trying to enter, or if done moving, has entered*/
	Block entering;
	/**Amount of movement still needing to be done this tick*/
	Vector3f remain= new Vector3f();
	/**one of the 3 will be distance to the next block*/
	Vector3f step= new Vector3f();	
	/**move the projectile along no more than 1 block at a time
	 * so as to not get stuck in walls and stuff*/
	protected void moveEntity(){
		noClip= true;
		if(this.noClip){
			this.posX+=v[0];
			this.posY+=v[1];
			this.posZ+=v[2];
			setPos(posX, posY, posZ);
			return;
		}

		remain.x= (float) this.v[0];
		remain.y= (float) this.v[1];
		remain.z= (float) this.v[2];

		//find distance to next block being moved into
		//find out the magnitude of the step and calculate step vector
		//do not assume that a bullet never rests perfectly on the edge of a block, you will crash
		//step vector < remaining ? continue :  move directly using remain (will never enter new voxel); break
		//get id of block whos edge this just moved onto
		//add step vector to position, subtract from remaining motion
		//check for effects (ricochet, slow, pierce), apply them, not to the current step
		//remember when reversing a motion component to reverse all of its factors, except step
		//when in doubt use abs()
		while(true){
			//prevent /0
			if(v[0]==0)
				v[0]=1e-9;
			if(v[1]==0)
				v[1]=1e-9;
			if(v[2]==0)
				v[2]=1e-9;

			//distance to next block
			final double[] dist= new double[3];
			dist[0]= ( v[0]>0 ? ceil(posX):floor(posX) ) - posX;
			dist[1]= ( v[1]>0 ? ceil(posY):floor(posY) ) - posY;
			dist[2]= ( v[2]>0 ? ceil(posZ):floor(posZ) ) - posZ;
			
			final double ta= posX, tb= posY, tc= posZ;
			//			//t=d/s; time to enter the next block
			final double[] t= new double[3];
			t[0]= abs(dist[0]/v[0]);
			t[1]= abs(dist[1]/v[1]);
			t[2]= abs(dist[2]/v[2]);
			//
			//			//			System.out.println(t[1]+" "+t[2]);
			//
			//			//find which time is smallest, thus block being moved into
			/**0 x 1 y 2 z*/
			final int dir;
			
			if(t[0]<t[1] && t[0]<t[2])
				dir=0;
			else if(t[1]<t[0] && t[1]<t[2])
				dir=1;
			else if(t[2]<t[0] && t[2]<t[1])
				dir=2;
			else{dir=0;}

			//is not moving onto an edge
			//thus not considered entering a new block; considered to be moving into a new block while on its edge
			//thus is done with move looping
			float remdir= dir==0? remain.x : dir==1? remain.y : remain.z;
			if(v[dir]>0? remdir<=0 : remdir>=0){
//			if(v[dir]>0? remain[dir]<dist[dir] : remain[dir]>dist[dir]){
//				posX+=remain[0];
//				posY+=remain[1];
//				posZ+=remain[2];
				break;
			}
			
			step= new Vector3f((float)v[0],(float)v[1],(float)v[2]);
			step.normalise();
			step.scale(.03f);
			
			posX+= step.x;
			posY+= step.y;
			posZ+= step.z;

			remain.x-= step.x;
			remain.y-= step.y;
			remain.z-= step.z;

			

//			switch(dir){//make sure it is moved perfectly onto the edge
//			case 0:
//				posX= (int) floor(posX+0.1);//nudge it to make sure its truncated properly
//				break;
//			case 1:
//				posY= (int) floor(posY+0.1);
//				break;
//			case 2:
//				posZ= (int) floor(posZ+0.1);
//				break;				
//			}

			entering= worldObj.getBlockState(new BlockPos(this)).getBlock();
						
			//check for effects (ricochet, slow, pierce, spall), apply them
			//remember when reversing a dironent to reverse all of its factors except step
			//does not affect the current step
			wasCollided=false;
			switch(getBlockBulletEffect(entering)){
			
			case 0://air
				isCollided=false;
				break;
			case 1://solid
				isCollided=true;
				ricochet(dir, entering);
				break;
			case 2://liquid
				isCollided=false;
				remain.x*=0.9;
				remain.y*=0.9;
				remain.z*=0.9;
				v[0]*=0.9;
				v[1]*=0.9;
				v[2]*=0.9;
				break;
			case 3://breakable
				//TODO allow bots to break blocks?
//				if(owner!=null && owner instanceof EntityPlayer 
//				&& getSpeed() > 0.4
//				&& worldObj..canMineBlock( (EntityPlayer)this.owner, (int)(posX+step[0]),(int)(posY+step[1]),(int)(posZ+step[2])))
//					worldObj.setBlock((int)(posX+step[0]),(int)(posY+step[1]),(int)(posZ+step[2]), Blocks.air);
//				isCollided=true;
//				remain[0]*=0.4;//reduce speed
//				remain[1]*=0.4;
//				remain[2]*=0.4;
//				v[0]*=0.4;
//				v[1]*=0.4;
//				v[2]*=0.4;
//				return;
				
			}

			if(isCollided && isFirstCollision){
//				id= worldObj.getBlockId((int)(posX),(int)(posY),(int)(posZ));
//				if(dir!=1)
//				System.out.println(dir);
				isFirstCollision=false;
				onFirstCollision();
			}

			//get off of the edge and into the next (or incase ricochet, back into the previous) block
//			final double a=  copySign(0.01, v[0]);
//			posX+= copySign(0.001, v[0]);
//			posY+= copySign(0.001, v[1]);
//			posZ+= copySign(0.001, v[2]);
		}
		setPos(posX, posY, posZ);
		if(posY<-2){
			System.out.println("SHIT WENT WRONG");
			this.setDead();}
	}
	/**@return 0 air 1 solid 2 viscous*/
	public static int getBlockBulletEffect(Block block){
		if(block==Blocks.air)
			return 0;
		if(block==Blocks.water || block==Blocks.lava)//viscous
			return 2;
		if(block==Blocks.glass || block==Blocks.glowstone || block==Blocks.redstone_lamp || block==Blocks.torch)//breakable
			return 3;
		return 1;//solidblock
	}
	protected void ricochet(int dir, Block block){
		float resist= block.getExplosionResistance(owner);
		double hitSpeed=0;//speed orthogonal to face hit

		//undo movement into the block
		//posX-= step[0]*4;
		//posY-= step[1]*4;
		//posZ-= step[2]*4;
//
//		remain[0]+= step[0];
//		remain[1]+= step[1];
//		remain[2]+= step[2];
		
		
		hitSpeed=v[dir];
		
		//deflect
		v[dir]*=-1;
		if(dir==0)
			remain.x*=-1;
		else if(dir==1)
			remain.y*=-1;
		else
			remain.z*=-1; 
			

		
		if(abs(v[1])<.1){//friction from the ground, prevents undue rolling
			v[0]*=.95;
			v[2]*=.95;
			//also 0 the y motion
			v[1]=0;
			remain.y=0;
		}


		final float p=penetration(resist, hitSpeed);
		if(p==-1){
			//TODO explode fx
			remain.set(0, 0, 0);
			this.isDead=true;
			this.setDead();
		}
		else{
			//if(abs(v[0])+abs(v[1])+abs(v[2])>0.1){
			remain.scale(p);
				v[0]*= p;
				v[1]*= p;
				v[2]*= p;
			}//}
	}
	/**@return -1 if penetrating, otherwise speed multiplier*/
	protected float penetration(float resist, double speed){
		speed= abs(speed);
		//System.out.println(speed/resist);
		//System.out.println(1/(10/resist +1));
		//constant is penetrabilit[1]. Higher = less bouncy bullets
		if(speed/resist < 1.0)
			return 1/(7/resist +1);//TODO calibrate
		else 
			return -1;
	}
	
	protected void slow(){
		final double s=0.40;
		v[0]*=s;
		v[1]*=s;
		v[2]*=s;
	}
	public void deviate(double d){
		d/=2;
		this.v[0]+= r.nextGaussian()*d;
		this.v[1]+= r.nextGaussian()*d;
		this.v[2]+= r.nextGaussian()*d;
	}
	private int lastTickSpeedCrunched=-1;
	private double speed;
	public double getSpeed(){
		if(lastTickSpeedCrunched!=this.ticksExisted)
			speed= sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
		return speed;
	}

	protected void checkHit(){}
	public void pew(){
		worldObj.playSoundAtEntity(this, 
				"t1a:"+ (entering.getExplosionResistance(this)>2? "ricochetA":"ricochetB")//TODO update ricochet sound
		, 1, 1);
	}
	/**gets called before applying ricochet, etc*/
	protected void onFirstCollision(){
		pew();
	}
	
	
	
	

//    	@Override
//    	protected void readEntityFromNBT(NBTTagCompound var1) {
//    		setPos(posX, posY, posZ);
//    		var1.setBoolean("first", false);
//    		speed0= var1.getDouble("s0");
//    		this.v[0]=motionX;
//    		this.v[1]=motionY;
//    		this.v[2]=motionZ;
//    		this.setDead();
//    	}
//    	@Override
//    	protected void writeEntityToNBT(NBTTagCompound var1) {
//    		firstTimeSpawning= var1.getBoolean("first");
//    		var1.setDouble("s0", speed0);
//    	}
    //
//    	public void writeSpawnData(ByteBuf data){
    ////
////    		data.writeDouble(v[0]);
////    		data.writeDouble(v[1]);
////    		data.writeDouble(v[2]);
////    		data.writeBoolean(firstTimeSpawning);
////    		if(this.owner!=null)
////    			data.writeInt(this.owner.entityId);
////    		else
////    			data.writeInt(-1);
//    	}
//    	public void readSpawnData(ByteBuf data){
////    			v[0]= data.readDouble();
////    			v[1]= data.readDouble();
////    			v[2]= data.readDouble();
    ////
////    			final boolean doRecoil= data.readBoolean();
////    			final int id= data.readInt();
////    			if(id!=-1)
////    				this.owner=worldObj.getEntityByID(id);
////    			if(doRecoil && id!=-1 && owner instanceof EntityPlayer)
////    				T1A.commproxy.doRecoil((EntityPlayer) owner, r);
//    	}
}