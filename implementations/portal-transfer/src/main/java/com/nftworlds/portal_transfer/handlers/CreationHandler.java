package com.nftworlds.portal_transfer.handlers;

import com.nftworlds.portal_transfer.PortalManager;
import com.nftworlds.portal_transfer.models.Cuboid;
import com.nftworlds.portal_transfer.models.Portal;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import java.util.ArrayList;
import java.util.List;

public class CreationHandler implements Listener {

    public void createPortal(Portal portal, boolean addWater) {
        List<Location> locations = new ArrayList<>();
        Cuboid cuboid = portal.getCuboid();
        for (int x = cuboid.getMinX() ; x <= cuboid.getMaxX() ; x++)
            for (int y = cuboid.getMinY() ; y <= cuboid.getMaxY() ; y++)
                for (int z = cuboid.getMinZ() ; z <= cuboid.getMaxZ() ; z++)
                    locations.add(new Location(portal.getCuboid().getWorld(), x, y, z));

        PortalManager portalManager = PortalManager.getInstance();

        for (Location loc : locations) {
            portalManager.addPortalBlock(loc.getBlock());
            if (loc.getBlock().getType().isAir()) {
                if (addWater)
                    loc.getBlock().setType(Material.WATER);
            }
        }
    }

    public void deletPortal(Portal portal) {
        List<Location> locations = new ArrayList<>();
        Cuboid cuboid = portal.getCuboid();
        for (int x = cuboid.getMinX() ; x <= cuboid.getMaxX() ; x++)
            for (int y = cuboid.getMinY() ; y <= cuboid.getMaxY() ; y++)
                for (int z = cuboid.getMinZ() ; z <= cuboid.getMaxZ() ; z++)
                    locations.add(new Location(portal.getCuboid().getWorld(), x, y, z));

        PortalManager portalManager = PortalManager.getInstance();

        for (Location loc : locations) {
            if (!loc.getBlock().getType().isSolid()) {
                loc.getBlock().setType(Material.AIR);
                portalManager.getPortalBlocks().remove(loc.getBlock());
            }
        }
    }

    @EventHandler
    public void onWaterPhysics(BlockFromToEvent e){
        if (e.getBlock().getType() != Material.WATER)
            return;
        if (PortalManager.getInstance().getPortalBlocks().contains(e.getBlock()))
            e.setCancelled(true);
    }
}
