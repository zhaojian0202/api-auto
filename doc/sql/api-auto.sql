
DROP TABLE IF EXISTS `test_cases`;
CREATE TABLE `test_cases` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `case_name` varchar(255) NOT NULL COMMENT '用例名称',
  `case_description` varchar(255) DEFAULT NULL COMMENT '用例描述',
  `case_property` varchar(32) DEFAULT NULL COMMENT '用例等级',
  `case_param` longtext COMMENT '用例参数',
  `case_type` int(2) DEFAULT NULL COMMENT '用例类型',
  `case_run` int(255) DEFAULT NULL COMMENT '是否运行',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;