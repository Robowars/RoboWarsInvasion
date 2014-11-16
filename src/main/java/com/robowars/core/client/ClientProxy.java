package com.robowars.core.client;

import com.robowars.core.CommonProxy;
import com.robowars.core.Entity.EntitylHeavyBotMelee;
import com.robowars.core.client.model.ModelHeavyBotMelee;
import com.robowars.core.client.renderer.entity.RenderHeavyBotMelee;
import cpw.mods.fml.client.registry.RenderingRegistry;

/**
 * Created by thomas on 16/11/14.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void doStuff() {
        RenderingRegistry.registerEntityRenderingHandler(EntitylHeavyBotMelee.class, new RenderHeavyBotMelee(new ModelHeavyBotMelee(), 0.5F));
        super.doStuff();
    }
}
