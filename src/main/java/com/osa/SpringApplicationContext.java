package com.osa;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContext implements ApplicationContextAware {
	
	/**
	 * get access to spring beans container
	 */
	
	private static ApplicationContext CONTEXT;

    @Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		CONTEXT = context;
	}

    // method that will return already created bean from spring container
    // NOTE: beanName follow camelCase notation
	public static Object getBean(String beanName) {
		return CONTEXT.getBean(beanName);
	}
}