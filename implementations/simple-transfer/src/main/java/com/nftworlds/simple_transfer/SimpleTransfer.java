package com.nftworlds.simple_transfer;

import com.nftworlds.simple_transfer.commands.Connect;
import com.nftworlds.simple_transfer.commands.ConnectTab;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleTransfer extends JavaPlugin {
    @Override
    public void onEnable() {
        getCommand("connect").setExecutor(new Connect());
        getCommand("connect").setTabCompleter(new ConnectTab());
    }

    @Override
    public void onDisable() {

    }

}
