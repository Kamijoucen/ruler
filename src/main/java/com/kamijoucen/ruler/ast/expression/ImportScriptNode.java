package com.kamijoucen.ruler.ast.expression;

import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.util.IOUtil;

public class ImportScriptNode extends ImportNode {

    private String script;

    public ImportScriptNode(String script, String alias, TokenLocation location) {
        super(IOUtil.getVirtualPath(script, alias), alias, location);
        this.script = script;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}
