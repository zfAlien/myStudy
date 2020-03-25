package com.juquren.trace.proxy;

import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MyDubboServiceAutoProxyCreator implements BeanPostProcessor, BeanFactoryAware {
    /** spring的bean工厂 */
    protected BeanFactory          beanFactory;

    private static final String DUBBO_SERVICE_BEAN     = "com.alibaba.dubbo.config.spring.ServiceBean";

    private static final String DUBBO_REFERENCE_BEAN   = "com.alibaba.dubbo.config.spring.ReferenceBean";

    private static final String DUBBO_METETHOD_SET_REF = "setRef";

    private static final String DUBBO_METETHOD_GET_REF = "getRef";

    /** dubbo service bean 的setRef方法 */
    private final Method setRefMethod;

    /** dubbo service bean 的getRef方法 */
    private final Method        getRefMethod;

    /** 发布的服务特定的拦截器名称 */
    protected String[]          serviceInterceptorNames = {"serviceDigestLogInterceptor"};

    /** 引用的服务特定的拦截器名称 */
    protected String[]          salInterceptorNames     = new String[0];

    private AdvisorAdapterRegistry advisorAdapterRegistry = GlobalAdvisorAdapterRegistry
            .getInstance();

    public MyDubboServiceAutoProxyCreator() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> dubboServiceName = Class.forName(DUBBO_SERVICE_BEAN);
        setRefMethod = dubboServiceName.getMethod(DUBBO_METETHOD_SET_REF, Object.class);
        setRefMethod.setAccessible(true);
        getRefMethod = dubboServiceName.getMethod(DUBBO_METETHOD_GET_REF);
        getRefMethod.setAccessible(true);
    }
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean != null && bean.getClass().getName().equals(DUBBO_SERVICE_BEAN)) {

            try {
                Object ref = getRefMethod.invoke(bean);
                ProxyFactory proxyFactory = new ProxyFactory();


                Advisor[] specificInterceptors = resolveInterceptorNames(serviceInterceptorNames);
                Advisor[] advisors = new Advisor[specificInterceptors.length];
                for (int i = 0; i < specificInterceptors.length; i++) {
                    advisors[i] = this.advisorAdapterRegistry.wrap(specificInterceptors[i]);
                }
                for (Advisor advisor : advisors) {
                    proxyFactory.addAdvisor(advisor);
                }

                proxyFactory.setTargetSource(new SingletonTargetSource(ref));
                proxyFactory.setFrozen(false);

                Object proxy = proxyFactory.getProxy();
                setRefMethod.invoke(bean, proxy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    private Advisor[] resolveInterceptorNames(String[] interceptorNames) {
        ConfigurableBeanFactory cbf = (this.beanFactory instanceof ConfigurableBeanFactory ? (ConfigurableBeanFactory) this.beanFactory
                : null);
        List<Advisor> advisors = new ArrayList<Advisor>();
        for (String beanName : interceptorNames) {
            if (cbf == null || !cbf.isCurrentlyInCreation(beanName)) {
                Object next = this.beanFactory.getBean(beanName);
                advisors.add(this.advisorAdapterRegistry.wrap(next));
            }
        }
        return advisors.toArray(new Advisor[advisors.size()]);
    }
}
