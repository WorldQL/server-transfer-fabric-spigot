package com.nftworlds.portal_transfer.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class EditorMenu implements Listener {
    public final Inventory inv;

    public EditorMenu() {
        inv = Bukkit.createInventory(null, 27, "§5§lPortal Transfer §8§lEditor");
        initializeItems();
    }

    public void initializeItems() {
        inv.setItem(10,createItem(Material.COMMAND_BLOCK, "§8§lMore §6§lSettings", " ", "§7Click to view more options"));
        inv.setItem(13, createItem(Material.PAPER, "§8§lCreate §5§lPortal", " ", "§7Click to create a new portal"));
        inv.setItem(16, createItem(Material.IRON_HELMET, "§8§lList §d§lPortals", " ", "§7List all current portals"));
        for (int i = 0 ; i < 27 ; i++) {
            if (inv.getItem(i) == null)
                inv.setItem(i, createItem(Material.BLACK_STAINED_GLASS_PANE, " ", " "));
        }

    }

    protected ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();

        if (!material.equals(Material.BLACK_STAINED_GLASS_PANE)) {
            meta.addEnchant(Enchantment.DURABILITY, 1, false);
            meta.setLore(Arrays.asList(lore));
        }

        meta.setDisplayName(name);

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);


        item.setItemMeta(meta);

        return item;
    }


    public void openInventory(HumanEntity player) {
        player.openInventory(inv);
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getInventory().equals(MenuManager.getInstance().getEditorMenu().inv)) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        if (event.getRawSlot() == 10) {
            MenuManager.getInstance().getSettingsMenu().openInventory(player);
        }
        else if (event.getRawSlot() == 13) {
            // open server selector menu
            MenuManager.getInstance().getSelectorMenu().openInventory(player);
        }
        else if (event.getRawSlot() == 16) {
            Bukkit.dispatchCommand(player,"portal list");
            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        }
    }


    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (e.getInventory().equals(MenuManager.getInstance().getEditorMenu().inv)) {
            e.setCancelled(true);
        }
    }
}