package com.nftworlds.portal_transfer.listeners;

import com.nftworlds.portal_transfer.PortalManager;
import com.nftworlds.portal_transfer.PortalTransfer;
import com.nftworlds.portal_transfer.events.PortalParticleEvent;
import com.nftworlds.portal_transfer.models.Cuboid;
import com.nftworlds.portal_transfer.models.Wand;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class ParticlePreview implements Listener {

    private static final Map<UUID, Integer> IDS = new HashMap<>();
    int taskID;

    @EventHandler
    public void onPreview(PortalParticleEvent event) {
        PortalManager manager = PortalManager.getInstance();
        Wand wand = manager.getWand(event.getPlayer().getUniqueId());
        UUID uuid = event.getPlayer().getUniqueId();

        List<Location> locations = new ArrayList<>();
        Cuboid cuboid = new Cuboid(wand.getFirstLocation(), wand.getSecondLocation());
        for (int x = cuboid.getMinX() ; x <= cuboid.getMaxX() + 1 ; x++)
            for (int y = cuboid.getMinY() ; y <= cuboid.getMaxY() + 1 ; y++)
                for (int z = cuboid.getMinZ() ; z <= cuboid.getMaxZ() + 1 ; z++)
                    locations.add(new Location(event.getWorld(), x, y, z));

        // don't load particles if portal is too big.
        if (locations.size() > 250) // if we wanted it could expand 5000 blocks :')
            return;

        if (IDS.containsKey(uuid)) {
            Bukkit.getScheduler().cancelTask(IDS.get(uuid));
            IDS.remove(uuid);
        }
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(PortalTransfer.getPlugin(PortalTransfer.class), new Runnable() {
            int timer = 0;
            final Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(0, 155, 255), 1);

            @Override
            public void run() {
                if (timer >= 480) {
                    Bukkit.getScheduler().cancelTask(taskID);
                    IDS.remove(uuid);
                }
                if (!IDS.containsKey(uuid))
                    IDS.put(uuid, taskID);

                timer++;
                for (Location loc : locations)
                    event.getPlayer().spawnParticle(Particle.REDSTONE, loc.getX(), loc.getY(), loc.getZ(), 1, dustOptions);
            }
        }, 0 ,5);
    }
}
