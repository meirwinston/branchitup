/*
 * YBLOB PROPRIETARY/CONFIDENTIAL.
 * 
 * yBlob Proprietary - USE PURSUANT TO COMPANY INSTRUCTIONS
 * USE of this information by anyone and for any purpose may only be 
 * made by the prior written consent of yBlob.  This 
 * confidential information is owned by yBlob, and is 
 * protected under United States copyright laws and international treaties.
 */
package com.branchitup.model;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import com.branchitup.system.Constants;

public class EntityModel<E> extends BaseModel{
	private static final long serialVersionUID = 1L;

	public E toJavaObject(){ 
		return null;
	}
	public EntityModel(){}
	
	public EntityModel(E e){
		this.toModelObject(e);
	}
	protected void toModelObject(E e){
		if(e == null) return;
		Field[] fields = e.getClass().getDeclaredFields();
		try {
			for(Field field : fields){
				if(!Modifier.isStatic(field.getModifiers())){
					field.setAccessible(true);
					if(field.getType().isAssignableFrom(Collection.class)){
					}
					else{
						//_persistence_primaryKey and _persistence_listener
						//are managed invisible fields that we need to avoid
						if(field.getAnnotation(Column.class) != null){
							Object val = field.get(e);
							
							if(val instanceof Date){
								this.put(field.getName(), ((Date)val).getTime());
							}
							else{
								if(val != null){
									this.put(field.getName(), val);
								}
							}
						}
					}
				}
			}
		} 
		catch (IllegalArgumentException exp) {
			Constants.LOGGER.error(exp);
		} 
		catch (IllegalAccessException exp) {
			Constants.LOGGER.error(exp);
		}
	}
	public List<EntityModel<E>> toModelArray ( E[] objectArray ){
		List<EntityModel<E>> jsonArray = new ArrayList<EntityModel<E>>();
		
		for (int i = 0; i < objectArray.length; i++) {
			E e = objectArray[i];
			jsonArray.add(new EntityModel<E>(e));
		}
		return jsonArray;
	}
	
	public List<?> toModelList ( Collection<E> col ){
		List<Object> list = new ArrayList<Object>();
		for ( E item : col ) {
			try {
				Object m = this.getClass().getConstructor(item.getClass()).newInstance(item);
				list.add ( m );
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
		}
		return list;
	}
	
	public static Map<String,Object> createErrorMessage(String title,String content){
		Map<String,Object> o = new HashMap<String,Object>();
		try {
			o.put("type", "exception");
			o.put("title", title);
			o.put("content", content);
		} catch (Exception exp) {
			exp.printStackTrace(System.out);
		}
		return o;
	}
	
//	public static String createRedirectMessage(String url){
//		Map<String,Object> o = new HashMap<String,Object>();
//		try {
//			o.put("type", "redirect");
//			o.put("url", url);
//		} catch (Exception exp) {
//			exp.printStackTrace(System.out);
//		}
//		return "(" + o + ")";
//	}
	
//	public static Map<String,Object> createErrorMessage(){
//		return createErrorMessage("Problem Occured!", "There was a problem in the system which prevented this operation to complete, please try again.");
//	}
	
//	public static String createErrorMessage(Exception exception )
//	{
//		if ( exception instanceof YBlobException )
//		{
//			return createErrorMessage( (YBlobException)exception);
//		}
//		else
//		{
//			return createErrorMessage("Problem Occured!", exception );
//		}
//	}
//	public static String createErrorMessage(YBlobException exception )
//	{
//		return createErrorMessage(exception.getTitle(), exception );
//	}
	
//	public static String createErrorMessage(String title, Exception exception){
//		Map<String,Object> o = new HashMap<String,Object>();
//		try {
//			o.put("type", "exception");
//			o.put("title", title);
//			o.put("message", exception.getMessage());
//			if(exception.getCause() != null){
//				o.put("cause", exception.getCause().getClass().getSimpleName() + ": " + exception.getCause().getMessage());
//			}
//			o.put("stacktrace", "");
//		} catch (Exception exp) {
//			exp.printStackTrace(System.out);
//		}
//		return "(" + o + ")";
//	}
}
