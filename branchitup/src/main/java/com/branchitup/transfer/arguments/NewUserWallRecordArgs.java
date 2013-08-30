package com.branchitup.transfer.arguments;

public class NewUserWallRecordArgs extends BaseArgs{
	public Long userAccountId;
	public Long senderAccountId;
	public String message;
	
	public NewUserWallRecordArgs(){}
	public NewUserWallRecordArgs(Long userAccountId,Long senderAccountId, String message){
		this.userAccountId = userAccountId;
		this.senderAccountId = senderAccountId;
		this.message = message;
	}
	@Override
	public String toString() {
		return "NewUserWallRecordArgs [userAccountId=" + userAccountId
				+ ", senderAccountId=" + senderAccountId + ", message="
				+ message + "]";
	}
}
