package com.w00tmast3r.skquery.elements.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.w00tmast3r.skquery.api.PropertyPatterns;
import com.w00tmast3r.skriptaddon.skaddonlib.util.Collect;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;


@PropertyPatterns(
        fromType = "entities",
        property = "land boat (state|ability|mode)"
)
public class ExprOMGLandBoats extends SimplePropertyExpression<Entity, Boolean> {
    @Override
    protected String getPropertyName() {
        return "blast size";
    }

    @Override
    public Boolean convert(Entity entity) {
        return entity instanceof Boat ? ((Boat) entity).getWorkOnLand() : null;
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.SET) return Collect.asArray(Boolean.class);
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        boolean b = delta != null && (Boolean) delta[0];
        switch (mode) {
            case SET:
                for (Entity en : getExpr().getAll(e))  {
                    if (en instanceof Boat) ((Boat) en).setWorkOnLand(b);
                }
                break;
            case RESET:
                for (Entity en : getExpr().getAll(e))  {
                    if (en instanceof Boat) ((Boat) en).setWorkOnLand(false);
                }
            case ADD:
            case REMOVE:
            case REMOVE_ALL:
            case DELETE:
                assert false;
        }
    }
}
