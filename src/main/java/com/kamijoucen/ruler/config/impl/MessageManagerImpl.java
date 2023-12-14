package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.config.MessageManager;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenLocation;

public class MessageManagerImpl implements MessageManager {

    @Override
    public String buildMessage(String msg, TokenLocation location, Scope currentScope) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buildMessage'");
    }

    @Override
    public String unknownSymbol(String symbol, TokenLocation location) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unknownSymbol'");
    }

    @Override
    public String notFoundStringEnd(char flag, TokenLocation location) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'notFoundStringEnd'");
    }

    @Override
    public String numberFormatError(String number, TokenLocation location) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'numberFormatError'");
    }

    @Override
    public String illegalIdentifier(String identifier, TokenLocation location) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'illegalIdentifier'");
    }

    @Override
    public String breakNotInLoop(TokenLocation location) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'breakNotInLoop'");
    }

    @Override
    public String continueNotInLoop(TokenLocation location) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'continueNotInLoop'");
    }


    
}
