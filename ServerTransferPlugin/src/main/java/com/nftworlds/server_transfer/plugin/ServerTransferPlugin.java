package com.nftworlds.server_transfer.plugin;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.papermc.lib.PaperLib;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;

public final class ServerTransferPlugin extends JavaPlugin {
    private static final String WQL_CHANNEL = "servertransfermod:wql_channel";

    @Override
    public void onEnable() {
        PaperLib.suggestPaper(this);

        if (!getServer().getMessenger().getOutgoingChannels().contains(WQL_CHANNEL)) {
            getServer().getMessenger().registerOutgoingPluginChannel(this, WQL_CHANNEL);
        }
    }

    @Override
    public void onDisable() {}

    public static void sendTransferPacket(Player player, String ip_address) {
        Preconditions.checkArgument(player.isOnline(), "Player must be online to send a transfer packet.");

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(ip_address);

        if (!player.getListeningPluginChannels().contains(WQL_CHANNEL)) {
            try {
                Method method = player.getClass().getDeclaredMethod("addChannel", String.class);
                method.invoke(player, WQL_CHANNEL);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        player.sendPluginMessage(ServerTransferPlugin.getPlugin(ServerTransferPlugin.class), WQL_CHANNEL, out.toByteArray());
    }

    public static void sendTransferPacket(String ip_address, Player... players) {
        for (Player player : players) {
            sendTransferPacket(player, ip_address);
        }
    }
}
