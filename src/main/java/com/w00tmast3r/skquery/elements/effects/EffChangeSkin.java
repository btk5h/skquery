package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.SkQuery;
import com.w00tmast3r.skquery.api.Dependency;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.util.packet.PlayerDisplayChanger;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@Deprecated
@Dependency("ProtocolLib")
@Patterns("change appearance of %players% to [skin of] %string% named %string%")
public class EffChangeSkin extends Effect {

    private Expression<Player> players;
    private Expression<String> skin, name;

    @Override
    protected void execute(Event event) {
        String s = skin.getSingle(event);
        String n = name.getSingle(event);
        if (s == null || n == null) return;
        for (Player p : players.getAll(event)) {
            new PlayerDisplayChanger(SkQuery.getInstance()).changeDisplay(p, s, n);
        }
    }

    @Override
    public String toString(Event event, boolean b) {
        return "display";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        players = (Expression<Player>) expressions[0];
        skin = (Expression<String>) expressions[1];
        name = (Expression<String>) expressions[2];
        return true;
    }
}
