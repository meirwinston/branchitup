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

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;
import com.steadystate.css.parser.CSSOMParser;
import com.branchitup.exception.PersistenceException;
import com.branchitup.export.ExportHandler.Paper;
import com.branchitup.persistence.BookRole;
import com.branchitup.system.Constants;
import com.branchitup.system.Utils;
import com.branchitup.transfer.arguments.PublishedBookExportArgs;
import com.branchitup.transfer.arguments.PublishedBookExportArgs.PublisherExportArgs;
import com.branchitup.transfer.arguments.PublishedBookExportArgs.SheetExportArgs;

public class PublishedBookHTMLExporter {
	public static final int IMAGE_SIZE_REDUCE_FACTOR = 30;
	private PublishedBookExportArgs pbookArgs;
	private CSSOMParser cssParser;
	
	public PublishedBookHTMLExporter(PublishedBookExportArgs pbookArgs){
		this.pbookArgs = pbookArgs;
		cssParser = new CSSOMParser();
	}
	
//	private static ImageFile findImageFileById(Long imageFileId){
//		if(imageFileId != null){
//			PersistenceModel pHandler = PersistenceModel.getInstance();
//			ImageFile imageFile = pHandler.selectImageFile(imageFileId);
//			return imageFile;
//		}
//		return null;
//	}
	
	public static Long parseIdFromUrl(String url){
		Long id = null;
		
		if(url != null && url.length() > 0){
			String[] arr = url.split("\\?");
			if(arr.length > 0){
				url = arr[1];
			}
			try {
				arr = url.split("&");
				for(int i = 0 ; i < arr.length ; i++){
					String[] pair = arr[i].split("=");
					if(pair.length == 2 && pair[0].equals("id")){
						id = Long.valueOf(pair[1]);
					}
				}
			} catch (Exception e) {
				//silent try and catch
			}
			
		}
		return id;
	}
	
//	private ImageFile findImageFileByIdUrl(String src){
//		if(src != null && src.startsWith("imageservice?")){
//			Long imageFileId = parseIdFromUrl(src);
//			ImageFile imageFile = bean.findImageFile(imageFileId);
////			ImageFile imageFile = findImageFileById(imageFileId);
//			return imageFile;
//		}
//		return null;
//	}
	
//	private void replaceImgSrc(Element content){
//		Elements imgElements = content.getElementsByTag("img");
//		Iterator<Element> itr = imgElements.listIterator();
//		while(itr.hasNext()){
//			Element element = itr.next();
//			String src = element.attr("src");
//			ImageFile imageFile = findImageFileByIdUrl(src);
//			if(imageFile != null){
//				String absolutePath = Utils.getProperty("branchitup.rootDir") + "/" + Utils.getProperty("branchitup.diskresources.imagesPath") + "/" + imageFile.getFolderName() + "/" + imageFile.getFileName();
////				String absolutePath = Utils.getProperty("branchitup.rootDir") + "/" + imageFile.dirPath + "/" + imageFile.fileName;
//				element.attr("src", absolutePath);
//			}
//		}
//	}
//	Utils.getProperty("branchitup.rootDir") 
//	+ "/" + Utils.getProperty("branchitup.diskresources.imagesPath")
	
	private void replaceImgSrc(Element content){
		Elements imgElements = content.getElementsByTag("img");
		Iterator<Element> itr = imgElements.listIterator();
		while(itr.hasNext()){
			Element element = itr.next();
			String src = element.attr("src");
			if(src != null && src.startsWith(Utils.getProperty("branchitup.diskresources.imagesUrl"))){
//				System.out.println("--2--->WKExporter image.src=" + src);
				src = src.replace(Utils.getProperty("branchitup.diskresources.imagesUrl"), Utils.getProperty("branchitup.diskresources.imagesPath"));
				src = Utils.getProperty("branchitup.rootDir") + "/" + src;
				element.attr("src", src);
			}
		}
	}
	
	private CSSStyleDeclaration parseCssText(String cssText) throws IOException{
		InputSource inputSource = new InputSource(new StringReader(cssText)); 
		CSSStyleDeclaration styleDeclaration = this.cssParser.parseStyleDeclaration(inputSource);
		return styleDeclaration;
	}
	
	private class BookIterator implements Iterator<Document>{
		int index = 0;
		Iterator<SheetExportArgs> sheetsItr = pbookArgs.sheetList.iterator();
		
		@Override
		public boolean hasNext() {
			if(index == 0 || sheetsItr.hasNext()){
				return true;
			}
			return false;
		}

