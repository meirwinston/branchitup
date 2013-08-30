package com.branchitup.transfer;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import com.branchitup.persistence.BookRole;
import com.branchitup.persistence.entities.Publisher;
import com.branchitup.persistence.entities.UserName;

public class PublicationContributors{
	public List<Publisher> publishers;
	public List<UserName> sheetOwners;
	
	public Map<BookRole, List<Publisher>> getPublishersByRole(){
		Map<BookRole,List<Publisher>> map = new Hashtable<BookRole, List<Publisher>>();
		if(this.publishers != null){
			for(Publisher p : publishers){
				List<BookRole> roles = p.getPublisherRoles();
				for(BookRole role : roles){
					List<Publisher> publishers = map.get(role);
					if(publishers == null){
						publishers = new ArrayList<Publisher>();
						map.put(role, publishers);
					}
					publishers.add(p);
				}
			}
		}
		return map;
	}
}
