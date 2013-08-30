/*
 * branchitup.com sql script
 */
CREATE DATABASE IF NOT EXISTS `branchitup` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;

USE `branchitup`;

/*Table structure for table `SEQUENCE` */

DROP TABLE IF EXISTS `SEQUENCE`;

CREATE TABLE `SEQUENCE` (
  `SEQ_NAME` VARCHAR(50) NOT NULL,
  `SEQ_COUNT` DECIMAL(38,0) DEFAULT NULL,
  PRIMARY KEY (`SEQ_NAME`)
) ENGINE=INNODB;

/*Table structure for table `imagefiles` */

DROP TABLE IF EXISTS `imagefiles`;

CREATE TABLE `imagefiles` (
  `imageFileId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `createdOn` DATETIME DEFAULT NULL,
  `width` INT(11) DEFAULT NULL COMMENT 'in pixels',
  `height` INT(11) DEFAULT NULL COMMENT 'in pixels',
  `size` INT(11) DEFAULT NULL COMMENT 'in bytes',
  `format` VARCHAR(20) DEFAULT NULL,
  `fileName` VARCHAR(255) DEFAULT NULL,
  `ownerAccountId` BIGINT(20) DEFAULT NULL,
  `folderName` VARCHAR(50) DEFAULT NULL COMMENT 'USER_DEFAULT, GARBAGE, folder management, user will be able to create folders',
  `album` VARCHAR(50) DEFAULT 'Default',
  PRIMARY KEY (`imageFileId`)
) ENGINE=INNODB AUTO_INCREMENT=1;

/*Table structure for table `useraccounts` */

DROP TABLE IF EXISTS `useraccounts`;
CREATE TABLE `useraccounts` (
  `userAccountId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) DEFAULT NULL COMMENT 'all other fields are in the directory service',
  `password` VARCHAR(255) NOT NULL,
  `groupName` VARCHAR(10) NOT NULL DEFAULT 'user' COMMENT 'user,admin',
  `firstName` VARCHAR(50) NOT NULL,
  `middleName` VARCHAR(50) DEFAULT NULL,
  `lastName` VARCHAR(50) NOT NULL,
  `createdOn` DATETIME NOT NULL,
  `email` VARCHAR(200) NOT NULL,
  `createdByIP` VARCHAR(45) DEFAULT NULL,
  `modifiedByIP` VARCHAR(45) DEFAULT NULL,
  `gender` FLOAT DEFAULT NULL,
  `profileImageFileId` BIGINT(20) DEFAULT NULL,
  `visibility` VARCHAR(30) NOT NULL DEFAULT 'HIDE_CONTACT_INFORMATION' COMMENT 'PUBLIC,PRIVATE, HIDE_EMAIL',
  `aboutMe` TEXT DEFAULT NULL,
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  `lastLogIn` DATETIME DEFAULT NULL,
  `loginCount` INT(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`userAccountId`),
  UNIQUE KEY `emailUniqueuIndex` (`email`),
  KEY `FK_useraccounts_imagefiles` (`profileImageFileId`),
  CONSTRAINT `FK_useraccounts_imagefiles` FOREIGN KEY (`profileImageFileId`) REFERENCES `imagefiles` (`imageFileId`)
) ENGINE=INNODB AUTO_INCREMENT=1;


/*Table structure for table `attachments` */

DROP TABLE IF EXISTS `attachments`;

CREATE TABLE `attachments` (
  `attachmentId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `fileName` VARCHAR(255) NOT NULL,
  `createdOn` DATETIME NOT NULL,
  `fileType` VARCHAR(50) NOT NULL,
  `dirPath` VARCHAR(255) DEFAULT NULL,
  `publishedBookId` BIGINT(20) DEFAULT NULL COMMENT 'one to many relationship between published books and attachments',
  `ownerAccountId` BIGINT(20) DEFAULT NULL,
  `size` INT DEFAULT NULL,
  `contentType` VARCHAR(50) DEFAULT NULL,
  `fileHeader` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`attachmentId`),
  KEY `FK_attachments_publishedbooks` (`publishedBookId`),
  KEY `FK_attachments_useraccounts` (`ownerAccountId`),
  CONSTRAINT `FK_attachments_useraccounts` FOREIGN KEY (`ownerAccountId`) REFERENCES `useraccounts` (`userAccountId`)
) ENGINE=INNODB AUTO_INCREMENT=3;



/*Table structure for table `bookreviews` */

DROP TABLE IF EXISTS `bookreviews`;

CREATE TABLE `bookreviews` (
  `reviewId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `bookId` BIGINT(20) NOT NULL,
  `rating` FLOAT DEFAULT NULL,
  `comment` VARCHAR(255) DEFAULT NULL,
  `username` VARCHAR(50) DEFAULT NULL,
  `timestamp` DATETIME NOT NULL,
  `parentId` BIGINT(20) DEFAULT NULL,
  `createdByIP` VARCHAR(45) DEFAULT NULL,
  `modifiedByIP` VARCHAR(45) DEFAULT NULL,
  PRIMARY KEY (`reviewId`),
  KEY `fk_bookreviews_books` (`bookId`),
  KEY `FK_users` (`username`)
) ENGINE=INNODB;

