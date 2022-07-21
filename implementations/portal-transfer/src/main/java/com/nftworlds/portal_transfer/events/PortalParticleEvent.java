package com.nftworlds.portal_transfer.events;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
public class PortalParticleEvent extends Event implements Cancellable {

    private final Player player;

    private final World world;
    private boolean isCancelled;

    public PortalParticleEvent(Player player, World world) {
        this.player = player;
        this.world = world;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return player;
    }

    public World getWorld() {
        return world;
    }

}
