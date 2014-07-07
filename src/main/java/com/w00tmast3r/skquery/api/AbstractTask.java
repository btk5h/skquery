package com.w00tmast3r.skquery.api;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.util.SimpleEvent;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A runnable with a set of tools that help for registering events.
 */
public abstract class AbstractTask implements Runnable {

    protected void registerEvent(String codeName, String name, Class<? extends Event> event, String... patterns) {
        registerEvent(null, codeName, name, SimpleEvent.class, event, patterns);
    }

    protected void registerEvent(String codeName, String name, Class<? extends SkriptEvent> handler, Class<? extends Event> event, final String... patterns) {
        registerEvent(null, codeName, name, handler, event, patterns);
    }

    @Deprecated
    protected void registerEvent(JavaPlugin owner, String codeName, String name, Class<? extends Event> event, String... patterns) {
        registerEvent(owner, codeName, name, SimpleEvent.class, event, patterns);
    }

    @Deprecated
    protected void registerEvent(JavaPlugin owner, String codeName, String name, Class<? extends SkriptEvent> handler, Class<? extends Event> event, final String... patterns) {
        /*
        if(RegistrationManager.codenames.contains(codeName)) return;
        RegistrationManager.codenames.add(codeName);
        Skript.registerEvent(name, handler, event, patterns);
        RegistrationManager.evtDocs.add(Documentation.getDocumentation(owner == null ? null : owner.getName(), event, null, new Patterns() {
            @Override
            public String[] value() {
                return patterns;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Patterns.class;
            }
        }, null));
        */
        Skript.registerEvent(name, handler, event, patterns);
    }
}
