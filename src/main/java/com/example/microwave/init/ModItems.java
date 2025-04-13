package com.example.microwave.init;

import com.example.microwave.MicrowaveMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MicrowaveMod.MOD_ID);

    // Update this line to use your custom tab instead of TAB_DECORATIONS
    public static final RegistryObject<Item> MICROWAVE = ITEMS.register("microwave",
            () -> new BlockItem(ModBlocks.MICROWAVE.get(), new Item.Properties().tab(MicrowaveMod.MICROWAVE_TAB)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}