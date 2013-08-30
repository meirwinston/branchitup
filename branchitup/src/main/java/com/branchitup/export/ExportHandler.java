/*
 * branchitup PROPRIETARY/CONFIDENTIAL.
 * 
 * branchitup Proprietary - USE PURSUANT TO COMPANY INSTRUCTIONS
 * USE of this information by anyone and for any purpose may only be 
 * made by the prior written consent of branchitup.  This 
 * confidential information is owned by branchitup, and is 
 * protected under United States copyright laws and international treaties.
 */
package com.branchitup.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.branchitup.persistence.AttachmentFileType;
import com.branchitup.system.Utils;
import com.branchitup.transfer.arguments.PublishedBookExportArgs;

/*
 * TO TEST THE TEMPLATE:

C:\Program Files\wkhtmltopdf>wkhtmltopdf --margin-left 0mm --margin-right 0mm --margin-top 0mm --margin-bottom 0mm --page-size Letter --page-width 216mm --page-height 279mm D:\projects\eclipse\yblob\tmp\book-template-2.html D:\projects\eclipse\yblob\tmp\book-template-1.html D:\yblobenv\tmp\a.pdf
 */
//@Service
public class ExportHandler {
//	private CSSOMParser cssParser = new CSSOMParser();;
	
	public enum WKProperty{
		TITLE("--title"),
		DISABLE_JAVASCRIPT("--disable-javascript",""),
		MARGIN_LEFT("--margin-left", "0mm"), 
		MARGIN_RIGHT("--margin-right", "0mm"), 
		MARGIN_TOP("--margin-top", "0mm"),
		MARGIN_BOTTOM("--margin-bottom", "0mm"),
		PAGE_SIZE("--page-size","Letter"),
		PAGE_WIDTH("--page-width","216mm"), //1024px
		PAGE_HEIGHT("--page-height","279mm"), //1448px
		PAGE_OFFSET("--page-offset","0"),
		ORIENTATION("--orientation","Portrait"),
		USER_STYLE_SHEET("--user-style-sheet"),// <url> Specify a user style sheet, to load with every page
		TOC("toc"),
		TOC_HEADER_TEXT("--toc-header-text");
		
		String name;
		String defaultValue;
		WKProperty(String name){
			this.name = name;
		}
		WKProperty(String name,String defaultValue){
			this.name = name;
			this.defaultValue = defaultValue;
		}
		WKProperty(){}
	}
	
	public enum Paper{ //size in mm
		A0(841,1189),
		A1(594,841),
		A2(420,594),
		A3(297,420),
		A4(210,297),
		A5(148,210),
		A6(105,248),
		A7(74,105),
		A8(52,74),
		A9(37,52),
		A10(26,37),
		
		LETTER(216,279),
		LEGAL(216,356),
		JUNIOR_LEGAL(203,127),
		LEDGER(432,279),
		TABLOID(279,432)
		;
		int width;
		int height;
		Paper(int width, int height){
			this.width = width;
			this.height = height;
		}
	}
	
	public List<EbookAttachmentCarrier> export(PublishedBookExportArgs pbook) 
	throws IOException, InterruptedException{
		List<EbookAttachmentCarrier> list = new ArrayList<EbookAttachmentCarrier>();
		PublishedBookHTMLExporter exporter = new PublishedBookHTMLExporter(pbook);
		
		//-- generate temporary files
		Iterator<Document> itr = exporter.iterator();
		List<File> files = new ArrayList<File>();
		pbook.sheetFiles = new ArrayList<>();
		while(itr.hasNext()){
			Document doc = itr.next();
			
			File f = File.createTempFile(UUID.randomUUID().toString(),".html");
			f.deleteOnExit();
			
			FileWriter outFile = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(outFile);
			bw.write(doc.toString());
			bw.close();
			outFile.close();
//			System.out.println("----------------WKExporter:export:doc::" + doc);
			files.add(f);
			
			pbook.sheetFiles.add(new PublishedBookExportArgs.SheetStruct(f,doc));
		}
		
		//-- generate PDF
		list.add(new EbookAttachmentCarrier(exportToPDF(files),AttachmentFileType.PDF));
		try {
			list.add(new EbookAttachmentCarrier(exportToEpub(pbook),AttachmentFileType.EPUB));
		} catch (Exception e) {
			Logger.getLogger(this.getClass()).error(e.getMessage(), e);
		}
		
		//-- delete temporary files
		for(File file : files){
			file.delete();
		}
		return list;
	}
	
//	private static void printProcessOutput(Process process) throws IOException{
//		BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//		String line;
//		do{
//			line = errorReader.readLine();
//			Constants.LOGGER.info("error: " + line);
//		}
//		while(line != null);
//	}
//	LRFExporter lrfExporter = new LRFExporter();
//	private File exportToEpub(List<File> sourceFiles, PublishedBookExportArgs pbook) throws Exception{
//		String fileName = UUID.randomUUID().toString();
////		String outputPath = Utils.getProperty("branchitup.rootDir") + "/" + Utils.getProperty("branchitup.diskresources.epubDir") + "/" + fileName + ".epub";
//		String outputPath = "/data/tmp/" + fileName + ".epub";
//		lrfExporter.start(outputPath);
//		lrfExporter.setCreator(pbook.publisherAccount.firstName + " " + pbook.publisherAccount.lastName);
//		lrfExporter.setIdentifier(pbook.bookId.toString());
//		lrfExporter.setPublisher(pbook.publisherAccount.firstName + " " + pbook.publisherAccount.lastName);
//		lrfExporter.setRights("free");
////		lrfExporter.setSubject()
//		lrfExporter.setTitle(pbook.title);
//		for(File htmlFile : sourceFiles){
//			lrfExporter.processFile(htmlFile);
//		}
//		
//		lrfExporter.end();
//		return null;
//	}
	