/*Table structure for table `publishedbooks` */

DROP TABLE IF EXISTS `publishedbooks`;

CREATE TABLE `publishedbooks` (
  `bookId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `createdOn` DATETIME NOT NULL,
  `publisherRoleMask` INT(11) UNSIGNED ZEROFILL DEFAULT '00000000000' COMMENT 'enum: AUTHOR,ILLUSTRATOR,EDITOR,COMMENTATOR,TRANSLATOR,PROOF_READER',
  `deficiencyMask` INT(11) UNSIGNED ZEROFILL DEFAULT '00000000000' COMMENT 'enum: CO_AUTHORING,ILLUSTRATION,EDITING,COMMENTING,TRANSLATION,PROOF_READING',
  `parentId` BIGINT(20) DEFAULT NULL,
  `subversionCount` INT(11) NOT NULL DEFAULT '0' COMMENT 'many times this publication has been branched, helps to construct hierarchical version',
  `version` VARCHAR(50) DEFAULT NULL COMMENT 'hierarchical dot delimited string, e.g. 1.2.1',
  `title` VARCHAR(50) NOT NULL,
  `bookSummary` VARCHAR(512) DEFAULT NULL,
  `coverImageFileId` BIGINT(20) DEFAULT NULL,
  `publisherAccountId` BIGINT(20) NOT NULL COMMENT 'the maximum length of an email address is 320 characters.',
  `properties` VARCHAR(512) DEFAULT NULL COMMENT 'JSON MAP of export properties',
  `publisherComment` VARCHAR(512) DEFAULT NULL,
  `bookLanguage` VARCHAR(3) NOT NULL DEFAULT 'ENG',
  `status` VARCHAR(20) DEFAULT NULL COMMENT 'helps to determine whether this book is active and should be displayed',
  
  `publishedByIP` VARCHAR(45) DEFAULT NULL,
  `branchable` TINYINT(4) DEFAULT 0,
  PRIMARY KEY (`bookId`),
  KEY `FK_publishedbooks_useraccounts` (`publisherAccountId`),
  KEY `FK_publishedbooks_publishedbooks` (`parentId`),
  CONSTRAINT `FK_publishedbooks_useraccounts` FOREIGN KEY (`publisherAccountId`) REFERENCES `useraccounts` (`userAccountId`),
  CONSTRAINT `FK_publishedbooks_publishedbooks` FOREIGN KEY (`parentId`) REFERENCES `publishedbooks` (`bookId`)
) ENGINE=INNODB AUTO_INCREMENT=1;

DROP TABLE IF EXISTS `audiofiles`;

CREATE TABLE `audiofiles` (
  `audioFileId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `ownerAccountId` BIGINT(20) NOT NULL,
  `fileName` VARCHAR(255) NOT NULL,
  `createdOn` DATETIME NOT NULL,
  `dirPath` VARCHAR(255) NOT NULL,
  `publishedBookId` BIGINT(20) NOT NULL,
  `description` VARCHAR(512) DEFAULT NULL,
  `size` BIGINT(20) DEFAULT NULL,
  `contentType` VARCHAR(50) DEFAULT NULL,
  `trackLength` INT(11) DEFAULT NULL,
  `sampleRate` INT(11) DEFAULT NULL,
  `bitRate` BIGINT(20) DEFAULT NULL,
  `channels` VARCHAR(32) DEFAULT NULL,
  `format` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (`audioFileId`),
  KEY `FK_audiofiles_useraccounts` (`ownerAccountId`),
  KEY `FK_audiofiles_publishedbooks` (`publishedBookId`),
  CONSTRAINT `FK_audiofiles_publishedbooks` FOREIGN KEY (`publishedBookId`) REFERENCES `publishedbooks` (`bookId`),
  CONSTRAINT `FK_audiofiles_useraccounts` FOREIGN KEY (`ownerAccountId`) REFERENCES `useraccounts` (`userAccountId`)
) ENGINE=INNODB AUTO_INCREMENT=4;


/*Table structure for table `books` */

DROP TABLE IF EXISTS `books`;

