package com.w00tmast3r.skquery.util.maps;


import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapView;

public abstract class RenderTask {

    public abstract void render(MapView mapView, MapCanvas mapCanvas, Player player);
}
