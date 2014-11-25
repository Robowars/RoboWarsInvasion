package com.robowars.core.item;

/**
 * Created by thomas on 25/11/2014.
 */
public class ItemPowerCore extends GenericItem {

    public ItemPowerCore(){
        super();
        setMaxStackSize(1);
    }

    @Override
    public String getName() {
        return "PowerCore";
    }
}
