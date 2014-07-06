package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.util.maps.SkriptMapRenderer;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.map.MapView;


@Patterns({"(manage|share) map [id] %number%",
        "(override|overwrite) map [id] %number%"})
public class EffManageMap extends Effect {

    private Expression<Number> id;
    private boolean overwrite;

    @Override
    protected void execute(Event event) {
        Number n = id.getSingle(event);
        if (n == null) return;
        short id = n.shortValue();
        MapView mapView;
        try {
            mapView = Bukkit.getMap(id);
        } catch (Exception e) {
            Bukkit.getLogger().warning("Map " + id + " has not been initialized yet!");
            return;
        }
        SkriptMapRenderer.createHandle(mapView, overwrite);
    }

    @Override
    public String toString(Event event, boolean b) {
        return "manage skript maps";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        id = (Expression<Number>) expressions[0];
        overwrite = i == 1;
        return true;
    }
}
