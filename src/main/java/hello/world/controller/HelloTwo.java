package hello.world.controller;

import hello.world.scope.MyScope;

@MyScope
public class HelloTwo extends AbstractBean{
    @Override
    void hello() {
        System.out.println("hello two");
    }
}
