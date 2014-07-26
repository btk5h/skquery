package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.elements.events.bukkit.FunctionEvent;
import com.w00tmast3r.skquery.elements.events.bukkit.ReturnEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;


@Patterns("return %objects%")
public class EffReturn extends Effect {

    private Expression<?> args;

    @Override
    protected void execute(Event event) {
        Bukkit.getPluginManager().callEvent(new ReturnEvent(((FunctionEvent) event).getInvoker(), args));
    }

    @Override
    public String toString(Event event, boolean b) {
        return "return";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        if(!ScriptLoader.isCurrentEvent(FunctionEvent.class)) {
            Skript.error("Return effects can only be used inside functions", ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        args = expressions[0];
        return true;
    }
}
