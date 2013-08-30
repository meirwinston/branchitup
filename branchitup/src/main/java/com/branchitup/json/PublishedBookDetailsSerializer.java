package com.branchitup.json;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.std.SerializerBase;
import com.branchitup.persistence.entities.PublishedBook;
import com.branchitup.persistence.entities.PublishedBookDetails;

/**
 * NOT IN USE
 * @author meir
 *
 */
public class PublishedBookDetailsSerializer extends SerializerBase<PublishedBookDetails>{
	public PublishedBookDetailsSerializer(){
		super(PublishedBook.class,true);
	}
	
	@Override
	public void serialize(PublishedBookDetails item, JsonGenerator gen,SerializerProvider p) 
	throws IOException,JsonProcessingException {
		gen.writeStartObject();
		
		gen.writeNumberField("bookId",item.getBookId());
		gen.writeStringField("title",item.getTitle());
		gen.writeStringField("version",item.getVersion());
		gen.writeNumberField("parentId",item.getParentId());
		gen.writeBooleanField("branchable",item.getBranchable());
		gen.writeStringField("bookLanguage",item.getBookLanguage());
		gen.writeStringField("bookSummary",item.getBookSummary());
		gen.writeNumberField("createdOn",item.getCreatedOn().getTime());
		gen.writeNumberField("publisherRoleMask",item.getPublisherRoleMask());
		gen.writeNumberField("deficiencyMask",item.getDeficiencyMask());
		gen.writeNumberField("coverImageFileId",item.getCoverImageFileId());
		gen.writeNumberField("publisherAccountId", item.getPublisherAccountId());
		gen.writeStringField("publisherFirstName", item.getPublisherFirstName());
		gen.writeStringField("publisherLastName", item.getPublisherLastName());
		gen.writeStringField("parentTitle", item.getParentTitle());
		gen.writeNumberField("pdfAttachmentId",item.getPdfAttachmentId());
		if(item.getCoverImageUrl() != null){
			gen.writeStringField("coverImageUrl", item.getCoverImageUrl());
		}
		
		//-- PublishedBook
//		gen.writeNumberField("bookId",item.getBookId());
//		gen.writeStringField("title",item.getTitle());
//		gen.writeStringField("version",item.getVersion());
//		gen.writeNumberField("parentId",item.getParentId());
//		gen.writeBooleanField("branchable",item.getBranchable());
//		gen.writeStringField("bookLanguage",item.getBookLanguage());
//		gen.writeStringField("bookSummary",item.getBookSummary());
//		gen.writeNumberField("createdOn",item.getCreatedOn().getTime());
//		gen.writeNumberField("publisherRoleMask",item.getPublisherRoleMask());
//		gen.writeNumberField("deficiencyMask",item.getDeficiencyMask());
//		gen.writeNumberField("coverImageFileId",item.getCoverImageFileId());
//		gen.writeNumberField("publisherAccountId", item.getPublisherAccountId());
//		gen.writeStringField("publisherFirstName", item.getPublisherAccount().getFirstName());
//		gen.writeStringField("publisherLastName", item.getPublisherAccount().getLastName());
//		gen.writeStringField("parentTitle", item.getParentBook().getTitle());
////		gen.writeNumberField("pdfAttachmentId",item.getPdfAttachmentId());
//		for(Attachment att : item.getAttachment()){
//			if(AttachmentFileType.PDF.equals(att.getFileType())){
//				gen.writeNumberField("pdfAttachmentId",att.getAttachmentId());
//			}
//		}
//		if(item.getCoverImageFileId() != null){
////			book.setCoverImageUrl(resolveThumbnailUrl(session, book.getCoverImageFileId()));
//			gen.writeStringField("coverImageUrl", StatelessDAO.resolveImageUrl(item.getCoverImageFile()));
//		}
		
//		String profileImageUrl = StatelessDAO.resolveThumbnailUrl(item.getSenderAccount().getProfileImageFile());
//		if(profileImageUrl != null){
//			gen.writeStringField("senderProfileImageUrl", profileImageUrl);
//		}
		
		
		gen.writeEndObject();
	}
}
