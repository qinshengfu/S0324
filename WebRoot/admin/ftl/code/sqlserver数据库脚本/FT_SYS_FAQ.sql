GO
/******  ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE FT_SYS_FAQ (
 		SYS_FAQ_ID  nvarchar(100) NOT NULL,
		GMT_CREATE nvarchar(32) DEFAULT NULL,
		GMT_MODIFIED nvarchar(32) DEFAULT NULL,
		CONTENT nvarchar(4000) DEFAULT NULL,
		TITLE nvarchar(155) DEFAULT NULL,
PRIMARY KEY CLUSTERED 
(
	[SYS_FAQ_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
