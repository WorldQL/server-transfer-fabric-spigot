package com.nftworlds.server_transfer.mod.network;

import com.nftworlds.server_transfer.mod.ServerTransferMod;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.FatalErrorScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.Address;
import net.minecraft.client.network.AllowedAddressResolver;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.MessageType;
import net.minecraft.network.NetworkState;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.nftworlds.server_transfer.mod.ServerTransferMod.LOGGER;

/***
 * Developed by @WorldQL
 *
 * This class receives packets being sent from the spigot plugin.
 * If the packet is valid then we will transfer the client to another server.
 */
public class ServerTransferReader {
    public static final Identifier WQL_CHANNEL = ServerTransferMod.id("wql_channel");
    public static AtomicInteger CONNECTOR_THREADS_COUNT = new AtomicInteger(0);

    public void register() {
        ClientPlayNetworking.registerGlobalReceiver(WQL_CHANNEL, (client, handler, buf, responseSender) -> {
            String host = new String(buf.getWrittenBytes());
            host = host.substring(2);

            // Send message to client
            MinecraftClient mc = MinecraftClient.getInstance();

            assert mc.player != null;
            mc.inGameHud.addChatMessage(MessageType.SYSTEM, Text.of("Attempting to connect to: " + host), mc.player.getUuid());

            MultiplayerScreen multiplayerScreen = new MultiplayerScreen(new TitleScreen(false));
            ServerAddress address = ServerAddress.parse(host);

            connectToServer(client, address, multiplayerScreen);
        });
    }

    private void connectToServer(MinecraftClient client, ServerAddress address, Screen screen) {
        LOGGER.info("Connecting to {}, {}", address.getAddress(), address.getPort());

        Thread thread = new Thread("Server Connector #" + CONNECTOR_THREADS_COUNT.incrementAndGet()) {
            @Override
            public void run() {
                InetSocketAddress inetSocketAddress;

                try {
                    Optional<InetSocketAddress> optional = AllowedAddressResolver.DEFAULT.resolve(address).map(Address::getInetSocketAddress);
                    if (optional.isEmpty()) {
                        client.execute(() -> client.setScreen(
                                new FatalErrorScreen(Text.of("Cannot connect to server."),
                                Text.of("Client was not able to connect to '" + address.getAddress() + "'"))
                        ));

                        return;
                    }

                    inetSocketAddress = optional.get();
                    ClientConnection connection = ClientConnection.connect(inetSocketAddress, client.options.shouldUseNativeTransport());

                    connection.setPacketListener(new ClientLoginNetworkHandler(connection, client, screen, ServerTransferReader.this::setStatus));
                    connection.send(new HandshakeC2SPacket(inetSocketAddress.getHostName(), inetSocketAddress.getPort(), NetworkState.LOGIN));
                    connection.send(new LoginHelloC2SPacket(client.getSession().getProfile()));

                    if (client.world != null) {
                        client.world.disconnect();
                    }
                } catch (Exception exception) {
                    client.execute(() -> client.setScreen(new FatalErrorScreen(Text.of("Cannot connect to server."),
                            Text.of("Client was not able to connect to '" + address.getAddress() + "'"))));
                }
            }
        };

        thread.start();
    }

    private void setStatus(Text status) {
    }
}
