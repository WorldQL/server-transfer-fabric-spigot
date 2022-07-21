package com.nftworlds.portal_transfer.handlers;

import com.nftworlds.portal_transfer.PortalTransfer;
import com.nftworlds.portal_transfer.models.Portal;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LocateHandler {

    public static void sendToPortal(Player player, Portal portal) {
        player.teleport(portal.getCuboid().getLocateLocation());
        player.getLocation().setYaw(90);
        Location ploc = player.getLocation().clone();
        Block block = ploc.subtract(0,1,0).getBlock();
        if (block.getType().isAir()) {
            block.setType(Material.GLASS);
            ArmorStand holo = (ArmorStand) player.getWorld().spawnEntity(block.getLocation().subtract(0,1,0), EntityType.ARMOR_STAND);
            holo.setVisible(false);
            holo.setCustomNameVisible(true);
            holo.setGravity(false);
            holo.setCustomName("§8§l[§c§l!§8§l] §cBlock will remove in 60 seconds.");
            removeBlock(block, holo); // removed block after 60 seconds
        }
    }

    /***
     * Cleans up temporary glass block.
     * @param block - glass block placed from locate command
     */
    private static void removeBlock(Block block, ArmorStand holo) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!block.getType().isAir())
                    block.setType(Material.AIR);
                holo.remove();
            }
        }.runTaskLater(PortalTransfer.getPlugin(PortalTransfer.class), 1200);
    }
}
