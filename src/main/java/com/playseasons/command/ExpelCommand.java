package com.playseasons.command;

import com.censoredsoftware.library.command.type.BaseCommand;
import com.censoredsoftware.library.command.type.CommandResult;
import com.demigodsrpg.chitchat.Chitchat;
import com.playseasons.PlaySeasons;
import com.playseasons.model.PlayerModel;
import com.playseasons.util.RegionUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class ExpelCommand extends BaseCommand {
    @Override
    protected CommandResult onCommand(CommandSender sender, Command command, String[] args) {
        if (command.getName().equals("expel")) {
            // Needs at least 1 argument
            if (args.length < 1) {
                return CommandResult.INVALID_SYNTAX;
            }

            // Get the player to be expelled
            Optional<PlayerModel> model = PlaySeasons.getPlayerRegistry().fromName(args[0]);
            if (model.isPresent()) {
                sender.sendMessage(ChatColor.RED + "Player is still a visitor.");
                return CommandResult.QUIET_ERROR;
            }
            OfflinePlayer expelled = model.get().getOfflinePlayer();

            // Unregister from console
            if (sender instanceof ConsoleCommandSender || PlaySeasons.getPlayerRegistry().isTrusted((Player) sender)) {
                PlaySeasons.getPlayerRegistry().unregister(model.get());
            }

            // Stop untrusted from expelling
            if (!PlaySeasons.getPlayerRegistry().isTrusted((Player) sender)) {
                sender.sendMessage(ChatColor.RED + "Sorry, you aren't (yet) a trusted player.");
                return CommandResult.QUIET_ERROR;
            }

            if (expelled.isOnline()) {
                expelled.getPlayer().teleport(RegionUtil.visitingLocation());
                Chitchat.sendTitle(expelled.getPlayer(), 10, 80, 10, ChatColor.RED + "Expelled.", ChatColor.YELLOW + "You were expelled, go away.");
            }
            // If this is reached, the invite worked
            sender.sendMessage(ChatColor.RED + expelled.getName() + " has been expelled.");

            return CommandResult.SUCCESS;
        }

        return CommandResult.ERROR;
    }
}
