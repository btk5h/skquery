package com.w00tmast3r.skquery.elements.expressions;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.registrations.Classes;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.skript.LambdaCondition;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Patterns("%objects% where %predicate%")
public class ExprWhere extends SimpleExpression<Object> {

    public Expression<?> objects;
    public Expression<LambdaCondition> lambda;
    public ClassInfo<?> returnType = Classes.getExactClassInfo(Object.class);

    @Override
    protected Object[] get(Event e) {
        ArrayList<Object> out = new ArrayList<Object>();
        LambdaCondition l = lambda.getSingle(e);
        if (l == null) return null;
        for (Object o : objects.getAll(e)) {
            ExprInput.setInput(e, o);
            if (l.check(e)) {
                out.add(o);
            }
            ExprInput.removeInput(e);
        }
        Object[] array = (Object[]) Array.newInstance(returnType.getC(), out.size());
        return out.toArray(array);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public boolean isLoopOf(String s) {
        return returnType.getCodeName().equals(s);
    }

    @Override
    public Class<?> getReturnType() {
        return returnType.getC();
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "stream";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        objects = exprs[0];
        lambda = (Expression<LambdaCondition>) exprs[1];
        returnType = Classes.getExactClassInfo(objects.getReturnType());
        //if (objects instanceof Variable) {
        //    objects = objects.getConvertedExpression(Object.class);
        //}
        return true;
    }
}
