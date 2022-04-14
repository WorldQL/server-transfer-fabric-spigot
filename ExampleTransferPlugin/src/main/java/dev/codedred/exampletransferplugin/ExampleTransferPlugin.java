package dev.codedred.exampletransferplugin;

import com.nftworlds.servertransferplugin.ServerTransferPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExampleTransferPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("connect")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can run this command!");
                return true;
            }
            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage("Incorrect Usage: /connect <host>");
                return true;
            }
            else {
                // send transfer packet to player with host(args[0])
                ServerTransferPlugin.sendTransferPacket(player, args[0]);
            }
        }

        return true;
    }
}
