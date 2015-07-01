package com.playseasons.dungeon.boss;

import com.demigodsrpg.chitchat.Chitchat;
import com.playseasons.dungeon.DungeonMob;
import com.playseasons.dungeon.DungeonMobs;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class Skeletor implements DungeonMob {
    @Override
    public String getName() {
        return "Skeletor";
    }

    @Override
    public EntityType getType() {
        return EntityType.SKELETON;
    }

    @Override
    public double getMaxHealth() {
        return 20;
    }

    @Override
    public boolean isBoss() {
        return true;
    }

    @Override
    public int getStrength() {
        return 1;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTarget(EntityTargetLivingEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity && DungeonMobs.getMobs(this).contains(event.getEntity())) {
            // Get the skeleton
            Skeleton skeleton = (Skeleton) event.getEntity();

            // Get the target
            LivingEntity target = event.getTarget();

            // Special stuff for players
            if (target instanceof Player) {
                Player player = (Player) target;

                // TODO DEBUG
                Chitchat.sendTitle(player, 10, 20, 10, "Skeletor has targeted you.", "Watch out for his dank arrows.");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onArrowLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Arrow && event.getEntity().getShooter() instanceof Skeleton && DungeonMobs.getMobs(this).contains(event.getEntity().getShooter())) {
            // Get the skeleton
            Skeleton skeleton = (Skeleton) event.getEntity().getShooter();

            // Spawn wither skull
            WitherSkull ws = skeleton.launchProjectile(WitherSkull.class);
            ws.setVelocity(event.getEntity().getVelocity());
            ws.setIsIncendiary(true);
            ws.setYield(8F);
            ws.setShooter(skeleton);

            // Cancel the event
            event.setCancelled(true);
        }
    }
}
