/*
 * %W% %E%
 *
 * YBLOB PROPRIETARY/CONFIDENTIAL.
 * 
 * yBlob Proprietary - USE PURSUANT TO COMPANY INSTRUCTIONS
 * USE of this information by anyone and for any purpose may only be 
 * made by the prior written consent of yBlob.  This 
 * confidential information is owned by yBlob, and is 
 * protected under United States copyright laws and international treaties.
 */
package com.branchitup.test;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.branchitup.persistence.entities.Genre;
import com.branchitup.system.Utils;

public class Test {
	public static void main(String[] args){
		
	}
	
	@SuppressWarnings("unchecked")
	public static List<Genre> readGenreList(){
		List<Genre> list = null;
		ObjectInputStream inputStream = null;
		try {
			String fileName = Utils.getProperty("yblob.testDir") + "/GenresList.obj";
			File file = new File(fileName);
			inputStream = new ObjectInputStream(new FileInputStream(file));
			Object obj = null;
			while ((obj = inputStream.readObject()) != null) {

				if (obj instanceof List) {

					list = (List<Genre>)obj;
				}
			}
		} 
		catch (EOFException ex) { //This exception will be caught when EOF is reached
			System.out.println("End of file reached.");
		} 
		catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} 
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} 
		catch (IOException ex) {
			ex.printStackTrace();
		} 
		finally {
			//Close the ObjectInputStream
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return list;
	}
	
	public static void write(List<Genre> list){
		ObjectOutputStream outputStream = null;
		try {
			String fileName = Utils.getProperty("yblob.testDir") + "/GenresList.obj";
			File file = new File(fileName);
			if(file.exists()){
				file.delete();
				file.createNewFile();
			}
			outputStream = new ObjectOutputStream(new FileOutputStream(file));
			outputStream.writeObject(list);
			
		} 
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} 
		catch (IOException ex) {
			ex.printStackTrace();
		} 
		finally {
			//Close the ObjectOutputStream
			try {
				if (outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
