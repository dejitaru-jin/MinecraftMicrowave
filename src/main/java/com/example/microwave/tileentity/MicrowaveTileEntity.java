package com.example.microwave.tileentity;

import com.example.microwave.block.MicrowaveBlock;
import com.example.microwave.init.ModTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MicrowaveTileEntity extends BlockEntity {
    private int timer = 0;
    private final int maxTime = 100; // 5 seconds at 20 ticks per second

    public MicrowaveTileEntity(BlockPos pos, BlockState state) {
        super(ModTileEntities.MICROWAVE.get(), pos, state);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        timer = tag.getInt("Timer");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Timer", timer);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, MicrowaveTileEntity entity) {
        if (state.getValue(MicrowaveBlock.POWERED)) {
            entity.timer++;
            
            if (entity.timer % 20 == 0) {
                // Play humming sound every second
                level.playSound(null, pos, SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 0.5F, 1.0F);
            }
            
            if (entity.timer >= entity.maxTime) {
                // Microwave cycle complete
                level.playSound(null, pos, SoundEvents.NOTE_BLOCK_PLING, SoundSource.BLOCKS, 0.5F, 1.0F);
                level.setBlock(pos, state.setValue(MicrowaveBlock.POWERED, false), 3);
                entity.timer = 0;
            }
        } else if (entity.timer > 0) {
            // Reset timer if turned off
            entity.timer = 0;
        }
    }
}