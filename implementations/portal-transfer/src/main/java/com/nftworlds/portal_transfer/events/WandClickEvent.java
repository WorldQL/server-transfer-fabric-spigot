package com.nftworlds.portal_transfer.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;


public class WandClickEvent extends Event implements Cancellable {
	
    private final Player player;
    private boolean isCancelled;
    private final Location loc;
    private final Action action;
	
	public WandClickEvent(Player player, Location loc, Action action) {
		this.player = player;
		this.loc = loc;
        this.action = action;
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
    
    public Location getLocation() {
    	return loc;
    }

    public Action getAction() {
        return action;
    }

}
