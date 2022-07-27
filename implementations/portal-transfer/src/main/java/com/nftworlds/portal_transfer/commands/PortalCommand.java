package com.nftworlds.portal_transfer.commands;

import com.nftworlds.portal_transfer.PortalManager;
import com.nftworlds.portal_transfer.events.PortalParticleEvent;
import com.nftworlds.portal_transfer.handlers.LocateHandler;
import com.nftworlds.portal_transfer.models.Cuboid;
import com.nftworlds.portal_transfer.models.Portal;
import com.nftworlds.portal_transfer.models.Wand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PortalCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }
        if (!player.hasPermission("ptransfer.admin")) {
            player.sendMessage(org.bukkit.ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }
        PortalManager manager = PortalManager.getInstance();

//    # pt create <name-of-portal> <host>
//    # pt delete <name-of-portal>
//    # pt locate <name-of-portal>
//    # pt sethost <name-of-portal> <host>
//    # pt list
      switch(args.length) {
          case 3:
              // portal sethost <name-of-portal> <host>
              // portal create  <name-of-portal> <host>
              if (args[0].equalsIgnoreCase("create")) {
                  if (!manager.hasWand(player.getUniqueId())
                            || !manager.getWand(player.getUniqueId()).hasLocations()) {
                      player.sendMessage("§8§l[§c§l!§8§l] §cYou must set locations first with the wand!\n/portalwand");
                      break;
                  }
                  String name = args[1];
                  String host = args[2];
                  if (manager.isPortal(name))
                      name = name + "-2";

                  Wand wand = manager.getWand(player.getUniqueId());
                  manager.addPortal(name, host, new Cuboid(wand.getFirstLocation(), wand.getSecondLocation()), true);
                  player.sendMessage("§8§l[§a§l!§8§l] §aPortal created successfully!");
                  break;
              }
              if (args[0].equalsIgnoreCase("sethost")) {
                  String name = args[1];
                  if (!manager.isPortal(name)) {
                      player.sendMessage(ChatColor.RED + "A portal with the name '" + name + "' has not yet been created!\nUse `/portal create <name> <host>` first!");
                      break;
                  }
                  String host = args[2];
                  manager.getPortal(name).setHost(host);
                  player.sendMessage(ChatColor.GREEN + "Host changed to '" + host + "' for the portal '" + name.toLowerCase() + "'!");
                  break;
              }

          case 2:
              // portal locate <name-of-portal>
              // portal delete <name-of-portal>
              // portal glow <name-of-portal>
              String name = args[1];
              if (!manager.isPortal(name)) {
                  player.sendMessage(ChatColor.RED + "A portal with the name '" + name + "' has not yet been created!\nUse `/portal create <name> <host>` first!");
                  break;
              }
              if (args[0].equalsIgnoreCase("delete")) {
                  manager.deletePortal(name);
                  player.sendMessage("§8§l[§c§l!§8§l] §cPortal successfully deleted.");
                  break;
              }
              if (args[0].equalsIgnoreCase("locate")) {
                  Portal portal = manager.getPortal(name);
                  LocateHandler.sendToPortal(player, portal);
                  player.sendMessage("§8§l[§a§l!§8§l] §aTeleported 10 blocks above portal!");
                  break;
              }
              if (args[0].equalsIgnoreCase("glow")) {
                  Portal portal = manager.getPortal(name);
                  Location loc1 = new Location(portal.getCuboid().getWorld(), portal.getCuboid().getMaxX(), portal.getCuboid().getMaxY(), portal.getCuboid().getMaxZ());
                  Location loc2 = new Location(portal.getCuboid().getWorld(), portal.getCuboid().getMinX(), portal.getCuboid().getMinY(), portal.getCuboid().getMinZ());
                  Wand wand = new Wand(loc1, loc2);
                  manager.setWand(player.getUniqueId(), wand);

                  Bukkit.getPluginManager().callEvent(new PortalParticleEvent(player, player.getWorld()));
                  player.sendMessage("§8§l[§a§l!§8§l] §aPortal is now highlighted for 2 minutes!");
                  break;
              }

          case 1:
              // portal list
              // portal wand - just in case
              if (args[0].equalsIgnoreCase("list")) {
                  player.sendMessage("\n§8§l[§a§l!§8§l] §a§nPortals:");
                  for (Map.Entry<String, Portal> p : manager.getPortals().entrySet()) {
                      player.spigot().sendMessage(getLocateComponent(p.getKey()), getGlowComponent(p.getKey()),
                              new TextComponent(" " + p.getKey() + " §7--> §f" + p.getValue().getHost()));
                  }
                  break;
              }
              else if (args[0].equalsIgnoreCase("wand")) {
                  Bukkit.dispatchCommand(player, "portalwand");
                  break;
              }

          default:
              // usage case
              player.sendMessage(ChatColor.LIGHT_PURPLE + "NFTWorlds Portal Usage:" +
                      ChatColor.YELLOW + "\n/portal wand - Gives you wand creation tool" +
                      "\n/portal create <name-of-portal> <host>" +
                      "\n/portal delete <name>" +
                      "\n/portal locate <name> - teleport to a portal" +
                      "\n/portal glow <name> - highlights portal" +
                      "\n/portal sethost <name> <host> - change host for a portal" +
                      "\n/portal list");

      }
        return true;
    }

    private TextComponent getLocateComponent(String portal) {
        TextComponent msg = new TextComponent("§9§l[Locate]");
        msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/portal locate " + portal));
        msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("Click here to locate the portal!").color(ChatColor.GRAY).italic(true).create()));

        return msg;
    }

    private TextComponent getGlowComponent(String portal) {
        TextComponent msg2 = new TextComponent("§5§l[Glow]");
        msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/portal glow " + portal));
        msg2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("Click here to glow the portal!").color(ChatColor.GRAY).italic(true).create()));
        return msg2;
    }
}
