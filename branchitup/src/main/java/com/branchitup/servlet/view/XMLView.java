package com.branchitup.servlet.view;

import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.View;

public class XMLView implements View {
//	private Document doc = null;

	public XMLView() {
	}

	public String getContentType() {
		return "text/xml";
	}

	/**
	 * Spring interface to render the XML view
	 * 
	 * @param model
	 *            Map of values with the data to render
	 * @param request
	 *            HTTP request
	 * @param response
	 *            HTTP response
	 * @throws java.lang.Exception
	 */
	public void render(Map model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

//		/** Generate the XML */
//		// Start document with "root" tag
//		this.doc = new Document(new Element("model"));
//		// Get the root tag
//		Element rootEl = this.doc.getRootElement();
//
//		// Add other tags
//		Map myModel = (Map) model.get("model");
//		List messages = (List) myModel.get("messages");
//		Iterator iter = messages.iterator();
//		while (iter.hasNext()) {
//			String message = (String) iter.next();
//
//			// Add new xml element
//			Element messageEl = new Element("message");
//			messageEl.setText(message);
//			rootEl.addContent(messageEl);
//		}
//
//		printXMLDocument(this.doc);
//
//		/** Set response type and write XML */
//		XMLOutputter outp = new XMLOutputter();
//		outp.setFormat(Format.getPrettyFormat());
//		String xmlAsString = outp.outputString(doc);
//
//		response.setContentType("text/xml");
//		response.setContentLength(xmlAsString.length());
//
		PrintWriter out = new PrintWriter(response.getOutputStream());
//		out.print(xmlAsString);
		out.flush();
		out.close();
	}
}