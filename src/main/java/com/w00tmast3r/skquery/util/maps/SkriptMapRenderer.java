package com.w00tmast3r.skquery.util.maps;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.util.ArrayList;
import java.util.UUID;

public class SkriptMapRenderer extends MapRenderer implements Listener {

    private final ArrayList<UUID> dirtyPlayers = new ArrayList<>();
    private ArrayList<RenderTask> tasks = new ArrayList<>();

    public SkriptMapRenderer() {
        super(false);
    }

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        UUID uuid = player.getUniqueId();
        if (dirtyPlayers.contains(uuid)) {
            for (int x = 0; x < 128; ++x) {
                for (int y = 0; y < 128; ++y) {
                    mapCanvas.setPixel(x, y, MapColor.TRANSPARENT.color());
                }
            }
            for (RenderTask r : tasks) {
                r.render(mapView, mapCanvas, player);
            }
            dirtyPlayers.remove(uuid);
        }
    }

    public void clearTasks() {
        tasks = new ArrayList<>();
        redraw();
    }

    public void update(final RenderTask task) {
        tasks.add(task);
        redraw();
    }

    public void redraw() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!dirtyPlayers.contains(p.getUniqueId())) dirtyPlayers.add(p.getUniqueId());
        }
    }

    public static void createHandle(MapView mapView, boolean overwrite) {
        if (mapView == null) return;
        if (overwrite && mapView.getRenderers() != null) {
            for (MapRenderer renderer : mapView.getRenderers()) {
                mapView.removeRenderer(renderer);
            }
        }
        mapView.addRenderer(new SkriptMapRenderer());
    }

    public static SkriptMapRenderer getRenderer(MapView mapView) {
        if (mapView == null || mapView.getRenderers() == null) return null;
        for (MapRenderer renderer : mapView.getRenderers()) {
            if (renderer instanceof SkriptMapRenderer) return (SkriptMapRenderer) renderer;
        }
        return null;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        dirtyPlayers.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!dirtyPlayers.contains(event.getPlayer().getUniqueId())) dirtyPlayers.add(event.getPlayer().getUniqueId());
    }
}
