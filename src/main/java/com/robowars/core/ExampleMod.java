package com.robowars.core;

import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(modid = ExampleMod.MODID, version = ExampleMod.VERSION)
public class ExampleMod
{
    public static final String MODID = "Robowars";
    public static final String VERSION = "0.1-ALPHA";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        //We need to do something here don't you think so?
    }
}
