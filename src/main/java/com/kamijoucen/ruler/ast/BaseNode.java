package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

import java.io.Serializable;

public interface BaseNode extends Serializable {

    BaseValue eval(NodeVisitor visitor);

    TokenLocation getLocation();

}
