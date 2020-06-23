/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.26-log : Database - changgou_order
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`changgou_order` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `changgou_order`;

/*Table structure for table `tb_category_report` */

DROP TABLE IF EXISTS `tb_category_report`;

CREATE TABLE `tb_category_report` (
  `category_id1` int(11) NOT NULL COMMENT '1级分类',
  `category_id2` int(11) NOT NULL COMMENT '2级分类',
  `category_id3` int(11) NOT NULL COMMENT '3级分类',
  `count_date` date NOT NULL COMMENT '统计日期',
  `num` int(11) DEFAULT NULL COMMENT '销售数量',
  `money` int(11) DEFAULT NULL COMMENT '销售额',
  PRIMARY KEY (`category_id1`,`category_id2`,`category_id3`,`count_date`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=FIXED;

/*Data for the table `tb_category_report` */

insert  into `tb_category_report`(`category_id1`,`category_id2`,`category_id3`,`count_date`,`num`,`money`) values (1,4,5,'2019-01-26',1,300),(74,7,8,'2019-01-26',5,900);

/*Table structure for table `tb_order` */

DROP TABLE IF EXISTS `tb_order`;

CREATE TABLE `tb_order` (
  `id` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '订单id',
  `total_num` int(11) DEFAULT NULL COMMENT '数量合计',
  `total_money` int(11) DEFAULT NULL COMMENT '金额合计',
  `pre_money` int(11) DEFAULT NULL COMMENT '优惠金额',
  `post_fee` int(11) DEFAULT NULL COMMENT '邮费',
  `pay_money` int(11) DEFAULT NULL COMMENT '实付金额',
  `pay_type` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT '支付类型，1、在线支付、0 货到付款',
  `create_time` datetime DEFAULT NULL COMMENT '订单创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '订单更新时间',
  `pay_time` datetime DEFAULT NULL COMMENT '付款时间',
  `consign_time` datetime DEFAULT NULL COMMENT '发货时间',
  `end_time` datetime DEFAULT NULL COMMENT '交易完成时间',
  `close_time` datetime DEFAULT NULL COMMENT '交易关闭时间',
  `shipping_name` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '物流名称',
  `shipping_code` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '物流单号',
  `username` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '用户名称',
  `buyer_message` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '买家留言',
  `buyer_rate` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否评价',
  `receiver_contact` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人',
  `receiver_mobile` varchar(12) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人手机',
  `receiver_address` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人地址',
  `source_type` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '订单来源：1:web，2：app，3：微信公众号，4：微信小程序  5 H5手机页面',
  `transaction_id` varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '交易流水号',
  `order_status` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '订单状态 ',
  `pay_status` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '支付状态 0:未支付 1:已支付',
  `consign_status` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '发货状态 0:未发货 1:已发货 2:已送达',
  `is_delete` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `create_time` (`create_time`) USING BTREE,
  KEY `status` (`order_status`) USING BTREE,
  KEY `payment_type` (`pay_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `tb_order` */

insert  into `tb_order`(`id`,`total_num`,`total_money`,`pre_money`,`post_fee`,`pay_money`,`pay_type`,`create_time`,`update_time`,`pay_time`,`consign_time`,`end_time`,`close_time`,`shipping_name`,`shipping_code`,`username`,`buyer_message`,`buyer_rate`,`receiver_contact`,`receiver_mobile`,`receiver_address`,`source_type`,`transaction_id`,`order_status`,`pay_status`,`consign_status`,`is_delete`) values ('1274167960998645760',1,500,NULL,NULL,500,'1','2020-06-20 02:31:29','2020-06-01 22:07:46',NULL,NULL,NULL,NULL,NULL,NULL,'heima',NULL,'0','小明','15855431218',NULL,'1','0','4','1','0',NULL),('1274227059392122880',1,500,NULL,NULL,500,'0','2020-06-20 06:26:20','2020-06-03 02:29:42',NULL,NULL,NULL,NULL,NULL,NULL,'heima',NULL,'0','小明名','15855431244',NULL,'2','','1','1','1',NULL),('1274227618123747328',1,500,NULL,NULL,500,'0','2020-06-20 06:28:33','2020-06-20 06:28:33',NULL,NULL,NULL,NULL,NULL,NULL,'heima',NULL,'0','王HV','15855431216',NULL,'3',NULL,'4','2','0',NULL),('1274246387973885952',1,500,NULL,NULL,500,'1','2020-06-20 07:43:08','2020-06-12 07:43:08',NULL,NULL,NULL,NULL,NULL,NULL,'heima',NULL,'0','劉姐','15855431213',NULL,'1',NULL,'3','2','0',NULL),('1274703536806039552',1,500,NULL,NULL,500,'0','2020-06-21 21:59:41','2020-06-23 19:37:28',NULL,NULL,'2020-06-23 19:37:28',NULL,'百世快递','557012265449738','heima',NULL,'0','小潘','15855431213',NULL,'2',NULL,'2','3','1',NULL),('1274703749507584000',3,1500,NULL,NULL,1500,'1','2020-06-21 22:00:31','2020-06-20 19:37:31',NULL,NULL,'2020-06-23 19:37:31',NULL,'百世快递','557012265449738','heima',NULL,'0','浩翔','15855431213',NULL,'4',NULL,'2','4','1',NULL),('1275254557466824704',1,500,NULL,NULL,500,'1','2020-06-23 10:29:14','2020-06-12 07:43:08',NULL,NULL,'2020-06-23 21:29:52',NULL,NULL,NULL,'heima',NULL,'0','靜靜','15855431217',NULL,'2',NULL,'3','5','1',NULL),('1275435036631502848',1,500,NULL,NULL,500,'1','2020-06-23 22:26:24','2020-06-22 22:26:24',NULL,NULL,NULL,NULL,NULL,NULL,'heima',NULL,'0','阿姐','15855431342',NULL,'1',NULL,'0','5','0',NULL),('1275435099600588800',1,500,NULL,NULL,500,'1','2020-06-23 22:26:39','2020-06-23 22:26:39',NULL,NULL,NULL,NULL,NULL,NULL,'heima',NULL,'0',NULL,'12223232333',NULL,'1',NULL,'4','5','0',NULL),('1275435151823867904',1,500,NULL,NULL,500,'1','2020-06-23 22:26:51','2020-06-23 22:26:51',NULL,NULL,NULL,NULL,NULL,NULL,'heima',NULL,'0',NULL,'15855431214',NULL,'1',NULL,'5','1','0',NULL),('1275435187123130368',1,500,NULL,NULL,500,'1','2020-06-23 22:27:00','2020-06-23 22:27:00',NULL,NULL,NULL,NULL,NULL,NULL,'heima',NULL,'0',NULL,NULL,NULL,'1',NULL,'4','1','0',NULL),('1275435253737066496',1,500,NULL,NULL,500,'1','2020-06-23 22:27:16','2020-06-23 22:27:16',NULL,NULL,NULL,NULL,NULL,NULL,'heima',NULL,'0',NULL,NULL,NULL,'1',NULL,'5','1','0',NULL);

/*Table structure for table `tb_order_config` */

DROP TABLE IF EXISTS `tb_order_config`;

CREATE TABLE `tb_order_config` (
  `id` int(11) NOT NULL COMMENT 'ID',
  `order_timeout` int(11) DEFAULT NULL COMMENT '正常订单超时时间（分）',
  `seckill_timeout` int(11) DEFAULT NULL COMMENT '秒杀订单超时时间（分）',
  `take_timeout` int(11) DEFAULT NULL COMMENT '自动收货（天）',
  `service_timeout` int(11) DEFAULT NULL COMMENT '售后期限',
  `comment_timeout` int(11) DEFAULT NULL COMMENT '自动五星好评',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `tb_order_config` */

insert  into `tb_order_config`(`id`,`order_timeout`,`seckill_timeout`,`take_timeout`,`service_timeout`,`comment_timeout`) values (1,60,10,15,7,7);

/*Table structure for table `tb_order_item` */

DROP TABLE IF EXISTS `tb_order_item`;

CREATE TABLE `tb_order_item` (
  `id` varchar(200) COLLATE utf8_bin NOT NULL COMMENT 'ID',
  `category_id1` int(11) DEFAULT NULL COMMENT '1级分类',
  `category_id2` int(11) DEFAULT NULL COMMENT '2级分类',
  `category_id3` int(11) DEFAULT NULL COMMENT '3级分类',
  `spu_id` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT 'SPU_ID',
  `sku_id` varchar(200) COLLATE utf8_bin NOT NULL COMMENT 'SKU_ID',
  `order_id` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '订单ID',
  `name` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '商品名称',
  `price` int(20) DEFAULT NULL COMMENT '单价',
  `num` int(10) DEFAULT NULL COMMENT '数量',
  `money` int(20) DEFAULT NULL COMMENT '总金额',
  `pay_money` int(11) DEFAULT NULL COMMENT '实付金额',
  `image` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '图片地址',
  `weight` int(11) DEFAULT NULL COMMENT '重量',
  `post_fee` int(11) DEFAULT NULL COMMENT '运费',
  `is_return` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否退货',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `item_id` (`sku_id`) USING BTREE,
  KEY `order_id` (`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `tb_order_item` */

insert  into `tb_order_item`(`id`,`category_id1`,`category_id2`,`category_id3`,`spu_id`,`sku_id`,`order_id`,`name`,`price`,`num`,`money`,`pay_money`,`image`,`weight`,`post_fee`,`is_return`) values ('1271803266447052800',104,105,106,NULL,'100000003145','1271803264781914112','vivo X23 8GB+128GB 幻夜蓝 水滴屏全面屏 游戏手机 移动联通电信全网通4G手机',10000,10,100000,100000,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/4612/28/6223/298257/5ba22d66Ef665222f/d97ed0b25cbe8c6e.jpg!q70.jpg.webp',10,NULL,NULL),('1271804042795945984',104,105,106,NULL,'100000003145','1271804042665922560','vivo X23 8GB+128GB 幻夜蓝 水滴屏全面屏 游戏手机 移动联通电信全网通4G手机',10000,10,100000,100000,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/4612/28/6223/298257/5ba22d66Ef665222f/d97ed0b25cbe8c6e.jpg!q70.jpg.webp',10,NULL,NULL),('1272128307722326016',104,105,106,NULL,'100000003145','1272128305981689856','vivo X23 8GB+128GB 幻夜蓝 水滴屏全面屏 游戏手机 移动联通电信全网通4G手机',10000,10,100000,100000,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/4612/28/6223/298257/5ba22d66Ef665222f/d97ed0b25cbe8c6e.jpg!q70.jpg.webp',10,NULL,NULL),('1272129452683431936',104,105,106,NULL,'100000003145','1272129451060236288','vivo X23 8GB+128GB 幻夜蓝 水滴屏全面屏 游戏手机 移动联通电信全网通4G手机',10000,10,100000,100000,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/4612/28/6223/298257/5ba22d66Ef665222f/d97ed0b25cbe8c6e.jpg!q70.jpg.webp',10,NULL,NULL),('1272129794141720576',104,105,106,NULL,'100000003145','1272129794103971840','vivo X23 8GB+128GB 幻夜蓝 水滴屏全面屏 游戏手机 移动联通电信全网通4G手机',10000,10,100000,100000,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/4612/28/6223/298257/5ba22d66Ef665222f/d97ed0b25cbe8c6e.jpg!q70.jpg.webp',10,NULL,NULL),('1272130399270735872',104,105,106,NULL,'100000003145','1272130397639151616','vivo X23 8GB+128GB 幻夜蓝 水滴屏全面屏 游戏手机 移动联通电信全网通4G手机',10000,10,100000,100000,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/4612/28/6223/298257/5ba22d66Ef665222f/d97ed0b25cbe8c6e.jpg!q70.jpg.webp',10,NULL,NULL),('1272130571082010624',104,105,106,NULL,'100000003145','1272130571044261888','vivo X23 8GB+128GB 幻夜蓝 水滴屏全面屏 游戏手机 移动联通电信全网通4G手机',10000,10,100000,100000,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/4612/28/6223/298257/5ba22d66Ef665222f/d97ed0b25cbe8c6e.jpg!q70.jpg.webp',10,NULL,NULL),('1272131182221463552',104,105,106,NULL,'100000003145','1272131182183714816','vivo X23 8GB+128GB 幻夜蓝 水滴屏全面屏 游戏手机 移动联通电信全网通4G手机',10000,10,100000,100000,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/4612/28/6223/298257/5ba22d66Ef665222f/d97ed0b25cbe8c6e.jpg!q70.jpg.webp',10,NULL,NULL),('1272149754423414784',104,105,106,NULL,'100000003145','1272149754150785024','vivo X23 8GB+128GB 幻夜蓝 水滴屏全面屏 游戏手机 移动联通电信全网通4G手机',10000,10,100000,100000,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/4612/28/6223/298257/5ba22d66Ef665222f/d97ed0b25cbe8c6e.jpg!q70.jpg.webp',10,NULL,NULL),('1272161265023848448',104,105,106,NULL,'100000003145','1272161263413235712','vivo X23 8GB+128GB 幻夜蓝 水滴屏全面屏 游戏手机 移动联通电信全网通4G手机',10000,10,100000,100000,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/4612/28/6223/298257/5ba22d66Ef665222f/d97ed0b25cbe8c6e.jpg!q70.jpg.webp',10,NULL,NULL),('1272169298793730048',104,105,106,NULL,'100000003145','1272169298508517376','vivo X23 8GB+128GB 幻夜蓝 水滴屏全面屏 游戏手机 移动联通电信全网通4G手机',10000,10,100000,100000,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/4612/28/6223/298257/5ba22d66Ef665222f/d97ed0b25cbe8c6e.jpg!q70.jpg.webp',10,NULL,NULL),('1272169777628057600',104,105,106,NULL,'100000003145','1272169777598697472','vivo X23 8GB+128GB 幻夜蓝 水滴屏全面屏 游戏手机 移动联通电信全网通4G手机',10000,10,100000,100000,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/4612/28/6223/298257/5ba22d66Ef665222f/d97ed0b25cbe8c6e.jpg!q70.jpg.webp',10,NULL,NULL),('1272170559127556096',104,105,106,NULL,'100000003145','1272170559089807360','vivo X23 8GB+128GB 幻夜蓝 水滴屏全面屏 游戏手机 移动联通电信全网通4G手机',10000,10,100000,100000,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/4612/28/6223/298257/5ba22d66Ef665222f/d97ed0b25cbe8c6e.jpg!q70.jpg.webp',10,NULL,NULL),('1272172623991803904',104,105,106,NULL,'100000003145','1272172623907917824','vivo X23 8GB+128GB 幻夜蓝 水滴屏全面屏 游戏手机 移动联通电信全网通4G手机',10000,10,100000,100000,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/4612/28/6223/298257/5ba22d66Ef665222f/d97ed0b25cbe8c6e.jpg!q70.jpg.webp',10,NULL,NULL),('1272173201585213440',104,105,106,NULL,'100000003145','1272173201463578624','vivo X23 8GB+128GB 幻夜蓝 水滴屏全面屏 游戏手机 移动联通电信全网通4G手机',10000,10,100000,100000,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/4612/28/6223/298257/5ba22d66Ef665222f/d97ed0b25cbe8c6e.jpg!q70.jpg.webp',10,NULL,NULL),('1272173545862074368',104,105,106,NULL,'100000003145','1272173545841102848','vivo X23 8GB+128GB 幻夜蓝 水滴屏全面屏 游戏手机 移动联通电信全网通4G手机',10000,10,100000,100000,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/4612/28/6223/298257/5ba22d66Ef665222f/d97ed0b25cbe8c6e.jpg!q70.jpg.webp',10,NULL,NULL),('1272342100142329856',104,105,106,NULL,'100000003145','1272342099857117184','vivo X23 8GB+128GB 幻夜蓝 水滴屏全面屏 游戏手机 移动联通电信全网通4G手机',10000,10,100000,100000,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/4612/28/6223/298257/5ba22d66Ef665222f/d97ed0b25cbe8c6e.jpg!q70.jpg.webp',10,NULL,NULL),('1272344587054223360',104,105,106,NULL,'100000003145','1272344587037446144','vivo X23 8GB+128GB 幻夜蓝 水滴屏全面屏 游戏手机 移动联通电信全网通4G手机',10000,10,100000,100000,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/4612/28/6223/298257/5ba22d66Ef665222f/d97ed0b25cbe8c6e.jpg!q70.jpg.webp',10,NULL,NULL),('1272358413992595456',104,105,106,NULL,'100000003145','1272358411832528896','vivo X23 8GB+128GB 幻夜蓝 水滴屏全面屏 游戏手机 移动联通电信全网通4G手机',10000,10,100000,100000,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/4612/28/6223/298257/5ba22d66Ef665222f/d97ed0b25cbe8c6e.jpg!q70.jpg.webp',10,NULL,NULL),('1272435498647949312',104,105,106,NULL,'100000003145','1272435498366930944','vivo X23 8GB+128GB 幻夜蓝 水滴屏全面屏 游戏手机 移动联通电信全网通4G手机',10000,10,100000,100000,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/4612/28/6223/298257/5ba22d66Ef665222f/d97ed0b25cbe8c6e.jpg!q70.jpg.webp',10,NULL,NULL),('1272436151029993472',104,105,106,NULL,'100000003145','1272436151009021952','vivo X23 8GB+128GB 幻夜蓝 水滴屏全面屏 游戏手机 移动联通电信全网通4G手机',10000,10,100000,100000,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/4612/28/6223/298257/5ba22d66Ef665222f/d97ed0b25cbe8c6e.jpg!q70.jpg.webp',10,NULL,NULL),('1272858734586105856',104,105,106,NULL,'100000003145','1272858732631560192','vivo X23 8GB+128GB 幻夜蓝 水滴屏全面屏 游戏手机 移动联通电信全网通4G手机',10000,10,100000,100000,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/4612/28/6223/298257/5ba22d66Ef665222f/d97ed0b25cbe8c6e.jpg!q70.jpg.webp',10,NULL,NULL),('1274167962533761024',104,105,106,'879054500','100000082995','1274167960998645760','锤子（smartisan ) 坚果 Pro 2S 6G+64GB 纯白色 全面屏双摄 全网通4G手机 双卡双待 游戏手机',500,1,500,500,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t23359/128/2369309375/207652/6973579a/5b7d05feN6f190d9a.jpg!q70.jpg.webp',10,0,'0'),('1274227060902072320',104,105,106,'879054500','100000082995','1274227059392122880','锤子（smartisan ) 坚果 Pro 2S 6G+64GB 纯白色 全面屏双摄 全网通4G手机 双卡双待 游戏手机',500,1,500,500,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t23359/128/2369309375/207652/6973579a/5b7d05feN6f190d9a.jpg!q70.jpg.webp',10,0,'0'),('1274227618278936576',104,105,106,'879054500','100000082995','1274227618123747328','锤子（smartisan ) 坚果 Pro 2S 6G+64GB 纯白色 全面屏双摄 全网通4G手机 双卡双待 游戏手机',500,1,500,500,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t23359/128/2369309375/207652/6973579a/5b7d05feN6f190d9a.jpg!q70.jpg.webp',10,0,'0'),('1274246389299286016',104,105,106,'879054500','100000082995','1274246387973885952','锤子（smartisan ) 坚果 Pro 2S 6G+64GB 纯白色 全面屏双摄 全网通4G手机 双卡双待 游戏手机',500,1,500,500,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t23359/128/2369309375/207652/6973579a/5b7d05feN6f190d9a.jpg!q70.jpg.webp',10,0,'0'),('1274703538567647232',104,105,106,'879054500','100000082995','1274703536806039552','锤子（smartisan ) 坚果 Pro 2S 6G+64GB 纯白色 全面屏双摄 全网通4G手机 双卡双待 游戏手机',500,1,500,500,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t23359/128/2369309375/207652/6973579a/5b7d05feN6f190d9a.jpg!q70.jpg.webp',10,0,'0'),('1274703749578887168',104,105,106,'879054500','100000082995','1274703749507584000','锤子（smartisan ) 坚果 Pro 2S 6G+64GB 纯白色 全面屏双摄 全网通4G手机 双卡双待 游戏手机',500,3,1500,1500,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t23359/128/2369309375/207652/6973579a/5b7d05feN6f190d9a.jpg!q70.jpg.webp',30,0,'0'),('1275254557584265216',104,105,106,'879054500','100000082995','1275254557466824704','锤子（smartisan ) 坚果 Pro 2S 6G+64GB 纯白色 全面屏双摄 全网通4G手机 双卡双待 游戏手机',500,1,500,500,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t23359/128/2369309375/207652/6973579a/5b7d05feN6f190d9a.jpg!q70.jpg.webp',10,0,'0'),('1275343744517935104',104,105,106,'879054500','100000082995','1275343744496963584','锤子（smartisan ) 坚果 Pro 2S 6G+64GB 纯白色 全面屏双摄 全网通4G手机 双卡双待 游戏手机',500,1,500,500,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t23359/128/2369309375/207652/6973579a/5b7d05feN6f190d9a.jpg!q70.jpg.webp',10,0,'0'),('1275361624508731392',104,105,106,'879054500','100000082995','1275361624487759872','锤子（smartisan ) 坚果 Pro 2S 6G+64GB 纯白色 全面屏双摄 全网通4G手机 双卡双待 游戏手机',500,1,500,500,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t23359/128/2369309375/207652/6973579a/5b7d05feN6f190d9a.jpg!q70.jpg.webp',10,0,'0'),('1275422113695666176',104,105,106,'879054500','100000082995','1275422113657917440','锤子（smartisan ) 坚果 Pro 2S 6G+64GB 纯白色 全面屏双摄 全网通4G手机 双卡双待 游戏手机',500,1,500,500,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t23359/128/2369309375/207652/6973579a/5b7d05feN6f190d9a.jpg!q70.jpg.webp',10,0,'0'),('1275428676011429888',1099,1117,1124,'10000000616300','100000006163','1275428675864629248','巴布豆(BOBDOG)柔薄悦动婴儿拉拉裤XXL码80片(15kg以上)',1,100,100,100,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t23998/350/2363990466/222391/a6e9581d/5b7cba5bN0c18fb4f.jpg!q70.jpg.webp',1000,NULL,'0'),('1275430148006612992',1099,1117,1124,'10000000616300','100000006163','1275430147994030080','巴布豆(BOBDOG)柔薄悦动婴儿拉拉裤XXL码80片(15kg以上)',1,100,100,100,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t23998/350/2363990466/222391/a6e9581d/5b7cba5bN0c18fb4f.jpg!q70.jpg.webp',1000,NULL,'0'),('1275433517328764928',104,105,106,'879054500','100000082995','1275433517320376320','锤子（smartisan ) 坚果 Pro 2S 6G+64GB 纯白色 全面屏双摄 全网通4G手机 双卡双待 游戏手机',500,1,500,500,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t23359/128/2369309375/207652/6973579a/5b7d05feN6f190d9a.jpg!q70.jpg.webp',10,0,'0'),('1275435036639891456',104,105,106,'879054500','100000082995','1275435036631502848','锤子（smartisan ) 坚果 Pro 2S 6G+64GB 纯白色 全面屏双摄 全网通4G手机 双卡双待 游戏手机',500,1,500,500,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t23359/128/2369309375/207652/6973579a/5b7d05feN6f190d9a.jpg!q70.jpg.webp',10,0,'0'),('1275435099608977408',104,105,106,'879054500','100000082995','1275435099600588800','锤子（smartisan ) 坚果 Pro 2S 6G+64GB 纯白色 全面屏双摄 全网通4G手机 双卡双待 游戏手机',500,1,500,500,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t23359/128/2369309375/207652/6973579a/5b7d05feN6f190d9a.jpg!q70.jpg.webp',10,0,'0'),('1275435151828062208',104,105,106,'879054500','100000082995','1275435151823867904','锤子（smartisan ) 坚果 Pro 2S 6G+64GB 纯白色 全面屏双摄 全网通4G手机 双卡双待 游戏手机',500,1,500,500,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t23359/128/2369309375/207652/6973579a/5b7d05feN6f190d9a.jpg!q70.jpg.webp',10,0,'0'),('1275435187131518976',104,105,106,'879054500','100000082995','1275435187123130368','锤子（smartisan ) 坚果 Pro 2S 6G+64GB 纯白色 全面屏双摄 全网通4G手机 双卡双待 游戏手机',500,1,500,500,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t23359/128/2369309375/207652/6973579a/5b7d05feN6f190d9a.jpg!q70.jpg.webp',10,0,'0'),('1275435253745455104',104,105,106,'879054500','100000082995','1275435253737066496','锤子（smartisan ) 坚果 Pro 2S 6G+64GB 纯白色 全面屏双摄 全网通4G手机 双卡双待 游戏手机',500,1,500,500,'https://m.360buyimg.com/mobilecms/s720x720_jfs/t23359/128/2369309375/207652/6973579a/5b7d05feN6f190d9a.jpg!q70.jpg.webp',10,0,'0');

