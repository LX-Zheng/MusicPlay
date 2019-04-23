/*
 Navicat Premium Data Transfer

 Source Server         : aliyun
 Source Server Type    : MySQL
 Source Server Version : 100119
 Source Host           : 120.77.203.242:3306
 Source Schema         : android

 Target Server Type    : MySQL
 Target Server Version : 100119
 File Encoding         : 65001

 Date: 23/04/2019 08:42:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for library
-- ----------------------------
DROP TABLE IF EXISTS `library`;
CREATE TABLE `library` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `library` varchar(255) DEFAULT NULL,
  `floor` varchar(255) DEFAULT NULL,
  `detail` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of library
-- ----------------------------
BEGIN;
INSERT INTO `library` VALUES (1, '常熟理工学院', '第二层', '外国语');
INSERT INTO `library` VALUES (2, '常熟理工学院', '第三层', '马克思');
INSERT INTO `library` VALUES (3, '常熟理工学院', '第四层', '电气');
INSERT INTO `library` VALUES (4, '常熟理工学院', '第五层', '计算机');
COMMIT;

-- ----------------------------
-- Table structure for relationship
-- ----------------------------
DROP TABLE IF EXISTS `relationship`;
CREATE TABLE `relationship` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `user` int(255) DEFAULT NULL,
  `friend` int(255) DEFAULT NULL,
  `memoname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of relationship
-- ----------------------------
BEGIN;
INSERT INTO `relationship` VALUES (1, 2, 1, '谭');
INSERT INTO `relationship` VALUES (2, 2, 3, '无');
INSERT INTO `relationship` VALUES (3, 2, 4, '经');
INSERT INTO `relationship` VALUES (4, 2, 5, '试');
INSERT INTO `relationship` VALUES (5, 2, 6, '与');
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `account` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `sex` int(255) DEFAULT NULL,
  `state` int(255) DEFAULT NULL,
  `library` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES ('w', 'w', '谭小萌', 1, 0, 1, '常熟理工学院');
INSERT INTO `user` VALUES ('q', 'q', 'cyhfvg', 2, 0, 1, '常熟理工学院');
INSERT INTO `user` VALUES ('e', 'e', 'kkttp', 3, 0, 1, '常熟理工学院');
INSERT INTO `user` VALUES ('y', 'y', 'weqw', 4, NULL, 1, '常熟理工学院');
INSERT INTO `user` VALUES ('u', 'u', 'weqwe', 5, NULL, 1, '常熟理工学院');
INSERT INTO `user` VALUES ('i', 'i', 'oidoq', 6, NULL, 1, '常熟理工学院');
INSERT INTO `user` VALUES ('o', 'o', 'qw', 7, NULL, 1, '常熟理工学院');
INSERT INTO `user` VALUES ('p', 'p', 'qq', 8, NULL, 1, '常熟理工学院');
INSERT INTO `user` VALUES ('l', 'l', '分页', 9, NULL, NULL, '常熟理工学院');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
