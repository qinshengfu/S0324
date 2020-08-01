-- ----------------------------
-- Table structure for "FT_SYS_FAQ"
-- ----------------------------
-- DROP TABLE "FT_SYS_FAQ";
CREATE TABLE "FT_SYS_FAQ" (
	"GMT_CREATE" VARCHAR2(32 BYTE) NULL ,
	"GMT_MODIFIED" VARCHAR2(32 BYTE) NULL ,
	"CONTENT" VARCHAR2(4000 BYTE) NULL ,
	"TITLE" VARCHAR2(155 BYTE) NULL ,
	"SYS_FAQ_ID" VARCHAR2(100 BYTE) NOT NULL 
)
LOGGING
NOCOMPRESS
NOCACHE
;

COMMENT ON COLUMN "FT_SYS_FAQ"."GMT_CREATE" IS '创建时间';
COMMENT ON COLUMN "FT_SYS_FAQ"."GMT_MODIFIED" IS '更新时间';
COMMENT ON COLUMN "FT_SYS_FAQ"."CONTENT" IS '内容';
COMMENT ON COLUMN "FT_SYS_FAQ"."TITLE" IS '标题';
COMMENT ON COLUMN "FT_SYS_FAQ"."SYS_FAQ_ID" IS 'ID';

-- ----------------------------
-- Indexes structure for table FT_SYS_FAQ
-- ----------------------------

-- ----------------------------
-- Checks structure for table "FT_SYS_FAQ"

-- ----------------------------

ALTER TABLE "FT_SYS_FAQ" ADD CHECK ("SYS_FAQ_ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table "FT_SYS_FAQ"
-- ----------------------------
ALTER TABLE "FT_SYS_FAQ" ADD PRIMARY KEY ("SYS_FAQ_ID");
