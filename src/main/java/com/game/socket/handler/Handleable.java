package com.game.socket.handler;


public interface Handleable<T,U> {

    void handle(T t, U u) throws Exception;

}
