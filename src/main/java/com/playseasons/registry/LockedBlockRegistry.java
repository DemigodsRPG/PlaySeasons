package com.playseasons.registry;

import com.demigodsrpg.util.LocationUtil;
import com.demigodsrpg.util.datasection.DataSection;
import com.playseasons.model.LockedBlockModel;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LockedBlockRegistry extends AbstractSeasonsRegistry<LockedBlockModel> {
    @Override
    protected LockedBlockModel valueFromData(String s, DataSection dataSection) {
        return new LockedBlockModel(s, dataSection);
    }

    @Override
    protected String getName() {
        return "locked_blocks";
    }

    public Optional<LockedBlockModel> fromLocation(Location location) {
        return Optional.ofNullable(fromId(LocationUtil.stringFromLocation(location)));
    }

    public boolean isLockable(Block block) {
        switch (block.getType()) {
            case ACACIA_DOOR:
            case BIRCH_DOOR:
            case DARK_OAK_DOOR:
            case JUNGLE_DOOR:
            case SPRUCE_DOOR:
            case IRON_DOOR:
            case WOOD_DOOR:
            case WOODEN_DOOR:
            case TRAP_DOOR:
            case IRON_TRAPDOOR:
            case DISPENSER:
            case FURNACE:
            case BURNING_FURNACE:
            case CHEST:
            case ENDER_CHEST:
            case TRAPPED_CHEST:
            case HOPPER:
            case HOPPER_MINECART:
            case STORAGE_MINECART:
            case POWERED_MINECART:
            case STONE_BUTTON:
            case WOOD_BUTTON:
            case GOLD_PLATE:
            case IRON_PLATE:
            case STONE_PLATE:
            case WOOD_PLATE:
            case LEVER:
                return true;
        }
        return false;
    }

    public boolean isRegistered(Block block) {
        if (isDoubleChest(block)) {
            return isRegisteredDoubleChest(getDoubleChest(block));
        }
        return isRegistered0(block);
    }

    private boolean isRegistered0(Block block) {
        return fromId(LocationUtil.stringFromLocation(block.getLocation())) != null;
    }

    private boolean isRegisteredDoubleChest(List<Block> chests) {
        boolean registered = false;
        for (Block chest : chests) {
            if (isRegistered0(chest)) {
                registered = true;
            }
        }
        return registered;
    }

    public boolean isLocked(Block block) {
        if (isDoubleChest(block)) {
            return isDoubleChestLocked(getDoubleChest(block));
        }
        return isLocked0(block);
    }

    private boolean isLocked0(Block block) {
        LockedBlockModel model = fromId(LocationUtil.stringFromLocation(block.getLocation()));
        return model != null && model.isLocked();
    }

    private boolean isDoubleChestLocked(List<Block> chests) {
        boolean locked = false;
        for (Block chest : chests) {
            if (isLocked0(chest)) {
                locked = true;
            }
        }
        return locked;
    }

    public boolean lockUnlock(Block block, Player player) {
        if (isDoubleChest(block)) {
            return doubleChestLockUnlock(getDoubleChest(block), player);
        }
        return lockUnlock0(block, player);
    }

    private boolean lockUnlock0(Block block, Player player) {
        LockedBlockModel model = fromId(LocationUtil.stringFromLocation(block.getLocation()));
        return model != null && (model.getOwner().equals(player.getUniqueId().toString()) ||
                player.hasPermission("seasons.bypasslock")) && model.setLocked(!model.isLocked());
    }

    private boolean doubleChestLockUnlock(List<Block> chests, Player player) {
        boolean locked = false;
        for (Block chest : chests) {
            if (lockUnlock0(chest, player)) {
                locked = true;
            }
        }
        return locked;
    }

    public List<LockedBlockModel> getCreated(String owner) {
        return getRegistered().stream().filter(model -> model.getOwner().equals(owner)).collect(Collectors.toList());
    }

    public boolean create(Block block, Player player) {
        if (isLockable(block) && !isRegistered(block)) {
            register(new LockedBlockModel(block.getLocation(), player.getUniqueId().toString()));
            return true;
        }
        return false;
    }

    public void delete(Block block) {
        LockedBlockModel model = fromId(LocationUtil.stringFromLocation(block.getLocation()));
        if (model != null) {
            unregister(model);
        }
    }

    public static List<Block> getSuroundingBlocks(Block block) {
        List<Block> ret = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Block loc = block.getRelative(x, y, z);
                    ret.add(loc);
                }
            }
        }
        return ret;
    }

    public static boolean isDoubleChest(Block block) {
        if (block.getType().equals(Material.CHEST)) {
            for (Block found : getSuroundingBlocks(block)) {
                if (found.getType().equals(Material.CHEST)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<Block> getDoubleChest(Block block) {
        return getSuroundingBlocks(block).stream().filter(found -> found.getType().equals(Material.CHEST)).
                collect(Collectors.toList());
    }
}
