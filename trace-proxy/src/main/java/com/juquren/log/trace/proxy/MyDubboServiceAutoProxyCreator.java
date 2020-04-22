package com.juquren.log.trace.proxy;

import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ProxyProcessorSupport;
import org.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
import org.springframework.aop.framework.autoproxy.AutoProxyUtils;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyDubboServiceAutoProxyCreator extends ProxyProcessorSupport implements BeanPostProcessor, BeanFactoryAware {
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
    protected String[]          salInterceptorNames     = {"salDigestLogInterceptor"};

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
    /**
     * dubbo服务Provider
     */
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean != null && bean.getClass().getName().equals(DUBBO_SERVICE_BEAN)) {

            try {
                Object ref = getRefMethod.invoke(bean);
                Object proxy = createProxy(ref.getClass(), beanName, this.serviceInterceptorNames,
                        new SingletonTargetSource(ref));
                setRefMethod.invoke(bean, proxy);
            } catch (Exception e) {
                e.printStackTrace();
            }

//            try {
//                Object ref = getRefMethod.invoke(bean);
//                Object proxy = new JDKDynamicProxy(ref).getProxy();
//                setRefMethod.invoke(bean, proxy);
//            } catch (Exception e) {
//                System.out.println("搞砸了");
//            }
        }
        return bean;
    }

    @Override
    /**
     * dubbo服务Consumer
     */
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //在从reference bean获取ref并注入到实际调用的类的时候返回一个代理对象
        String referenceBeanName = BeanFactory.FACTORY_BEAN_PREFIX + beanName;
        if (beanFactory.containsBean(referenceBeanName)) {
            Object referenceBean = beanFactory.getBean(referenceBeanName);
            //1、配置的是dubbo的reference bean表明使用的是是引用的服务
            if (referenceBean != null
                    && referenceBean.getClass().getName().equals(DUBBO_REFERENCE_BEAN)
                    && !bean.getClass().getName().equals(DUBBO_REFERENCE_BEAN)) {
                bean = createProxy(bean.getClass(), beanName, this.salInterceptorNames,
                        new SingletonTargetSource(bean));
//                bean = new JDKDynamicProxy(bean).getProxy();
            }
        }
        return bean;
    }

    private Advisor[] resolveInterceptorNames(String[] interceptorNames) {
        ConfigurableBeanFactory cbf = (this.beanFactory instanceof ConfigurableBeanFactory ?
                (ConfigurableBeanFactory) this.beanFactory : null);
        List<Advisor> advisors = new ArrayList<Advisor>();
        for (String beanName : interceptorNames) {
            if (cbf == null || !cbf.isCurrentlyInCreation(beanName)) {
                Object next = this.beanFactory.getBean(beanName);
                advisors.add(this.advisorAdapterRegistry.wrap(next));
            }
        }
        return advisors.toArray(new Advisor[advisors.size()]);
    }

    private Object createProxy(Class<?> beanClass, String beanName,
                               String[] specificInterceptorNames, TargetSource targetSource){

        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.copyFrom(this);

        if (!proxyFactory.isProxyTargetClass()) {
            if (shouldProxyTargetClass(beanClass, beanName)) {
                proxyFactory.setProxyTargetClass(true);
            }
            else {
                evaluateProxyInterfaces(beanClass, proxyFactory);
            }
        }

        Advisor[] advisors = buildAdvisors(beanName, specificInterceptorNames);
        for (Advisor advisor : advisors) {
            proxyFactory.addAdvisor(advisor);
        }

        proxyFactory.setTargetSource(targetSource);
        proxyFactory.setFrozen(false);

        return proxyFactory.getProxy(getProxyClassLoader());

    }

    protected boolean shouldProxyTargetClass(Class<?> beanClass, String beanName) {
        return (this.beanFactory instanceof ConfigurableListableBeanFactory &&
                AutoProxyUtils.shouldProxyTargetClass((ConfigurableListableBeanFactory) this.beanFactory, beanName));
    }

    protected Advisor[] buildAdvisors(String beanName, String[] specificInterceptors) {
        Advisor[] commonInterceptors = resolveInterceptorNames(specificInterceptors);

        List<Advisor> allInterceptors = new ArrayList<>(Arrays.asList(commonInterceptors));
        Advisor[] advisors = new Advisor[allInterceptors.size()];
        for (int i = 0; i < allInterceptors.size(); i++) {
            advisors[i] = this.advisorAdapterRegistry.wrap(allInterceptors.get(i));
        }
        return advisors;
    }
}
