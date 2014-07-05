package com.w00tmast3r.skquery.elements.effects.json;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skriptaddon.skaddonlib.messaging.JSONMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;


@Patterns("(send|message) %jsonmessage% to %players%")
public class EffSendJson extends Effect {

    private Expression<JSONMessage> json;
    private Expression<Player> players;

    @Override
    protected void execute(Event event) {
        JSONMessage j = json.getSingle(event);
        if (j == null) return;
        j.send(players.getAll(event));
    }

    @Override
    public String toString(Event event, boolean b) {
        return "json";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        json = (Expression<JSONMessage>) expressions[0];
        players = (Expression<Player>) expressions[1];
        return true;
    }
}
