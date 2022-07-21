package com.nftworlds.portal_transfer.listeners;

import com.nftworlds.portal_transfer.PortalManager;
import com.nftworlds.portal_transfer.events.PortalParticleEvent;
import com.nftworlds.portal_transfer.events.WandClickEvent;
import com.nftworlds.portal_transfer.models.Wand;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;

import java.util.*;

public class WandInteract implements Listener {

    @EventHandler
    public void onWandInteract(WandClickEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        PortalManager manager = PortalManager.getInstance();

        Wand wand = new Wand(null,null);
        if (manager.hasWand(uuid))
            wand = manager.getWand(uuid);

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            wand.setFirstLocation(event.getLocation());
            event.getPlayer().sendMessage("§8§l[§a§l!§8§l] §aPosition §lone §aset!");
        }
        else {
            wand.setSecondLocation(event.getLocation());
            event.getPlayer().sendMessage("§8§l[§a§l!§8§l] §aPosition §ltwo §aset!");
        }

        manager.setWand(uuid, wand);

        //display particles if two locations were selected
        if (manager.getWand(uuid).hasLocations())
            Bukkit.getPluginManager().callEvent(new PortalParticleEvent(event.getPlayer(), event.getPlayer().getWorld()));

    }
}
