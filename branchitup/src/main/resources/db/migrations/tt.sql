DELIMITER $$

USE `branchitup`$$

DROP PROCEDURE IF EXISTS `useritems.scrollByOwner`$$

CREATE DEFINER=`root`@`10.0.2.2` PROCEDURE `useritems.scrollByOwner`(userAccountId BIGINT, `offset` BIGINT, maxResults INT)
BEGIN
		(SELECT b.title AS `name`, b.bookId AS id, b.createdOn, b.modifiedOn, "BOOK" AS `type` FROM books AS b WHERE b.ownerAccountId = userAccountId)
		UNION
		(SELECT s.name, s.sheetId AS id, s.createdOn, s.modifiedOn, IF(s.blogId IS NULL,"SHEET","ARTICLE") AS `type` FROM sheets AS s WHERE s.ownerAccountId = userAccountId AND s.status = 'ACTIVE')
		ORDER BY modifiedOn DESC,createdOn DESC
		 LIMIT `offset`,maxResults;
    END$$

DELIMITER ;

   

BEGIN;
UPDATE useraccounts SET aboutMe = "Nee Lurie and guided by the work of Rashi, I studied to be a translator (French-English), earning a BA in Foreign Languages (German sub-major) at the University of the Witwatersrand (Wits) in Johannesburg; the city in which I was born. I read French literature and philosophy at the University of Nice during those undergraduate years and thereafter French-English translation and terminology at the School of Languages and Linguistics at Georgetown University in Washington DC. This culminated in finding my own Jewish and South African roots and thereafter a short stint introducing African languages onto South Africa's then and only lexcial data base at the South African Defence Force.
I switched to journalism, learning on the job, first for the South Africa Foundation and thereafter - \"where the action was\" -  for the Financial Mail in South Africa under legendary editors Steve Mulholland and Nigel Bruce. The latter who knew how to make work fun. By dint of circumstance I specialized in property - commercial, industrial, residential and low-income housing - graduating from a staff writer in the 80s to property editor of the FM from 1991-99.
Winning the Sanlam Financial Journalist of the Year award in 1995, I then began investigative journalism.
During a brief two and a half sojourn in Jerusalem at the behest of an uncle who asked me why I wasn't doing for Israel what I was doing for South Africa (while at the South Africa Foundation), I worked for great academics and masterful writers - struggling to find sufficient English work with minimal Hebrew - of which my uncle was not aware until I left Israel. I combined my day job with volunteering to combat disinformation about Israel from 2002 online and eventually in print.
I returned to South Africa - tried selling real estate which I hated - and made teshuva (return, repentance) working as a freelance journalist for The South African Jewish Report from 2007. In 2012  I started contributing op-eds  for and continued to send my comments to The Jerusalem Post and others.
Chafing at the bit to cover material how and when I want to, I had the good fortune recently of meeting online Meir Winston of BranchItUp in the New York City area to help me publish my work, and here I am."
WHERE userAccountId=99;
COMMIT;

CREATE TABLE `blogs` (
  `blogId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(250) COLLATE utf8_unicode_ci NOT NULL,
  `createdOn` DATETIME NOT NULL,
  `lastUpdated` DATETIME DEFAULT NULL,
  `userAccountId` BIGINT(20) NOT NULL,
  `mastheadImageFileId` BIGINT(20) DEFAULT NULL,
  `subtitle` VARCHAR(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` VARCHAR(4000) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`blogId`),
  KEY `FK_blogs_useraccounts` (`userAccountId`),
  CONSTRAINT `FK_blogs_useraccounts` FOREIGN KEY (`userAccountId`) REFERENCES `useraccounts` (`userAccountId`)
) ENGINE=INNODB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci


-- 
CREATE TABLE `useraccount_usergroup` (
  `userAccountId` BIGINT(20) NOT NULL,
  `userGroupId` BIGINT(20) NOT NULL,
  PRIMARY KEY (`userAccountId`,`userGroupId`),
  KEY `FK_useraccount_usergroup_usergroups` (`userGroupId`),
  CONSTRAINT `FK_useraccount_usergroup_usergroups` FOREIGN KEY (`userGroupId`) REFERENCES `usergroups` (`userGroupId`),
  CONSTRAINT `FK_useraccount_usergroup_useraccounts` FOREIGN KEY (`userAccountId`) REFERENCES `useraccounts` (`userAccountId`)
) ENGINE=INNODB


CREATE TABLE `usergroup_authority` (
  `userGroupId` BIGINT(20) NOT NULL,
  `authorityId` BIGINT(20) NOT NULL,
  PRIMARY KEY (`userGroupId`,`authorityId`),
  KEY `FK_usergroup_authority_authorities` (`authorityId`),
  CONSTRAINT `FK_usergroup_authority_authorities` FOREIGN KEY (`authorityId`) REFERENCES `authorities` (`authorityId`),
  CONSTRAINT `FK_usergroup_authority_usergroups` FOREIGN KEY (`userGroupId`) REFERENCES `usergroups` (`userGroupId`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci

CREATE TABLE `usergroups` (
  `userGroupId` BIGINT(20) NOT NULL,
  `createdOn` DATETIME NOT NULL,
  PRIMARY KEY (`userGroupId`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci

CREATE TABLE `authorities` (
  `authorityId` BIGINT(20) NOT NULL,
  `name` VARCHAR(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`authorityId`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci


