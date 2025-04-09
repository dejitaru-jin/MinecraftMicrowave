package com.example.microwave.init;

import com.example.microwave.MicrowaveMod;
import com.example.microwave.block.MicrowaveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MicrowaveMod.MOD_ID);

    public static final RegistryObject<Block> MICROWAVE = BLOCKS.register("microwave",
            () -> new MicrowaveBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(3.5F)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}