package com.nftworlds.servertransferplugin;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public final class ServerTransferPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        if (!getServer().getMessenger().getOutgoingChannels().contains("servertransfermod:wql_channel"))
            getServer().getMessenger().registerOutgoingPluginChannel(this, "servertransfermod:wql_channel");

    }

    @Override
    public void onDisable() {
    }


    public static void sendTransferPacket(Player player, String ip_address) {
        Preconditions.checkArgument(player.isOnline(), "Player must be online to send a transfer packet.");

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(ip_address);

        CraftPlayer craftPlayer = (CraftPlayer) player;

        if (!player.getListeningPluginChannels().contains("servertransfermod:wql_channel"))
            craftPlayer.addChannel("servertransfermod:wql_channel");

        player.sendPluginMessage(ServerTransferPlugin.getPlugin(ServerTransferPlugin.class), "servertransfermod:wql_channel", out.toByteArray());

    }

    public static void sendTransferPacket(String ip_address, Player... players) {
        for(Player player : players)
            sendTransferPacket(player, ip_address);
    }

}
