package com.branchitup.transfer.arguments;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.jsoup.nodes.Document;
import com.branchitup.persistence.entities.ImageFile;

public class PublishedBookExportArgs extends BaseArgs implements Serializable{
	static final long serialVersionUID = 1l;
	
	public static class SheetExportArgs{
		public Long sheetId;
		public String name;
		public String content;
		public String cssText;
	}
	public static class PublisherExportArgs{
//		public Long userAccountId;
		public String firstName;
		public String lastName;
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((firstName == null) ? 0 : firstName.hashCode());
			result = prime * result
					+ ((lastName == null) ? 0 : lastName.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PublisherExportArgs other = (PublisherExportArgs) obj;
			if (firstName == null) {
				if (other.firstName != null)
					return false;
			} else if (!firstName.equals(other.firstName))
				return false;
			if (lastName == null) {
				if (other.lastName != null)
					return false;
			} else if (!lastName.equals(other.lastName))
				return false;
			return true;
		}
	}
	public Long bookId;
	public String title;
	public ImageFile coverImageFile;
	public List<SheetExportArgs> sheetList;
	public Map<String,List<PublisherExportArgs>> contributorsByRole;
	public List<PublisherExportArgs> sheetOwners;
	public PublisherExportArgs publisherAccount;
	
	public static class SheetStruct{
		public File file;
		public Document doc;
		
		public SheetStruct(File file,Document doc){
			this.file = file;
			this.doc = doc;
		}
	}
	
	public List<SheetStruct> sheetFiles;
}
