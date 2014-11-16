package com.robowars.core;

import com.robowars.core.Entity.EntitylHeavyBotMelee;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;

@Mod(modid = RoboWarsMod.MODID, version = RoboWarsMod.VERSION)
public class RoboWarsMod
{
    public static final String MODID = "Robowars";
    public static final String VERSION = "0.1-ALPHA";

    @SidedProxy(clientSide="com.robowars.core.client.ClientProxy", serverSide ="com.robowars.core.CommonProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        System.out.println("started");
        EntityRegistry.registerGlobalEntityID(EntitylHeavyBotMelee.class, "HeavyBotMelee", EntityRegistry.findGlobalUniqueEntityId(), 80, 1);
        proxy.doStuff();
    }
}