-- MySQL dump 10.16  Distrib 10.2.12-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: mysql    Database: zipkin
-- ------------------------------------------------------
-- Server version	10.1.26-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `zipkin_annotations`
--

DROP TABLE IF EXISTS `zipkin_annotations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zipkin_annotations` (
  `trace_id_high` bigint(20) NOT NULL DEFAULT '0' COMMENT 'If non zero, this means the trace uses 128 bit traceIds instead of 64 bit',
  `trace_id` bigint(20) NOT NULL COMMENT 'coincides with zipkin_spans.trace_id',
  `span_id` bigint(20) NOT NULL COMMENT 'coincides with zipkin_spans.id',
  `a_key` varchar(255) NOT NULL COMMENT 'BinaryAnnotation.key or Annotation.value if type == -1',
  `a_value` blob COMMENT 'BinaryAnnotation.value(), which must be smaller than 64KB',
  `a_type` int(11) NOT NULL COMMENT 'BinaryAnnotation.type() or -1 if Annotation',
  `a_timestamp` bigint(20) DEFAULT NULL COMMENT 'Used to implement TTL; Annotation.timestamp or zipkin_spans.timestamp',
  `endpoint_ipv4` int(11) DEFAULT NULL COMMENT 'Null when Binary/Annotation.endpoint is null',
  `endpoint_ipv6` binary(16) DEFAULT NULL COMMENT 'Null when Binary/Annotation.endpoint is null, or no IPv6 address',
  `endpoint_port` smallint(6) DEFAULT NULL COMMENT 'Null when Binary/Annotation.endpoint is null',
  `endpoint_service_name` varchar(255) DEFAULT NULL COMMENT 'Null when Binary/Annotation.endpoint is null',
  UNIQUE KEY `trace_id_high` (`trace_id_high`,`trace_id`,`span_id`,`a_key`,`a_timestamp`) COMMENT 'Ignore insert on duplicate',
  KEY `trace_id_high_2` (`trace_id_high`,`trace_id`,`span_id`) COMMENT 'for joining with zipkin_spans',
  KEY `trace_id_high_3` (`trace_id_high`,`trace_id`) COMMENT 'for getTraces/ByIds',
  KEY `endpoint_service_name` (`endpoint_service_name`) COMMENT 'for getTraces and getServiceNames',
  KEY `a_type` (`a_type`) COMMENT 'for getTraces',
  KEY `a_key` (`a_key`) COMMENT 'for getTraces',
  KEY `trace_id` (`trace_id`,`span_id`,`a_key`) COMMENT 'for dependencies job'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zipkin_annotations`
--

LOCK TABLES `zipkin_annotations` WRITE;
/*!40000 ALTER TABLE `zipkin_annotations` DISABLE KEYS */;
INSERT INTO `zipkin_annotations` VALUES (0,-747967603336782775,4420130216367886776,'sr',NULL,-1,1515237436050000,-1408106490,NULL,9001,'cpuhog'),(0,-747967603336782775,4420130216367886776,'ss',NULL,-1,1515237438700617,-1408106490,NULL,9001,'cpuhog'),(0,-747967603336782775,4420130216367886776,'mvc.controller.class','CpuhogController',6,1515237436050000,-1408106490,NULL,9001,'cpuhog'),(0,-747967603336782775,4420130216367886776,'mvc.controller.method','burncpu',6,1515237436050000,-1408106490,NULL,9001,'cpuhog'),(0,-747967603336782775,4420130216367886776,'spring.instance_id','ddfb6dde9f71:Cpuhog:9001',6,1515237436050000,-1408106490,NULL,9001,'cpuhog'),(0,-747967603336782775,-3268776777031956931,'cs',NULL,-1,1515237435865000,-1408106489,NULL,9999,'gateway'),(0,-747967603336782775,-3268776777031956931,'cr',NULL,-1,1515237438713000,-1408106489,NULL,9999,'gateway'),(0,-747967603336782775,-3268776777031956931,'http.host','cpuhog',6,1515237435865000,-1408106489,NULL,9999,'gateway'),(0,-747967603336782775,-3268776777031956931,'http.method','GET',6,1515237435865000,-1408106489,NULL,9999,'gateway'),(0,-747967603336782775,-3268776777031956931,'http.path','/burncpu',6,1515237435865000,-1408106489,NULL,9999,'gateway'),(0,-747967603336782775,-3268776777031956931,'http.url','http://cpuhog:9001/burncpu?time_msec=2500',6,1515237435865000,-1408106489,NULL,9999,'gateway'),(0,-747967603336782775,-3268776777031956931,'spring.instance_id','5843205e6e17:Gateway:9999',6,1515237435865000,-1408106489,NULL,9999,'gateway'),(0,-747967603336782775,4420130216367886776,'cs',NULL,-1,1515237435864000,-1408106489,NULL,9999,'gateway'),(0,-747967603336782775,4420130216367886776,'cr',NULL,-1,1515237438722000,-1408106489,NULL,9999,'gateway'),(0,-747967603336782775,4420130216367886776,'http.host','cpuhog',6,1515237435864000,-1408106489,NULL,9999,'gateway'),(0,-747967603336782775,4420130216367886776,'http.method','GET',6,1515237435864000,-1408106489,NULL,9999,'gateway'),(0,-747967603336782775,4420130216367886776,'http.path','/burncpu',6,1515237435864000,-1408106489,NULL,9999,'gateway'),(0,-747967603336782775,4420130216367886776,'http.url','http://cpuhog:9001/burncpu?time_msec=2500',6,1515237435864000,-1408106489,NULL,9999,'gateway'),(0,-747967603336782775,4420130216367886776,'spring.instance_id','5843205e6e17:Gateway:9999',6,1515237435864000,-1408106489,NULL,9999,'gateway'),(0,-747967603336782775,-747967603336782775,'sr',NULL,-1,1515237435811000,-1408106489,NULL,9999,'gateway'),(0,-747967603336782775,-747967603336782775,'ss',NULL,-1,1515237438736327,-1408106489,NULL,9999,'gateway'),(0,-747967603336782775,-747967603336782775,'http.host','localhost',6,1515237435811000,-1408106489,NULL,9999,'gateway'),(0,-747967603336782775,-747967603336782775,'http.method','GET',6,1515237435811000,-1408106489,NULL,9999,'gateway'),(0,-747967603336782775,-747967603336782775,'http.path','/invoke/single-cpu',6,1515237435811000,-1408106489,NULL,9999,'gateway'),(0,-747967603336782775,-747967603336782775,'http.status_code','200',6,1515237435811000,-1408106489,NULL,9999,'gateway'),(0,-747967603336782775,-747967603336782775,'http.url','http://localhost:9999/invoke/single-cpu',6,1515237435811000,-1408106489,NULL,9999,'gateway'),(0,-747967603336782775,-747967603336782775,'mvc.controller.class','GatewayController',6,1515237435811000,-1408106489,NULL,9999,'gateway'),(0,-747967603336782775,-747967603336782775,'mvc.controller.method','invoke',6,1515237435811000,-1408106489,NULL,9999,'gateway'),(0,-747967603336782775,-747967603336782775,'spring.instance_id','5843205e6e17:Gateway:9999',6,1515237435811000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,919509467286965348,'cs',NULL,-1,1515237448750000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,919509467286965348,'cr',NULL,-1,1515237450771000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,919509467286965348,'http.host','cpuhog',6,1515237448750000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,919509467286965348,'http.method','GET',6,1515237448750000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,919509467286965348,'http.path','/burncpu',6,1515237448750000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,919509467286965348,'http.url','http://cpuhog:9001/burncpu?time_msec=2000',6,1515237448750000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,919509467286965348,'spring.instance_id','5843205e6e17:Gateway:9999',6,1515237448750000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,3650612569389201682,'cs',NULL,-1,1515237448750000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,3650612569389201682,'cr',NULL,-1,1515237450772000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,3650612569389201682,'http.host','cpuhog',6,1515237448750000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,3650612569389201682,'http.method','GET',6,1515237448750000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,3650612569389201682,'http.path','/burncpu',6,1515237448750000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,3650612569389201682,'http.url','http://cpuhog:9001/burncpu?time_msec=2000',6,1515237448750000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,3650612569389201682,'spring.instance_id','5843205e6e17:Gateway:9999',6,1515237448750000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,3650612569389201682,'sr',NULL,-1,1515237448756000,-1408106490,NULL,9001,'cpuhog'),(0,-3952542349986978166,3650612569389201682,'ss',NULL,-1,1515237450769967,-1408106490,NULL,9001,'cpuhog'),(0,-3952542349986978166,3650612569389201682,'mvc.controller.class','CpuhogController',6,1515237448756000,-1408106490,NULL,9001,'cpuhog'),(0,-3952542349986978166,3650612569389201682,'mvc.controller.method','burncpu',6,1515237448756000,-1408106490,NULL,9001,'cpuhog'),(0,-3952542349986978166,3650612569389201682,'spring.instance_id','ddfb6dde9f71:Cpuhog:9001',6,1515237448756000,-1408106490,NULL,9001,'cpuhog'),(0,-3952542349986978166,2199402995387371259,'sr',NULL,-1,1515237450777000,-1408106490,NULL,9001,'cpuhog'),(0,-3952542349986978166,2199402995387371259,'ss',NULL,-1,1515237451781799,-1408106490,NULL,9001,'cpuhog'),(0,-3952542349986978166,2199402995387371259,'mvc.controller.class','CpuhogController',6,1515237450777000,-1408106490,NULL,9001,'cpuhog'),(0,-3952542349986978166,2199402995387371259,'mvc.controller.method','burncpu',6,1515237450777000,-1408106490,NULL,9001,'cpuhog'),(0,-3952542349986978166,2199402995387371259,'spring.instance_id','ddfb6dde9f71:Cpuhog:9001',6,1515237450777000,-1408106490,NULL,9001,'cpuhog'),(0,-3952542349986978166,-4766729690489789511,'cs',NULL,-1,1515237450773000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-4766729690489789511,'cr',NULL,-1,1515237451782000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-4766729690489789511,'http.host','cpuhog',6,1515237450773000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-4766729690489789511,'http.method','GET',6,1515237450773000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-4766729690489789511,'http.path','/burncpu',6,1515237450773000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-4766729690489789511,'http.url','http://cpuhog:9001/burncpu?time_msec=1000',6,1515237450773000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-4766729690489789511,'spring.instance_id','5843205e6e17:Gateway:9999',6,1515237450773000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,2199402995387371259,'cs',NULL,-1,1515237450773000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,2199402995387371259,'cr',NULL,-1,1515237451783000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,2199402995387371259,'http.host','cpuhog',6,1515237450773000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,2199402995387371259,'http.method','GET',6,1515237450773000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,2199402995387371259,'http.path','/burncpu',6,1515237450773000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,2199402995387371259,'http.url','http://cpuhog:9001/burncpu?time_msec=1000',6,1515237450773000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,2199402995387371259,'spring.instance_id','5843205e6e17:Gateway:9999',6,1515237450773000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-7085456595534717338,'sr',NULL,-1,1515237451786000,-1408106490,NULL,9001,'cpuhog'),(0,-3952542349986978166,-7085456595534717338,'ss',NULL,-1,1515237452290484,-1408106490,NULL,9001,'cpuhog'),(0,-3952542349986978166,-7085456595534717338,'mvc.controller.class','CpuhogController',6,1515237451786000,-1408106490,NULL,9001,'cpuhog'),(0,-3952542349986978166,-7085456595534717338,'mvc.controller.method','burncpu',6,1515237451786000,-1408106490,NULL,9001,'cpuhog'),(0,-3952542349986978166,-7085456595534717338,'spring.instance_id','ddfb6dde9f71:Cpuhog:9001',6,1515237451786000,-1408106490,NULL,9001,'cpuhog'),(0,-3952542349986978166,-3517567483243985447,'cs',NULL,-1,1515237451784000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-3517567483243985447,'cr',NULL,-1,1515237452291000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-3517567483243985447,'http.host','cpuhog',6,1515237451784000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-3517567483243985447,'http.method','GET',6,1515237451784000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-3517567483243985447,'http.path','/burncpu',6,1515237451784000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-3517567483243985447,'http.url','http://cpuhog:9001/burncpu?time_msec=500',6,1515237451784000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-3517567483243985447,'spring.instance_id','5843205e6e17:Gateway:9999',6,1515237451784000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-7085456595534717338,'cs',NULL,-1,1515237451784000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-7085456595534717338,'cr',NULL,-1,1515237452292000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-7085456595534717338,'http.host','cpuhog',6,1515237451784000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-7085456595534717338,'http.method','GET',6,1515237451784000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-7085456595534717338,'http.path','/burncpu',6,1515237451784000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-7085456595534717338,'http.url','http://cpuhog:9001/burncpu?time_msec=500',6,1515237451784000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-7085456595534717338,'spring.instance_id','5843205e6e17:Gateway:9999',6,1515237451784000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-3952542349986978166,'sr',NULL,-1,1515237448746000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-3952542349986978166,'ss',NULL,-1,1515237452295836,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-3952542349986978166,'http.host','localhost',6,1515237448746000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-3952542349986978166,'http.method','GET',6,1515237448746000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-3952542349986978166,'http.path','/invoke/three-cpu',6,1515237448746000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-3952542349986978166,'http.status_code','200',6,1515237448746000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-3952542349986978166,'http.url','http://localhost:9999/invoke/three-cpu',6,1515237448746000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-3952542349986978166,'mvc.controller.class','GatewayController',6,1515237448746000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-3952542349986978166,'mvc.controller.method','invoke',6,1515237448746000,-1408106489,NULL,9999,'gateway'),(0,-3952542349986978166,-3952542349986978166,'spring.instance_id','5843205e6e17:Gateway:9999',6,1515237448746000,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,-4918951110302691249,'sr',NULL,-1,1515237462315000,-1408106490,NULL,9001,'cpuhog'),(0,6109457896939656648,-4918951110302691249,'ss',NULL,-1,1515237464818847,-1408106490,NULL,9001,'cpuhog'),(0,6109457896939656648,-4918951110302691249,'mvc.controller.class','CpuhogController',6,1515237462315000,-1408106490,NULL,9001,'cpuhog'),(0,6109457896939656648,-4918951110302691249,'mvc.controller.method','burncpu',6,1515237462315000,-1408106490,NULL,9001,'cpuhog'),(0,6109457896939656648,-4918951110302691249,'spring.instance_id','ddfb6dde9f71:Cpuhog:9001',6,1515237462315000,-1408106490,NULL,9001,'cpuhog'),(0,6109457896939656648,-5934756430793060449,'cs',NULL,-1,1515237462312000,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,-5934756430793060449,'cr',NULL,-1,1515237464820000,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,-5934756430793060449,'http.host','cpuhog',6,1515237462312000,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,-5934756430793060449,'http.method','GET',6,1515237462312000,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,-5934756430793060449,'http.path','/burncpu',6,1515237462312000,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,-5934756430793060449,'http.url','http://cpuhog:9001/burncpu?time_msec=2500',6,1515237462312000,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,-5934756430793060449,'spring.instance_id','5843205e6e17:Gateway:9999',6,1515237462312000,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,-4918951110302691249,'cs',NULL,-1,1515237462312000,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,-4918951110302691249,'cr',NULL,-1,1515237464821000,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,-4918951110302691249,'http.host','cpuhog',6,1515237462312000,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,-4918951110302691249,'http.method','GET',6,1515237462312000,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,-4918951110302691249,'http.path','/burncpu',6,1515237462312000,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,-4918951110302691249,'http.url','http://cpuhog:9001/burncpu?time_msec=2500',6,1515237462312000,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,-4918951110302691249,'spring.instance_id','5843205e6e17:Gateway:9999',6,1515237462312000,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,6109457896939656648,'sr',NULL,-1,1515237462308000,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,6109457896939656648,'ss',NULL,-1,1515237464822196,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,6109457896939656648,'http.host','localhost',6,1515237462308000,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,6109457896939656648,'http.method','GET',6,1515237462308000,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,6109457896939656648,'http.path','/invoke/single-cpu',6,1515237462308000,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,6109457896939656648,'http.status_code','200',6,1515237462308000,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,6109457896939656648,'http.url','http://localhost:9999/invoke/single-cpu',6,1515237462308000,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,6109457896939656648,'mvc.controller.class','GatewayController',6,1515237462308000,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,6109457896939656648,'mvc.controller.method','invoke',6,1515237462308000,-1408106489,NULL,9999,'gateway'),(0,6109457896939656648,6109457896939656648,'spring.instance_id','5843205e6e17:Gateway:9999',6,1515237462308000,-1408106489,NULL,9999,'gateway');
/*!40000 ALTER TABLE `zipkin_annotations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zipkin_dependencies`
--

DROP TABLE IF EXISTS `zipkin_dependencies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zipkin_dependencies` (
  `day` date NOT NULL,
  `parent` varchar(255) NOT NULL,
  `child` varchar(255) NOT NULL,
  `call_count` bigint(20) DEFAULT NULL,
  `error_count` bigint(20) DEFAULT NULL,
  UNIQUE KEY `day` (`day`,`parent`,`child`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zipkin_dependencies`
--

LOCK TABLES `zipkin_dependencies` WRITE;
/*!40000 ALTER TABLE `zipkin_dependencies` DISABLE KEYS */;
/*!40000 ALTER TABLE `zipkin_dependencies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zipkin_spans`
--

DROP TABLE IF EXISTS `zipkin_spans`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zipkin_spans` (
  `trace_id_high` bigint(20) NOT NULL DEFAULT '0' COMMENT 'If non zero, this means the trace uses 128 bit traceIds instead of 64 bit',
  `trace_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `debug` bit(1) DEFAULT NULL,
  `start_ts` bigint(20) DEFAULT NULL COMMENT 'Span.timestamp(): epoch micros used for endTs query and to implement TTL',
  `duration` bigint(20) DEFAULT NULL COMMENT 'Span.duration(): micros used for minDuration and maxDuration query',
  UNIQUE KEY `trace_id_high` (`trace_id_high`,`trace_id`,`id`) COMMENT 'ignore insert on duplicate',
  KEY `trace_id_high_2` (`trace_id_high`,`trace_id`,`id`) COMMENT 'for joining with zipkin_annotations',
  KEY `trace_id_high_3` (`trace_id_high`,`trace_id`) COMMENT 'for getTracesByIds',
  KEY `name` (`name`) COMMENT 'for getTraces and getSpanNames',
  KEY `start_ts` (`start_ts`) COMMENT 'for getTraces ordering and range'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Adding row to indicate test data set identity
--
INSERT INTO zipkin_spans(trace_id, trace_id_high, id, name, parent_id)
VALUES(1, 1, 1, 'apollo-set3', 99) ;

--
-- Dumping data for table `zipkin_spans`
--

LOCK TABLES `zipkin_spans` WRITE;
/*!40000 ALTER TABLE `zipkin_spans` DISABLE KEYS */;
INSERT INTO `zipkin_spans` VALUES (0,-3952542349986978166,-7085456595534717338,'http:/burncpu',-3952542349986978166,NULL,1515237451784000,508000),(0,-3952542349986978166,-4766729690489789511,'http:/burncpu',2199402995387371259,NULL,1515237450773000,1009000),(0,-3952542349986978166,-3952542349986978166,'http:/invoke/three-cpu',NULL,NULL,1515237448746000,3549836),(0,-3952542349986978166,-3517567483243985447,'http:/burncpu',-7085456595534717338,NULL,1515237451784000,507000),(0,-3952542349986978166,919509467286965348,'http:/burncpu',3650612569389201682,NULL,1515237448750000,2021000),(0,-3952542349986978166,2199402995387371259,'http:/burncpu',-3952542349986978166,NULL,1515237450773000,1010000),(0,-3952542349986978166,3650612569389201682,'http:/burncpu',-3952542349986978166,NULL,1515237448750000,2022000),(0,-747967603336782775,-3268776777031956931,'http:/burncpu',4420130216367886776,NULL,1515237435865000,2848000),(0,-747967603336782775,-747967603336782775,'http:/invoke/single-cpu',NULL,NULL,1515237435811000,2925327),(0,-747967603336782775,4420130216367886776,'http:/burncpu',-747967603336782775,NULL,1515237435864000,2858000),(0,6109457896939656648,-5934756430793060449,'http:/burncpu',-4918951110302691249,NULL,1515237462312000,2508000),(0,6109457896939656648,-4918951110302691249,'http:/burncpu',6109457896939656648,NULL,1515237462312000,2509000),(0,6109457896939656648,6109457896939656648,'http:/invoke/single-cpu',NULL,NULL,1515237462308000,2514196);
/*!40000 ALTER TABLE `zipkin_spans` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-06 11:18:15
