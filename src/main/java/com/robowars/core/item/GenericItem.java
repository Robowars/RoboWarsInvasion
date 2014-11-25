package com.robowars.core.item;

import com.robowars.core.RoboWarsMod;
import net.minecraft.item.Item;

/**
 * Created by thomas on 25/11/2014.
 */
public abstract class GenericItem extends Item {

    public GenericItem(){
        setCreativeTab(RoboWarsMod.CREATIVE_TAB);
        setUnlocalizedName(RoboWarsMod.MODID + "." + getName());
    }

    public abstract String getName();

}