/*Table structure for table `tb_order_log` */

DROP TABLE IF EXISTS `tb_order_log`;

CREATE TABLE `tb_order_log` (
  `id` varchar(20) NOT NULL COMMENT 'ID',
  `operater` varchar(50) DEFAULT NULL COMMENT '操作员',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单ID',
  `order_status` char(1) DEFAULT NULL COMMENT '订单状态',
  `pay_status` char(1) DEFAULT NULL COMMENT '付款状态',
  `consign_status` char(1) DEFAULT NULL COMMENT '发货状态',
  `remarks` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `tb_order_log` */

insert  into `tb_order_log`(`id`,`operater`,`operate_time`,`order_id`,`order_status`,`pay_status`,`consign_status`,`remarks`) values ('1272862954647326720','system','2020-06-16 20:05:52',1272858732631560192,'4',NULL,'0',NULL),('1272887523231797248','system',NULL,1272858732631560192,'3',NULL,NULL,NULL),('1275430234392498176','system','2020-06-23 22:07:19',1275430147994030080,'1','1',NULL,'交易流水号:2020062322001450360500779116'),('1275432734440951808','system','2020-06-23 22:17:15',1275428675864629248,'4',NULL,NULL,NULL),('1275433608068337664','heima','2020-06-23 22:20:43',1274703749507584000,'3',NULL,'2',NULL),('1275433656931979264','system','2020-06-23 22:20:55',1275433517320376320,'4',NULL,NULL,NULL);

/*Table structure for table `tb_preferential` */

DROP TABLE IF EXISTS `tb_preferential`;

CREATE TABLE `tb_preferential` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `buy_money` int(11) DEFAULT NULL COMMENT '消费金额',
  `pre_money` int(11) DEFAULT NULL COMMENT '优惠金额',
  `category_id` int(20) DEFAULT NULL COMMENT '品类ID',
  `start_time` date DEFAULT NULL COMMENT '活动开始日期',
  `end_time` date DEFAULT NULL COMMENT '活动截至日期',
  `state` varchar(1) DEFAULT NULL COMMENT '状态',
  `type` varchar(1) DEFAULT NULL COMMENT '类型1不翻倍 2翻倍',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `tb_preferential` */

insert  into `tb_preferential`(`id`,`buy_money`,`pre_money`,`category_id`,`start_time`,`end_time`,`state`,`type`) values (1,10000,3000,757,'2019-07-01','2020-01-18','1','1'),(2,30000,10000,757,'2019-07-01','2020-01-18','1','1'),(3,60000,30000,757,'2019-07-01','2020-01-18','1','1'),(4,10000,4000,76,'2019-07-01','2020-03-23','1','2');

/*Table structure for table `tb_return_cause` */

DROP TABLE IF EXISTS `tb_return_cause`;

CREATE TABLE `tb_return_cause` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `cause` varchar(100) DEFAULT NULL COMMENT '原因',
  `seq` int(11) DEFAULT '1' COMMENT '排序',
  `status` char(1) DEFAULT NULL COMMENT '是否启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `tb_return_cause` */

/*Table structure for table `tb_return_order` */

DROP TABLE IF EXISTS `tb_return_order`;

CREATE TABLE `tb_return_order` (
  `id` varchar(20) NOT NULL COMMENT '服务单号',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单号',
  `apply_time` datetime DEFAULT NULL COMMENT '申请时间',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `user_account` varchar(11) DEFAULT NULL COMMENT '用户账号',
  `linkman` varchar(20) DEFAULT NULL COMMENT '联系人',
  `linkman_mobile` varchar(11) DEFAULT NULL COMMENT '联系人手机',
  `type` char(1) DEFAULT NULL COMMENT '类型',
  `return_money` int(11) DEFAULT NULL COMMENT '退款金额',
  `is_return_freight` char(1) DEFAULT NULL COMMENT '是否退运费',
  `status` char(1) DEFAULT NULL COMMENT '申请状态',
  `dispose_time` datetime DEFAULT NULL COMMENT '处理时间',
  `return_cause` int(11) DEFAULT NULL COMMENT '退货退款原因',
  `evidence` varchar(1000) DEFAULT NULL COMMENT '凭证图片',
  `description` varchar(1000) DEFAULT NULL COMMENT '问题描述',
  `remark` varchar(1000) DEFAULT NULL COMMENT '处理备注',
  `admin_id` int(11) DEFAULT NULL COMMENT '管理员id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `tb_return_order` */

/*Table structure for table `tb_return_order_item` */

DROP TABLE IF EXISTS `tb_return_order_item`;

CREATE TABLE `tb_return_order_item` (
  `id` varchar(20) COLLATE utf8_bin NOT NULL COMMENT 'ID',
  `category_id` int(20) DEFAULT NULL COMMENT '分类ID',
  `spu_id` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'SPU_ID',
  `sku_id` varchar(20) COLLATE utf8_bin NOT NULL COMMENT 'SKU_ID',
  `order_id` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '订单ID',
  `order_item_id` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '订单明细ID',
  `return_order_id` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '退货订单ID',
  `title` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '标题',
  `price` int(20) DEFAULT NULL COMMENT '单价',
  `num` int(10) DEFAULT NULL COMMENT '数量',
  `money` int(20) DEFAULT NULL COMMENT '总金额',
  `pay_money` int(20) DEFAULT NULL COMMENT '支付金额',
  `image` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '图片地址',
  `weight` int(11) DEFAULT NULL COMMENT '重量',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `item_id` (`sku_id`) USING BTREE,
  KEY `order_id` (`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `tb_return_order_item` */

/*Table structure for table `tb_task` */

DROP TABLE IF EXISTS `tb_task`;

CREATE TABLE `tb_task` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  `task_type` varchar(32) DEFAULT NULL COMMENT '任务类型',
  `mq_exchange` varchar(64) DEFAULT NULL COMMENT '交换机名称',
  `mq_routingkey` varchar(64) DEFAULT NULL COMMENT 'routingkey',
  `request_body` varchar(512) DEFAULT NULL COMMENT '任务请求的内容',
  `status` varchar(32) DEFAULT NULL COMMENT '任务状态',
  `errormsg` varchar(512) DEFAULT NULL COMMENT '任务错误信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Data for the table `tb_task` */

/*Table structure for table `tb_task_his` */

DROP TABLE IF EXISTS `tb_task_his`;

CREATE TABLE `tb_task_his` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  `task_type` varchar(32) DEFAULT NULL COMMENT '任务类型',
  `mq_exchange` varchar(64) DEFAULT NULL COMMENT '交换机名称',
  `mq_routingkey` varchar(64) DEFAULT NULL COMMENT 'routingkey',
  `request_body` varchar(512) DEFAULT NULL COMMENT '任务请求的内容',
  `status` varchar(32) DEFAULT NULL COMMENT '任务状态',
  `errormsg` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1272858740672040969 DEFAULT CHARSET=utf8;

/*Data for the table `tb_task_his` */

insert  into `tb_task_his`(`id`,`create_time`,`update_time`,`delete_time`,`task_type`,`mq_exchange`,`mq_routingkey`,`request_body`,`status`,`errormsg`) values (1271803266686128128,'2020-06-13 21:55:02','2020-06-13 21:55:02','2020-06-13 21:55:11',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1271803264781914112\",\"point\":100000,\"username\":\"heima\"}',NULL,NULL),(1271804043219570688,'2020-06-13 21:58:08','2020-06-13 21:58:08','2020-06-13 22:02:25',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1271804042665922560\",\"point\":100000,\"username\":\"heima\"}',NULL,NULL),(1272128307948818432,'2020-06-14 19:26:38','2020-06-14 19:26:38','2020-06-14 19:26:41',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272128305981689856\",\"point\":100000,\"username\":\"heima\"}',NULL,NULL),(1272128364223795200,'2020-06-14 19:26:52','2020-06-14 19:26:52','2020-06-14 19:27:00',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272128364177657856\",\"point\":0,\"username\":\"heima\"}',NULL,NULL),(1272128638426419200,'2020-06-14 19:27:57','2020-06-14 19:27:57','2020-06-14 19:28:00',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272128638380281856\",\"point\":0,\"username\":\"heima\"}',NULL,NULL),(1272128801140248576,'2020-06-14 19:28:36','2020-06-14 19:28:36','2020-06-14 19:28:40',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272128801098305536\",\"point\":0,\"username\":\"heima\"}',NULL,NULL),(1272129452901535744,'2020-06-14 19:31:11','2020-06-14 19:31:11','2020-06-14 19:31:20',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272129451060236288\",\"point\":100000,\"username\":\"heima\"}',NULL,NULL),(1272129794242383872,'2020-06-14 19:32:33','2020-06-14 19:32:33','2020-06-14 19:32:40',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272129794103971840\",\"point\":100000,\"username\":\"heima\"}',NULL,NULL),(1272129851935035392,'2020-06-14 19:32:46','2020-06-14 19:32:46','2020-06-14 19:32:50',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272129851897286656\",\"point\":0,\"username\":\"heima\"}',NULL,NULL),(1272130399476256768,'2020-06-14 19:34:57','2020-06-14 19:34:57','2020-06-14 19:35:00',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272130397639151616\",\"point\":100000,\"username\":\"heima\"}',NULL,NULL),(1272130571161702400,'2020-06-14 19:35:38','2020-06-14 19:35:38','2020-06-14 19:35:40',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272130571044261888\",\"point\":100000,\"username\":\"heima\"}',NULL,NULL),(1272131182322126848,'2020-06-14 19:38:04','2020-06-14 19:38:04','2020-06-14 19:38:10',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272131182183714816\",\"point\":100000,\"username\":\"heima\"}',NULL,NULL),(1272149754524078080,'2020-06-14 20:51:52','2020-06-14 20:51:52','2020-06-14 20:52:00',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272149754150785024\",\"point\":100000,\"username\":\"heima\"}',NULL,NULL),(1272161270371586048,'2020-06-14 21:37:37','2020-06-14 21:37:37','2020-06-14 21:37:42',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272161263413235712\",\"point\":100000,\"username\":\"heima\"}',NULL,NULL),(1272169299318018048,'2020-06-14 22:09:31','2020-06-14 22:09:31','2020-06-14 22:09:40',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272169298508517376\",\"point\":100000,\"username\":\"heima\"}',NULL,NULL),(1272169777841967104,'2020-06-14 22:11:25','2020-06-14 22:11:25','2020-06-14 22:11:30',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272169777598697472\",\"point\":100000,\"username\":\"heima\"}',NULL,NULL),(1272170559232413696,'2020-06-14 22:14:32','2020-06-14 22:14:32','2020-06-14 22:14:40',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272170559089807360\",\"point\":100000,\"username\":\"heima\"}',NULL,NULL),(1272172624096661504,'2020-06-14 22:22:44','2020-06-14 22:22:44','2020-06-14 22:22:50',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272172623907917824\",\"point\":100000,\"username\":\"heima\"}',NULL,NULL),(1272173201782345728,'2020-06-14 22:25:02','2020-06-14 22:25:02','2020-06-14 22:25:10',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272173201463578624\",\"point\":100000,\"username\":\"heima\"}',NULL,NULL),(1272173545971126272,'2020-06-14 22:26:24','2020-06-14 22:26:24','2020-06-14 22:26:30',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272173545841102848\",\"point\":100000,\"username\":\"heima\"}',NULL,NULL),(1272342102893793280,'2020-06-15 09:36:11','2020-06-15 09:36:11','2020-06-15 09:36:20',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272342099857117184\",\"point\":100000,\"username\":\"heima\"}',NULL,NULL),(1272344587167469568,'2020-06-15 09:46:03','2020-06-15 09:46:03','2020-06-15 09:46:10',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272344587037446144\",\"point\":100000,\"username\":\"heima\"}',NULL,NULL),(1272358414600769536,'2020-06-15 10:41:00','2020-06-15 10:41:00','2020-06-15 10:41:10',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272358411832528896\",\"point\":100000,\"username\":\"heima\"}',NULL,NULL),(1272435499184820224,'2020-06-15 15:47:18','2020-06-15 15:47:18','2020-06-15 15:47:20',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272435498366930944\",\"point\":100000,\"username\":\"heima\"}',NULL,NULL),(1272436151151628288,'2020-06-15 15:49:54','2020-06-15 15:49:54','2020-06-15 15:50:00',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272436151009021952\",\"point\":100000,\"username\":\"heima\"}',NULL,NULL),(1272858740672040960,'2020-06-16 19:49:07','2020-06-16 19:49:07','2020-06-16 19:49:12',NULL,'ex_orderpoint','qu_setpoint','{\"orderId\":\"1272858732631560192\",\"point\":100000,\"username\":\"heima\"}',NULL,NULL),(1272858740672040961,'2020-06-23 22:01:08','2020-06-23 22:01:08','2020-06-23 22:01:11',NULL,'ex_buying_addpointuser','addpoint','{\"orderId\":\"1275428675864629248\",\"point\":100,\"username\":\"heima\"}',NULL,NULL),(1272858740672040962,'2020-06-23 22:06:58','2020-06-23 22:06:58','2020-06-23 22:07:00',NULL,'ex_buying_addpointuser','addpoint','{\"orderId\":\"1275430147994030080\",\"point\":100,\"username\":\"heima\"}',NULL,NULL),(1272858740672040963,'2020-06-23 22:20:22','2020-06-23 22:20:22','2020-06-23 22:20:22',NULL,'ex_buying_addpointuser','addpoint','{\"orderId\":\"1275433517320376320\",\"point\":500,\"username\":\"heima\"}',NULL,NULL),(1272858740672040964,'2020-06-23 22:26:24','2020-06-23 22:26:24','2020-06-23 22:26:24',NULL,'ex_buying_addpointuser','addpoint','{\"orderId\":\"1275435036631502848\",\"point\":500,\"username\":\"heima\"}',NULL,NULL),(1272858740672040965,'2020-06-23 22:26:39','2020-06-23 22:26:39','2020-06-23 22:26:40',NULL,'ex_buying_addpointuser','addpoint','{\"orderId\":\"1275435099600588800\",\"point\":500,\"username\":\"heima\"}',NULL,NULL),(1272858740672040966,'2020-06-23 22:26:51','2020-06-23 22:26:51','2020-06-23 22:26:52',NULL,'ex_buying_addpointuser','addpoint','{\"orderId\":\"1275435151823867904\",\"point\":500,\"username\":\"heima\"}',NULL,NULL),(1272858740672040967,'2020-06-23 22:27:00','2020-06-23 22:27:00','2020-06-23 22:27:00',NULL,'ex_buying_addpointuser','addpoint','{\"orderId\":\"1275435187123130368\",\"point\":500,\"username\":\"heima\"}',NULL,NULL),(1272858740672040968,'2020-06-23 22:27:16','2020-06-23 22:27:16','2020-06-23 22:27:16',NULL,'ex_buying_addpointuser','addpoint','{\"orderId\":\"1275435253737066496\",\"point\":500,\"username\":\"heima\"}',NULL,NULL);

/*Table structure for table `undo_log` */

DROP TABLE IF EXISTS `undo_log`;

CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime DEFAULT NULL,
  `log_modified` datetime DEFAULT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_unionkey` (`xid`,`branch_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `undo_log` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
