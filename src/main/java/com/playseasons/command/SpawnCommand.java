package com.playseasons.command;

import com.demigodsrpg.command.type.BaseCommand;
import com.demigodsrpg.command.type.CommandResult;
import com.playseasons.impl.PlaySeasons;
import com.playseasons.util.RegionUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class SpawnCommand extends BaseCommand {

    final PlaySeasons plugin;

    public SpawnCommand(PlaySeasons plugin) {
        this.plugin = plugin;
    }

    @Override
    protected CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (command.getName().equals("spawn")) {
            if (sender instanceof ConsoleCommandSender) {
                return CommandResult.PLAYER_ONLY;
            }
            if (plugin.getPlayerRegistry().isVisitorOrExpelled((Player) sender)) {
                sender.sendMessage(ChatColor.YELLOW + "Currently you are just a " + ChatColor.GRAY + ChatColor.ITALIC +
                        "visitor" + ChatColor.YELLOW + ", ask for an invite!");
                return CommandResult.QUIET_ERROR;
            }

            ((Player) sender).teleport(RegionUtil.spawnLocation());
            sender.sendMessage(ChatColor.YELLOW + "Warped to spawn.");
        }
        return CommandResult.SUCCESS;
    }
}
