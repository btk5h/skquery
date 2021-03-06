package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.map.MapView;


@Patterns("send [map] %number% to %players%")
public class EffSendMap extends Effect {

    private Expression<Number> id;
    private Expression<Player> players;

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
        for (Player p : players.getAll(event)) {
            p.sendMap(mapView);
        }
    }

    @Override
    public String toString(Event event, boolean b) {
        return "manage skript maps";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        id = (Expression<Number>) expressions[0];
        players = (Expression<Player>) expressions[1];
        return true;
    }
}
