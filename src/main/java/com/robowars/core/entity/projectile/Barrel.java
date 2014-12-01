package com.robowars.core.entity.projectile;

import org.lwjgl.util.vector.Vector3f;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
/** Uses proper angles*/
/** Uses proper angles*/
public class Barrel {

	protected int lastTickCrunched= 0;

	public float length,mountX,mountY,mountZ;
	public Entity aimer, vehicle;

	protected Vector3f mount, mountSphere;
	/**the result; where the bullets exit*/
	public Vector3f muzzleEndTransformed;
	/**used for determining bullet motion/laser vector*/
	public Vector3f lookdir;

	//public double yaw, pitch;

	public Barrel(){}
	
	/**Mount position and Entity to mount on 
	 * @param vehicle may be set to null to mount on the aimer
	 * @param length of the barrel*
	 * @param mountA rotational center of the weapon, relative to rotational cener the vehicle*/
	public Barrel(Entity aimer, Entity vehicle, float length, float mountX, float mountY, float mountZ){
		this.aimer= aimer;
		this.vehicle= vehicle;

		this.length= length;
		this.mountX= -mountX;
		this.mountY= -mountY;
		this.mountZ= mountZ;

		mount= new Vector3f(this.mountX, this.mountY, this.mountZ);

		Vector3f mnorm= new Vector3f(mount);
		mnorm.normalise();
		this.mountSphere= new Vector3f((float)Math.acos(mountX),(float)Math.asin(mountY),mount.length());
	}

	/**uses the aimer's rotations, use a dummy Entity if necessary
	 * @return x,y,z of where the bullet comes out*/
	public void calculatePosAndLook(){
		if(lastTickCrunched == aimer.ticksExisted)
			return;
		lastTickCrunched= aimer.ticksExisted;

//			lookdir= new Vector3f(length, aimer.rotationPitch, aimer.rotationYaw).toCartesian();
			
			mountSphere.y+= getEntityMountedTo() instanceof EntityPlayer? 0: -getEntityMountedTo().rotationPitch;
			mountSphere.z+= getEntityMountedTo().rotationYaw;

//		muzzleEndTransformed= mountSphere.toCartesian().add(lookdir);

		muzzleEndTransformed.x+= aimer.posX;
		muzzleEndTransformed.y+= aimer.posY;
		muzzleEndTransformed.z+= aimer.posZ;

//		mountSphere= mount.toSpherical();//be sure to reset it

//		this.lookdir.normalize();
	}

	//	/**Use setVec if you can
	//	 * @param yaw pitch use normal radian angles not retarded ones*/
	//	public void setAngles(double yaw, double pitch){
	//		this.yaw= yaw;
	//		this.pitch= pitch;
	//	}

	/**@return the vehicle if not null, else the player*/
	public Entity getEntityMountedTo(){
		return this.vehicle==null ? this.aimer : this.vehicle;
	}
}