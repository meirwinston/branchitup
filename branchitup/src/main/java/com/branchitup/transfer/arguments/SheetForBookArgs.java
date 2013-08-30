package com.branchitup.transfer.arguments;

public class SheetForBookArgs extends BaseArgs{
	public Long bookId;
	public boolean includeSheets = true;
	public boolean includeGenre = false;

	@Override
	public String toString() {
		return "SheetForBookArgs [bookId=" + bookId + ", includeSheets="
				+ includeSheets + ", includeGenre=" + includeGenre + "]";
	}
}
