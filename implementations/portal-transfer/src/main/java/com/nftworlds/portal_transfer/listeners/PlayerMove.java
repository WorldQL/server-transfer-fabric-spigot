package com.nftworlds.portal_transfer.listeners;

import com.nftworlds.portal_transfer.PortalManager;
import com.nftworlds.portal_transfer.ServerTransfer;
import com.nftworlds.portal_transfer.models.Portal;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Map;

public class PlayerMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!event.getTo().getBlock().isLiquid())
            return;
        if (event.getFrom().getBlock().isLiquid())
            return;
        if (!PortalManager.getInstance().getPortalBlocks().contains(event.getTo().getBlock()))
            return;

        PortalManager manager = PortalManager.getInstance();
        Portal portal = null;
        for (Map.Entry<String, Portal> p : manager.getPortals().entrySet()) {
            if (p.getValue().getCuboid().contains(event.getTo())) {
                portal = p.getValue();
                break;
            }
        }

        if (portal == null)
            return;

        event.setCancelled(true);
        Bukkit.dispatchCommand(event.getPlayer(), "spawn");
        ServerTransfer.sendTransferPacket(portal.getHost(), event.getPlayer());
    }

}



















