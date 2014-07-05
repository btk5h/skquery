package com.w00tmast3r.skquery.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Checker;
import com.w00tmast3r.skriptaddon.skriptplus.elements.lang.FunctionEvent;
import com.w00tmast3r.skriptaddon.skriptplus.elements.lang.RoutineEvent;
import org.bukkit.event.Event;

@SuppressWarnings("unchecked")
public class EvtCustom extends SkriptEvent {

    private Literal<String> s;

    @Override
    public boolean init(Literal<?>[] literals, int i, SkriptParser.ParseResult parseResult) {
        s = (Literal<String>) literals[0];
        return true;
    }

    @Override
    public boolean check(final Event event) {
        if(event instanceof RoutineEvent) return s.check(event, new Checker<String>() {
            @Override
            public boolean check(String s) {
                return ((RoutineEvent) event).getCause().equalsIgnoreCase(s);
            }
        });
        else if(event instanceof FunctionEvent) return s.check(event, new Checker<String>() {
            @Override
            public boolean check(String s) {
                return ((FunctionEvent) event).getCause().equalsIgnoreCase(s);
            }
        });
        return false;
    }

    @Override
    public String toString(Event event, boolean b) {
        return "custom event";
    }
}
