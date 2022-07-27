package com.nftworlds.portal_transfer.data;

import com.nftworlds.portal_transfer.PortalManager;
import com.nftworlds.portal_transfer.PortalTransfer;
import com.nftworlds.portal_transfer.models.Cuboid;
import com.nftworlds.portal_transfer.models.Portal;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class DataManager {

    private static DataManager instance;

    private DataManager() {}

    public static synchronized DataManager getInstance() {
        if (instance == null)
            instance = new DataManager();
        return instance;
    }

   // private final CustomConfig config = new CustomConfig(PortalTransfer.getPlugin(PortalTransfer.class), "config.yml");
    private final CustomConfig data = new CustomConfig(PortalTransfer.getPlugin(PortalTransfer.class), "data.yml");

   // public FileConfiguration getConfig() {
   //     return config.getConfig();
  //  }

   // public CustomConfig config() {
   //     return config;
   // }

    public FileConfiguration getData() {
        return data.getConfig();
    }

    public CustomConfig data() {
        return data;
    }

    public void savePortal(Portal portal) {
        getData().set("data." + portal.getName() + ".host", portal.getHost());
        getData().set("data." + portal.getName() + ".cuboid.world", portal.getCuboid().getWorld().getName());
        getData().set("data." + portal.getName() + ".cuboid.minX", portal.getCuboid().getMinX());
        getData().set("data." + portal.getName() + ".cuboid.minY", portal.getCuboid().getMinY());
        getData().set("data." + portal.getName() + ".cuboid.minZ", portal.getCuboid().getMinZ());
        getData().set("data." + portal.getName() + ".cuboid.maxX", portal.getCuboid().getMaxX());
        getData().set("data." + portal.getName() + ".cuboid.maxY", portal.getCuboid().getMaxY());
        getData().set("data." + portal.getName() + ".cuboid.maxZ", portal.getCuboid().getMaxZ());
        data().saveConfig();
    }

    public void deletePortal(String name) {
        getData().set("data." + name.toLowerCase(), null);
        data().saveConfig();
    }

    public void loadPortals() {
        if (!getData().contains("data"))
            return;
        PortalManager manager = PortalManager.getInstance();
        getData().getConfigurationSection("data").getKeys(false).forEach(key ->{
            Cuboid cuboid = new Cuboid(getData().getInt("data." + key + ".cuboid.minX"),
                    getData().getInt("data." + key + ".cuboid.minY"),
                    getData().getInt("data." + key + ".cuboid.minZ"),
                    getData().getInt("data." + key + ".cuboid.maxX"),
                    getData().getInt("data." + key + ".cuboid.maxY"),
                    getData().getInt("data." + key + ".cuboid.maxZ"),
                    Bukkit.getWorld(getData().getString("data." + key + ".cuboid.world")));
            manager.addPortal(key, getData().getString("data." + key + ".host"), cuboid, false);
        });
    }
}
