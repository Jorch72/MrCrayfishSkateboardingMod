package com.mrcrayfish.skateboarding;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;

import com.mrcrayfish.skateboarding.api.TrickRegistry;
import com.mrcrayfish.skateboarding.entity.EntitySkateboard;
import com.mrcrayfish.skateboarding.event.SkateboardingEvents;
import com.mrcrayfish.skateboarding.init.SkateboardingItems;
import com.mrcrayfish.skateboarding.network.PacketHandler;
import com.mrcrayfish.skateboarding.proxy.CommonProxy;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION)
public class MrCrayfishSkateboardingMod {
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	public static CreativeTabs skateTab = new SkateTab("skateTab");
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		SkateboardingItems.init();
		SkateboardingItems.register();
		
		EntityRegistry.registerModEntity(EntitySkateboard.class, "csmSkateboard", 0, this, 80, 10, true);

		PacketHandler.init();
		
		TrickRegistry.registerTricks();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.registerRenders();
		
		if(event.getSide() == Side.CLIENT)
		{
			FMLCommonHandler.instance().bus().register(new EntitySkateboard(null));
		}
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}

}