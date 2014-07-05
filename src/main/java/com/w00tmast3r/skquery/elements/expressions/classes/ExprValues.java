package com.w00tmast3r.skquery.elements.expressions.classes;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Examples;
import com.w00tmast3r.skquery.api.ManualDoc;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skriptaddon.skriptplus.util.ArrayUtils;
import org.bukkit.event.Event;


@Patterns("[all ]enum values of %classinfo%")
@ManualDoc(
        name = "Enum Values",
        description = "Returns a collection of strings that represent the enum values of a class. Keep in mind <classinfo> cannot be used in as a command argument."
)
@Examples("message \"%all enum values of particle%\"")
public class ExprValues extends SimpleExpression<String> {

    Expression<ClassInfo> cInfo;

    @Override
    protected String[] get(Event event) {
        ClassInfo c = cInfo.getSingle(event);
        return c.getC().isEnum() ? ArrayUtils.toFriendlyStringArray(c.getC().getEnumConstants()) : null;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(Event event, boolean b) {
        return "enum values";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        cInfo = (Expression<ClassInfo>) expressions[0];
        return true;
    }
}
