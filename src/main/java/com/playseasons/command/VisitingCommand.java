package com.playseasons.command;

import com.demigodsrpg.command.type.BaseCommand;
import com.demigodsrpg.command.type.CommandResult;
import com.playseasons.util.RegionUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class VisitingCommand extends BaseCommand {
    @Override
    protected CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (command.getName().equals("visiting")) {
            if (sender instanceof ConsoleCommandSender) {
                return CommandResult.PLAYER_ONLY;
            }

            ((Player) sender).teleport(RegionUtil.visitingLocation());
            sender.sendMessage(ChatColor.YELLOW + "Warped to visiting spawn.");
        }
        return CommandResult.SUCCESS;
    }
}
