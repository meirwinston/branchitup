/*
 * branchitup PROPRIETARY/CONFIDENTIAL.
 * 
 * branchitup Proprietary - USE PURSUANT TO COMPANY INSTRUCTIONS
 * USE of this information by anyone and for any purpose may only be 
 * made by the prior written consent of branchitup.  This 
 * confidential information is owned by branchitup, and is 
 * protected under United States copyright laws and international treaties.
 */
package com.branchitup.system;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import net.coobird.thumbnailator.Thumbnails;
import nl.captcha.text.renderer.DefaultWordRenderer;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;

import com.branchitup.transfer.arguments.AttachAudioArgs;

public class FileUtilities {

//	public static BufferedImage readFileToBufferedImage(String name) throws IOException {
//		BufferedImage image = ImageIO.read(new File(name));
//		return image;
//	}
	
	public static void main(String[] args){
		String s = "/branchitupenv/diskresources/images/books/9cef05b5-bc0d-4ed2-af40-63b7e140ced9.JPEG";
		String d = "/branchitupenv/diskresources/images/books/test.JPEG";
		try {
			System.out.println("copyFile: " + copyFile(s, d));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * working
	 */
	public static boolean deleteFile(String fullRelativeName){
		String fileFullPath = Utils.getProperty("branchitup.rootDir") + "/" + fullRelativeName;
		System.out.println("FileUtilities.deleteFile " + fileFullPath);
		File file = new File(fileFullPath);
		if(file.exists()){
			return file.delete();
		}
		return false;
	}
	
	
//	public static Map<String,Object> writeImageToDisk(FileItem fileItem, String dir) throws Exception{
//		String fileName = UUID.randomUUID().toString();
//		
//		String dirPath = Utils.getProperty("branchitup.diskresources.imagesPath") + "/" + dir;
//		String fileFullPath = Utils.getProperty("branchitup.rootDir") + "/" + dirPath;
//		
//		Map<String,Object> props = new HashMap<String, Object>();
//		
//		BufferedImage bufferedImage = FileUtilities.toImage(fileItem);
//		String imageFormat = FileUtilities.getImageFormat(fileItem.getInputStream());
//		File file = new File(fileFullPath + "/" + fileName + "." + imageFormat);
//		if(!file.exists()){
//			file.createNewFile();
//		}
////		ImageIO.write(bufferedImage, imageFormat, file); //some formats like gif are getting distorted
//		fileItem.write(file);
//		props.put("width", bufferedImage.getWidth());
//		props.put("height", bufferedImage.getHeight());
//		props.put("format", imageFormat);
//		props.put("fileName", file.getName());
//		props.put("absolutePath", file.getAbsolutePath());
//		props.put("dirPath", dirPath);
//		props.put("size", fileItem.getSize());
//		props.put("bufferedImage", bufferedImage);
//		
//		return props;
//		
//	}
	
	public static boolean createNewFile(File file) throws IOException{
		if(!file.exists()){
			File parent = file.getParentFile();
			if(!parent.exists() && !parent.mkdirs()){
			    throw new IllegalStateException("Couldn't create dir: " + parent);
			}
			return file.createNewFile();
		}
		return false;
	}
	
	public static Map<String,Object> writeImageToDisk(FileItem fileItem, String dir, boolean generateThumbnail) throws Exception{
		String fileName = UUID.randomUUID().toString();
		
		String dirPath = Utils.getProperty("branchitup.diskresources.imagesPath") + "/" + dir;
		String fileFullPath = Utils.getProperty("branchitup.rootDir") + "/" + dirPath;
		
		Map<String,Object> props = new HashMap<String, Object>();
		
		BufferedImage bufferedImage = FileUtilities.toImage(fileItem);
		String imageFormat = FileUtilities.getImageFormat(fileItem.getInputStream());
		File file = new File(fileFullPath + "/" + fileName + "." + imageFormat);
//		System.out.println("FileUtilities " + fileFullPath + "/" + fileName + "." + imageFormat);
		
//		if(!file.exists()){
//			file.createNewFile();
//		}
		createNewFile(file);
		
//		ImageIO.write(bufferedImage, imageFormat, file); //some formats like gif are getting distorted
		fileItem.write(file);
		props.put("width", bufferedImage.getWidth());
		props.put("height", bufferedImage.getHeight());
		props.put("format", imageFormat);
		props.put("fileName", file.getName());
		props.put("absolutePath", file.getAbsolutePath());
		props.put("dirPath", dirPath);
		props.put("size", fileItem.getSize());
		props.put("bufferedImage", bufferedImage);
		
		if(generateThumbnail){
			File thumbnailFile = new File(fileFullPath + "/thumbnail_" + fileName + "." + imageFormat);//file.getAbsolutePath() + ".thumbnail." + imageFormat);
//			if(!thumbnailFile.exists()){
//				thumbnailFile.createNewFile();
//			}
			createNewFile(thumbnailFile);
			
			streamImage(file.getAbsolutePath(), new FileOutputStream(thumbnailFile),200);
		}
		
		return props;
		
	}
	
	public static boolean delete(com.branchitup.persistence.entities.AudioFile audioFile){
		boolean deleted = false;
		String fullName = Utils.getProperty("branchitup.rootDir") + "/" + audioFile.getDirPath() + "/" + audioFile.getFileName();
		
		File file = new File(fullName);
//		System.out.println("Attachment.delete: " + file.getAbsolutePath() + ", " + file.exists());
		if(file.exists()){
			deleted = file.delete();
		}
		else{
			System.out.println("FileSystem.DID NOT delete: " + fullName + "(" + audioFile.getFileName() + ")");
		}
		return deleted;
	}
	
	public static File writeAudioFileToDisk(AttachAudioArgs args) throws Exception{
		String fileName = UUID.randomUUID().toString();
		String dirPath = Utils.getProperty("branchitup.diskresources.audioDir");
		String fileFullPath = Utils.getProperty("branchitup.rootDir") + "/" + dirPath;
		String extension = "mp3";
//		if("audio/mpeg".equalsIgnoreCase(args.fileItem.getContentType())){
//			extension = "mp3";
//		}
//		else{
//			throw new InvalidAudioFrameException("This file format is not supported, please upload only a valid MP3 file<br><br>Thank You<br>");
//		}
		File file = new File(fileFullPath + "/" + fileName + "." + extension);
		createNewFile(file);
		args.fileItem.write(file);
		AudioFile audioFile;
		try {
			audioFile = AudioFileIO.read(file);
		} catch (CannotReadException | IOException | TagException
				| ReadOnlyFileException | InvalidAudioFrameException exp) {
			if(file != null && file.exists()){
				file.delete();
			}
			throw exp;
		}
		//CannotReadException wrong extension
		//InvalidAudioFrameException wrong binary audio
		
		if(audioFile instanceof MP3File){
//			Tag tag = audioFile.getTag();
			AudioHeader header = audioFile.getAudioHeader();

			args.trackLength = header.getTrackLength();
			args.sampleRate = header.getSampleRateAsNumber();
			args.bitRate = header.getBitRateAsNumber();
			args.channels = header.getChannels(); //Stereo
			args.format = header.getFormat();//MPEG-1 Layer 3
		}
		else{
			if(file.exists()){
				file.delete();
			}
			throw new InvalidAudioFrameException("This file format is not supported, please upload only a valid MP3 file<br><br>Thank You<br>");
		}
		
		return file;
	}
	
	
//	public static void streamJPG(String path,OutputStream out,int width) throws IOException{
//		File file = new File(path);
//		if(file.exists()){
//			Thumbnails.of(new File[]{file})
//	        .size(width, width)
//	        .outputFormat(ImageFile.Format.JPG.toString()).toOutputStream(out);
//		}
//	}
	
	/*
	 * TODO large image uploads will throw out of memory exception
	 */
	public static void streamImage(String path,OutputStream out,int width) throws IOException{
		File file = new File(path);
//		System.out.println("--->FileUtilities.thumbnail before save: " + path);
		if(file.exists()){
			Thumbnails.of(new File[]{file}).size(width, width).toOutputStream(out);
//			System.out.println("--->FileUtilities.thumbnail after save: " + path);
		}
	}
	
//	public static byte[] imageToByteArray(String path,int width) throws IOException{
//		File file = new File(path);
//		if(file.exists()){
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//			Thumbnails.of(new File[]{file}).size(width, width).toOutputStream(out);
//			return out.toByteArray();
//		}
//		return null;
//	}

	public static BufferedImage toImage(FileItem fileItem) throws IOException{
		BufferedImage image = ImageIO.read(fileItem.getInputStream());
		return image;
	}
	
	public static String getImageFormat(Object in) throws IOException{ //File, input stram
		// Create an image input stream on the image
		ImageInputStream iis = ImageIO.createImageInputStream(in);
		
		// Find all image readers that recognize the image format
        Iterator<?> iter = ImageIO.getImageReaders(iis);
        if (!iter.hasNext()) {
            // No readers found
            return null;
        }
        
     // Use the first reader
        ImageReader reader = (ImageReader)iter.next();
        
     // Close stream
        iis.close();

//      reader.getImageMetadata(0); //throws java.lang.IllegalStateException: Input source not set!
        // Return the format name
        return reader.getFormatName();

	}
	
	public static String getImageFormat(String path) throws IOException{
		return getImageFormat(new File(path));
	}
	
	public static void writeFileToStream(String path,OutputStream out) throws IOException{
		File file = new File(path);
		if(file.exists()){
			InputStream in = new FileInputStream(file);
			IOUtils.write(IOUtils.toByteArray(in), out);
			
			in.close();//release the OS's possession over this file (otherwise we will not be able to delete this file)
		}
	}
	
	public static File writeToFile(InputStream in, String fullPath) 
	throws FileNotFoundException, IOException{
		
		File file = new File(fullPath);
		if(createNewFile(file)){
			FileOutputStream fos = new FileOutputStream(file);
			DataOutputStream out = new DataOutputStream(new BufferedOutputStream(fos));
			int c;
			while((c = in.read()) != -1) {
				out.writeByte(c);
			}
			in.close();
			out.close();
		}
		
		return file;
	}
	
	private static void copyDirectory(File sourceLocation , File targetLocation)
	throws IOException {
		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				targetLocation.mkdir();
			}
			String[] children = sourceLocation.list();
			for (int i=0; i<children.length; i++) {
				copyDirectory(new File(sourceLocation, children[i]),new File(targetLocation, children[i]));
			}
		} 
		else {
			InputStream in = new FileInputStream(sourceLocation);
			OutputStream out = new FileOutputStream(targetLocation);

			// Copy the bits from instream to outstream
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}
	}

	
	public static File copyBookResourcesDirectory(long sourceBookId, long newBookId) throws IOException{
		File destDir = new File(Utils.getProperty("branchitup.resources.booksDir") + "/" + newBookId);
		File sourceDir = new File(Utils.getProperty("branchitup.resources.booksDir") + "/" + sourceBookId);
		if(sourceDir.exists() && !destDir.exists() && destDir.mkdir()){
			copyDirectory(sourceDir, destDir);
		}
		return destDir;
	}
	
//	public static File copyBookResourcesDirectory(long sourceBookId, long newBookId, Map<String,String> resourcePathMap) throws IOException{
//		File destDir = new File(Utils.getProperty("branchitup.resources.booksDir") + "/" + newBookId);
//		File sourceDir = new File(Utils.getProperty("branchitup.resources.booksDir") + "/" + sourceBookId);
//		if(sourceDir.exists() && !destDir.exists() && destDir.mkdir()){
//			copyDirectory(sourceDir, destDir);
//		}
//		return destDir;
//	}
	
