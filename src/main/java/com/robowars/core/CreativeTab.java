package com.robowars.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by Evydder on 28/11/2014.
 */
public class CreativeTab {

    public static final CreativeTabs RoboWarsTab = new CreativeTabs("RoboWars") {
        @Override
        public Item getTabIconItem() {
            return RoboWarsMod.ITEM_POWER_CORE;
        }
    };
}
