package com.example.microwave;

import com.example.microwave.init.ModBlocks;
import com.example.microwave.init.ModItems;
import com.example.microwave.init.ModTileEntities;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MicrowaveMod.MOD_ID)
public class MicrowaveMod {
    public static final String MOD_ID = "microwave";
    public static final Logger LOGGER = LogManager.getLogger();

    public MicrowaveMod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        ModBlocks.register(eventBus);
        ModItems.register(eventBus);
        ModTileEntities.register(eventBus);
        
        MinecraftForge.EVENT_BUS.register(this);
    }
}