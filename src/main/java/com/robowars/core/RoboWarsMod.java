package com.robowars.core;

import com.robowars.core.entity.monster.EntityBotMelee;
import com.robowars.core.entity.monster.EntityBotShoot;
import com.robowars.core.entity.monster.EntityHeavyBotMelee;
import com.robowars.core.entity.projectile.EntityLaser;
import com.robowars.core.item.GenericItem;
import com.robowars.core.item.ItemPowerCore;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import sun.net.www.content.text.Generic;

@Mod(modid = RoboWarsMod.MODID, version = RoboWarsMod.VERSION)
public class RoboWarsMod
{
    public static final String MODID = "robowars";
    public static final String VERSION = "0.1-ALPHA";

    public static final GenericItem ITEM_POWER_CORE = new ItemPowerCore();

    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs("RoboWars") {
        @Override
        public Item getTabIconItem() {
            return ITEM_POWER_CORE ;
        }
    };


    @SidedProxy(clientSide="com.robowars.core.client.ClientProxy", serverSide ="com.robowars.core.CommonProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        System.out.println("started");
        EntityRegistry.registerGlobalEntityID(EntityHeavyBotMelee.class, "HeavyBotMelee", EntityRegistry.findGlobalUniqueEntityId(), 80, 1);
        EntityRegistry.registerGlobalEntityID(EntityBotMelee.class, "BotMelee", EntityRegistry.findGlobalUniqueEntityId(), 80, 1);
        EntityRegistry.registerGlobalEntityID(EntityBotShoot.class, "BotSchoot", EntityRegistry.findGlobalUniqueEntityId(), 80, 1);
        EntityRegistry.registerModEntity(EntityLaser.class, "EntityLaser", EntityRegistry.findGlobalUniqueEntityId(), this, 64, 10, true);
        GameRegistry.registerItem(ITEM_POWER_CORE, ITEM_POWER_CORE.getName());
        proxy.doStuff();
    }
}
