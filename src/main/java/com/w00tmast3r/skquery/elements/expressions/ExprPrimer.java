package com.w00tmast3r.skquery.elements.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.w00tmast3r.skquery.api.PropertyPatterns;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;


@PropertyPatterns(
        fromType = "entities",
        property = "(primer|fuse lighting piece of shit)"
)
public class ExprPrimer extends SimplePropertyExpression<Entity, Entity> {

    @Override
    protected String getPropertyName() {
        return "tnt source";
    }

    @Override
    public Entity convert(Entity entity) {
        return entity instanceof TNTPrimed ? ((TNTPrimed) entity).getSource() : null;
    }

    @Override
    public Class<? extends Entity> getReturnType() {
        return Entity.class;
    }
}