	private File exportToEpub(PublishedBookExportArgs pbook) throws Exception{
		String fileName = UUID.randomUUID().toString();
		
		Book epubBook = new Book();
		
		// Set the title
		epubBook.getMetadata().addTitle(pbook.title);
		
		// Add an Author
		epubBook.getMetadata().addAuthor(new Author(pbook.publisherAccount.firstName, pbook.publisherAccount.lastName));
			
		// Set cover image
		if(pbook.coverImageFile != null){
			File f = new File(pbook.coverImageFile.resolveFullPath());
//			System.out.println("---->cover image file::: " + f.exists() + ", " + f.getAbsolutePath());
			if(f.exists()){
				epubBook.setCoverImage(new Resource(new FileInputStream(f),"images/" + pbook.coverImageFile.getFileName()));
			}
		}
		
					
		for(PublishedBookExportArgs.SheetStruct s : pbook.sheetFiles){
			Elements imgArr = s.doc.getElementsByTag("img");
			for(Element img: imgArr){
				String url = img.attr("src");
				File f = new File(url);
				if(f.exists()){
			 		Resource resource = new Resource(new FileInputStream(f),"images/" + f.getName());
					epubBook.getResources().add(resource);
					img.attr("src","images/" + f.getName());
				}
			}
			epubBook.addSection(s.doc.head().getElementsByTag("title").text(), new Resource(new StringReader(s.doc.toString()),s.file.getName()));
		}
		
		// Create EpubWriter
		EpubWriter epubWriter = new EpubWriter();
			
		// Write the Book as Epub
		String outputPath = Utils.getProperty("branchitup.rootDir")
				+ "/" 
				+ Utils.getProperty("branchitup.diskresources.epubDir") 
				+ "/" + 
				fileName + ".epub";
		File outputFile = new File(outputPath);
		epubWriter.write(epubBook, new FileOutputStream(outputFile));
		
		return outputFile;
	}
	
	private File exportToPDF(List<File> sourceFiles) throws IOException, InterruptedException{
		List<String> processCommand = new ArrayList<String>();
		processCommand.add(Utils.getProperty("branchitup.process.wkhtmltopdf"));
		processCommand.add("--margin-left");
		processCommand.add("0");
		processCommand.add("--margin-right");
		processCommand.add("0");
		for(File source : sourceFiles){
			processCommand.add(source.getAbsolutePath());
//			System.out.println("ADD:: -----------------: " + source.getAbsolutePath());
		}
		
//		processCommand.add("/tmp/61fbcee8-43b7-44ed-acfd-c40941d61b43165223152444959977.html");

//		System.out.println("--------->WKExporter.exportToPDF: THE PROCESS COMMAND: -----------:::" + processCommand + ",     ****    ,");

//		fileProps.put("fileName",UUID.randomUUID() + ".pdf");
//		fileProps.put("fileType","PDF");
//		fileProps.put("dirPath", Utils.getProperty("branchitup.diskresources.pdfDir"));
//		fileProps.put("fullPath", Utils.getProperty("branchitup.rootDir") + "/" + fileProps.get("dirPath") + "/" + fileProps.get("fileName"));
		String fileName = UUID.randomUUID().toString();
		File pdfFile = new File(Utils.getProperty("branchitup.rootDir") + "/" + Utils.getProperty("branchitup.diskresources.pdfDir") + "/" + fileName + ".pdf");
		
		processCommand.add(pdfFile.getAbsolutePath());
		//
//		System.out.println("----->PDF full path: " + pdfFile.getAbsolutePath());

		Process process = Runtime.getRuntime().exec(processCommand.toArray(new String[0]));

//		printProcessOutput(process);
		process.waitFor();

		return pdfFile;
	}
	
	public static void main(String[] args) throws IOException, InterruptedException{
//		System.out.println("-*---------GENERATED PDF-----------:::");
//		Process process = Runtime.getRuntime().exec("C:\\Program Files (x86)\\wkhtmltopdf\\wkhtmltopdf http://www.yahoo.com E:\\aaa.pdf");
//		printProcessOutput(process);
//		process.waitFor();
		
	}
}
