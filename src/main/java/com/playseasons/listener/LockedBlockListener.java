package com.playseasons.listener;

import com.playseasons.PlaySeasons;
import com.playseasons.model.LockedBlockModel;
import com.playseasons.registry.LockedBlockRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Optional;

public class LockedBlockListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (PlaySeasons.getLockedBlockRegistry().isLockable(event.getBlockPlaced())) {
            Location location = event.getBlockPlaced().getLocation();
            Bukkit.getScheduler().scheduleSyncDelayedTask(PlaySeasons.getPlugin(), () -> {
                if (PlaySeasons.getLockedBlockRegistry().create(location.getBlock(), event.getPlayer())) {
                    event.getPlayer().sendMessage(ChatColor.RED + "Locked block created.");
                }
            }, 5);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void onBlockBreak(BlockBreakEvent event) {
        String playerId = event.getPlayer().getUniqueId().toString();
        Optional<LockedBlockModel> oModel = PlaySeasons.getLockedBlockRegistry().
                fromLocation(event.getBlock().getLocation());
        if (oModel.isPresent()) {
            if (oModel.get().getOwner().equals(playerId)) {
                PlaySeasons.getLockedBlockRegistry().delete(event.getBlock());
                event.getPlayer().sendMessage(ChatColor.RED + "Locked block destroyed.");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (PlaySeasons.getLockedBlockRegistry().isRegistered(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        String playerId = event.getPlayer().getUniqueId().toString();
        Block block = event.getClickedBlock();
        if (event.getPlayer().isSneaking() && event.getPlayer().getItemInHand().getType().equals(Material.BONE) &&
                PlaySeasons.getLockedBlockRegistry().isRegistered(block)) {
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            LockedBlockRegistry.LockState state = PlaySeasons.getLockedBlockRegistry().
                    lockUnlock(block, event.getPlayer());
            if (state == LockedBlockRegistry.LockState.LOCKED) {
                event.getPlayer().sendMessage(ChatColor.RED + "This block is locked.");
            } else if (state == LockedBlockRegistry.LockState.UNLOCKED) {
                event.getPlayer().sendMessage(ChatColor.YELLOW + "This block is unlocked.");
            } else {
                event.getPlayer().sendMessage(ChatColor.YELLOW + "You don't have the key to this block.");
            }
        } else if (PlaySeasons.getLockedBlockRegistry().getLockState(event.getClickedBlock()) ==
                LockedBlockRegistry.LockState.LOCKED) {
            // Deny interaction
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);

            // Cancel break animation
            PlaySeasons.getServerDataRegistry().put(playerId, "NO-BREAK", true);
            event.getPlayer().addPotionEffect(PotionEffectType.SLOW_DIGGING.createEffect(9999999, 5), true);
        } else if (block == null || PlaySeasons.getServerDataRegistry().contains(playerId, "NO-BREAK")) {
            // Allow break animation
            PlaySeasons.getServerDataRegistry().remove(playerId, "NO-BREAK");
            event.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRedstone(BlockRedstoneEvent event) {
        if (PlaySeasons.getLockedBlockRegistry().getLockState(event.getBlock()) == LockedBlockRegistry.LockState.LOCKED) {
            event.setNewCurrent(event.getOldCurrent()); // cancelled
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemMove(InventoryMoveItemEvent event) {
        if (event.getSource().getHolder() instanceof Block) {
            if (PlaySeasons.getLockedBlockRegistry().getLockState((Block) event.getSource().getHolder()) ==
                    LockedBlockRegistry.LockState.LOCKED) {
                event.setCancelled(true);
            }
        }
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityExplode(final EntityExplodeEvent event) {
        PlaySeasons.getLockedBlockRegistry().getRegistered().stream().filter(model -> model.getLocation().
                distance(event.getLocation()) <= 10).map(save -> save.getLocation().getBlock()).forEach(block -> {
            if (LockedBlockRegistry.isDoubleChest(block)) {
                LockedBlockRegistry.getDoubleChest(block).forEach(chest -> event.blockList().remove(chest));
            } else {
                event.blockList().remove(block);
            }
        });
    }
}
