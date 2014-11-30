package com.robowars.core.client.renderer.entity.monster;

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


		public static class ModelBaseBot extends ModelBase{
			@Override
			public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_){
				super.render(p_78088_1_, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_);

				EntityBot bot= (EntityBot)p_78088_1_;
				hfx.render(bot.ticksExisted, 0);
			}
		}

}
