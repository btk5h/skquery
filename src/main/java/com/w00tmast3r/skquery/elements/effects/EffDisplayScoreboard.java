package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Description;
import com.w00tmast3r.skquery.api.Name;
import com.w00tmast3r.skquery.api.Patterns;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

@Name("Display Scoreboard")
@Description("Display a new scoreboard to a player. Overwrites existing scoreboards.")
@Deprecated
@Patterns("display [score]board named %string% to %player%")
public class EffDisplayScoreboard extends Effect {

    private Expression<Player> player;
    private Expression<String> title;

    @Override
    protected void execute(Event event) {
        Player p = player.getSingle(event);
        String t = title.getSingle(event);
        if(p == null || t == null) return;
        ScoreboardManager sbm = Bukkit.getScoreboardManager();
        Scoreboard sb = sbm.getNewScoreboard();
        Objective obj = sb.registerNewObjective("skript", "dummy");
        obj.setDisplayName(t);
        p.setScoreboard(sb);
    }

    @Override
    public String toString(Event event, boolean b) {
        return "display board";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        title = (Expression<String>) expressions[0];
        player = (Expression<Player>) expressions[1];
        return true;
    }
}
