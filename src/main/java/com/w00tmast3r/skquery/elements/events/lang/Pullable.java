package com.w00tmast3r.skquery.elements.events.lang;

import ch.njol.skript.lang.Expression;
import org.bukkit.event.Event;

public interface Pullable {

    Expression<?>[] getArgs();

    Event getSuperEvent();
}