CREATE TABLE `books` (
  `bookId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `derivedFromId` BIGINT(20) DEFAULT NULL,
  `title` VARCHAR(50) NOT NULL,
  `ownerAccountId` BIGINT(20) NOT NULL,
  `bookSummary` VARCHAR(1024) DEFAULT NULL,
  `createdOn` DATETIME NOT NULL,
  `modifiedOn` DATETIME DEFAULT NULL,
  `publicationId` BIGINT(20) DEFAULT NULL COMMENT 'NULL if this book is not published',
  `createdByIP` VARCHAR(45) DEFAULT NULL,
  `modifiedByIP` VARCHAR(45) DEFAULT NULL,
  `status` VARCHAR(20) DEFAULT 'ACTIVE',
  PRIMARY KEY (`bookId`),
  KEY `FK_parentBook` (`derivedFromId`),
  KEY `FK_books_useraccounts` (`ownerAccountId`),
  CONSTRAINT `FK_books_publishedbooks` FOREIGN KEY (`derivedFromId`) REFERENCES `publishedbooks` (`bookId`),
  CONSTRAINT `FK_books_useraccounts` FOREIGN KEY (`ownerAccountId`) REFERENCES `useraccounts` (`userAccountId`)
) ENGINE=INNODB AUTO_INCREMENT=1;

/*Table structure for table `genres` */

DROP TABLE IF EXISTS `genres`;

CREATE TABLE `genres` (
  `genreId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `description` VARCHAR(512) DEFAULT NULL,
  `modifiedOn` DATETIME DEFAULT NULL,
  `modifiedBy` BIGINT(20) DEFAULT NULL,
  `originatorAccountId` BIGINT(20) DEFAULT NULL,
  `createdOn` DATETIME NOT NULL,
  `iconImageFileId` BIGINT(20) DEFAULT NULL,
  `createdByIP` VARCHAR(45) DEFAULT NULL,
  `modifiedByIP` VARCHAR(45) DEFAULT NULL,
  PRIMARY KEY (`genreId`),
  KEY `FK_genres_imagefiles` (`iconImageFileId`),
  KEY `FK_genres_useraccounts` (`originatorAccountId`),
  KEY `FK_genres_useraccounts-modifiedBy` (`modifiedBy`),
  CONSTRAINT `FK_genres_useraccounts` FOREIGN KEY (`originatorAccountId`) REFERENCES `useraccounts` (`userAccountId`),
  CONSTRAINT `FK_genres_useraccounts-modifiedBy` FOREIGN KEY (`modifiedBy`) REFERENCES `useraccounts` (`userAccountId`)
) ENGINE=INNODB AUTO_INCREMENT=1;

/*Table structure for table `publishedbooks_genres` */

DROP TABLE IF EXISTS `publishedbooks_genres`;

CREATE TABLE `publishedbooks_genres` (
  `bookId` BIGINT(20) NOT NULL,
  `genreId` BIGINT(20) NOT NULL,
  `sequenceIndex` SMALLINT(6) DEFAULT NULL,
  PRIMARY KEY (`bookId`,`genreId`),
  KEY `FK_publishedbooks_genres_genres` (`genreId`)
) ENGINE=INNODB;

/*Table structure for table `publishedsheets` */

DROP TABLE IF EXISTS `publishedsheets`;

CREATE TABLE `publishedsheets` (
  `sheetId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(96) DEFAULT NULL,
  `cssText` VARCHAR(255) DEFAULT NULL,
  `content` MEDIUMTEXT,
  `bookId` BIGINT(20) DEFAULT NULL,
  `sequenceIndex` INT(11) NOT NULL,
  `ownerAccountId` BIGINT(20) NOT NULL,
  PRIMARY KEY (`sheetId`),
  KEY `FK_publishedsheets_publishedbooks` (`bookId`),
  KEY `FK_publishedsheets_useraccounts` (`ownerAccountId`),
  CONSTRAINT `FK_publishedsheets_publishedbooks` FOREIGN KEY (`bookId`) REFERENCES `publishedbooks` (`bookId`)
) ENGINE=INNODB AUTO_INCREMENT=1;

/*Table structure for table `ratings` */

DROP TABLE IF EXISTS `ratings`;

CREATE TABLE `ratings` (
  `userAccountId` BIGINT(20) NOT NULL,
  `bookId` BIGINT(20) NOT NULL,
  `rate` DOUBLE NOT NULL COMMENT 'Assessment or classification of a book on a scale according to how much or how little of a particular quality is possesses',
  `createdOn` DATETIME NOT NULL,
  PRIMARY KEY (`userAccountId`,`bookId`),
  KEY `FK_ratings_publishedbooks` (`bookId`),
  CONSTRAINT `FK_ratings_useraccounts` FOREIGN KEY (`userAccountId`) REFERENCES `useraccounts` (`userAccountId`)
) ENGINE=INNODB;

/*Table structure for table `reviews` */

DROP TABLE IF EXISTS `reviews`;

CREATE TABLE `reviews` (
  `reviewId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `bookId` BIGINT(20) NOT NULL,
  `comment` VARCHAR(1024) DEFAULT NULL,
  `userAccountId` BIGINT(20) NOT NULL,
  `createdOn` DATETIME DEFAULT NULL,
  `parentId` BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (`reviewId`),
  KEY `FK_userreviews_publications` (`bookId`)
) ENGINE=INNODB;

