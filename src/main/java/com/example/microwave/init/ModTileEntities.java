package com.example.microwave.init;

import com.example.microwave.MicrowaveMod;
import com.example.microwave.tileentity.MicrowaveTileEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModTileEntities {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(
            ForgeRegistries.BLOCK_ENTITY_TYPES, MicrowaveMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<MicrowaveTileEntity>> MICROWAVE = TILE_ENTITIES.register(
            "microwave", () -> BlockEntityType.Builder.of(
                    MicrowaveTileEntity::new, ModBlocks.MICROWAVE.get()).build(null));

    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);
    }
}