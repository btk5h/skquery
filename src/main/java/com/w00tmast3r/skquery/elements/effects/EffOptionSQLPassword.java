package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.lang.SkriptParser;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.elements.effects.base.OptionsPragma;
import com.w00tmast3r.skquery.db.ScriptCredentials;

import java.io.File;

@Patterns("$ db password <.+>")
public class EffOptionSQLPassword extends OptionsPragma {
    @Override
    protected void register(File executingScript, SkriptParser.ParseResult parseResult) {
        ScriptCredentials.setPassword(executingScript, parseResult.regexes.get(0).group());
    }
}
