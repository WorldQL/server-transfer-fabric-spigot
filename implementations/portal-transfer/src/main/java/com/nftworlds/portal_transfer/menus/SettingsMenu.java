package com.nftworlds.portal_transfer.menus;

import com.nftworlds.portal_transfer.PortalManager;
import com.nftworlds.portal_transfer.PortalTransfer;
import com.nftworlds.portal_transfer.models.Portal;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Map;

public class SettingsMenu implements Listener {
    private final Inventory inv;

    public SettingsMenu() {
        inv = Bukkit.createInventory(null, 9, "§5§lPortal Transfer §8§lSettings");
        initializeItems();
    }

    public void initializeItems() {
        inv.setItem(1,createItem(Material.BARRIER, "§c§lDelete §8§lPortal", " ", "§7Click to delete a portal"));
        inv.setItem(3, createItem(Material.DIAMOND_BOOTS, "§a§lLocate §8§lPortal", " ", "§7Click to locate a portal"));
        inv.setItem(5, createItem(Material.GLOW_INK_SAC, "§d§lGlow §8§lPortal", " ", "§7Click to highlight a portal"));
        //inv.setItem(7, createItem(Material.AXOLOTL_SPAWN_EGG, "§9§lChange §8§lHost", " ", "§7Click to change a portals host"));
        for (int i = 0 ; i < 9 ; i++) {
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
        if (!event.getInventory().equals(MenuManager.getInstance().getSettingsMenu().inv)) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        if (event.getRawSlot() == 1) {
            deleteMessage(player);
            event.getWhoClicked().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        }
        else if (event.getRawSlot() == 3 || event.getRawSlot() == 5) {
            Bukkit.dispatchCommand(player,"portal list");
            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        }
    }


    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().equals(MenuManager.getInstance().getSettingsMenu().inv))
            event.setCancelled(true);
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event) {
        if (event.getInventory().equals(MenuManager.getInstance().getSettingsMenu().inv))
            if (event.getReason() != InventoryCloseEvent.Reason.PLUGIN) {
               new BukkitRunnable() {
                   @Override
                   public void run() {
                       MenuManager.getInstance().getEditorMenu().openInventory(event.getPlayer());
                   }
               }.runTaskLater(PortalTransfer.getPlugin(PortalTransfer.class), 3);
            }
    }

    private void deleteMessage(Player player) {
        PortalManager manager = PortalManager.getInstance();
        player.sendMessage("\n§8§l[§a§l!§8§l] §a§nPortals:");
        for (Map.Entry<String, Portal> p : manager.getPortals().entrySet()) {
            player.spigot().sendMessage(getDeleteComponent(p.getKey()),
                    new TextComponent(" " + p.getKey() + " §7--> §f" + p.getValue().getHost()));
        }
    }

    private TextComponent getDeleteComponent(String portal) {
        TextComponent msg2 = new TextComponent("§4§l[Delete]");
        msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/portal delete " + portal));
        msg2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("Click here to delete the portal!").color(ChatColor.GRAY).italic(true).create()));
        return msg2;
    }
}
