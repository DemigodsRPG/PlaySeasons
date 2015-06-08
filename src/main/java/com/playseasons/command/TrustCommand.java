package com.playseasons.command;

import com.censoredsoftware.library.command.type.BaseCommand;
import com.censoredsoftware.library.command.type.CommandResult;
import com.demigodsrpg.chitchat.Chitchat;
import com.playseasons.PlaySeasons;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrustCommand extends BaseCommand {
    @Override
    protected CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (command.getName().equals("trust")) {
            // Needs at least 1 argument
            if (args.length < 1) {
                return CommandResult.INVALID_SYNTAX;
            }

            // Get the invitee
            Player invitee = Bukkit.getPlayer(args[0]);
            if (invitee == null) {
                sender.sendMessage(ChatColor.RED + "Player either offline or does not exist, please try again later.");
                return CommandResult.QUIET_ERROR;
            }

            if (!sender.hasPermission("seasons.admin")) {
                return CommandResult.NO_PERMISSIONS;
            }

            // Register from player
            else {
                if (!PlaySeasons.getPlayerRegistry().isVisitor(invitee)) {
                    PlaySeasons.getPlayerRegistry().fromPlayer(invitee).get().setTrusted(true);
                } else {
                    sender.sendMessage(ChatColor.RED + "That player is still a visitor.");
                    return CommandResult.QUIET_ERROR;
                }
            }

            Chitchat.sendTitle(invitee, 10, 80, 10, ChatColor.YELLOW + "Celebrate!", ChatColor.GREEN + "You are now trusted!");

            // If this is reached, the invite worked
            sender.sendMessage(ChatColor.RED + invitee.getName() + " has been trusted.");

            return CommandResult.SUCCESS;
        }

        return CommandResult.ERROR;
    }
}
