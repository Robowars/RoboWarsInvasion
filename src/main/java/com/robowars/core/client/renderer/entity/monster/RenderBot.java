package com.robowars.core.client.renderer.entity.monster;

import java.util.Random;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import com.robowars.core.client.renderer.CubeRenderer;
import com.robowars.core.client.renderer.ParticleGroup;
import com.robowars.core.client.renderer.RenderWarp;
import com.robowars.core.client.renderer.ShaderUtil;
import com.robowars.core.entity.monster.EntityBot;

	public abstract class RenderBot extends MonsterRender{

		public RenderBot(ModelBase modelBase, float shadow,
				ResourceLocation glowingTexture) {
			super(modelBase, shadow, glowingTexture);

		}

		
		
		private static ParticleGroupHoverFX hfx= new ParticleGroupHoverFX(); 
		public static class ParticleGroupHoverFX extends ParticleGroup{
			static final int COUNT=32;
			static int colorbase= 0x0028ff55;
			public ParticleGroupHoverFX(){
				super(COUNT, colorbase);
				this.cr.scale= .06f;
			}

			@Override
			public void transformParticle(Vector3f in, Vector3f out, int particleNumber, int tick, float pT){
				this.cr.scale= .1f;
				final int lifetime= 5;
				float time= ((tick&511)+pT);//modulus to preserve precision
				float t= ( noise(particleNumber) + time/lifetime );
				int cycle= (int)(t);
				t%= 1;
				float pfac= (float)(particleNumber)/this.count;
				
				float theta= noise(particleNumber)*(float)Math.PI+ cycle*10;
				float r= noise(pfac+cycle)*(1+t)/8;
				
				out.x= MathHelper.cos(theta)*r;
				out.y= ( (float)t*lifetime )/20f+1.2f;
				out.z= MathHelper.sin(theta)*r;
			}
		}
		
		
		
		
		private static ParticleGroupSparks sparkfx= new ParticleGroupSparks();
		public static class ParticleGroupSparks extends ParticleGroup{
			public ParticleGroupSparks() {
				super(Sparks.MAX, 0xffdd22dd);
				cr.scale= .035f;
			}
			public void render(Sparks sp, int tick, float pT){
				if(sp.lasttickmade+sp.maxlife<=tick)
					return;//no sparks active
				sp.update(tick, pT);
				cr.render(sp.pos);
			}
			
			@Override
			@Deprecated
			public final void render(int tick, float pT){try{throw new Exception();}catch(Exception e){e.printStackTrace();}}
			
		}
		public static class Sparks{
			public static final int MAX= 64;
			public static int maxlife= 35;
			float[] tickMade= new float[MAX];
			static float[] pos= new float[MAX*3];
			static float[] pos0= new float[MAX*3];
			static float[] v0= new float[MAX*3];
			float g= .015f;
			
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
					v0[x]= (float)r.nextGaussian()/maxlife*3f;
					v0[y]= (float)r.nextGaussian()/maxlife*2.5f-.1f;
					v0[z]= (float)r.nextGaussian()/maxlife*3f;
				}
			}
			

			int lasttickmade= -100;
			int next=0;
			public void make(int time, int number){
				//init();
				lasttickmade= time;
				if(next>=MAX)
					next=0;
				if(next+number>=MAX)
					number= MAX-1-next;
				int mark= next+number;
				for(; next<=mark; next++){
					tickMade[next]= time;
				}
			}
			
			public void update(int time, float pT){
				for(int i=0; i!=MAX; i++){
					int x= i*3+0;
					int y= i*3+1;
					int z= i*3+2;
					if(tickMade[i]>=time+maxlife){
						pos[x]= 0;
						pos[y]= 0;
						pos[z]= 0;
					}
					float t= (time-tickMade[i])+pT;
					pos[x]= pos0[x] + v0[x]*t*(float)Math.exp(-t/25.f);
					pos[y]= pos0[y] + v0[y]*t + t*t*g/2;
					pos[z]= pos0[z] + v0[z]*t*(float)Math.exp(-t/25.f);
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


//				GL11.glPushMatrix();
//				RenderWarp.queue(ShaderUtil.getLegacyMVP());
//				GL11.glPopMatrix();
			}
		}

}
