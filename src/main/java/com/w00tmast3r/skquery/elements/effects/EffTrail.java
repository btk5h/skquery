package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.SkQuery;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.util.CancellableBukkitTask;
import com.w00tmast3r.skquery.util.packet.particle.Particle;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.entity.ProjectileLaunchEvent;


@Patterns("trail projectile with %particle%")
public class EffTrail extends Effect {

    private Expression<Particle> particle;

    @Override
    protected void execute(Event event) {
        final Particle p = particle.getSingle(event);
        if(p == null) return;
        final ProjectileLaunchEvent e = (ProjectileLaunchEvent) event;
        CancellableBukkitTask task = new CancellableBukkitTask() {
            @Override
            public void run() {
                p.play(e.getEntity().getLocation(), Bukkit.getOnlinePlayers());
                if(!e.getEntity().isValid() || e.getEntity().isOnGround()) {
                    cancel();
                }
            }
        };
        task.setTaskId(Bukkit.getScheduler().scheduleSyncRepeatingTask(SkQuery.getInstance(), task, 0, 1));
    }

    @Override
    public String toString(Event event, boolean b) {
        return "projectile trail";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        if(!ScriptLoader.isCurrentEvent(ProjectileLaunchEvent.class)) {
            Skript.error("Trailing can only be used in a shoot event", ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        particle = (Expression<Particle>) expressions[0];
        return true;
    }
}
