package com.nftworlds.simple_transfer.commands;

import com.nftworlds.server_transfer.plugin.ServerTransferPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Connect implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can run this command!");
            return true;
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage("Incorrect Usage: /connect <host>");
            return true;
        }
        else
            ServerTransferPlugin.sendTransferPacket(args[0], player);


        return true;
    }
}
