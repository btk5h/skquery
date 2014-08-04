package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.elements.effects.base.Pragma;
import com.w00tmast3r.skquery.elements.events.bukkit.FunctionEvent;
import org.bukkit.event.Event;

import java.io.File;
import java.lang.reflect.Method;

@Patterns("$ access")
public class EffOptionAccess extends Pragma {
    @Override
    protected TriggerItem walk(Event e) {
        try {
            Method walk = TriggerItem.class.getDeclaredMethod("walk", Event.class);
            walk.setAccessible(true);
            walk.invoke(getNext(), ((FunctionEvent) e).getInvoker());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return getNext() == null ? null : getNext().getNext();
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        if (!ScriptLoader.isCurrentEvent(FunctionEvent.class)) {
            Skript.error("Level Access pragmas can only be declared in functions", ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        return true;
    }

    @Override
    protected void register(File executingScript, SkriptParser.ParseResult parseResult) {

    }
}
