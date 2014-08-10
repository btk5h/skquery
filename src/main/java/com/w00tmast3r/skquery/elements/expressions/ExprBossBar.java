package com.w00tmast3r.skquery.elements.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.w00tmast3r.skquery.api.Disabled;
import com.w00tmast3r.skquery.api.PropertyFrom;
import com.w00tmast3r.skquery.api.PropertyTo;
import com.w00tmast3r.skquery.api.UsePropertyPatterns;
import com.w00tmast3r.skquery.util.Collect;
import com.w00tmast3r.skquery.util.packet.BossBars;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Disabled
@UsePropertyPatterns
@PropertyFrom("offlineplayers")
@PropertyTo("boss bar")
public class ExprBossBar extends SimplePropertyExpression<OfflinePlayer, BossBars.BossBarProxy> {
    @Override
    protected String getPropertyName() {
        return "bar";
    }

    @Override
    public BossBars.BossBarProxy convert(OfflinePlayer offlinePlayer) {
        return new BossBars.BossBarProxy(offlinePlayer);
    }

    @Override
    public Class<? extends BossBars.BossBarProxy> getReturnType() {
        return BossBars.BossBarProxy.class;
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET) return Collect.asArray(Object.class);
        return null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        new BossBars.BossBarProxy(getExpr().getSingle(e)).removeBar();
    }
}
