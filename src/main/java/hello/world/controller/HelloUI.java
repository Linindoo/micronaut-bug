package hello.world.controller;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

import java.util.Collection;


@Controller("/hello")
public class HelloUI {
    private ApplicationContext context;

    public HelloUI(ApplicationContext context) {
        this.context = context;
    }

    @Get("/world")
    @Produces(MediaType.TEXT_JSON)
    public String hello() {
        Collection<AbstractBean> beansOfType = context.getBeansOfType(AbstractBean.class);
        for (AbstractBean abstractBean : beansOfType) {
            abstractBean.hello();
        }
        return "sb";
    }

}
