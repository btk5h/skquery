package com.w00tmast3r.skquery.elements.lang;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import org.bukkit.event.Event;


@Patterns("escape %number% [(levels|lines)]")
public class EffEscape extends Effect {

    private Expression<Number> esc;

    @Override
    protected void execute(Event event) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected TriggerItem walk(Event e) {
        debug(e, false);
        Number eli = esc.getSingle(e);
        if(eli == null) return null;
        int el = eli.intValue();
        if(el <= 0) return getNext();
        TriggerItem item = getNext();
        for(int i = 0; i < el; i++){
            if(item == null) return null;
            item = item.getNext();
        }
        return item;
    }



    @Override
    public String toString(Event event, boolean b) {
        return "escape";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        esc = (Expression<Number>) expressions[0];
        return true;
    }
}
