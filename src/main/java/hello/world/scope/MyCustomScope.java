package hello.world.scope;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.LifeCycle;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.exceptions.BeanDestructionException;
import io.micronaut.context.scope.BeanCreationContext;
import io.micronaut.context.scope.CreatedBean;
import io.micronaut.context.scope.CustomScope;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.inject.BeanIdentifier;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class MyCustomScope implements CustomScope<MyScope>, LifeCycle<MyCustomScope>, ApplicationEventListener {
    public static final String SCOPED_BEANS_ATTRIBUTE = "CN.VTOHRU.SCOPED_BEANS";
    private ApplicationContext beanContext;
    private Map<BeanIdentifier, CreatedBean> map = new HashMap<>();

    public MyCustomScope(ApplicationContext beanContext) {
        this.beanContext =  beanContext;
    }

    protected <T> CreatedBean<T> doCreate(@NonNull BeanCreationContext<T> creationContext) {
        return creationContext.create();
    }

    @Override
    public Class<MyScope> annotationType() {
        return MyScope.class;
    }

    @Override
    public <T> T getOrCreate(BeanCreationContext<T> creationContext) {
        final Map<BeanIdentifier, CreatedBean> scopeMap = getScopeMap(true);
        final BeanIdentifier id = creationContext.id();
        CreatedBean<T> createdBean = scopeMap.get(id);
        if (createdBean != null) {
            return createdBean.bean();
        } else {
            createdBean = scopeMap.get(id);
            if (createdBean != null) {
                return createdBean.bean();
            } else {
                createdBean = doCreate(creationContext);
                scopeMap.put(id, createdBean);
                return createdBean.bean();
            }
        }
    }

    @Override
    public <T> Optional<T> remove(BeanIdentifier identifier) {
        if (identifier == null) {
            return Optional.empty();
        }
        Map<BeanIdentifier, CreatedBean> scopeMap;
        try {
            scopeMap = getScopeMap(false);
        } catch (IllegalStateException e) {
            return Optional.empty();
        }
        if (CollectionUtils.isNotEmpty(scopeMap)) {
            CreatedBean<T> createdBean = scopeMap.get(identifier);
            if (createdBean != null) {
                try {
                    createdBean.close();
                } catch (BeanDestructionException e) {
                }
                return  Optional.of(createdBean.bean());
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }


    protected <T> Map<BeanIdentifier, CreatedBean> getScopeMap(boolean forCreation) {
        return map;
    }
    @Override
    public MyCustomScope stop() {
        return this;
    }

    public boolean isRunning() {
        return true;
    }


    @Override
    public void onApplicationEvent(Object event) {
    }
}
