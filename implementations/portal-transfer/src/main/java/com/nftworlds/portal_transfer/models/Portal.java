package com.nftworlds.portal_transfer.models;

public class Portal {

    private final Cuboid cuboid;
    private final String name;
    private String host;

    public Portal(Cuboid cuboid, String name, String host) {
        this.cuboid = cuboid;
        this.name = name;
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public String getName() {
        return name;
    }

    public Cuboid getCuboid() {
        return cuboid;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
