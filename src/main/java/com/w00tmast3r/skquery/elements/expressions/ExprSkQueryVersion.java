package com.w00tmast3r.skquery.elements.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.w00tmast3r.skquery.SkQuery;
import com.w00tmast3r.skquery.api.Patterns;

@Patterns("skquery version")
public class ExprSkQueryVersion extends SimplePropertyExpression<String, String> {
    @Override
    protected String getPropertyName() {
        return "plugin version";
    }

    @Override
    public String convert(String s) {
        return SkQuery.getInstance().getDescription().getVersion();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
}
