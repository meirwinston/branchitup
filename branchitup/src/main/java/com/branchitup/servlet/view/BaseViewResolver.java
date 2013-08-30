package com.branchitup.servlet.view;

import java.util.Locale;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import com.branchitup.system.Constants;

public class BaseViewResolver implements ViewResolver {
	private ViewResolver beanNameViewResolver;
	private ViewResolver inernalResourceViewResolver;

	public void setBeanNameViewResolver(ViewResolver beanNameViewResolver) {
		this.beanNameViewResolver = beanNameViewResolver;
	}

	public void setInernalResourceViewResolver(ViewResolver inernalResourceViewResolver) {
		this.inernalResourceViewResolver = inernalResourceViewResolver;
	}
	
	@Override
	public View resolveViewName(String veiwName, Locale locale) throws Exception {
		System.out.println("RESOLVE: " + veiwName);
		if(Constants.Views.JSON_VIEW.equals(veiwName)){
			System.out.println("JSON_VIEW");
			return beanNameViewResolver.resolveViewName(veiwName, locale);
		}
		else if(Constants.Views.IMAGE_VIEW.equals(veiwName)){
			System.out.println("IMAGE_VIEW");
			return beanNameViewResolver.resolveViewName(veiwName, locale);
		}
		else{
			System.out.println("DEFAULT VIEW");
			return inernalResourceViewResolver.resolveViewName(veiwName, locale);
		}
	}
}
