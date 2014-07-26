package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.lang.SkriptParser;
import com.w00tmast3r.skquery.Documentation;
import com.w00tmast3r.skquery.api.DocumentationHidden;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.elements.effects.base.OptionsPragma;

import java.io.File;

@DocumentationHidden
@Patterns("$ generate documentation")
public class EffOptionGenerateDocs extends OptionsPragma {

    @Override
    protected void register(final File executingScript, final SkriptParser.ParseResult parseResult) {
        Documentation.generateDocs();
    }
}