		@Override
		public Document next() {
			if(index == 0){
				index++;
				try {
					return bookCoverToHTMLDocument();
				} catch (IOException e) {
					Constants.LOGGER.error(e);
					index = 0;
				} catch (PersistenceException e) {
					Constants.LOGGER.error(e);
					index = 0;
				}
			}
			else{
				SheetExportArgs s = sheetsItr.next();
				index++;
				try {
					return toHTMLDocument(s);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		public void remove() { //DO NOTHING, we are not maintaining a data structure
		}
	}
	
	public Iterator<Document> iterator(){
		return new BookIterator();
	}
	
	/**
	 * build an HTML document presenting a single sheet
	 * @param psheet
	 * @return
	 * @throws IOException
	 */
	private Document toHTMLDocument(SheetExportArgs psheet) throws IOException{
		Document doc = Jsoup.parse("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title></title></head><body style=\"padding: 0; border-spacing: 0; margin: 0; width: 100%;\"></body></html>");
		doc.getElementsByTag("title").first().text(psheet.name);
		
		
		Element body = doc.getElementsByTag("body").first();
		body.appendElement("h5").text(psheet.name);
		
		if(psheet.content != null){
			body.append(psheet.content);
		}
		
		replaceImgSrc(body);
		
		if(psheet.cssText != null){
			CSSStyleDeclaration style = parseCssText(psheet.cssText);
//			System.out.println("SHEET CSS::" + style.getCssText());
			CSSValue value = style.getPropertyCSSValue("background-image");
			if(value != null){
				String urlString = value.getCssText();
				urlString = urlString.substring(urlString.indexOf('(') + 1, urlString.indexOf(')'));
//				ImageFile imageFile = findImageFileByIdUrl(urlString);
//				if(imageFile != null){
//					//Utils.getProperty("branchitup.rootDir") + "/" + Utils.getProperty("branchitup.diskresources.imagesPath") + "/" + imageFile.folder + "/" + imageFile.fileName;
//					style.setProperty("background-image", "url('" + Utils.getProperty("branchitup.rootDir") + "/" + Utils.getProperty("branchitup.diskresources.imagesPath") + "/" + imageFile.getFolderName() + "/" + imageFile.getFileName() + "')", "");
////					System.out.println("url('" + Utils.getProperty("branchitup.rootDir") + "/" + imageFile.dirPath + "/" + imageFile.fileName + "')");
//					style.setProperty("background-repeat", "repeat", "");
//					style.setProperty("background-size", "100%", "");
//					style.setProperty("background-position", "top", "");
////					style.setProperty("margin", "0", "");
//////					style.setProperty("width", paper.width + "mm", "");
//////					style.setProperty("height", paper.height + "mm", "");
//				}
			}
			
//			System.out.println("setting body CSS: " + style.getCssText());
			body.attr("style", style.getCssText());
		}
		return doc;
	}
	
//	private Dimension normalizeImageSize(Paper paper,Dimension dimension){
//		Dimension d = new Dimension();//147.279
//		float ratio = (float)dimension.width / (float)dimension.height;
//		System.out.println("calculateImageSize: " + dimension.width + ", " + dimension.height);
//		if(dimension.width > dimension.height){
//			d.width = paper.width;
//			d.height = (int)((float)paper.width / ratio);
//		}
//		else{
//			d.width = (int)(paper.height * ratio);
//			d.height = paper.height;
//		}
//		d.width = (d.width - IMAGE_SIZE_REDUCE_FACTOR <= 0 ? d.width : d.width - IMAGE_SIZE_REDUCE_FACTOR);
//		d.height = (d.height - IMAGE_SIZE_REDUCE_FACTOR <= 0 ? d.height : d.height - IMAGE_SIZE_REDUCE_FACTOR);
//		System.out.println("calculateImageSize: AFTER::" + d.width + ", " + d.height);
//		return d;
//	}
//	private Dimension normalizeImageSize(Paper paper,ImageFile imageFile){
//		return normalizeImageSize(paper, new Dimension(imageFile.getWidth(), imageFile.getHeight()));
//	}
	
	private static void populateNames(Element contributorsDom, List<PublisherExportArgs> list, String title){
//		List<PublisherExportArgs> list = contibutorsMap.get(BookRole.WRITING.toString());
		Iterator<PublisherExportArgs> itr = list.iterator();
		StringBuilder sb = new StringBuilder();
		
		while(itr.hasNext()){
			PublisherExportArgs args = itr.next();
			sb.append(args.firstName + " " + args.lastName);
			if(itr.hasNext()){
				sb.append(", ");
			}
		}
		
		Element tr = contributorsDom.appendElement("tr");
		Element td1 = tr.appendElement("td");
		td1.attr("style","width: 10pt;padding-top: 0;padding-bottom: 0;padding-right: 5px;padding-left: 0; border-bottom: 1pt solid #BBBBBB;font-weight: bold; color: rgb(16,112,197)");
		Element td2 = tr.appendElement("td");
		td2.attr("style","padding: 0; border-bottom: 1pt solid #BBBBBB;");
		
		td1.html(title);
		td2.html(sb.toString());
	}
	
	private Document bookCoverToHTMLDocument() throws IOException, PersistenceException{
		Document doc;
		Paper paper = Paper.LETTER;
		
		doc = Jsoup.parse("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title></title></head><body style=\"padding: 0; border-spacing: 0; margin: 5mm; width: 100%;\"></body></html>");

		Element titleDom = doc.appendElement("div");
		titleDom.attr("style","width: 100%; text-align: center; color: #5F6D81;");
		titleDom.html(this.pbookArgs.title);
		
		//-- COVER IMAGE
		if(this.pbookArgs.coverImageFile != null){
			
			Element coverImgDom = doc.createElement("img");
			
			//http://www.branchitup.com/branchitup/images/BOOK_COVERS/thumbnail_739ad669-736e-4bea-a9bf-94eae228340a.JPEG
			
			//branchitup.localUrl
//			String imageSrc = 
//					Utils.getProperty("branchitup.diskresources.imagesUrl")
//					+ "/BOOK_COVERS/" + this.pbookArgs.coverImageFile.getFileName();
//			System.out.println("HERE is the cover image " + imageSrc);
			String imageSrc = Utils.getProperty("branchitup.rootDir") 
					+ "/" + Utils.getProperty("branchitup.diskresources.imagesPath") 
					+ "/" + this.pbookArgs.coverImageFile.getFolderName() 
					+ "/" + this.pbookArgs.coverImageFile.getFileName();
			
//			System.out.println("---->cover image path: " + imageSrc + " exists? " + (new File(imageSrc)).exists());
			coverImgDom.attr("src", imageSrc);
//			Dimension d = normalizeImageSize(paper, this.pbookArgs.coverImageFile);
//			coverImgDom.attr("style","width: " + d.width + "mm; height: " + d.height + "mm; border: 2px solid gray;");

			coverImgDom.attr("style","width: " + (paper.width/3) + "mm; height: auto; border: 2px solid gray;");
			
			Element coverImgDivDom = doc.appendElement("div");
			coverImgDivDom.attr("style","width: 100%; text-align: center; color: #5F6D81;");
			coverImgDivDom.appendChild(coverImgDom);
		}
		else{
			Constants.LOGGER.warn("WKExporter.buildHTMLDocument. coverImageFile is null, why?");
		}
		
		//--
		doc.appendElement("br");
		doc.appendElement("br");
		Element contributorsDom = doc.appendElement("table");
		contributorsDom.attr("style","width: 100%;border-spacing: 0px; border-top: 0px solid gray;border-left: 0px solid gray;border-right: 0px solid gray; font-family: arial;");
		
		Map<String,List<PublisherExportArgs>> contibutorsMap = this.pbookArgs.contributorsByRole;
		List<PublisherExportArgs> list;
		
		list = contibutorsMap.get(BookRole.WRITING.toString());
		if(list != null && list.size() > 0){
			populateNames(contributorsDom,list,"Writers");
		}

		list = contibutorsMap.get(BookRole.ILLUSTRATING.toString());
		if(list != null && list.size() > 0){
			populateNames(contributorsDom,list,"Illustrators");
		}
		
		list = contibutorsMap.get(BookRole.TRANSLATING.toString());
		if(list != null && list.size() > 0){
			populateNames(contributorsDom,list,"Translators");
		}
		
		list = contibutorsMap.get(BookRole.EDITING.toString());
		if(list != null && list.size() > 0){
			populateNames(contributorsDom,list,"Editors");
		}
		
		list = contibutorsMap.get(BookRole.PROOF_READING.toString());
		if(list != null && list.size() > 0){
			populateNames(contributorsDom,list,"Proof-readers");
		}
		
		//--
		if(this.pbookArgs.publisherAccount != null){
			Element publisherDom = doc.createElement("div");
			publisherDom.html(this.pbookArgs.publisherAccount.firstName + " " + this.pbookArgs.publisherAccount.lastName);
		}
		else{
			Constants.LOGGER.warn("PublishedBook.publisherAccount is null, why?");
		}
//		System.out.println("COVER DOC:::"+doc);
		return doc;
	}
//	private Document bookCoverToHTMLDocument() throws IOException, PersistenceException{
//		Document doc;
//		Paper paper = Paper.LETTER;
//		
//		doc = Jsoup.parse("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title></title></head><body style=\"padding: 0; border-spacing: 0; margin: 5mm; width: 100%;\"></body></html>");
//
//		Element titleDom = doc.appendElement("div");
//		titleDom.attr("style","width: 100%; text-align: center; color: #5F6D81;");
//		titleDom.html(this.pbookArgs.title);
//		
//		//-- COVER IMAGE
//		if(this.pbookArgs.coverImageFile != null){
//			
//			Element coverImgDom = doc.createElement("img");
//			
//			String imageSrc = Utils.getProperty("branchitup.rootDir") 
//					+ "/" + Utils.getProperty("branchitup.diskresources.imagesPath") 
//					+ "/" + this.pbookArgs.coverImageFile.getFolderName() 
//					+ "/" + this.pbookArgs.coverImageFile.getFileName();
//			
//			System.out.println("---->cover image path: " + imageSrc + " exists? " + (new File(imageSrc)).exists());
//			coverImgDom.attr("src", imageSrc);
////			Dimension d = normalizeImageSize(paper, this.pbookArgs.coverImageFile);
////			coverImgDom.attr("style","width: " + d.width + "mm; height: " + d.height + "mm; border: 2px solid gray;");
//
//			coverImgDom.attr("style","width: " + (paper.width/2) + "mm; height: auto; border: 2px solid gray;");
//			
//			Element coverImgDivDom = doc.appendElement("div");
//			coverImgDivDom.attr("style","width: 100%; text-align: center; color: #5F6D81;");
//			coverImgDivDom.appendChild(coverImgDom);
//		}
//		else{
//			Constants.LOGGER.warn("WKExporter.buildHTMLDocument. coverImageFile is null, why?");
//		}
//		
//		Element contributorsDom = doc.appendElement("div");
//		contributorsDom.attr("style","width: 100%; text-align: left; color: #5F6D81;");
//		
////		logger.debug("PublishedBookHTMLExporter.bookCoverToHTMLDocument " + bean +", " + this.pbook);
//		System.out.println("PublishedBookHTMLExporter.bookCoverToHTMLDocument " + this.pbookArgs);
////		Map<String,Object> pc = bean.findPublicationContributors(this.pbook.getBookId());
////		System.out.println("----->PDF:bookId " + this.pbookArgs.bookId + ":size:" + pc.get("contributorsByRole") + ", sheet owners: " + pc.get("sheetOwners"));
//		Iterator<Entry<String,List<PublisherExportArgs>>> itr = this.pbookArgs.contributorsByRole.entrySet().iterator();
//		while(itr.hasNext()){
////			Entry<PublisherRole,List<Publisher>> e = itr.next();
//			Entry<String,List<PublisherExportArgs>> e = itr.next();
//			List<PublisherExportArgs> publishers = e.getValue();
//			System.out.println("----->PDF::1:");
//			if(publishers.size() > 0){
//				System.out.println("----->PDF::2:");
//				StringBuilder sb = new StringBuilder(e.getKey() + ":");
//				for(PublisherExportArgs p : publishers){
//					sb.append(p.firstName + " " + p.lastName + ", ");
//				}
//				System.out.println("----->PDF::3:");
//				//add sheet owners if exist, to the authors list
//				if(e.getKey().equals(BookRole.WRITING) && this.pbookArgs.sheetOwners.size() > 0){
//					System.out.println("----->PDF::4:");
//					for(PublisherExportArgs user : this.pbookArgs.sheetOwners){
//						sb.append(user.firstName + " " + user.lastName + ", ");
//					}
//				}
//				System.out.println("----->PDF::text:" + sb);
//				contributorsDom.html(sb.toString());
//			}
//		}
//		if(this.pbookArgs.publisherAccount != null){
//			Element publisherDom = doc.createElement("div");
//			publisherDom.html(this.pbookArgs.publisherAccount.firstName + " " + this.pbookArgs.publisherAccount.lastName);
//		}
//		else{
//			Constants.LOGGER.warn("PublishedBook.publisherAccount is null, why?");
//		}
//		System.out.println("COVER DOC:::"+doc);
//		return doc;
//	}
}
