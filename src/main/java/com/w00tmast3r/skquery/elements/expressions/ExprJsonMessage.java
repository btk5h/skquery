package com.w00tmast3r.skquery.elements.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.util.minecraft.JSONMessage;


@Patterns("json [of] %string%")
public class ExprJsonMessage extends SimplePropertyExpression<String, JSONMessage> {

    @Override
    protected String getPropertyName() {
        return "json equivalent";
    }

    @Override
    public JSONMessage convert(String s) {
        return new JSONMessage(s);
    }

    @Override
    public Class<? extends JSONMessage> getReturnType() {
        return JSONMessage.class;
    }
}
