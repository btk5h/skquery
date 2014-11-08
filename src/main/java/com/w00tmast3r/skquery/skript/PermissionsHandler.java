package com.w00tmast3r.skquery.skript;

import com.w00tmast3r.skquery.SkQuery;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;
import java.util.UUID;

public class PermissionsHandler implements Listener {

    private static boolean enabled = false;
    private static HashMap<UUID, PermissionAttachment> permissions = new HashMap<>();

    public static void enable() {
        if (!enabled) {
            Bukkit.getPluginManager().registerEvents(new PermissionsHandler(), SkQuery.getInstance());
        }
        enabled = true;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static PermissionAttachment getPermissions(Player player) {
        return permissions.get(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        permissions.put(event.getPlayer().getUniqueId(), event.getPlayer().addAttachment(SkQuery.getInstance()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.getPlayer().removeAttachment(getPermissions(event.getPlayer()));
        permissions.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        event.getPlayer().removeAttachment(getPermissions(event.getPlayer()));
        permissions.remove(event.getPlayer().getUniqueId());
    }
}
