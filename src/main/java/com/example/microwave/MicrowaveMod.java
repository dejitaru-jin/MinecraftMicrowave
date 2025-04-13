package com.example.microwave;

import com.example.microwave.client.gui.MicrowaveScreen;
import com.example.microwave.init.ModBlocks;
import com.example.microwave.init.ModItems;
import com.example.microwave.init.ModMenuTypes;
import com.example.microwave.init.ModTileEntities;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MicrowaveMod.MOD_ID)
public class MicrowaveMod {
    public static final String MOD_ID = "microwave";
    public static final Logger LOGGER = LogManager.getLogger();
    
    // Custom Creative Tab
    public static final CreativeModeTab MICROWAVE_TAB = new CreativeModeTab("microwave") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.MICROWAVE.get());
        }
    };

    public MicrowaveMod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        ModBlocks.register(eventBus);
        ModItems.register(eventBus);
        ModTileEntities.register(eventBus);
        ModMenuTypes.register(eventBus);
        
        eventBus.addListener(this::clientSetup);
        
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenuTypes.MICROWAVE_MENU.get(), MicrowaveScreen::new);
        });
    }
}
