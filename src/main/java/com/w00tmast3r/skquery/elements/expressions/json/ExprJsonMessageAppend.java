package com.w00tmast3r.skquery.elements.expressions.json;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skriptaddon.skaddonlib.messaging.JSONMessage;
import com.w00tmast3r.skriptaddon.skaddonlib.util.Collect;
import org.bukkit.event.Event;


@Patterns("%jsonmessage% then %string%")
public class ExprJsonMessageAppend extends SimpleExpression<JSONMessage> {

    private Expression<JSONMessage> json;
    private Expression<String> append;

    @Override
    protected JSONMessage[] get(Event event) {
        JSONMessage j = json.getSingle(event);
        String a = append.getSingle(event);
        if (j == null || a == null) return null;
        return Collect.asArray(j.then(a));
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends JSONMessage> getReturnType() {
        return JSONMessage.class;
    }

    @Override
    public String toString(Event event, boolean b) {
        return json.getSingle(event).toOldMessageFormat();
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        json = (Expression<JSONMessage>) expressions[0];
        append = (Expression<String>) expressions[1];
        return true;
    }
}
