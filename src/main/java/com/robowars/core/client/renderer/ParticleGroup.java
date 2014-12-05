package com.robowars.core.client.renderer;

import org.lwjgl.util.vector.Vector3f;

import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public abstract class ParticleGroup {
	public CubeRenderer cr;
	protected int count;
	protected float[] particles;
	private int color= 0;
	protected byte[] colors= null;

	private void init(int count){
		cr= new CubeRenderer(count);
		this.count= count;
		particles= new float[count*3];
	}
	/**Dynamic color*/
	public ParticleGroup(int count){
		init(count);
		this.colors= new byte[count*4];
	}
	/**Uniform color*/
	public ParticleGroup(int count, int color){
		init(count);
		cr.fillColor(color);
	}
	
	
	@SubscribeEvent
	public void render(RenderWorldLastEvent e){
		render(0, e.partialTicks);
	}
		
	public void render(int tick, float pT){
		transform(tick, pT);
		if(colors==null)
			cr.render(particles);
		else
			cr.render(particles, colors);
	}
	
	Vector3f in= new Vector3f(), out= new Vector3f();
	public void transform(int tick, float pT){
		for(int i=0; i!=count; i++){
			in.x= particles[i*3+0];
			in.y= particles[i*3+1];
			in.z= particles[i*3+2];
			transformParticle(in, out, i, tick, pT);
			particles[i*3+0]= out.x;
			particles[i*3+1]= out.y;
			particles[i*3+2]= out.z;
		}
	}
	protected void transformParticle(Vector3f in, Vector3f out, int particleNumber, int tick, float pT){}
	
	public static float noise(float t){
		return (float)Math.sin(t*42645366.433);
	}
}
