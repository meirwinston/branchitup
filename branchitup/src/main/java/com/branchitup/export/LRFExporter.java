package com.branchitup.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;
//import lrf.epub.EPUBMetaData;

public class LRFExporter{
	
}
//public class LRFExporter extends EPUBMetaData{
//	private String creator;
//	private String identifier;
//	private String publisher;
//	private String rights;
//	private Vector<String> subject;
//	private String title;
//	
//	public LRFExporter(){
//		this("ENG");
//	}
//	
//	public LRFExporter(String language){
//		super(language);
//	}
//	
//	protected File htmlToXhtml(File htmlFile) throws FileNotFoundException, IOException, Exception{
//		String contenido = htmlToXhtml(htmlFile.getName(),new FileInputStream(htmlFile));
//		File xhtmlFile = new File(htmlFile.getParent(),htmlFile.getName() + ".xhtml");
//		FileOutputStream fos = new FileOutputStream(xhtmlFile);
//        fos.write(contenido.getBytes());
//        fos.close();
//        return xhtmlFile;
//	}
//	
//	public void start(String outputFile) throws FileNotFoundException, IOException{
//		init(outputFile);
//	}
//	
//	public void end() throws IOException{
//		close();
//	}
//	
//	public void processFile(File htmlFile) throws FileNotFoundException, IOException, Exception{
////		File xhtmlFile = htmlToXhtml(htmlFile);
//        processFile(htmlFile, htmlFile.getName() + ".html");
//	}
//	
//	@Override
//	public String getCreator() {
//		return creator;
//	}
//
//	@Override
//	public String getIdentifier() {
//		return identifier;
//	}
//
//	@Override
//	public String getPublisher() {
//		return publisher;
//	}
//
//	@Override
//	public String getRights() {
//		return rights;
//	}
//
//	@Override
//	public Vector<String> getSubject() {
//		return subject;
//	}
//
//	@Override
//	public String getTitle() {
//		return title;
//	}
//	
//	public void setCreator(String creator) {
//		this.creator = creator;
//	}
//
//	public void setIdentifier(String identifier) {
//		this.identifier = identifier;
//	}
//
//	public void setPublisher(String publisher) {
//		this.publisher = publisher;
//	}
//
//	public void setRights(String rights) {
//		this.rights = rights;
//	}
//
//	public void setSubject(Vector<String> subject) {
//		this.subject = subject;
//	}
//
//	public void setTitle(String title) {
//		this.title = title;
//	}
//
//	public static void main(String[] args) {
//
//	}
//
//}

/*
public static void main(String[] args) {
		try {
			// Create new Book
			Book book = new Book();
	
			// Set the title
			book.getMetadata().addTitle("Epublib test book 1");
			
			// Add an Author
			book.getMetadata().addAuthor(new Author("Joe", "Tester"));
	
			// Set cover image
			book.setCoverImage(new InputStreamResource(Simple1.class.getResourceAsStream("/book1/cover.png"), MediatypeService.PNG));
			
			// Add Chapter 1
			book.addResourceAsSection("Introduction", new InputStreamResource(Simple1.class.getResourceAsStream("/book1/chapter1.html"), MediatypeService.XHTML));
	
			// Add css file
			book.getResources().add(new InputStreamResource(Simple1.class.getResourceAsStream("/book1/book1.css"), "book1.css"));
	
			// Add Chapter 2
			Section chapter2 = book.addResourceAsSection("Second Chapter", new InputStreamResource(Simple1.class.getResourceAsStream("/book1/chapter2.html"), "chapter2.html"));
			
			// Add image used by Chapter 2
			book.getResources().add(new InputStreamResource(Simple1.class.getResourceAsStream("/book1/flowers_320x240.jpg"), "flowers.jpg"));
	
			// Add Chapter2, Section 1
			book.addResourceAsSubSection(chapter2, "Chapter 2, section 1", new InputStreamResource(Simple1.class.getResourceAsStream("/book1/chapter2_1.html"), "chapter2_1.html"));
	
			// Add Chapter 3
			book.addResourceAsSection("Conclusion", new InputStreamResource(Simple1.class.getResourceAsStream("/book1/chapter3.html"), "chapter3.html"));
	
			// Create EpubWriter
			EpubWriter epubWriter = new EpubWriter();
	
			// Write the Book as Epub
			epubWriter.write(book, new FileOutputStream("test1_book1.epub"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
*/
