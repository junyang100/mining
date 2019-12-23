/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.100.64（M）
 Source Server Type    : MySQL
 Source Server Version : 50630
 Source Host           : 192.168.100.64:3306
 Source Schema         : config-center-mine

 Target Server Type    : MySQL
 Target Server Version : 50630
 File Encoding         : 65001

 Date: 17/12/2019 11:23:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for application
-- ----------------------------
DROP TABLE IF EXISTS `application`;
CREATE TABLE `application`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `application_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `application_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `platform` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `application_id_uindex`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of application
-- ----------------------------
INSERT INTO `application` VALUES ('common', '通用服务', '通用配置', '2019-12-17 11:03:21', 'mining');
INSERT INTO `application` VALUES ('mining-api', 'demo-api', 'demo-api服务', '2019-12-17 10:41:12', 'mining');

-- ----------------------------
-- Table structure for config_change_push
-- ----------------------------
DROP TABLE IF EXISTS `config_change_push`;
CREATE TABLE `config_change_push`  (
  `id` bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `application` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '应用名称',
  `application_ip` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'ip地址',
  `application_port` int(11) NULL DEFAULT NULL COMMENT '端口号',
  `push_status` tinyint(11) NOT NULL DEFAULT 1 COMMENT '1 推送中   2 成功 3 失败',
  `push_time` timestamp(0) NULL DEFAULT NULL COMMENT '推送时间 ',
  `push_finish_time` timestamp(0) NULL DEFAULT NULL COMMENT '推送完成时间 ',
  `push_user_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `push_description` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '推送描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1300 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of config_change_push
-- ----------------------------
INSERT INTO `config_change_push` VALUES (1297, 'mining-api', '192.168.160.92', 8080, 1, '2019-12-17 10:51:58', NULL, '管理员', '');
INSERT INTO `config_change_push` VALUES (1298, 'mining-api', '192.168.160.92', 8080, 2, '2019-12-17 11:12:22', '2019-12-17 11:12:23', '管理员', '');
INSERT INTO `config_change_push` VALUES (1299, 'mining-api', '192.168.160.92', 8080, 2, '2019-12-17 11:22:09', '2019-12-17 11:22:11', '管理员', '');

-- ----------------------------
-- Table structure for platform
-- ----------------------------
DROP TABLE IF EXISTS `platform`;
CREATE TABLE `platform`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `platform_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `contact_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `contact_mobile` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `platform_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `update_time` timestamp(0) NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `platform_id_uindex`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of platform
-- ----------------------------
INSERT INTO `platform` VALUES ('mining', 'demo服务', 'junyang', '18600001111', 'demo服务', '2019-12-17 10:40:35');

-- ----------------------------
-- Table structure for properties
-- ----------------------------
DROP TABLE IF EXISTS `properties`;
CREATE TABLE `properties`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id ',
  `application` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '应用程序名称',
  `profile` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '环境',
  `profile_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '环境编码描述',
  `label` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '版本号',
  `key` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '建值对的key',
  `value` varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '键值对值',
  `description` varchar(300) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '属性描述',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `config_group` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '所属分组',
  `label_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '版本号描述',
  `platform` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '所属平台',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_idx_app`(`application`, `profile`, `label`, `key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1396 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of properties
-- ----------------------------
INSERT INTO `properties` VALUES (1389, 'mining-api', 'dev', '', '1.0.0', 'com.mining.demo', 'test555', '', '2019-12-17 11:22:07', NULL, '应用配置', '', NULL);
INSERT INTO `properties` VALUES (1390, 'mining-api', 'dev', '', '1.0.0', 'config.refersh.zk.connect-string', '172.17.33.39:2181', '', '2019-12-17 11:03:55', NULL, '系统配置', '', NULL);
INSERT INTO `properties` VALUES (1391, 'mining-api', 'dev', '', '1.0.0', 'config.refersh.zk.connection-timeout-ms', '5000', '', '2019-12-17 11:04:22', NULL, '系统配置', '', NULL);
INSERT INTO `properties` VALUES (1392, 'mining-api', 'dev', '', '1.0.0', 'config.refersh.zk.enable', 'true', '', '2019-12-17 11:04:52', NULL, '系统配置', '', NULL);
INSERT INTO `properties` VALUES (1393, 'mining-api', 'dev', '', '1.0.0', 'config.refersh.zk.max-retry-count', '3', '', '2019-12-17 11:05:13', NULL, '系统配置', '', NULL);
INSERT INTO `properties` VALUES (1394, 'mining-api', 'dev', '', '1.0.0', 'config.refersh.zk.namespace', 'config-refresh-node', '', '2019-12-17 11:05:39', NULL, '系统配置', '', NULL);
INSERT INTO `properties` VALUES (1395, 'mining-api', 'dev', '', '1.0.0', 'config.refersh.zk.session-timeout-ms', '30000', '', '2019-12-17 11:06:08', NULL, '系统配置', '', NULL);

-- ----------------------------
-- Table structure for tb_menus
-- ----------------------------
DROP TABLE IF EXISTS `tb_menus`;
CREATE TABLE `tb_menus`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `menu_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '菜单名称',
  `menu_parent` int(11) NOT NULL COMMENT '父菜单 -1 表示顶级菜单,只支持两级菜单',
  `menu_action` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '菜单触发的调用',
  `menu_order` int(11) NULL DEFAULT NULL COMMENT '菜单的顺序',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `menu_displayname` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '菜单显示名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tb_menus
-- ----------------------------
INSERT INTO `tb_menus` VALUES (1, '配置管理', 0, '-1', 1, '2018-08-29 17:56:52', NULL, '配置管理');
INSERT INTO `tb_menus` VALUES (3, '平台管理', 9, '/html/platform.html', 3, '2018-08-30 17:34:32', '2018-09-12 06:04:49', '平台管理');
INSERT INTO `tb_menus` VALUES (5, '服务管理', 9, '/html/application.html', 2, '2018-08-30 17:38:14', '2018-09-12 05:58:59', '服务管理');
INSERT INTO `tb_menus` VALUES (7, '配置管理', 1, '/html/config.html', 1, '2018-08-30 17:40:57', NULL, '配置管理');
INSERT INTO `tb_menus` VALUES (9, '系统管理', 0, '-1', 2, '2018-08-30 17:41:58', NULL, '系统管理');
INSERT INTO `tb_menus` VALUES (11, '用户管理', 9, '/html/user.html', 1, '2018-08-30 17:43:12', NULL, '用户管理');
INSERT INTO `tb_menus` VALUES (15, '菜单管理', 9, '/html/menu.html', 3, '2018-08-30 17:46:57', NULL, '菜单管理');

-- ----------------------------
-- Table structure for tb_users
-- ----------------------------
DROP TABLE IF EXISTS `tb_users`;
CREATE TABLE `tb_users`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '用户名',
  `password_sha1` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '两次sha1密码',
  `status` smallint(11) NOT NULL DEFAULT 1 COMMENT '用户状态 0 停用 1 启用',
  `nick_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '昵称',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uidx_user_name`(`user_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tb_users
-- ----------------------------
INSERT INTO `tb_users` VALUES (1, 'admin', '7b2e9f54cdff413fcde01f330af6896c3cd7e6cd', 1, '管理员', '2019-07-02 09:54:45', '2019-07-02 09:54:56');

-- ----------------------------
-- Table structure for tr_users_menu
-- ----------------------------
DROP TABLE IF EXISTS `tr_users_menu`;
CREATE TABLE `tr_users_menu`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `menu_id` int(11) NOT NULL COMMENT '权限id',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 146 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tr_users_menu
-- ----------------------------
INSERT INTO `tr_users_menu` VALUES (133, 1, 7, '2018-09-11 17:45:05');
INSERT INTO `tr_users_menu` VALUES (135, 1, 1, '2018-09-11 17:45:05');
INSERT INTO `tr_users_menu` VALUES (137, 1, 5, '2018-09-11 17:45:05');
INSERT INTO `tr_users_menu` VALUES (139, 1, 3, '2018-09-11 17:45:05');
INSERT INTO `tr_users_menu` VALUES (141, 1, 11, '2018-09-11 17:45:05');
INSERT INTO `tr_users_menu` VALUES (143, 1, 9, '2018-09-11 17:45:05');
INSERT INTO `tr_users_menu` VALUES (145, 1, 15, '2018-09-11 17:45:05');

-- ----------------------------
-- Table structure for tr_users_privillege
-- ----------------------------
DROP TABLE IF EXISTS `tr_users_privillege`;
CREATE TABLE `tr_users_privillege`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `privilege_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '权限id',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1441 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tr_users_privillege
-- ----------------------------
INSERT INTO `tr_users_privillege` VALUES (1439, 1, 'mining-api', '2019-12-17 10:41:35');
INSERT INTO `tr_users_privillege` VALUES (1440, 1, 'mining', '2019-12-17 10:41:35');

-- ----------------------------
-- Table structure for vehicle_sub_info
-- ----------------------------
DROP TABLE IF EXISTS `vehicle_sub_info`;
CREATE TABLE `vehicle_sub_info`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `userId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `vimsId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `simNo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `vehicle_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `is_del` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `vno` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
