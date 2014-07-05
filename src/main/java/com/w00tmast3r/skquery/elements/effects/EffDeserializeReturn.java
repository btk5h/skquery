package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skriptaddon.skriptplus.util.comphenix.InventorySerialUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;


@Patterns("restore inventory [of] %player% (to|from) %string%")
public class EffDeserializeReturn extends Effect {

    private Expression<Player> player;
    private Expression<String> inv;

    @Override
    protected void execute(Event event) {
        Player p = player.getSingle(event);
        String i = inv.getSingle(event);
        if(p == null || i == null) return;
        p.getInventory().setContents(InventorySerialUtils.fromBase64(i).getContents());
    }

    @Override
    public String toString(Event event, boolean b) {
        return "deserialize return";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        player = (Expression<Player>) expressions[0];
        inv = (Expression<String>) expressions[1];
        return true;
    }
}
