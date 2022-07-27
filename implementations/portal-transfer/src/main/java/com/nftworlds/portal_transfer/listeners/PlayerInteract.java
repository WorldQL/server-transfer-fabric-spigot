package com.nftworlds.portal_transfer.listeners;

import com.nftworlds.portal_transfer.events.WandClickEvent;
import com.nftworlds.portal_transfer.menus.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (!event.hasItem())
            return;
        if (event.getMaterial() != Material.WOODEN_AXE)
            return;
        if (!event.getItem().hasItemMeta())
            return;
        if (!event.getItem().getItemMeta().hasDisplayName())
            return;
        if (!event.getItem().getItemMeta().getDisplayName().contains("NFTWorlds Portal Wand"))
            return;
        if (!event.getPlayer().hasPermission("ptransfer.admin"))
            return;

        event.setCancelled(true);

        if (event.getAction().equals(Action.RIGHT_CLICK_AIR)
                || event.getAction().equals(Action.LEFT_CLICK_AIR)) {
            MenuManager.getInstance().getEditorMenu().openInventory(event.getPlayer());
            return;
        }

        Bukkit.getPluginManager().callEvent(
                new WandClickEvent(event.getPlayer(), event.getClickedBlock().getLocation(), event.getAction()));
    }
}
