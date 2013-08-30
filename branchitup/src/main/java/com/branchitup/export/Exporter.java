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
package com.branchitup.export;

public abstract class Exporter<E>{
	public abstract void export(E e);
}

//public class Exporter {
//	private static byte[] toPDFBytes(JEditorPane editorPane) throws Exception {
//		MessageFormat headerFormat = new MessageFormat("MY title");
//		MessageFormat footerFormat = new MessageFormat("Page {0,number,integer}");
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		Printable printable = editorPane.getPrintable(headerFormat, footerFormat);
//		
//		Rectangle page = PageSize.LETTER;
//		PageFormat pageFormat = new PageFormat();
////		if (getOrientation() == OrientationRequested.LANDSCAPE) {
////			page = page.rotate();
////			pageFormat.setOrientation(PageFormat.LANDSCAPE);
////		}
//		Document document = new Document(page);
//		PdfWriter writer = PdfWriter.getInstance(document, out);
//		document.open();
//		PdfContentByte cb = writer.getDirectContent();
//
//		float width = ((float) pageFormat.getWidth());
//		float height = ((float) pageFormat.getHeight());
//		int result = Printable.PAGE_EXISTS;
//		int pageNum = 0;
//		do {
//			PdfTemplate tp = cb.createTemplate(width, height);
//			// Graphics2D g2 = tp.createGraphicsShapes(width, height);
//			Graphics2D g2 = tp.createGraphics(width, height);
//			result = printable.print(g2, pageFormat, pageNum);
//			g2.dispose();
//			if (result == Printable.PAGE_EXISTS) {
//				cb.addTemplate(tp, 0, 0);
//				document.newPage();
//				pageNum++;
//			}
//		} while (result == Printable.PAGE_EXISTS);
//		document.close();
//		return out.toByteArray();
//	}
//	
//	public static FileResource toPDF(Book book){
//		FileResource attachment = new FileResource();
////		attachment.contentType = FileResource.PDF_CONTENT_TYPE;
//		attachment.fileName = "mybook.pdf";
//		
//		try {
//			JEditorPane pane = new JEditorPane("text/html",book.bookSummary);
////			JFrame frame = new JFrame();
////			frame.getContentPane().add(pane);
////			frame.setBounds(0, 0, 800, 600);
////			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
////			frame.setVisible(true);
//			
//			attachment.content = toPDFBytes(pane);
//			
//			File file = new File("/home/winston/Desktop/test.pdf");
//			if(!file.exists()){
//				file.createNewFile();
//			}
//			else{
//				file.delete();
//				file.createNewFile();
//			}
//			FileOutputStream out = new FileOutputStream(file);
//			out.write((byte[])attachment.content);
//			out.flush();
//			out.close();
//			
//		} 
//		catch (Exception e) {
//			
//			e.printStackTrace();
//		}
//		
//		return attachment;
//	}
//	
////	public static Attachment toPDF(Book book){
////		Attachment attachment = new Attachment();
////		attachment.type = Attachment.PDF_CONTENT_TYPE;
////		attachment.fileName = "mybook.pdf";
////		
////		try {
////			JEditorPane pane = new JEditorPane("text/html","<i>update</i><font face='cursive'>number</font>2<br/>");
//////			JFrame frame = new JFrame();
//////			frame.getContentPane().add(pane);
//////			frame.setBounds(0, 0, 800, 600);
//////			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//////			frame.setVisible(true);
////			
////			File f = new File("/home/winston/Desktop/test.pdf");
////			FileInputStream in = new FileInputStream(f);
////			byte[] gg = new byte[100000];
////			in.read(gg);
////			attachment.content = gg;//toPDFBytes(pane);
////			
////			File file = new File("/home/winston/Desktop/test.pdf");
////			if(!file.exists()){
////				file.createNewFile();
////			}
////			else{
////				file.delete();
////				file.createNewFile();
////			}
////			FileOutputStream out = new FileOutputStream(file);
////			out.write((byte[])attachment.content);
////			out.flush();
////			out.close();
////			
////		} 
////		catch (Exception e) {
////			
////			e.printStackTrace();
////		}
////		
////		return attachment;
////	}
//	
//	public static void main(String[] args){
//		JEditorPane pane = new JEditorPane("text/html","<i>update</i><font face='cursive'>number</font>2<br/>");
//		JFrame frame = new JFrame();
//		frame.getContentPane().add(pane);
//		frame.setBounds(0, 0, 800, 600);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setVisible(true);
//		
//		try {
//			byte[] bytes = toPDFBytes(pane);
//			
//			File file = new File("/home/winston/Desktop/test.pdf");
//			if(!file.exists()){
//				file.createNewFile();
//			}
//			else{
//				file.delete();
//				file.createNewFile();
//			}
//			FileOutputStream out = new FileOutputStream(file);
//			out.write(bytes);
//			out.flush();
//			out.close();
//		} 
//		catch (Exception e) {
//			
//			e.printStackTrace();
//		}
//		
//		
//	}
//}
