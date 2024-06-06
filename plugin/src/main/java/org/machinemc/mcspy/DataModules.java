package org.machinemc.mcspy;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.machinemc.mcspy.modules.BlockModule;
import org.machinemc.mcspy.modules.BlockStateModule;
import org.machinemc.mcspy.modules.ItemModule;

import java.util.List;

/**
 * Collection of all available data modules.
 */
public final class DataModules {

    public static final DataModule<Block> BLOCK = new BlockModule();
    public static final DataModule<List<BlockState>> BLOCK_STATE = new BlockStateModule();
    public static final DataModule<Item> ITEM = new ItemModule();

    private DataModules() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns all available data modules.
     *
     * @return all modules
     */
    public static List<DataModule<?>> getAll() {
        return List.of(
                BLOCK,
                BLOCK_STATE,
                ITEM
        );
    }

}
