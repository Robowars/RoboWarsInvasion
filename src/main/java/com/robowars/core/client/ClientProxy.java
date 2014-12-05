package com.robowars.core.client;

import com.robowars.core.CommonProxy;
import com.robowars.core.RoboWarsMod;
import com.robowars.core.client.model.entity.ModelBotShoot;
import com.robowars.core.client.model.entity.ModelHeavyBotMelee;
import com.robowars.core.client.model.item.ModelPowerCore;
import com.robowars.core.client.renderer.CubeRenderer;
import com.robowars.core.client.renderer.ParticleGroup;
import com.robowars.core.client.renderer.RenderWarp;
import com.robowars.core.client.renderer.entity.monster.RenderBotMelee;
import com.robowars.core.client.renderer.entity.monster.RenderBotShoot;
import com.robowars.core.client.renderer.entity.monster.RenderHeavyBotMelee;
import com.robowars.core.client.renderer.entity.projectile.RenderLaser;
import com.robowars.core.client.renderer.item.ItemRender;
import com.robowars.core.entity.monster.EntityBotMelee;
import com.robowars.core.entity.monster.EntityBotShoot;
import com.robowars.core.entity.monster.EntityHeavyBotMelee;
import com.robowars.core.entity.projectile.EntityLaser;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {
    @Override
    public void RegisterRendering() {
        RenderingRegistry.registerEntityRenderingHandler(EntityHeavyBotMelee.class, new RenderHeavyBotMelee());
        RenderingRegistry.registerEntityRenderingHandler(EntityBotShoot.class, new RenderBotShoot());
        RenderingRegistry.registerEntityRenderingHandler(EntityBotMelee.class, new RenderBotMelee());
        RenderingRegistry.registerEntityRenderingHandler(EntityLaser.class, new RenderLaser());
        MinecraftForgeClient.registerItemRenderer(RoboWarsMod.ITEM_POWER_CORE, new ItemRender(new ModelPowerCore()));
        if(OpenGlHelper.isFramebufferEnabled())
        	MinecraftForge.EVENT_BUS.register(new RenderWarp());
    }
}
