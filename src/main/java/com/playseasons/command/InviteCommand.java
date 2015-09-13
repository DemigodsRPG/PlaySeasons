package com.playseasons.command;

import com.censoredsoftware.library.command.type.BaseCommand;
import com.censoredsoftware.library.command.type.CommandResult;
import com.demigodsrpg.chitchat.Chitchat;
import com.playseasons.impl.PlaySeasons;
import com.playseasons.model.PlayerModel;
import com.playseasons.util.RegionUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class InviteCommand extends BaseCommand {

    final PlaySeasons plugin;

    public InviteCommand(PlaySeasons plugin) {
        this.plugin = plugin;
    }

    @Override
    protected CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (command.getName().equals("invite")) {
            // Needs at least 1 argument
            if (args.length < 1) {
                return CommandResult.INVALID_SYNTAX;
            }

            // Get the invitee
            OfflinePlayer invitee = Bukkit.getOfflinePlayer(args[0]);

            // Already invited
            if (!plugin.getPlayerRegistry().isVisitor(invitee)) {
                sender.sendMessage(ChatColor.RED + "That player is already invited.");
                return CommandResult.QUIET_ERROR;
            }

            // Check if they were expelled and give a warning
            if (plugin.getPlayerRegistry().isExpelled(invitee)) {
                sender.sendMessage(ChatColor.RED + "That player was expelled, please be cautious of them.");
                Optional<PlayerModel> opModel = plugin.getPlayerRegistry().fromPlayer(invitee);
                if (opModel.isPresent()) {
                    PlayerModel expelled = opModel.get();
                    expelled.setExpelled(false);
                    if (sender instanceof ConsoleCommandSender) {
                        expelled.setInvitedFrom("CONSOLE");
                    } else {
                        expelled.setInvitedFrom(((Player) sender).getUniqueId().toString());
                    }
                }
            }

            // Register from console
            if (sender instanceof ConsoleCommandSender) {
                plugin.getPlayerRegistry().inviteConsole(invitee);
            }

            // Stop untrusted from inviting
            else if (!plugin.getPlayerRegistry().isTrusted((Player) sender)) {
                sender.sendMessage(ChatColor.RED + "Sorry, you aren't (yet) a trusted player.");
                return CommandResult.QUIET_ERROR;
            }

            // Register from player
            else {
                plugin.getPlayerRegistry().invite(invitee, (Player) sender);
            }

            // Let the invitee know
            if (invitee.isOnline()) {
                invitee.getPlayer().teleport(RegionUtil.spawnLocation());
                Chitchat.sendTitle(invitee.getPlayer(), 10, 80, 10, ChatColor.YELLOW + "Celebrate!", ChatColor.GREEN +
                        "You were invited! Have fun!");
            }

            // If this is reached, the invite worked
            sender.sendMessage(ChatColor.RED + invitee.getName() + " has been invited.");

            return CommandResult.SUCCESS;
        }

        return CommandResult.ERROR;
    }
}
