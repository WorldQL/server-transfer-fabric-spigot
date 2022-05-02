package com.nftworlds.server_transfer.mod;

import com.nftworlds.server_transfer.mod.network.ServerTransferReader;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerTransferMod implements ModInitializer {
    public static final String ID = "server-transfer-mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    public static Identifier id(String name) {
        return new Identifier(ID, name);
    }

    @Override
    public void onInitialize() {
        ServerTransferReader reader = new ServerTransferReader();
        reader.register();
    }
}
