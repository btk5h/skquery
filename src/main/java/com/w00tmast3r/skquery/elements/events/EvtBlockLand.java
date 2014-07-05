package com.w00tmast3r.skquery.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import com.w00tmast3r.skquery.api.EventSkClass;
import com.w00tmast3r.skquery.api.SuppressOutput;
import com.w00tmast3r.skriptaddon.skaddonlib.util.Collect;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityChangeBlockEvent;

@SuppressOutput

public class EvtBlockLand extends SkriptEvent implements EventSkClass {
    @Override
    public boolean init(Literal<?>[] literals, int i, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(Event event) {
        return ((EntityChangeBlockEvent) event).getEntity() instanceof FallingBlock;
    }

    @Override
    public String toString(Event event, boolean b) {
        return "block land";
    }

    @Override
    public Class<? extends Event> getEvent() {
        return EntityChangeBlockEvent.class;
    }

    @Override
    public String getEventName() {
        return "Falling Block Land";
    }

    @Override
    public String[] getPatterns() {
        return Collect.asArray("block land");
    }

    @Override
    public void onEnable() {

    }
}