	public static String getFileExtention(String fileName){
		return fileName.substring(fileName.lastIndexOf(".")+1);
	}
	
	public static File saveSheetResource(Long sheetId,FileItem fileItem) throws IOException{
		String fileExtension = getFileExtention(fileItem.getName());
//		fileExtension = fileExtension.substring(fileExtension.lastIndexOf(".")+1);
		if(fileExtension.trim().equals("")){
			throw new IOException("Invalid File Name, file name must contain an extention");
		}
		
		File file = new File(Utils.getProperty("branchitup.resources.sheetsDir") + "/" + sheetId);
		boolean success = file.exists();
		if(!success){
			success = file.mkdir();
		}
		if(success){
			return writeToFile(fileItem.getInputStream(), file.getAbsolutePath() + "/" + fileItem.getName());
		}
		return null;
	}
	
	public static File saveSheetResource(Long bookId,Long sheetId,FileItem fileItem) throws IOException{
		String fileExtension = fileItem.getName();
		fileExtension = fileExtension.substring(fileExtension.lastIndexOf(".")+1);
		if(fileExtension.trim().equals("")){
			throw new IOException("Invalid File Name, file name must contain an extention");
		}
		System.out.println("saveSheetResource::1");
		File file = new File(Utils.getProperty("branchitup.resources.booksDir") + "/" + bookId + "/sheets/" + sheetId);
		System.out.println("saveSheetResource::2: " + file.getAbsolutePath());
		boolean success = file.exists();
		System.out.println("saveSheetResource::3:");
		if(!success){
			System.out.println("saveSheetResource::4::"+success);
			success = file.mkdir();
			System.out.println("saveSheetResource::5::"+success);
		}
		if(success){
			System.out.println("saveSheetResource::6");
			return writeToFile(fileItem.getInputStream(), file.getAbsolutePath() + "/" + fileItem.getName());
		}
		return null;
	}
	
