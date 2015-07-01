package com.playseasons.command;

import com.censoredsoftware.library.command.type.BaseCommand;
import com.censoredsoftware.library.command.type.CommandResult;
import com.playseasons.dungeon.DungeonMobs;
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
        Location location = ((Player) sender).getLocation();
        //DungeonMobs.spawnDungeonMob(location, DungeonMobs.SKELETOR);
        DungeonMobs.spawnDungeonMob(location, DungeonMobs.EVIL_SQUID);
        return CommandResult.SUCCESS;
    }
}
