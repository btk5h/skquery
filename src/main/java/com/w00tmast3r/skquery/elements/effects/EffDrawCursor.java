package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.util.maps.RenderTask;
import com.w00tmast3r.skquery.util.maps.SkriptMapRenderer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapView;


@Patterns("draw cursor %mapcursortype% pointing %number% on [map] %number% at %number%, %number%")
public class EffDrawCursor extends Effect {

    private Expression<MapCursor.Type> text;
    private Expression<Number> id, f, x, y;

    @Override
    protected void execute(Event event) {
        final MapCursor.Type c = text.getSingle(event);
        Number n = id.getSingle(event);
        Number fO = f.getSingle(event);
        Number xO = x.getSingle(event);
        Number yO = y.getSingle(event);
        if (fO == null || xO == null || yO == null || n == null || c == null) return;
        short id = n.shortValue();
        final byte fFO = n.byteValue();
        final byte fXO = xO.byteValue();
        final byte fYO = yO.byteValue();
        MapView mapView;
        try {
            mapView = Bukkit.getMap(id);
        } catch (Exception e) {
            Bukkit.getLogger().warning("Map " + id + " has not been initialized yet!");
            return;
        }
        SkriptMapRenderer renderer = SkriptMapRenderer.getRenderer(mapView);
        if (renderer == null) return;
        renderer.update(new RenderTask() {
            @Override
            public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
                mapCanvas.getCursors().addCursor(new MapCursor(fXO, fYO, fFO, (byte) 2, true));
            }
        });
    }

    @Override
    public String toString(Event event, boolean b) {
        return "manage skript maps";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        text = (Expression<MapCursor.Type>) expressions[0];
        id = (Expression<Number>) expressions[1];
        f = (Expression<Number>) expressions[2];
        x = (Expression<Number>) expressions[3];
        y = (Expression<Number>) expressions[4];
        return true;
    }

}
