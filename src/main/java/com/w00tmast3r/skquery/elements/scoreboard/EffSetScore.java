package com.w00tmast3r.skquery.elements.scoreboard;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;


@Patterns("make score [of] %string% in [score]board of %player% [to] %number%")
public class EffSetScore extends Effect {

    private Expression<Player> player;
    private Expression<String> item;
    private Expression<Number> val;

    @Override
    protected void execute(Event event) {
        Player p = player.getSingle(event);
        String i = item.getSingle(event);
        Number v = val.getSingle(event);
        if(p == null || i == null || v == null) return;
        Scoreboard sb = p.getScoreboard();
        Objective obj = sb.getObjective("skript");
        if(sb == null || obj == null) return;
        try {i = i.substring(0, 16);} catch (Exception ignored) {}
        obj.getScore(Bukkit.getOfflinePlayer(i)).setScore(v.intValue());
        p.setScoreboard(sb);
    }

    @Override
    public String toString(Event event, boolean b) {
        return "display board";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        item = (Expression<String>) expressions[0];
        player = (Expression<Player>) expressions[1];
        val = (Expression<Number>) expressions[2];
        return true;
    }
}
