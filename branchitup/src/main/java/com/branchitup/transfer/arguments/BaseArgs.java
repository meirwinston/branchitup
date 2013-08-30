package com.branchitup.transfer.arguments;

import org.hibernate.Session;
import org.springframework.security.core.userdetails.UserDetails;

public class BaseArgs {
	public Session session;
	public UserDetails userDetails;
	public BaseArgs(){}
	
//	public BaseArgs(Map<String,String> args){
//		initByStringMap(args);
//	}
//	
//	private void setSimpleFieldValue(Field field, Object val) throws NumberFormatException, IllegalArgumentException, IllegalAccessException{
//		if(field.getType().equals(String.class)){
//			field.set(this, val);
//		}
//		else if(field.getType().equals(Long.TYPE)){
//			if(val != null){
//				field.set(this, Long.parseLong(val.toString()));	
//			}
//		}
//		else if(field.getType().equals(Long.class)){
//			if(val != null){
//				field.set(this, Long.valueOf((String)val));
//			}
//			else{
//				field.set(this, null);
//			}
//		}
//		else if(field.getType().equals(Double.TYPE)){
//			if(val != null){
//				field.set(this, Double.parseDouble(val.toString()));
//			}
//		}
//		else if(field.getType().equals(Double.class)){
//			if(val != null){
//				field.set(this, Double.valueOf(val.toString()));						
//			}
//		}
//		else if(field.getType().equals(Integer.TYPE)){
//			if(val != null){
//				field.set(this, Integer.parseInt(val.toString()));							
//			}
//		}
//		else if(field.getType().equals(Integer.class)){
//			if(val != null){
//				field.set(this, Integer.valueOf(val.toString()));
//			}
//		}
//		else if(field.getType().equals(Float.TYPE)){
//			if(val != null){
//				field.set(this, Float.parseFloat(val.toString()));							
//			}
//		}
//		else if(field.getType().equals(Float.class)){
//			if(val != null){
//				field.set(this, Float.valueOf(val.toString()));							
//			}
//		}
//	}
//	public void initObjectMap(Map<String,Object> args){
//		Field[] fields = this.getClass().getDeclaredFields();
//		Object val;
//		for(Field field : fields){
//			try {
//				if(Modifier.isPublic(field.getModifiers())){
//					field.setAccessible(true);
//					
//					if(field.getType().isArray()){
//						val = args.get(field.getName());
//						
//						if(val != null){
//							Object[] arr = (Object[])val;
//							Object[] newArr = new Object[arr.length];
//							for(Object o : arr){
//								field.set(this, val);
//							}
//							
//						}
//					}
//					else{
//						val = args.get(field.getName());
//						setSimpleFieldValue(field, val);	
//					}
////					else if(field.getType().equals(Timestamp.class)){
////						field.set(this, args.get(field.getName()));
////					}
//					
//				}
//			} 
//			catch (IllegalArgumentException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	public void initByStringMap(Map<String,String> args){
//		Field[] fields = this.getClass().getDeclaredFields();
//		for(Field field : fields){
//			try {
//				if(Modifier.isPublic(field.getModifiers())){
//					field.setAccessible(true);
//					if(field.getType().equals(String.class)){
//						field.set(this, args.get(field.getName()));
//					}
//					else if(field.getType().equals(Long.TYPE)){
//						field.set(this, Long.parseLong(args.get(field.getName())));
//					}
//					else if(field.getType().equals(Long.class)){
//						String val = args.get(field.getName());
//						if(val != null){
//							field.set(this, Long.valueOf(val));
//						}
//						else{
//							field.set(this, null);
//						}
//					}
//					else if(field.getType().equals(Double.TYPE)){
//						field.set(this, Double.parseDouble(args.get(field.getName())));
//					}
//					else if(field.getType().equals(Double.class)){
//						field.set(this, Double.valueOf(args.get(field.getName())));
//					}
//					else if(field.getType().equals(Integer.TYPE)){
//						field.set(this, Integer.parseInt(args.get(field.getName())));
//					}
//					else if(field.getType().equals(Integer.class)){
//						field.set(this, Integer.valueOf(args.get(field.getName())));
//					}
//					else if(field.getType().equals(Float.TYPE)){
//						field.set(this, Float.parseFloat(args.get(field.getName())));
//					}
//					else if(field.getType().equals(Float.class)){
//						field.set(this, Float.valueOf(args.get(field.getName())));
//					}
////					else if(field.getType().equals(Timestamp.class)){
////						field.set(this, args.get(field.getName()));
////					}
//					
//				}
//			} 
//			catch (IllegalArgumentException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	
//	public BaseArgs(JsonNode jnode){
//		
//		Field[] fields = this.getClass().getDeclaredFields();
//		for(Field field : fields){
//			try {
//				if(Modifier.isPublic(field.getModifiers())){
//					field.setAccessible(true);
//					JsonNode n = jnode.get(field.getName());
//					if(field.getType().equals(String.class)){
//						if(n != null){
//							field.set(this, n.getTextValue());
//						}
//						else{
//							field.set(this, null);
//						}
//					}
//					else if(field.getType().equals(Long.TYPE) || field.getType().equals(Long.class)){
//						if(n != null){
//							field.set(this, n.getLongValue());
//						}
//						else{
//							field.set(this, null);
//						}
//					}
//					else if(field.getType().equals(Double.TYPE) || field.getType().equals(Double.class)){
//						if(n != null){
//							field.set(this, n.getDoubleValue());
//						}
//						else{
//							field.set(this, null);
//						}
//					}
//					else if(field.getType().equals(Integer.TYPE) || field.getType().equals(Integer.class)){
//						if(n != null){
//							field.set(this, n.getIntValue());
//						}
//						else{
//							field.set(this, null);
//						}
//					}
//					else if(field.getType().equals(Float.TYPE) || field.getType().equals(Float.class)){
//						if(n != null){
//							field.set(this, (float)n.getDoubleValue());
//						}
//						else{
//							field.set(this, null);
//						}
//					}
////					else if(field.getType().equals(Timestamp.class)){
////						field.set(this, args.get(field.getName()));
////					}
//					
//				}
//			} 
//			catch (IllegalArgumentException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	@Override
//	public String toString() {
//		StringBuilder sb = new StringBuilder("BaseArgs [");
//		
//		Field[] fields = this.getClass().getDeclaredFields();
//		for(Field field : fields){
//			try {
//				if(Modifier.isPublic(field.getModifiers())){
//					sb.append(field.getName());
//					sb.append(": ");
//					sb.append(field.get(this));
//					sb.append(", ");
//				}
//			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}
//		}
//		sb.append("]");
//		return sb.toString();
//	}
}
