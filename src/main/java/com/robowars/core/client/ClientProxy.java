package com.robowars.core.client;

import com.robowars.core.CommonProxy;
import com.robowars.core.client.model.ModelBotMelee;
import com.robowars.core.client.model.ModelBotSchoot;
import com.robowars.core.client.model.ModelHeavyBotMelee;
import com.robowars.core.client.renderer.entity.RenderBotMelee;
import com.robowars.core.client.renderer.entity.RenderBotSchoot;
import com.robowars.core.client.renderer.entity.RenderHeavyBotMelee;
import com.robowars.core.client.renderer.entity.RenderLaser;
import com.robowars.core.entity.monster.EntityBotMelee;
import com.robowars.core.entity.monster.EntityBotSchoot;
import com.robowars.core.entity.monster.EntityHeavyBotMelee;
import com.robowars.core.entity.projectile.EntityLaser;
import cpw.mods.fml.client.registry.RenderingRegistry;

/**
 * Created by thomas on 16/11/14.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void doStuff() {
        RenderingRegistry.registerEntityRenderingHandler(EntityHeavyBotMelee.class, new RenderHeavyBotMelee(new ModelHeavyBotMelee(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityBotSchoot.class, new RenderBotSchoot(new ModelBotSchoot(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityBotMelee.class, new RenderBotMelee(new ModelBotMelee(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityLaser.class, new RenderLaser());
        super.doStuff();
    }
}
