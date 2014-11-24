package com.robowars.core;

import com.robowars.core.entity.monster.EntityBotMelee;
import com.robowars.core.entity.monster.EntityBotShoot;
import com.robowars.core.entity.monster.EntityHeavyBotMelee;
import com.robowars.core.entity.projectile.EntityLaser;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;

@Mod(modid = RoboWarsMod.MODID, version = RoboWarsMod.VERSION)
public class RoboWarsMod
{
    public static final String MODID = "robowars";
    public static final String VERSION = "0.1-ALPHA";

    @SidedProxy(clientSide="com.robowars.core.client.ClientProxy", serverSide ="com.robowars.core.CommonProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        System.out.println("started");
        EntityRegistry.registerGlobalEntityID(EntityHeavyBotMelee.class, "HeavyBotMelee", EntityRegistry.findGlobalUniqueEntityId(), 80, 1);
        EntityRegistry.registerGlobalEntityID(EntityBotMelee.class, "BotMelee", EntityRegistry.findGlobalUniqueEntityId(), 80, 1);
        EntityRegistry.registerGlobalEntityID(EntityBotShoot.class, "BotSchoot", EntityRegistry.findGlobalUniqueEntityId(), 80, 1);
        EntityRegistry.registerModEntity(EntityLaser.class, "EntityLaser", EntityRegistry.findGlobalUniqueEntityId(),this,  64, 10, true);
        proxy.doStuff();
    }
}
