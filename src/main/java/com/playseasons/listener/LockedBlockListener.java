package com.playseasons.listener;

import com.playseasons.PlaySeasons;
import com.playseasons.model.LockedBlockModel;
import com.playseasons.registry.LockedBlockRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onBlockBreak(BlockBreakEvent event) {
        Optional<LockedBlockModel> oModel = PlaySeasons.getLockedBlockRegistry().
                fromLocation(event.getBlock().getLocation());
        if (oModel.isPresent()) {
            if (oModel.get().getOwner().equals(event.getPlayer().getUniqueId().toString())) {
                PlaySeasons.getLockedBlockRegistry().delete(event.getBlock());
                event.getPlayer().sendMessage(ChatColor.RED + "Locked block destroyed.");
            } else {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "This block is currently locked.");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (PlaySeasons.getLockedBlockRegistry().isLocked(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockDamage(BlockDamageEvent event) {
        String playerId = event.getPlayer().getUniqueId().toString();
        if (PlaySeasons.getLockedBlockRegistry().isLocked(event.getBlock())) {
            // Cancel break animation
            PlaySeasons.getServerDataRegistry().put(playerId, "NO-BREAK", true);
            event.getPlayer().addPotionEffect(PotionEffectType.SLOW_DIGGING.createEffect(9999999, 5), true);
            event.setCancelled(true);
        } else if (PlaySeasons.getServerDataRegistry().contains(playerId, "NO-BREAK")) {
            // Allow break animation
            PlaySeasons.getServerDataRegistry().remove(playerId, "NO-BREAK");
            event.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        String playerId = event.getPlayer().getUniqueId().toString();
        Block block = event.getClickedBlock();
        if (event.getPlayer().isSneaking() && PlaySeasons.getLockedBlockRegistry().isRegistered(event.getClickedBlock())) {
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            if (PlaySeasons.getLockedBlockRegistry().lockUnlock(block, event.getPlayer())) {
                event.getPlayer().sendMessage(ChatColor.RED + "This block is locked.");
            } else {
                event.getPlayer().sendMessage(ChatColor.YELLOW + "This block is unlocked.");
            }
        } else if (PlaySeasons.getLockedBlockRegistry().isLocked(event.getClickedBlock())) {
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            event.getPlayer().sendMessage(ChatColor.RED + "This block is currently locked.");
        } else if (block == null && PlaySeasons.getServerDataRegistry().contains(playerId, "NO-BREAK")) {
            // Allow break animation
            PlaySeasons.getServerDataRegistry().remove(playerId, "NO-BREAK");
            event.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRedstone(BlockRedstoneEvent event) {
        if (PlaySeasons.getLockedBlockRegistry().isLocked(event.getBlock())) {
            event.setNewCurrent(event.getOldCurrent()); // cancelled
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemMove(InventoryMoveItemEvent event) {
        if (event.getSource().getHolder() instanceof Block) {
            if (PlaySeasons.getLockedBlockRegistry().isLocked((Block) event.getSource().getHolder())) {
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
