package com.robowars.core.client;

import com.robowars.core.CommonProxy;
import com.robowars.core.RoboWarsMod;
import com.robowars.core.client.model.entity.ModelBotShoot;
import com.robowars.core.client.model.entity.ModelHeavyBotMelee;
import com.robowars.core.client.model.item.ModelPowerCore;
import com.robowars.core.client.renderer.entity.monster.RenderBotMelee;
import com.robowars.core.client.renderer.entity.monster.RenderBotShoot;
import com.robowars.core.client.renderer.entity.monster.RenderHeavyBotMelee;
import com.robowars.core.client.renderer.entity.projectile.RenderLaser;
import com.robowars.core.client.renderer.item.ItemRender;
import com.robowars.core.entity.monster.EntityBotMelee;
import com.robowars.core.entity.monster.EntityBotShoot;
import com.robowars.core.entity.monster.EntityHeavyBotMelee;
import com.robowars.core.entity.projectile.EntityLaser;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
    @Override
    public void doStuff() {
        RenderingRegistry.registerEntityRenderingHandler(EntityHeavyBotMelee.class, new RenderHeavyBotMelee(new ModelHeavyBotMelee(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityBotShoot.class, new RenderBotShoot(new ModelBotShoot(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityBotMelee.class, new RenderBotMelee(0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityLaser.class, new RenderLaser());
        MinecraftForgeClient.registerItemRenderer(RoboWarsMod.ITEM_POWER_CORE, new ItemRender(new ModelPowerCore()));
        super.doStuff();
    }
}
