package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Description;
import com.w00tmast3r.skquery.api.Name;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.util.packet.particle.Particle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@Name("Play Particle")
@Description("Play particle effects at a certain location.")
@Patterns("spawn %number% [of] %particle% at %locations% [to %-players%]")
public class EffParticle extends Effect {

    private Expression<Particle> effect;
    private Expression<Number> amt;
    private Expression<Location> loc;
    private Expression<Player> player;

    @Override
    protected void execute(Event event) {
        Particle e = effect.getSingle(event);
        Number a = amt.getSingle(event);
        Player[] list;
        if (player == null) list = Bukkit.getOnlinePlayers();
        else list = player.getAll(event);
        if(e == null || a == null) return;
        e.setAmount(a.intValue());
        for (Location l : loc.getAll(event)) {
            e.play(l, list);
        }
    }

    @Override
    public String toString(Event event, boolean b) {
        return "sound";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        amt = (Expression<Number>) expressions[0];
        effect = (Expression<Particle>) expressions[1];
        loc = (Expression<Location>) expressions[2];
        player = (Expression<Player>) expressions[3];
        return true;
    }
}
