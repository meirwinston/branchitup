package com.branchitup.exception.runtime;

import javax.persistence.EntityNotFoundException;

public class SheetNotFoundException extends EntityNotFoundException{
	private static final long serialVersionUID = 1L;
	public long sheetId;
	
	public SheetNotFoundException (long sheetId) {
		super("Sheet with id " + sheetId + " does not exist");
		this.sheetId = sheetId;
	}
	
	public SheetNotFoundException () {
	}

}
