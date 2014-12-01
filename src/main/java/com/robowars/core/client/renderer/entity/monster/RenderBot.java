package com.robowars.core.client.renderer.entity.monster;

import java.util.Random;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.util.vector.Vector3f;

import com.robowars.core.client.renderer.ParticleGroup;
import com.robowars.core.entity.monster.EntityBot;

	public abstract class RenderBot extends MonsterRender{

		public RenderBot(ModelBase modelBase, float shadow,
				ResourceLocation glowingTexture) {
			super(modelBase, shadow, glowingTexture);

		}

		private static ParticleGroupHoverFX hfx= new ParticleGroupHoverFX(); 
		public static class ParticleGroupHoverFX extends ParticleGroup{
			public ParticleGroupHoverFX(){
				super(64);
				this.cr.scale= .03f;
				this.cr.color= new float[]{0, .005f, 1f, .7f};
			}

			@Override
			public void transformParticle(Vector3f in, Vector3f out, int particleNumber, int tick, float pT){
				final float lifetime= 15;
				float pfac= (float)(particleNumber)/this.count;
				float lifecycle= ( noise(particleNumber)+tick/lifetime+pT )%1f;
				
				float theta= noise(particleNumber)*21.14f*(float)Math.PI + lifecycle*lifecycle;
				float r= pfac/4+.03f;
				r*= 1+lifecycle*lifecycle*2;
				
				out.x= MathHelper.cos(theta)*r;
				out.y= ( (float)Math.sqrt(lifecycle)*lifetime )/38f+1.2f;
				out.z= MathHelper.sin(theta)*r;
			}
		}
		private static ParticleGroupSparks sparkfx= new ParticleGroupSparks();
		public static class ParticleGroupSparks extends ParticleGroup{
			public ParticleGroupSparks() {
				super(Sparks.MAX);
				cr.scale= .05f;
				this.cr.color= new float[]{1f, .8f, 0, .4f};
			}
			public void render(Sparks sp, int tick, float pT){
				cr.render(sp.pos);
				this.cr.color= new float[]{1f, .8f, 0, .4f};
			}
			
			@Override
			@Deprecated
			public void render(int tick, float pT){}
			
		}
		public static class Sparks{
			public static final int MAX= 32;
			float[] pos= new float[MAX*3];
			float[] life= new float[MAX];
			float maxlife= 30;
			float[] pos0= new float[MAX*3];
			float[] v0= new float[MAX*3];
			float g= .012f;
			{
				init();
			}
			void init(){
				Random r= new Random();
				for(int i=0; i!=MAX; i++){
					int x= i*3+0;
					int y= i*3+1;
					int z= i*3+2;
					//initial position
					pos0[x]= (r.nextFloat()*2-1)/3.5f;
					pos0[y]= (r.nextFloat()*1-.5f);
					pos0[z]= 0;
					//initial velocity
					//remember y is backwards
					v0[x]= (float)r.nextGaussian()/maxlife*1.2f;
					v0[y]= (float)r.nextGaussian()/maxlife*.5f-.055f;
					v0[z]= (float)r.nextGaussian()/maxlife*1.2f;
				}
			}
			
			
			int next=0;
			public void make(int number){
//				init();
				if(next>=MAX)
					next=0;
				if(next+number>=MAX)
					number= MAX-1-next;
				int mark= next+number;
				for(; next<=mark; next++){
					int x= next*3+0;
					int y= next*3+1;
					int z= next*3+2;
					life[next]=0;
					pos[x]= pos0[x];
					pos[y]= pos0[y];
					pos[z]= pos0[z];
				}
			}
			
			public void update(){
				for(int i=0; i!=MAX; i++){
					int x= i*3+0;
					int y= i*3+1;
					int z= i*3+2;
					if(life[i]++>=maxlife){
						pos[x]= 0;
						pos[y]= 0;
						pos[z]= 0;
					}
					float l= life[i];
					pos[x]+= v0[x]*l*Math.exp(-l/5.5f);
					pos[y]+= v0[y]*l + l*l*g/2;
					pos[z]+= v0[z]*l*Math.exp(-l/5.5f);
				}
			}
			
		}


		public static class ModelBaseBot extends ModelBase{
			@Override
			public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_){
				super.render(p_78088_1_, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_);

				EntityBot bot= (EntityBot)p_78088_1_;
				hfx.render(bot.ticksExisted, 0);
				sparkfx.render(bot.sparks, bot.ticksExisted, 0);
			}
		}

}
