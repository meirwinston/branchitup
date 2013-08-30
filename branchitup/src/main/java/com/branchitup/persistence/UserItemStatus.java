package com.branchitup.persistence;

public enum UserItemStatus {
	ACTIVE,
	REMOVED, /*hardcoded in the DB deletePublication*/
	PENDING_ACTIVATION
}