	//moved to diskResource class
//	public static boolean delete(DiskResource diskResource){
//		boolean deleted = false;
//		String fullName = Utils.getProperty("branchitup.rootDir") + "/" + diskResource.getDirPath() + "/" + diskResource.getFileName();
//		File file = new File(fullName);
//		if(file.exists()){
//			deleted = file.delete();
//			System.out.println(fullName + " Has been deleted successfuly!");
//		}
//		else{
//			System.out.println("FileSystem.DID NOT delete: " + fullName);
//		}
//		return deleted;
//	}
	
//	public static boolean deleteFile(String fullPath){
//		boolean deleted = false;
//		File file = new File(fullPath);
//		if(file.exists()){
//			deleted = file.delete();
//		}
//		return deleted;
//	}
	
	public static String parseExtention(String imageFileName){
		String extention = null;
		if(imageFileName != null){
			String[] arr = imageFileName.split("\\.");
			if(arr.length > 1){
				extention = arr[1];
			}
			else{
				extention = arr[0];
			}
		}
		return extention;
	}
	
	public static byte[] readFile(File file) throws IOException{
		FileInputStream in = new FileInputStream(file);
		byte[] bytes = new byte[(int)file.length()];
//		long length = in.read(bytes);
//		if(length > 0 ){
//			return bytes;
//		}
//		else{
//			return null;
//		}
		in.read(bytes);
		in.close();
		return bytes;
	}
	