/*Table structure for table `sheets` */

DROP TABLE IF EXISTS `sheets`;

CREATE TABLE `sheets` (
  `sheetId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `ownerAccountId` BIGINT(20) NOT NULL,
  `content` TEXT,
  `name` VARCHAR(96) DEFAULT NULL,
  `createdOn` DATETIME DEFAULT NULL,
  `modifiedOn` DATETIME DEFAULT NULL,
  `permissionsMask` SMALLINT(6) NOT NULL DEFAULT '0',
  `derivedFromId` BIGINT(20) DEFAULT NULL,
  `cssText` VARCHAR(255) DEFAULT NULL,
  `createdByIP` VARCHAR(45) DEFAULT NULL,
  `modifiedByIP` VARCHAR(45) DEFAULT NULL,
  `folderName` VARCHAR(32) NOT NULL DEFAULT 'Default' COMMENT 'user sheet folder',
  `status` VARCHAR(20) DEFAULT 'ACTIVE',
  `blogId` BIGINT(20) DEFAULT NULL,
  `viewsCount` BIGINT(20) DEFAULT '0',
  `publishedOn` DATETIME DEFAULT NULL,
  PRIMARY KEY (`sheetId`),
  KEY `FK_sheets_sheets` (`derivedFromId`),
  CONSTRAINT `FK_sheets_blogs` FOREIGN KEY (`blogId`) REFERENCES `blogs` (`blogId`),
  CONSTRAINT `FK_sheets` FOREIGN KEY (`derivedFromId`) REFERENCES `publishedsheets` (`sheetId`)
) ENGINE=INNODB AUTO_INCREMENT=1;

/*Table structure for table `sheet_book` */

DROP TABLE IF EXISTS `sheet_book`;

CREATE TABLE `sheet_book` (
  `sheetId` BIGINT(20) NOT NULL,
  `bookId` BIGINT(20) NOT NULL,
  `sequenceIndex` INT(11) NOT NULL,
  PRIMARY KEY (`sheetId`,`bookId`),
  KEY `FK_sheet_book_books` (`bookId`),
  CONSTRAINT `FK_sheet_book_books` FOREIGN KEY (`bookId`) REFERENCES `books` (`bookId`),
  CONSTRAINT `FK_sheet_book_sheets` FOREIGN KEY (`sheetId`) REFERENCES `sheets` (`sheetId`)
) ENGINE=INNODB;

/*Table structure for table `sheet_genre` */

DROP TABLE IF EXISTS `sheet_genre`;

CREATE TABLE `sheet_genre` (
  `sheetId` BIGINT(20) NOT NULL,
  `genreId` BIGINT(20) NOT NULL,
  PRIMARY KEY (`sheetId`,`genreId`),
  KEY `FK_sheet_genre_genres` (`genreId`)
) ENGINE=INNODB COMMENT='deffered - no obvious reason why to make many to many';

/*Table structure for table `userdownloadscount` */

DROP TABLE IF EXISTS `userdownloadscount`;

CREATE TABLE `userdownloadscount` (
  `downloadId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `userAccountId` BIGINT(20) DEFAULT NULL,
  `bookId` BIGINT(20) NOT NULL,
  `downloadsCount` INT(11) DEFAULT NULL,
  `ipAddress` VARCHAR(45) DEFAULT NULL,
  PRIMARY KEY (`downloadId`),
  UNIQUE KEY `uniquekey` (`userAccountId`,`bookId`),
  KEY `FK_userdownloadcounts_publishedbooks` (`bookId`),
  CONSTRAINT `FK_userdownloadcounts_publishedbooks` FOREIGN KEY (`bookId`) REFERENCES `publishedbooks` (`bookId`),
  CONSTRAINT `FK_userdownloadscount_useraccounts` FOREIGN KEY (`userAccountId`) REFERENCES `useraccounts` (`userAccountId`)
) ENGINE=INNODB AUTO_INCREMENT=4;

