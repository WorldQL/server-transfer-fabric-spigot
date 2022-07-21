package com.nftworlds.portal_transfer.models;

import org.bukkit.Location;

public class Wand {

    private Location loc1;
    private Location loc2;

    public Wand(Location loc1, Location loc2) {
        this.loc1 = loc1;
        this.loc2 = loc2;
    }

    public Location getFirstLocation() {
        return loc1;
    }

    public Location getSecondLocation() {
        return loc2;
    }

    public void setFirstLocation(Location loc) {
        loc1 = loc;
    }

    public void setSecondLocation(Location loc) {
        loc2 = loc;
    }

    public boolean hasLocations() {
        if (loc1 != null && loc2 != null)
            return true;
        return false;
    }
}
