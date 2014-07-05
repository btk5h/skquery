package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.ManualDoc;
import com.w00tmast3r.skquery.api.Patterns;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;


@Patterns("touch my server in places my server has never been touched before")
@ManualDoc(
      name = "Touch My Server In Places That Should Never Be Touched",
      description = "Your server will die of fatigue after this ;)"
)
public class EffTouchMyServerInPlacesThatShouldNeverBeTouched extends Effect {

    @Override
    protected void execute(Event event) {
        Bukkit.shutdown();
    }

    @Override
    public String toString(Event event, boolean b) {
        return "yes! yes! yes!";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        return true;
    }
}
