package com.example.microwave.tileentity;

import com.example.microwave.block.MicrowaveBlock;
import com.example.microwave.init.ModTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MicrowaveTileEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 100; // 5 seconds at 20 ticks per second

    public MicrowaveTileEntity(BlockPos pos, BlockState state) {
        super(ModTileEntities.MICROWAVE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> MicrowaveTileEntity.this.progress;
                    case 1 -> MicrowaveTileEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> MicrowaveTileEntity.this.progress = value;
                    case 1 -> MicrowaveTileEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.microwave.microwave");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MicrowaveMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("microwave.progress", this.progress);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("microwave.progress");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, MicrowaveTileEntity entity) {
        if (!level.isClientSide()) {
            if (state.getValue(MicrowaveBlock.POWERED)) {
                if (hasRecipe(entity)) {
                    entity.progress++;
                    
                    if (entity.progress % 20 == 0) {
                        // Play humming sound every second
                        level.playSound(null, pos, SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 0.5F, 1.0F);
                    }
                    
                    setChanged(level, pos, state);
                    
                    if (entity.progress >= entity.maxProgress) {
                        // Microwave cycle complete
                        craftItem(entity);
                        level.playSound(null, pos, SoundEvents.NOTE_BLOCK_PLING, SoundSource.BLOCKS, 0.5F, 1.0F);
                        level.setBlock(pos, state.setValue(MicrowaveBlock.POWERED, false), 3);
                        entity.progress = 0;
                    }
                } else {
                    // No valid recipe, reset
                    level.setBlock(pos, state.setValue(MicrowaveBlock.POWERED, false), 3);
                    entity.progress = 0;
                    setChanged(level, pos, state);
                }
            } else if (entity.progress > 0) {
                // Reset timer if turned off
                entity.progress = 0;
                setChanged(level, pos, state);
            }
        }
    }

    private static boolean hasRecipe(MicrowaveTileEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        // Check if there's something in the input slot
        if (inventory.getItem(0).isEmpty()) {
            return false;
        }

        // Check if the output slot can accept the result
        ItemStack result = level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, inventory, level)
                .map(recipe -> recipe.getResultItem(level.registryAccess()))
                .orElse(ItemStack.EMPTY);

        if (result.isEmpty()) {
            return false;
        }

        ItemStack outputSlot = entity.itemHandler.getStackInSlot(1);
        if (outputSlot.isEmpty()) {
            return true;
        }

        if (!outputSlot.is(result.getItem())) {
            return false;
        }

        return outputSlot.getCount() + result.getCount() <= outputSlot.getMaxStackSize();
    }

    private static void craftItem(MicrowaveTileEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, inventory, level)
                .ifPresent(recipe -> {
                    ItemStack result = recipe.getResultItem(level.registryAccess());
                    
                    // Craft the item
                    ItemStack outputSlot = entity.itemHandler.getStackInSlot(1);
                    if (outputSlot.isEmpty()) {
                        entity.itemHandler.setStackInSlot(1, result.copy());
                    } else if (outputSlot.is(result.getItem())) {
                        outputSlot.grow(result.getCount());
                    }
                    
                    // Consume the input
                    entity.itemHandler.extractItem(0, 1, false);
                });
    }
}
