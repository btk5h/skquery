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


@Patterns("nullify score [of] %string% in [score]board of %player%")
public class EffRemoveScore extends Effect {

    private Expression<Player> player;
    private Expression<String> item;

    @Override
    protected void execute(Event event) {
        Player p = player.getSingle(event);
        String i = item.getSingle(event);
        if(p == null || i == null) return;
        Scoreboard sb = p.getScoreboard();
        Objective obj = sb.getObjective("skript");
        if(sb == null || obj == null) return;
        sb.resetScores(Bukkit.getOfflinePlayer(i));
        p.setScoreboard(sb);
    }

    @Override
    public String toString(Event event, boolean b) {
        return "reset board";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        player = (Expression<Player>) expressions[1];
        item = (Expression<String>) expressions[0];
        return true;
    }
}
