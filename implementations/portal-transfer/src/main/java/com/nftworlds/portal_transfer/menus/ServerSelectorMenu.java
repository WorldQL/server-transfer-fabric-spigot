package com.nftworlds.portal_transfer.menus;

import com.nftworlds.portal_transfer.models.Server;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerSelectorMenu implements Listener {

    public static List<Inventory> serverGUI;

    public ServerSelectorMenu() {
        serverGUI = new ArrayList<>();
    }

    public void initializeItems() {
        List<Server> servers = MenuManager.getInstance().serverList;

        int size = servers.size();
        double total = size/54.0;

        if (total > 1) {
            while (servers.size() > 45) {
                Inventory inv = Bukkit.createInventory(null, 54, "§6§lServer Selector");
                inv.setItem(53, createItem(Material.ARROW, "§e§oNext Page", " ", "§7Click to view next page"));
                if (!serverGUI.isEmpty())
                    inv.setItem(45, createItem(Material.BOOK, "§c§oGo Back", " ", "§7Click to view previous page"));

                int c = 0;
                List<Server> newList = servers.subList(0, 45);
                for (Server s : newList) {
                    String desc = s.description();
                    if (desc.length() >= 45) {
                        desc = desc.substring(0, 45);
                        desc = desc + "...";
                    }

                    inv.setItem(c, createItem(Material.CACTUS, "§f§l" + s.name(), " ", "§7Host: §9§o" + s.host(), "§7Description -",
                    "    §b" + desc, "§7Player count: §d§l" + s.playersCount()));
                    c++;
                }
                servers = servers.subList(45, servers.size());
                serverGUI.add(inv);
            }
        }

        Inventory inv = Bukkit.createInventory(null, 54, "§6§lServer Selector");
        if (!serverGUI.isEmpty())
            inv.setItem(45, createItem(Material.BOOK, "§c§oGo Back", " ", "§7Click to view previous page"));
        int c = 0;
        for (Server s : servers) {
            String desc = s.description();
            if (desc.length() >= 45) {
                desc = desc.substring(0, 45);
                desc = desc + "...";
            }

            inv.setItem(c, createItem(Material.CACTUS, "§f§l" + s.name(), " ", "§7Host: §9§o" + s.host(), "§7Description -",
                    "   §b" + desc, "§7Player count: §d§l" + s.playersCount()));
            c++;
        }

        serverGUI.add(inv);
    }

    public void openInventory(HumanEntity player) {
        player.openInventory(serverGUI.get(0));
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

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!serverGUI.contains(event.getInventory()))
            return;
        if (event.getCurrentItem() == null)
            return;
        event.setCancelled(true);
        if (event.getInventory().getType() == InventoryType.PLAYER)
            return;
        Player player = (Player) event.getWhoClicked();

        if (event.getSlot() == 53) {
            // next page
            if (serverGUI.size() > 1) {
                int count = 0;
                for (Inventory inv : serverGUI) {
                    if (inv == event.getInventory()) {
                        if (serverGUI.get(count + 1) != null) {
                            player.openInventory(serverGUI.get(count + 1));
                            return;
                        }
                    }
                    count++;
                }
            }
        }

        else if (event.getSlot() == 45) {
            // back page
            if (serverGUI.size() > 1) {
                int count = 0;
                for (Inventory inv : serverGUI) {
                    if (inv == event.getInventory()) {
                        if (serverGUI.get(count - 1) != null) {
                            player.openInventory(serverGUI.get(count - 1));
                            return;
                        }
                    }
                    count++;
                }
            }
        }
        else {
            String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            Server server = null;
            for (Server s : MenuManager.getInstance().serverList) {
                if (s.name().equals(name)) {
                    server = s;
                    break;
                }
            }
            String serverName = server.name().replace(" ", "-");
            if (serverName.endsWith("-"))
                serverName = serverName.substring(0, serverName.length()-1);

            Bukkit.dispatchCommand(player, "portal create " + serverName.toLowerCase() + " " + server.host());
            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        }

    }
}
