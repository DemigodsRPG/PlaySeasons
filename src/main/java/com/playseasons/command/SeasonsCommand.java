package com.playseasons.command;


import com.demigodsrpg.command.type.BaseCommand;
import com.demigodsrpg.command.type.CommandResult;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SeasonsCommand extends BaseCommand {
    @Override
    protected CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        sender.sendMessage(ChatColor.YELLOW + "Welcome to" + ChatColor.DARK_GREEN + " Seasons" + ChatColor.YELLOW + "!");
        sender.sendMessage(ChatColor.DARK_GREEN + "Seasons" + ChatColor.YELLOW + " is a Coop-Survival Minecraft server.");
        sender.sendMessage(ChatColor.YELLOW + "Work with your friends to survive unique challenges!");
        return CommandResult.SUCCESS;
    }
}
