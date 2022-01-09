package zm.irc.ioc;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {


    public static ApplicationContext init(){
        ApplicationContext context = new ApplicationContext();


        context.putBean(context.getClass().getName(),context);
        return context;
    }


    private Map<String,Object> beanPool;

    private ApplicationContext(){
        this.beanPool = new HashMap<>();
    }



    public void putBean(String beanId,Object bean){
        this.beanPool.put(beanId,bean);
    }

    public Object getBean(String beanId){
        return beanPool.get(beanId);
    }


    public <T> T getBean(String beanId, Class<T> clazz){
        Object beanObj = getBean(beanId);
        return beanObj == null? null : (T)beanObj;
    }

    public <T> T getBean(Class<T> clazz){
        if(clazz == null){
            return null;
        }
        Object beanObj = getBean(clazz.getName());
        return beanObj == null? null : (T)beanObj;
    }

}
