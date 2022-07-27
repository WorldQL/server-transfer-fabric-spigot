package com.nftworlds.portal_transfer;

import com.nftworlds.portal_transfer.data.DataManager;
import com.nftworlds.portal_transfer.handlers.CreationHandler;
import com.nftworlds.portal_transfer.models.Cuboid;
import com.nftworlds.portal_transfer.models.Portal;
import com.nftworlds.portal_transfer.models.Wand;
import org.bukkit.block.Block;

import java.util.*;

public class PortalManager {

    public static PortalManager instance;

    private PortalManager() {}

    public static synchronized PortalManager getInstance() {
        if (instance == null)
            instance = new PortalManager();
        return instance;
    }

    private final Map<UUID, Wand> wands = new HashMap<>();
    private final Map<String, Portal> portals = new HashMap<>();
    private final List<Block> portalBlocks = new ArrayList<>();

    public boolean hasWand(UUID uuid) {
        return wands.containsKey(uuid);
    }

    public Wand getWand(UUID uuid) {
        return wands.get(uuid);
    }

    public void setWand(UUID uuid, Wand wand) {
        wands.put(uuid, wand);
    }

    public Portal getPortal(String name) {
        return portals.get(name.toLowerCase());
    }

    public boolean isPortal(String name) {
        return portals.containsKey(name.toLowerCase());
    }

    public void addPortal(String name, String host, Cuboid cuboid, boolean addWater) {
        Portal portal = new Portal(cuboid, name, host);
        portals.put(name, portal);
        DataManager.getInstance().savePortal(portal);
        new CreationHandler().createPortal(portal, addWater);
    }

    public void deletePortal(String name) {
        new CreationHandler().deletPortal(portals.get(name));
        portals.remove(name.toLowerCase());
        DataManager.getInstance().deletePortal(name);
    }

    public Map<String, Portal> getPortals() {
        return portals;
    }


    public List<Block> getPortalBlocks() {
        return portalBlocks;
    }

    public void addPortalBlock(Block b) {
        portalBlocks.add(b);
    }
}
