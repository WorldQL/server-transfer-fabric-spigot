package com.nftworlds.portal_transfer;

import com.nftworlds.portal_transfer.commands.PortalCommand;
import com.nftworlds.portal_transfer.commands.WandCommand;
import com.nftworlds.portal_transfer.data.DataManager;
import com.nftworlds.portal_transfer.handlers.CreationHandler;
import com.nftworlds.portal_transfer.listeners.ParticlePreview;
import com.nftworlds.portal_transfer.listeners.PlayerInteract;
import com.nftworlds.portal_transfer.listeners.PlayerMove;
import com.nftworlds.portal_transfer.listeners.WandInteract;
import com.nftworlds.portal_transfer.menus.EditorMenu;
import com.nftworlds.portal_transfer.menus.MenuManager;
import com.nftworlds.portal_transfer.menus.ServerSelectorMenu;
import com.nftworlds.portal_transfer.menus.SettingsMenu;
import io.papermc.lib.PaperLib;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import static com.nftworlds.portal_transfer.ServerTransfer.WQL_CHANNEL;

public final class PortalTransfer extends JavaPlugin {

    @Override
    public void onEnable() {
        PaperLib.suggestPaper(this);

        DataManager.getInstance().loadPortals();
        MenuManager.getInstance(); // pre load servers

        registerCommands();
        registerListeners();

        MenuManager.getInstance().init(); // load server gui

        if (!getServer().getMessenger().getOutgoingChannels().contains(WQL_CHANNEL)) {
            getServer().getMessenger().registerOutgoingPluginChannel(this, WQL_CHANNEL);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {
        getCommand("portalwand").setExecutor(new WandCommand());
        getCommand("portal").setExecutor(new PortalCommand());
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ParticlePreview(), this);
        pm.registerEvents(new PlayerInteract(), this);
        pm.registerEvents(new WandInteract(), this);
        pm.registerEvents(new EditorMenu(), this);
        pm.registerEvents(new SettingsMenu(), this);
        pm.registerEvents(new ServerSelectorMenu(), this);
        pm.registerEvents(new CreationHandler(), this);
        pm.registerEvents(new PlayerMove(), this);
    }
}