/*Table structure for table `attachmentratings` */

DROP TABLE IF EXISTS `audiofileratings`;

CREATE TABLE `audiofileratings` (
  `userAccountId` BIGINT(20) NOT NULL,
  `audioFileId` BIGINT(20) NOT NULL,
  `rate` DOUBLE NOT NULL,
  `createdOn` DATETIME NOT NULL,
  PRIMARY KEY (`userAccountId`,`audioFileId`),
  KEY `FK_audiofileratings_audiofiles` (`audioFileId`),
  CONSTRAINT `FK_audiofileratings_useraccounts` FOREIGN KEY (`userAccountId`) REFERENCES `useraccounts` (`userAccountId`),
  CONSTRAINT `FK_audiofileratings_audiofiles` FOREIGN KEY (`audioFileId`) REFERENCES `audiofiles` (`audioFileId`)
) ENGINE=INNODB;


DROP TABLE IF EXISTS `contacts`;

CREATE TABLE `contacts` (
  `contactId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(200) DEFAULT NULL,
  `fullName` VARCHAR(100) DEFAULT NULL,
  `lastSent` DATETIME DEFAULT NULL,
  `sentCount` INT(11) NOT NULL DEFAULT '0',
  `category` VARCHAR(50) DEFAULT 'WRITER',
  `comments` VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (`contactId`),
  UNIQUE KEY `emailindex` (`email`)
) ENGINE=INNODB AUTO_INCREMENT=1;

CREATE TABLE `userwallrecords` (
  `recordId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `userAccountId` BIGINT(20) NOT NULL,
  `senderAccountId` BIGINT(20) NOT NULL,
  `message` VARCHAR(512) NOT NULL,
  `createdOn` DATETIME NOT NULL,
  PRIMARY KEY (`recordId`),
  KEY `FK_userwall_useraccounts` (`userAccountId`),
  KEY `FK_userwall_senderAccountId` (`senderAccountId`),
  CONSTRAINT `FK_userwall_senderAccountId` FOREIGN KEY (`senderAccountId`) REFERENCES `useraccounts` (`userAccountId`),
  CONSTRAINT `FK_userwall_useraccounts` FOREIGN KEY (`userAccountId`) REFERENCES `useraccounts` (`userAccountId`)
) ENGINE=INNODB AUTO_INCREMENT=1;

