package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.log.CountingLogHandler;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.skript.log.ParseLogHandler;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.skript.registrations.Classes;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.SkQuery;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.elements.events.bukkit.FunctionEvent;
import com.w00tmast3r.skquery.elements.events.bukkit.ReturnEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.logging.Level;


@Patterns({"sync[hronize] to [function] %string%",
        "sync[hronize] %~object% to [function] %string%",
        "sync[hronize] %~object% to [function] %string% from %objects%"})
public class EffSync extends Effect implements Listener {

    private Expression<?> changed = null;
    private Expression<String> cause;
    private Expression<Object> args = null;
    private Event storedEvent;
    private TriggerItem next;
    private Class<?>[] rs, rs2;
    private boolean allSingle;

    @Override
    protected void execute(Event event) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected TriggerItem walk(Event e) {
        String c = cause.getSingle(e);
        if(c == null) return null;
        Bukkit.getPluginManager().registerEvents(this, SkQuery.getInstance());
        Bukkit.getPluginManager().callEvent(new FunctionEvent(c, (args == null ? null : args.getAll(e)), this));
        storedEvent = e;
        next = getNext();
        return null;
    }

    @EventHandler
    public void onFunction(ReturnEvent event) {
        if(event.getInvoker() == this) {
            Expression<?> changer = event.getArgs();
            if (changer != null) {
                Expression<?> v = null;
                final ParseLogHandler log = SkriptLogger.startParseLogHandler();
                try {
                    for (final Class<?> r : rs) {
                        log.clear();
                        if ((r.isArray() ? r.getComponentType() : r).isAssignableFrom(changer.getReturnType())) {
                            v = changer.getConvertedExpression(Object.class);
                            break; // break even if v == null as it won't convert to Object apparently
                        }
                    }
                    if (v == null)
                        v = changer.getConvertedExpression((Class<Object>[]) rs2);
                    if (v == null) {
                        return;
                    }
                    log.printLog();
                } finally {
                    log.stop();
                }
                boolean single = allSingle;
                for (int i = 0; i < rs.length; i++) {
                    if (rs2[i].isAssignableFrom(v.getReturnType())) {
                        single = !rs[i].isArray();
                        break;
                    }
                }
                changer = v;
                if (!changer.isSingle() && single) return;
                Object[] delta = event.getArgs() == null ? null : event.getArgs().getArray(event);
                if (delta != null && delta.length == 0) return;
                if (changed != null) changed.change(storedEvent, delta, Changer.ChangeMode.SET);
                TriggerItem.walk(next, storedEvent);
                HandlerList.unregisterAll(this);
            }
        }
    }

    @Override
    public String toString(Event event, boolean b) {
        return "sync";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        if (i == 0) {
            cause = (Expression<String>) expressions[0];
        } else {
            changed = expressions[0];
            cause = (Expression<String>) expressions[1];
            if(i == 2) args = (Expression<Object>) expressions[2];
        }
         String what;
        CountingLogHandler h = SkriptLogger.startLogHandler(new CountingLogHandler(Level.SEVERE));
        try {
            rs = changed.acceptChange(Changer.ChangeMode.SET);
            ClassInfo<?> c = Classes.getSuperClassInfo(changed.getReturnType());
            what = c.getChanger() == null || !Arrays.equals(c.getChanger().acceptChange(Changer.ChangeMode.SET), rs) ? changed.toString(null, false) : c.getName().withIndefiniteArticle();
        } finally {
            h.stop();
        }
        if (rs == null) {
            if (h.getCount() > 0) return false;
            Skript.error(what + " can't be set to anything", ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        rs2 = new Class<?>[rs.length];
        for (int in = 0; in < rs.length; in++) rs2[in] = rs[in].isArray() ? rs[in].getComponentType() : rs[in];
        allSingle = Arrays.equals(rs, rs2);
        return true;
    }
}
