package com.w00tmast3r.skquery.elements.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.w00tmast3r.skquery.api.Disabled;
import com.w00tmast3r.skquery.api.PropertyFrom;
import com.w00tmast3r.skquery.api.PropertyTo;
import com.w00tmast3r.skquery.api.UsePropertyPatterns;
import com.w00tmast3r.skquery.util.Collect;
import com.w00tmast3r.skquery.util.packet.BossBars;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Disabled
@UsePropertyPatterns
@PropertyFrom("bossbars")
@PropertyTo("displayed percent")
public class ExprBossBarPercent extends SimplePropertyExpression<BossBars.BossBarProxy, Number> {
    @Override
    protected String getPropertyName() {
        return "bar percent";
    }

    @Override
    public Number convert(BossBars.BossBarProxy bar) {
        return bar.getPercent();
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) return Collect.asArray(Number.class);
        return null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        BossBars.BossBarProxy proxy = getExpr().getSingle(e);
        if (proxy == null) return;
        Number to = delta[0] == null ? 0 : (Number) delta[0];
        proxy.setPercent(to.floatValue());
    }
}
