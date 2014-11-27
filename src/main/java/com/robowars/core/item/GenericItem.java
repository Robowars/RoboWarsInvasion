package com.robowars.core.item;

import com.robowars.core.CreativeTab;
import com.robowars.core.RoboWarsMod;
import com.robowars.core.CreativeTab;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;



/**
 * Created by thomas on 25/11/2014.
 */
public abstract class GenericItem extends Item {

    public GenericItem(){

        super();
        this.setCreativeTab(CreativeTab.RoboWarsTab);
    }


    public abstract String getName();

}
