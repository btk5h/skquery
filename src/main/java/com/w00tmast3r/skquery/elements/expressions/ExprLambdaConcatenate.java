package com.w00tmast3r.skquery.elements.expressions;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.skript.Lambda;
import com.w00tmast3r.skquery.util.Collect;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Patterns("%lambda%-\\>%lambda%")
public class ExprLambdaConcatenate extends SimpleExpression<Lambda> {

    private Expression<Lambda> base, tail;

    @Override
    protected Lambda[] get(Event e) {
        Lambda b = base.getSingle(e);
        Lambda t = tail.getSingle(e);
        if (b == null || t == null) return null;
        return Collect.asArray(new Lambda(false).add(b).add(t));
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Lambda> getReturnType() {
        return Lambda.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "lambda";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        base = (Expression<Lambda>) exprs[0];
        tail = (Expression<Lambda>) exprs[1];
        return true;
    }
}
