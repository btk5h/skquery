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
@PropertyTo("displayed text")
public class ExprBossBarText extends SimplePropertyExpression<BossBars.BossBarProxy, String> {
    @Override
    protected String getPropertyName() {
        return "bar text";
    }

    @Override
    public String convert(BossBars.BossBarProxy bar) {
        return bar.getText();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) return Collect.asArray(String.class);
        return null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        BossBars.BossBarProxy proxy = getExpr().getSingle(e);
        if (proxy == null) return;
        String to = delta[0] == null ? "" : (String) delta[0];
        proxy.setText(to);
    }
}
