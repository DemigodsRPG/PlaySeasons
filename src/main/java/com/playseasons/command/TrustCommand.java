package com.playseasons.command;

import com.demigodsrpg.chitchat.Chitchat;
import com.demigodsrpg.command.type.BaseCommand;
import com.demigodsrpg.command.type.CommandResult;
import com.playseasons.impl.PlaySeasons;
import com.playseasons.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class TrustCommand extends BaseCommand {

    final PlaySeasons plugin;

    public TrustCommand(PlaySeasons plugin) {
        this.plugin = plugin;
    }

    @Override
    protected CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (command.getName().equals("trust")) {
            // Needs at least 1 argument
            if (args.length < 1) {
                return CommandResult.INVALID_SYNTAX;
            }

            // Get the invitee
            Optional<PlayerModel> model = plugin.getPlayerRegistry().fromName(args[0]);
            if (!model.isPresent()) {
                sender.sendMessage(ChatColor.RED + "Player is still a visitor, please try again later.");
                return CommandResult.QUIET_ERROR;
            } else if (model.get().isExpelled()) {
                sender.sendMessage(ChatColor.RED + "Player is expelled, please try a different name.");
                return CommandResult.QUIET_ERROR;
            }
            OfflinePlayer invitee = model.get().getOfflinePlayer();

            if (!sender.hasPermission("seasons.admin")) {
                return CommandResult.NO_PERMISSIONS;
            }

            // Register from player
            else {
                model.get().setTrusted(true);
            }

            // If they are online, let them know
            if (invitee.isOnline()) {
                Chitchat.sendTitle(invitee.getPlayer(), 10, 80, 10, ChatColor.YELLOW + "Celebrate!", ChatColor.GREEN +
                        "You are now trusted!");
            }

            // If this is reached, the invite worked
            sender.sendMessage(ChatColor.RED + invitee.getName() + " has been trusted.");

            return CommandResult.SUCCESS;
        }

        return CommandResult.ERROR;
    }
}