CREATE TABLE `blogs` (
  `blogId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) COLLATE utf8_unicode_ci NOT NULL,
  `createdOn` DATETIME NOT NULL,
  `lastUpdated` DATETIME DEFAULT NULL,
  `userAccountId` BIGINT(20) NOT NULL,
  `mastheadImageFileId` BIGINT(20) DEFAULT NULL,
  `title` VARCHAR(250) DEFAULT NULL,
  `subtitle` VARCHAR(250) DEFAULT NULL,
  `description` TEXT DEFAULT NULL,
  `footer` TEXT DEFAULT NULL,
  PRIMARY KEY (`blogId`),
  KEY `FK_blogs_useraccounts` (`userAccountId`),
  CONSTRAINT `FK_blogs_useraccounts` FOREIGN KEY (`userAccountId`) REFERENCES `useraccounts` (`userAccountId`)
) ENGINE=INNODB AUTO_INCREMENT=2;


CREATE TABLE `articlecomments` (
  `commentId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `sheetId` BIGINT(20) NOT NULL,
  `comment` VARCHAR(4000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `userAccountId` BIGINT(20) DEFAULT NULL,
  `email` VARCHAR(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fullName` VARCHAR(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `parentId` BIGINT(20) DEFAULT NULL,
  `thumbsUp` INT(11) DEFAULT '0',
  `createdOn` DATETIME NOT NULL,
  PRIMARY KEY (`commentId`),
  KEY `FK_articlecomments_articlecomments` (`parentId`),
  KEY `FK_articlecomments_sheets` (`sheetId`),
  KEY `FK_articlecomments_useraccounts` (`userAccountId`),
  CONSTRAINT `FK_articlecomments_articlecomments` FOREIGN KEY (`parentId`) REFERENCES `articlecomments` (`commentId`),
  CONSTRAINT `FK_articlecomments_sheets` FOREIGN KEY (`sheetId`) REFERENCES `sheets` (`sheetId`),
  CONSTRAINT `FK_articlecomments_useraccounts` FOREIGN KEY (`userAccountId`) REFERENCES `useraccounts` (`userAccountId`)
) ENGINE=INNODB AUTO_INCREMENT=1;



/* Procedure structure for procedure `publicrecord.selectTopByPhrase` */

DROP PROCEDURE IF EXISTS  `publicrecord.selectTopByPhrase`;

DELIMITER $$

CREATE PROCEDURE `publicrecord.selectTopByPhrase`(phrase VARCHAR(40), maxResults INT)
BEGIN
		(SELECT 'BOOK' AS `type`, p.bookId AS id, p.title AS `name` FROM publishedbooks AS p WHERE p.title LIKE phrase ORDER BY p.title)
		UNION
		(SELECT 'SHEET' AS `type`, s.sheetId AS id, s.name FROM sheets AS s WHERE s.name LIKE phrase AND s.permissionsMask >= 287 ORDER BY s.name)
		UNION
		(SELECT 'GENRE' AS `type`, g.genreId AS id, g.name AS id FROM genres AS g WHERE g.name LIKE phrase ORDER BY g.name)
		UNION
		(SELECT 'USER' AS `type`, u.username AS `id`, CONCAT(u.firstName, ' ', u.lastName) AS `name` FROM useraccounts AS u WHERE u.firstName LIKE phrase OR u.lastName LIKE phrase ORDER BY u.firstName, u.lastName)
		LIMIT 0,maxResults;
END $$
DELIMITER ;


/* Procedure structure for procedure `publishedbookdetails.countActive` */

DROP PROCEDURE IF EXISTS  `publishedbookdetails.countActive`;

DELIMITER $$

CREATE PROCEDURE `publishedbookdetails.countActive`()
BEGIN
SELECT DISTINCT COUNT(p.bookId) 
FROM publishedbooks AS p 
INNER JOIN useraccounts AS u ON p.publisherAccountId = u.userAccountId 
WHERE p.status = 'ACTIVE';
END $$
DELIMITER ;

/* Procedure structure for procedure `publishedbookdetails.countByGenre` */

DROP PROCEDURE IF EXISTS  `publishedbookdetails.countByGenre`;

DELIMITER $$

CREATE PROCEDURE `publishedbookdetails.countByGenre`(genreId BIGINT)
BEGIN
SELECT DISTINCT COUNT(p.bookId) 
FROM publishedbooks AS p INNER JOIN useraccounts AS u ON p.publisherAccountId = u.userAccountId 
INNER JOIN publishedbooks_genres AS g ON p.bookId = g.bookId AND g.genreId = genreId 
WHERE p.status = 'ACTIVE' AND g.sequenceIndex = 0;
END $$
DELIMITER ;

/* Procedure structure for procedure `publishedbookdetails.countByPublisher` */

DROP PROCEDURE IF EXISTS  `publishedbookdetails.countByPublisher`;

DELIMITER $$

CREATE PROCEDURE `publishedbookdetails.countByPublisher`(publisher VARCHAR(50))
BEGIN
SELECT DISTINCT COUNT(p.bookId) 
FROM publishedbooks AS p 
INNER JOIN useraccounts AS u ON p.publisherAccountId = u.userAccountId 
WHERE p.status = 'ACTIVE' AND u.userAccountId = publisher;
END $$
DELIMITER ;

/* Procedure structure for procedure `publishedbookdetails.countLikeBookTitle` */

DROP PROCEDURE IF EXISTS  `publishedbookdetails.countLikeBookTitle` ;

DELIMITER $$

CREATE PROCEDURE `publishedbookdetails.countLikeBookTitle`(phrase VARCHAR(50))
BEGIN
SELECT DISTINCT COUNT(p.bookId) 
FROM publishedbooks AS p 
INNER JOIN useraccounts AS u ON p.publisherAccountId = u.userAccountId 
WHERE p.status = 'ACTIVE' AND p.title LIKE CONCAT('%',phrase,'%'); 
END $$
DELIMITER ;


/* Procedure structure for procedure `publishedbooks.incrementParentVersion` */

DROP PROCEDURE IF EXISTS  `publishedbooks.incrementParentVersion`;

DELIMITER $$

CREATE PROCEDURE `publishedbooks.incrementParentVersion`(bookId BIGINT)
BEGIN
		UPDATE publishedbooks AS pb 
		SET pb.subversionCount = (pb.subversionCount + 1) 
		WHERE b.derivedFromId = bookId;
		
		SELECT pb.version, pb.subversionCount 
		FROM publishedbooks AS pb 
		WHERE pb.derivedFromId = bookId;
    END $$
DELIMITER ;

/* Procedure structure for procedure `publishedbooks.selectTitleByPhrase` */

DROP PROCEDURE IF EXISTS  `publishedbooks.selectTitleByPhrase`;

DELIMITER $$

CREATE PROCEDURE `publishedbooks.selectTitleByPhrase`(phrase VARCHAR(50), `offset` BIGINT, maxResults INT)
BEGIN
	SELECT p.bookId, p.title, p.coverImageFileId
	FROM publishedbooks AS p 
	WHERE p.title LIKE phrase
	ORDER BY p.title 
	LIMIT `offset`,maxResults;
    END $$
DELIMITER ;

/* Procedure structure for procedure `Publisher.selectByBookId` */

DROP PROCEDURE IF EXISTS  `Publisher.selectByBookId`;

DELIMITER $$

CREATE PROCEDURE `Publisher.selectByBookId`(bookId BIGINT)
BEGIN
	SELECT u.userName, u.firstName, u.lastName, pb.publisherRoleMask AS roleMask FROM publishedbooks AS pb
	INNER JOIN useraccounts AS u ON pb.publisherUserName = u.userName
	WHERE pb.bookId = bookId;
    END $$
DELIMITER ;

/* Procedure structure for procedure `reviewrate.selectByBookIdWithRate` */

DROP PROCEDURE IF EXISTS  `reviewrate.selectByBookIdWithRate`;

DELIMITER $$

CREATE PROCEDURE `reviewrate.selectByBookIdWithRate`(`bookId` BIGINT,`offset` BIGINT, maxResults INT)
BEGIN
	SELECT r.reviewId,r.`comment`, r.userAccountId, r.createdOn, r.parentId, u.firstName, u.lastName, ra.rate
	FROM reviews AS r
	INNER JOIN useraccounts AS u ON r.userAccountId = u.userAccountId
	LEFT JOIN ratings AS ra ON (ra.bookId = r.bookId AND ra.userAccountId = r.userAccountId)
	WHERE r.bookId = bookId
	ORDER BY r.createdOn DESC
	LIMIT `offset`,maxResults;
    END $$
DELIMITER ;

/* Procedure structure for procedure `reviewrecord.selectByBookId` */

DROP PROCEDURE IF EXISTS  `reviewrecord.selectByBookId`;

DELIMITER $$

CREATE PROCEDURE `reviewrecord.selectByBookId`(`bookId` BIGINT,`offset` BIGINT, maxResults INT)
BEGIN
	SELECT r.reviewId,r.`comment`, r.userName, r.createdOn, r.parentId, u.firstName, u.lastName
	FROM reviews AS r
	INNER JOIN useraccounts AS u ON r.userName = u.userName
	WHERE r.bookId = bookId
	ORDER BY r.createdOn DESC
	LIMIT `offset`,maxResults;
    END $$
DELIMITER ;

/* Procedure structure for procedure `sheetcontent.selectById` */

DROP PROCEDURE IF EXISTS  `sheetcontent.selectById`;

DELIMITER $$

CREATE PROCEDURE `sheetcontent.selectById`(sheetId BIGINT)
BEGIN
		SELECT s.name, s.cssText, s.content, s.sheetId FROM sheets AS s
		WHERE s.sheetId = sheetId;
    END $$
DELIMITER ;

/* Procedure structure for procedure `SheetContentRecord.selectBySheetId` */

DROP PROCEDURE IF EXISTS  `SheetContentRecord.selectBySheetId`;

DELIMITER $$

CREATE PROCEDURE `SheetContentRecord.selectBySheetId`(sheetId BIGINT)
BEGIN
	SELECT s.sheetId, s.name, s.modifiedOn, s.owner, s.createdOn, s.content, s.genreId, g.name AS genreName
	FROM sheets AS s 
	LEFT JOIN genres AS g ON s.genreId = g.genreId
	WHERE s.sheetId = sheetId;
    END $$
DELIMITER ;

/* Procedure structure for procedure `useraccounts.countByEmail` */

DROP PROCEDURE IF EXISTS  `useraccounts.countByEmail`;

DELIMITER $$

CREATE PROCEDURE `useraccounts.countByEmail`(userName VARCHAR(320))
BEGIN
	SELECT COUNT(u.userName) FROM useraccounts AS u WHERE u.userName = userName;
    END $$
DELIMITER ;

/* Procedure structure for procedure `useritems.scrollByOwner` */

DROP PROCEDURE IF EXISTS  `useritems.scrollByOwner`;

DELIMITER $$

CREATE PROCEDURE `useritems.scrollByOwner`(userAccountId BIGINT, `offset` BIGINT, maxResults INT)
BEGIN
		(SELECT b.title AS `name`, b.bookId AS id, b.createdOn, b.modifiedOn, "BOOK" AS `type` FROM books AS b WHERE b.ownerAccountId = userAccountId)
		UNION
		(SELECT s.name, s.sheetId AS id, s.createdOn, s.modifiedOn, IF(s.blogId IS NULL,"SHEET","ARTICLE") AS `type` FROM sheets AS s WHERE s.ownerAccountId = userAccountId AND s.status = 'ACTIVE')
		ORDER BY modifiedOn DESC,createdOn DESC
		 LIMIT `offset`,maxResults;
    END$$

DELIMITER ;

/* Procedure structure for procedure `useritems.scrollGenresLikeName` */

DROP PROCEDURE IF EXISTS  `useritems.scrollGenresLikeName` ;

DELIMITER $$

CREATE PROCEDURE `useritems.scrollGenresLikeName`(phrase VARCHAR(50),`offset` BIGINT, maxResults INT)
BEGIN
		SELECT g.name, g.genreId AS id, NULL AS createdOn, NULL AS modifiedOn, "GENRE" AS `type` 
		FROM genres AS g 
		WHERE g.name LIKE CONCAT("%",phrase,"%")
		ORDER BY modifiedOn,createdOn DESC
		LIMIT `offset`,maxResults;
END $$
DELIMITER ;

/* Procedure structure for procedure `useritems.scrollLikeItemName` */

DROP PROCEDURE IF EXISTS  `useritems.scrollLikeItemName` ;

DELIMITER $$

CREATE PROCEDURE `useritems.scrollLikeItemName`(phrase VARCHAR(50), `offset` BIGINT, maxResults INT)
BEGIN
		(SELECT 'BOOK' AS `type`, p.bookId AS id, p.title AS `name`, p.createdOn, NULL AS modifiedOn FROM publishedbooks AS p WHERE p.title LIKE CONCAT("%",phrase,"%") AND p.status = 'ACTIVE' ORDER BY p.title)
		UNION
		(SELECT 'SHEET' AS `type`, s.sheetId AS id, s.name, s.createdOn, NULL AS modifiedOn FROM sheets AS s WHERE s.name LIKE CONCAT("%",phrase,"%") AND s.status = 'ACTIVE' AND s.permissionsMask >= 216 ORDER BY s.name)
		UNION
		(SELECT 'GENRE' AS `type`, g.genreId AS id, g.name AS id, g.createdOn, NULL AS modifiedOn FROM genres AS g WHERE g.name LIKE CONCAT("%",phrase,"%") ORDER BY g.name)
		UNION
		(SELECT 'USER' AS `type`, u.userAccountId AS `id`, CONCAT(u.firstName,' ', u.lastName) AS `name`, u.createdOn, NULL AS modifiedOn FROM useraccounts AS u WHERE CONCAT(u.firstName,' ',u.lastName) LIKE CONCAT("%",phrase,"%") ORDER BY u.firstName, u.lastName)
		LIMIT `offset`,maxResults;
    END $$
DELIMITER ;


DELIMITER $$
DROP PROCEDURE IF EXISTS `bookshowcase.scrollBookShowcaseByDownloads`$$
CREATE PROCEDURE `bookshowcase.scrollBookShowcaseByDownloads`(`offset` BIGINT, maxResults INT)
BEGIN
		SELECT c.bookId, b.title, b.bookSummary, b.coverImageFileId, COUNT(c.bookId) AS downloadsCount,ipAddress
		FROM userdownloadscount AS c 
		INNER JOIN publishedbooks AS b ON b.bookId = c.bookId
		GROUP BY c.bookId
		ORDER BY downloadsCount DESC
		LIMIT `offset`,maxResults;
END $$

DELIMITER ;

DELIMITER $$

DROP PROCEDURE IF EXISTS `userdownloadscount.countByBookGroupByUserAccount`$$

CREATE PROCEDURE `userdownloadscount.countByBookGroupByUserAccount`(bookId BIGINT)
BEGIN    
	SELECT COUNT(c.downloadsCount) AS downloadsCount 
	FROM (SELECT downloadsCount
	FROM userdownloadscount 
	WHERE userdownloadscount.bookId = bookId
	AND userAccountId IS NOT NULL 
GROUP BY userAccountId ) AS c;
END$$

DELIMITER ;


DELIMITER $$
DROP PROCEDURE IF EXISTS `userdownloadscount.countByBookGroupByIP`$$

CREATE PROCEDURE `userdownloadscount.countByBookGroupByIP`(bookId BIGINT)
BEGIN
	SELECT COUNT(c.downloadsCount) AS downloadsCount 
	FROM (SELECT downloadsCount
	FROM userdownloadscount 
	WHERE userdownloadscount.bookId = bookId
	AND userAccountId IS NULL 
	GROUP BY ipAddress ) AS c;
END$$

DELIMITER ;

