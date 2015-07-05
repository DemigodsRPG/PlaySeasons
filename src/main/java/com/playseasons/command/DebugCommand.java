package com.playseasons.command;

import com.censoredsoftware.library.command.type.BaseCommand;
import com.censoredsoftware.library.command.type.CommandResult;
import com.playseasons.dungeon.mob.DungeonMobs;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class DebugCommand extends BaseCommand {
    @Override
    protected CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            return CommandResult.PLAYER_ONLY;
        }
        if (!sender.hasPermission("seasons.admin")) {
            return CommandResult.NO_PERMISSIONS;
        }
        Location location = ((Player) sender).getLocation();
        DungeonMobs.spawnDungeonMob(location, DungeonMobs.SKELETOR);
        sender.sendMessage(ChatColor.YELLOW + "Skeletor has been spawned.");
        //DungeonMobs.spawnDungeonMob(location, DungeonMobs.EVIL_SQUID);
        return CommandResult.SUCCESS;
    }
}
