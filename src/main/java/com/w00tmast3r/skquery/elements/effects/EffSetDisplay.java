package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;


@Patterns("move display of %player% to %displayslot%")
public class EffSetDisplay extends Effect {

    private Expression<Player> player;
    private Expression<DisplaySlot> slot;

    @Override
    protected void execute(Event event) {
        Player p = player.getSingle(event);
        DisplaySlot s = slot.getSingle(event);
        if(p == null || s == null) return;
        Scoreboard sb = p.getScoreboard();
        Objective obj = sb.getObjective("skript");
        if(sb == null || obj == null) return;
        obj.setDisplaySlot(s);
        p.setScoreboard(sb);
    }

    @Override
    public String toString(Event event, boolean b) {
        return "display board";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        player = (Expression<Player>) expressions[0];
        slot = (Expression<DisplaySlot>) expressions[1];
        return true;
    }
}