	public static byte[] readFile(String path) throws IOException{
		File file = new File(path);
		if(file.exists()){
			return readFile(file);
		}
		else{
			throw new IOException("File Not found on server " + path);
		}
		
	}
	
	public static byte[] streamFile(File file,OutputStream out) throws IOException{
		FileInputStream in = new FileInputStream(file);
		
		byte[] bytes = new byte[(int)file.length()];
		long length = in.read(bytes);
		in.close();
		if(length > 0 ){
			return bytes;
		}
		else{
			return null;
		}
	}
	
	public static File copyFile(String source, String dest) throws IOException{
		File sourceFile = new File(source);
		File destFile = new File(dest);
		copyFile(sourceFile, destFile);
		
		return destFile;
	}
	
	public static void copyFile(File sourceFile, File destFile) throws IOException {
//		if(!destFile.exists()) {
//			destFile.createNewFile();
//		}
		createNewFile(destFile);

		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		}
		finally {
			if(source != null) {
				source.close();
			}
			if(destination != null) {
				destination.close();
			}
		}
	}
	
//	public static byte[] generateCaptchaImageBytes(String phrase){
//		BufferedImage image = generateCaptcha(phrase);
//		
//	}
	
	public static BufferedImage generateCaptcha(String phrase){
		BufferedImage image = new BufferedImage(250, 50, BufferedImage.TYPE_INT_ARGB);
		List<Color> colorList = new ArrayList<Color>();
		colorList.add(Color.GRAY);
		colorList.add(Color.DARK_GRAY);
		colorList.add(new Color(200,200,200));
		
		List<Font> fontList = new ArrayList<Font>();
		fontList.add(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		fontList.add(new Font(Font.SERIF, Font.ITALIC, 50));
		fontList.add(new Font(Font.MONOSPACED, Font.PLAIN, 50));
		fontList.add(new Font(Font.SERIF, Font.PLAIN, 30));
		fontList.add(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
		nl.captcha.text.renderer.DefaultWordRenderer renderer = new DefaultWordRenderer(colorList,fontList);
		renderer.render(phrase, image);
		
		return image;
	}
	
//	private static void copyfile(String source, String dest){
//		try{
//			File f1 = new File(source);
//			File f2 = new File(dest);
//			InputStream in = new FileInputStream(f1);
//
//			//For Append the file.
//			//  OutputStream out = new FileOutputStream(f2,true);
//
//			//For Overwrite the file.
//			OutputStream out = new FileOutputStream(f2);
//
//			byte[] buf = new byte[1024];
//			int len;
//			while ((len = in.read(buf)) > 0){
//				out.write(buf, 0, len);
//			}
//			in.close();
//			out.close();
//			System.out.println("File copied.");
//		}
//		catch(FileNotFoundException ex){
//			System.out.println(ex.getMessage() + " in the specified directory.");
//			System.exit(0);
//		}
//		catch(IOException e){
//			System.out.println(e.getMessage());  
//		}
//	}
}
