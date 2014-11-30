package com.robowars.core.client.renderer;

import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ParticleGroup {
	CubeRenderer cr;
	int count;
	float[] particles;
	
	public ParticleGroup(int count){
		cr= new CubeRenderer(count);
		this.count= count;
		particles= new float[count*3];
	}
	
	
	@SubscribeEvent
	public void render(RenderWorldLastEvent e){
		transform(e.partialTicks);
		cr.render(particles);
	}
	
	float time;
	float timestep= .1f;
	public void transform(float pT){
		time+= timestep/40;
		for(int i=0; i!=count; i++){
			float x= particles[i*3+0];
			float y= particles[i*3+1];
			float z= particles[i*3+2];
			
			float t= time+i;
			float h= Math.abs(t%10-5)/2;
			particles[i*3+0]= MathHelper.sin(t);
			particles[i*3+1]= h;
			particles[i*3+2]= MathHelper.cos(t);
		}
	}
}
