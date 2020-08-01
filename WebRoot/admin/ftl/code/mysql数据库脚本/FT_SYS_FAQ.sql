
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `FT_SYS_FAQ`
-- ----------------------------
DROP TABLE IF EXISTS `FT_SYS_FAQ`;
CREATE TABLE `FT_SYS_FAQ` (
 		`SYS_FAQ_ID` varchar(100) NOT NULL,
		`GMT_CREATE` varchar(32) DEFAULT NULL COMMENT '创建时间',
		`GMT_MODIFIED` varchar(32) DEFAULT NULL COMMENT '更新时间',
		`CONTENT` varchar(4000) DEFAULT NULL COMMENT '内容',
		`TITLE` varchar(155) DEFAULT NULL COMMENT '标题',
  		PRIMARY KEY (`SYS_FAQ_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
