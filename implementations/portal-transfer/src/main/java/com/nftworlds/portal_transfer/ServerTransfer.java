package com.nftworlds.portal_transfer;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Objects;

public class ServerTransfer {
    public static final String WQL_CHANNEL = "server-transfer-mod:wql_channel";

    /**
     * @param ip_address IP Address to connect to
     * @param player     Player to send connect packets to
     */
    public static void sendTransferPacket(@NotNull String ip_address, @NotNull Player player) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(ip_address);
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

        player.sendPluginMessage(PortalTransfer.getPlugin(PortalTransfer.class), WQL_CHANNEL, out.toByteArray());
    }

    /**
     * @param ip_address IP Address to connect to
     * @param players    Players to send connect packets to
     */
    public static void sendTransferPacket(@NotNull String ip_address, Player @NotNull ... players) {
        for (Player player : players) {
            sendTransferPacket(ip_address, player);
        }
    }
}
