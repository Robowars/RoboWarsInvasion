package com.robowars.core.item;

import com.robowars.core.RoboWarsMod;
import net.minecraft.item.Item;

/**
 * Created by thomas on 25/11/2014.
 */
public class ItemPowerCore extends GenericItem {

    public ItemPowerCore(){
        super();
        setMaxStackSize(1);
    }

    @Override
    public Item setTextureName(String name) {
        return this;
    }

    @Override
    public String getName() {
        return "PowerCore";
    }
}