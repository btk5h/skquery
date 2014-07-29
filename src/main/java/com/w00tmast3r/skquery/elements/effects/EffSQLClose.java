package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.db.ScriptCredentials;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

@Patterns("close %queryresult%")
public class EffSQLClose extends Effect {

    private Expression<ResultSet> query;

    @Override
    protected void execute(Event event) {
        ResultSet q = query.getSingle(event);
        if (q == null) return;
        try {
            q.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "sql query";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        if (ScriptCredentials.get(ScriptLoader.currentScript.getFile()).getConnection() == null) {
            Skript.error("Database features are disabled until the script has SQL credentials associated with it.", ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        query = (Expression<ResultSet>) expressions[0];
        return true;
    }
}
