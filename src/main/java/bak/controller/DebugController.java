package bak.controller;


import bak.annotation.NeedLogin;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


@RestController
public class DebugController implements ApplicationContextAware {


    ApplicationContext applicationContext;

    //获取spring容器的引用
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }


    //该方法需要登入才可以访问
    @NeedLogin
    @RequestMapping("debug")
    public String debug(String beanName,String methodName,String[] params) throws InvocationTargetException, IllegalAccessException {

        Object bean = applicationContext.getBean(beanName);
        if(bean==null){
            return "spring容器中没有对应的bean";
        }

        Method[] methods = bean.getClass().getDeclaredMethods();
        for(Method method:methods){
            if(method.getName().equals(methodName)){
                method.invoke(bean, params);
                return "调用成功";
            }
        }

        return "没有对应的方法";

    }

}
