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
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;


@Patterns({"draw %string% on [map] %number%",
        "draw %string% on [map] %number% [starting] from %number%, %number%"})
public class EffDrawText extends Effect {

    private Expression<String> text;
    private Expression<Number> id, x, y;
    private boolean useCoordinates;

    @Override
    protected void execute(Event event) {
        final String i = text.getSingle(event);
        Number n = id.getSingle(event);
        int xO = 0;
        int yO = 0;
        if (useCoordinates) {
            Number xC = x.getSingle(event);
            Number yC = y.getSingle(event);
            if (xC == null || yC == null) return;
            xO = xC.intValue();
            yO = yC.intValue();
        }
        if (n == null || i == null) return;
        short id = n.shortValue();
        final int fXO = xO;
        final int fYO = yO;
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
                mapCanvas.drawText(fXO, fYO, MinecraftFont.Font, i);

            }
        });
    }

    @Override
    public String toString(Event event, boolean b) {
        return "manage skript maps";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        text = (Expression<String>) expressions[0];
        id = (Expression<Number>) expressions[1];
        useCoordinates = i == 1;
        if (useCoordinates) {
            x = (Expression<Number>) expressions[2];
            y = (Expression<Number>) expressions[3];
        }
        return true;
    }
}
