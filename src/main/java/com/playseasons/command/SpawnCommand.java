package com.playseasons.command;

import com.censoredsoftware.library.command.type.BaseCommand;
import com.censoredsoftware.library.command.type.CommandResult;
import com.playseasons.PlaySeasons;
import com.playseasons.util.RegionUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends BaseCommand {
    @Override
    protected CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (command.getName().equals("spawn")) {
            if (sender instanceof ConsoleCommandSender) {
                return CommandResult.PLAYER_ONLY;
            }
            if (PlaySeasons.getPlayerRegistry().isVisitorOrExpelled((Player) sender)) {
                sender.sendMessage(ChatColor.YELLOW + "Currently you are just a " + ChatColor.GRAY + ChatColor.ITALIC + "visitor" + ChatColor.YELLOW + ", ask for an invite!");
                return CommandResult.QUIET_ERROR;
            }

            ((Player) sender).teleport(RegionUtil.spawnLocation());
            sender.sendMessage(ChatColor.YELLOW + "Warped to spawn.");
        }
        return CommandResult.SUCCESS;
    }
}
