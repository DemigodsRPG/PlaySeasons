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
    public enum LockState {
        LOCKED, UNLOCKED, UNCHANGED, NO_LOCK
    }

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
        if (block == null) {
            return false;
        }
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

    public LockState getLockState(Block block) {
        if (block == null) {
            return LockState.UNLOCKED;
        }
        if (isDoubleChest(block)) {
            return getDoubleChestLockState(getDoubleChest(block));
        }
        return getLockState0(block);
    }

    private LockState getLockState0(Block block) {
        LockedBlockModel model = fromId(LocationUtil.stringFromLocation(block.getLocation()));
        return model != null && model.isLocked() ? LockState.LOCKED : LockState.UNLOCKED;
    }

    private LockState getDoubleChestLockState(List<Block> chests) {
        for (Block chest : chests) {
            if (getLockState0(chest) == LockState.LOCKED) {
                return LockState.LOCKED;
            }
        }
        return LockState.UNLOCKED;
    }

    public LockState lockUnlock(Block block, Player player) {
        if (isDoubleChest(block)) {
            return doubleChestLockUnlock(getDoubleChest(block), player);
        }
        return lockUnlock0(block, player);
    }

    private LockState lockUnlock0(Block block, Player player) {
        LockedBlockModel model = fromId(LocationUtil.stringFromLocation(block.getLocation()));
        if (model != null) {
            if ((model.getOwner().equals(player.getUniqueId().toString()) ||
                    player.hasPermission("seasons.bypasslock"))) {
                return model.setLocked(!model.isLocked()) ? LockState.LOCKED : LockState.UNLOCKED;
            } else {
                return LockState.UNCHANGED;
            }
        }
        return LockState.NO_LOCK;
    }

    private LockState doubleChestLockUnlock(List<Block> chests, Player player) {
        boolean unlocked = false;
        for (Block chest : chests) {
            LockState state = lockUnlock0(chest, player);
            if (state == LockState.LOCKED) {
                return LockState.LOCKED;
            } else if (state == LockState.UNCHANGED) {
                return LockState.UNCHANGED;
            } else if (state == LockState.UNLOCKED) {
                unlocked = true;
            }
        }
        return unlocked ? LockState.UNLOCKED : LockState.NO_LOCK;
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
