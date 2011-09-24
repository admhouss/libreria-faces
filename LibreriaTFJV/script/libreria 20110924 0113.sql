-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.5.16


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema libreria
--

CREATE DATABASE IF NOT EXISTS libreria;
USE libreria;

--
-- Definition of table `actividadusuario`
--

DROP TABLE IF EXISTS `actividadusuario`;
CREATE TABLE `actividadusuario` (
  `ID_ACTIVIDAD` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_USUARIO` varchar(45) NOT NULL,
  `ACTIVIDAD` varchar(45) NOT NULL,
  `FECHA` datetime NOT NULL,
  PRIMARY KEY (`ID_ACTIVIDAD`),
  KEY `FK_actividadUsuario_usuario` (`ID_USUARIO`),
  CONSTRAINT `FK_actividadUsuario_usuario` FOREIGN KEY (`ID_USUARIO`) REFERENCES `usuarioadministrativo` (`ID_USUARIO`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `actividadusuario`
--

/*!40000 ALTER TABLE `actividadusuario` DISABLE KEYS */;
INSERT INTO `actividadusuario` (`ID_ACTIVIDAD`,`ID_USUARIO`,`ACTIVIDAD`,`FECHA`) VALUES 
 (1,'admin','SOPORTE AL SISTEMA','2011-09-15 02:00:00'),
 (3,'root','FUNCIONALIDAD AL SISTEMA','2011-09-23 02:00:00');
/*!40000 ALTER TABLE `actividadusuario` ENABLE KEYS */;


--
-- Definition of table `almacen`
--

DROP TABLE IF EXISTS `almacen`;
CREATE TABLE `almacen` (
  `ID_ARTICULO` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `EXISTENCIA` tinyint(1) unsigned NOT NULL,
  `EN_CONSIGNA` tinyint(1) unsigned NOT NULL,
  `EN_FIRME` tinyint(1) unsigned NOT NULL,
  PRIMARY KEY (`ID_ARTICULO`),
  CONSTRAINT `FK_almacen_articulo` FOREIGN KEY (`ID_ARTICULO`) REFERENCES `articulo` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `almacen`
--

/*!40000 ALTER TABLE `almacen` DISABLE KEYS */;
/*!40000 ALTER TABLE `almacen` ENABLE KEYS */;


--
-- Definition of table `articulo`
--

DROP TABLE IF EXISTS `articulo`;
CREATE TABLE `articulo` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `UNIDAD` int(10) unsigned NOT NULL,
  `DIVISA` varchar(50) NOT NULL,
  `COSTO` decimal(12,2) NOT NULL,
  `PRECIO_UNITARIO` decimal(12,2) NOT NULL,
  `DESCRIPCION` varchar(100) NOT NULL,
  `ID_PROVEEDOR` int(10) unsigned NOT NULL,
  `ID_TIPO` int(10) unsigned NOT NULL,
  `FECHA_REGISTRO` datetime NOT NULL,
  `ID_IDC` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_articulo_ID_IDC` (`ID_IDC`),
  KEY `FK_articulo_ID_PROVEEDOR` (`ID_PROVEEDOR`),
  KEY `FK_articulo_ID_TIPO` (`ID_TIPO`),
  CONSTRAINT `FK_articulo_ID_IDC` FOREIGN KEY (`ID_IDC`) REFERENCES `publicacion` (`ID_DC`),
  CONSTRAINT `FK_articulo_ID_PROVEEDOR` FOREIGN KEY (`ID_PROVEEDOR`) REFERENCES `proveedor` (`ID`),
  CONSTRAINT `FK_articulo_ID_TIPO` FOREIGN KEY (`ID_TIPO`) REFERENCES `tipo_articulo` (`ID`),
  CONSTRAINT `FK_articulo_proveedor` FOREIGN KEY (`ID_PROVEEDOR`) REFERENCES `proveedor` (`ID`),
  CONSTRAINT `FK_articulo_publicacion` FOREIGN KEY (`ID_IDC`) REFERENCES `publicacion` (`ID_DC`),
  CONSTRAINT `FK_articulo_tipo` FOREIGN KEY (`ID_TIPO`) REFERENCES `tipo_articulo` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `articulo`
--

/*!40000 ALTER TABLE `articulo` DISABLE KEYS */;
INSERT INTO `articulo` (`ID`,`UNIDAD`,`DIVISA`,`COSTO`,`PRECIO_UNITARIO`,`DESCRIPCION`,`ID_PROVEEDOR`,`ID_TIPO`,`FECHA_REGISTRO`,`ID_IDC`) VALUES 
 (1,2,'MEXICO','232.00','32.00','DESS',3,1,'2011-09-14 02:00:00',16),
 (2,12,'Brazil','232.00','23.00','dsdsd',1,7,'2011-09-29 02:00:00',14),
 (3,32,'PUERTO RICO','12.00','23.00','SDSD',2,5,'2011-09-30 02:00:00',14),
 (4,212,'CANADA','2323.00','23.00','DES',5,6,'2011-09-13 02:00:00',19);
/*!40000 ALTER TABLE `articulo` ENABLE KEYS */;


--
-- Definition of table `bitacora_cliente`
--

DROP TABLE IF EXISTS `bitacora_cliente`;
CREATE TABLE `bitacora_cliente` (
  `ID_MOVIMIENTO` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_CLIENTE` varchar(45) NOT NULL,
  `ID_ARTICULO` int(10) unsigned NOT NULL,
  `FECHA` datetime NOT NULL,
  `HORA` varchar(45) NOT NULL,
  `ESTADO` tinyint(1) unsigned NOT NULL,
  PRIMARY KEY (`ID_MOVIMIENTO`,`ID_CLIENTE`,`ID_ARTICULO`),
  KEY `FK_bitacora_cliente_cliente` (`ID_CLIENTE`),
  KEY `FK_bitacora_cliente_articulo` (`ID_ARTICULO`),
  CONSTRAINT `FK_bitacora_cliente_articulo` FOREIGN KEY (`ID_ARTICULO`) REFERENCES `articulo` (`ID`),
  CONSTRAINT `FK_bitacora_cliente_cliente` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `cliente` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bitacora_cliente`
--

/*!40000 ALTER TABLE `bitacora_cliente` DISABLE KEYS */;
/*!40000 ALTER TABLE `bitacora_cliente` ENABLE KEYS */;


--
-- Definition of table `bitacora_consulta`
--

DROP TABLE IF EXISTS `bitacora_consulta`;
CREATE TABLE `bitacora_consulta` (
  `ID_MOVIMIENTO` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_ARTICULO` int(10) unsigned NOT NULL,
  `ESTADO` tinyint(1) unsigned NOT NULL,
  `FECHA` datetime NOT NULL,
  `HORA` varchar(45) NOT NULL,
  PRIMARY KEY (`ID_MOVIMIENTO`),
  KEY `FK_bitacora_consulta_articulo` (`ID_ARTICULO`),
  CONSTRAINT `FK_bitacora_consulta_articulo` FOREIGN KEY (`ID_ARTICULO`) REFERENCES `articulo` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bitacora_consulta`
--

/*!40000 ALTER TABLE `bitacora_consulta` DISABLE KEYS */;
/*!40000 ALTER TABLE `bitacora_consulta` ENABLE KEYS */;


--
-- Definition of table `categoria_cliente`
--

DROP TABLE IF EXISTS `categoria_cliente`;
CREATE TABLE `categoria_cliente` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_CLIENTE` varchar(45) NOT NULL,
  `CONCEPTO` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_categoria_cliente_cliente` (`ID_CLIENTE`),
  CONSTRAINT `FK_categoria_cliente_cliente` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `cliente` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `categoria_cliente`
--

/*!40000 ALTER TABLE `categoria_cliente` DISABLE KEYS */;
/*!40000 ALTER TABLE `categoria_cliente` ENABLE KEYS */;


--
-- Definition of table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
CREATE TABLE `cliente` (
  `ID` varchar(100) NOT NULL,
  `NOMBRE` varchar(45) NOT NULL,
  `PATERNO` varchar(45) NOT NULL,
  `MATERNO` varchar(45) NOT NULL,
  `TELEFONO` varchar(45) NOT NULL,
  `FAX` varchar(45) DEFAULT NULL,
  `EMAIL` varchar(45) NOT NULL,
  `ESTATUS` tinyint(1) unsigned NOT NULL,
  `MODS` varchar(45) NOT NULL,
  `ID_ESTADO` int(10) unsigned NOT NULL,
  `PASSWORD` varchar(45) NOT NULL,
  `FECHA_ALTA` datetime NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_cliente_estado` (`ID_ESTADO`),
  CONSTRAINT `FK_cliente_estado` FOREIGN KEY (`ID_ESTADO`) REFERENCES `estado` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cliente`
--

/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` (`ID`,`NOMBRE`,`PATERNO`,`MATERNO`,`TELEFONO`,`FAX`,`EMAIL`,`ESTATUS`,`MODS`,`ID_ESTADO`,`PASSWORD`,`FECHA_ALTA`) VALUES 
 ('1010','JUAN ','XXX','SDD','DSD','DSDS','SDSD',1,'sdd',1,'2323','2011-09-22 12:15:08'),
 ('12','YAMIL OMAR','DELGADO','GONZALEZ','555555','34DF','yamildelgado@hotmail.com',1,'SDFS',1,'DSDAD','2011-09-22 11:56:56');
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;


--
-- Definition of table `compra`
--

DROP TABLE IF EXISTS `compra`;
CREATE TABLE `compra` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_CLIENTE` varchar(45) NOT NULL,
  `ID_PEDIDO` int(10) unsigned NOT NULL,
  `FECHA_ENVIO` datetime NOT NULL,
  `TIPO_ENVIO` varchar(45) NOT NULL,
  `PAGO_TOTAL` decimal(12,2) NOT NULL,
  `ESTADO` varchar(45) NOT NULL,
  `OBSERVACIONES` varchar(45) NOT NULL,
  `FECHA` datetime NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_compra_cliente` (`ID_CLIENTE`),
  KEY `FK_compra_pedido` (`ID_PEDIDO`),
  CONSTRAINT `FK_compra_cliente` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `cliente` (`ID`),
  CONSTRAINT `FK_compra_pedido` FOREIGN KEY (`ID_PEDIDO`) REFERENCES `pedido` (`ID_PEDIDO`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `compra`
--

/*!40000 ALTER TABLE `compra` DISABLE KEYS */;
/*!40000 ALTER TABLE `compra` ENABLE KEYS */;


--
-- Definition of table `contacto`
--

DROP TABLE IF EXISTS `contacto`;
CREATE TABLE `contacto` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_CLIENTE` varchar(45) NOT NULL,
  `NOMBRE` varchar(45) NOT NULL,
  `PATERNO` varchar(45) NOT NULL,
  `MATERNO` varchar(45) NOT NULL,
  `PUESTO` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_contacto_cliente` (`ID_CLIENTE`),
  CONSTRAINT `FK_contacto_cliente` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `cliente` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `contacto`
--

/*!40000 ALTER TABLE `contacto` DISABLE KEYS */;
INSERT INTO `contacto` (`ID`,`ID_CLIENTE`,`NOMBRE`,`PATERNO`,`MATERNO`,`PUESTO`) VALUES 
 (1,'1010','NESTOR','GONZALEZ','GONZALEZ','PROGRAMADOR');
/*!40000 ALTER TABLE `contacto` ENABLE KEYS */;


--
-- Definition of table `descuento`
--

DROP TABLE IF EXISTS `descuento`;
CREATE TABLE `descuento` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `PORCENTAJE` decimal(12,2) NOT NULL,
  `TIPO_DESCUENTO` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `descuento`
--

/*!40000 ALTER TABLE `descuento` DISABLE KEYS */;
/*!40000 ALTER TABLE `descuento` ENABLE KEYS */;


--
-- Definition of table `descuento_articulo`
--

DROP TABLE IF EXISTS `descuento_articulo`;
CREATE TABLE `descuento_articulo` (
  `ID_ARTICULO` int(10) unsigned NOT NULL,
  `descuento` decimal(12,2) NOT NULL,
  PRIMARY KEY (`ID_ARTICULO`) USING BTREE,
  CONSTRAINT `FK_descuento_articulo_articulo` FOREIGN KEY (`ID_ARTICULO`) REFERENCES `articulo` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `descuento_articulo`
--

/*!40000 ALTER TABLE `descuento_articulo` DISABLE KEYS */;
/*!40000 ALTER TABLE `descuento_articulo` ENABLE KEYS */;


--
-- Definition of table `descuento_cliente`
--

DROP TABLE IF EXISTS `descuento_cliente`;
CREATE TABLE `descuento_cliente` (
  `ID_CLIENTE` varchar(45) NOT NULL,
  `ID_DESCUENTO` int(10) unsigned NOT NULL,
  `FECHA_INICIO` datetime NOT NULL,
  `FECHA_FIN` datetime NOT NULL,
  PRIMARY KEY (`ID_CLIENTE`,`ID_DESCUENTO`),
  KEY `FK_descuento_cliente_descuento` (`ID_DESCUENTO`),
  CONSTRAINT `FK_descuento_cliente_cliente` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `cliente` (`ID`),
  CONSTRAINT `FK_descuento_cliente_descuento` FOREIGN KEY (`ID_DESCUENTO`) REFERENCES `descuento` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `descuento_cliente`
--

/*!40000 ALTER TABLE `descuento_cliente` DISABLE KEYS */;
/*!40000 ALTER TABLE `descuento_cliente` ENABLE KEYS */;


--
-- Definition of table `difacturacion`
--

DROP TABLE IF EXISTS `difacturacion`;
CREATE TABLE `difacturacion` (
  `RFC` varchar(45) NOT NULL,
  `ID_CLIENTE` varchar(45) NOT NULL,
  `RAZON_SOCIAL` varchar(100) NOT NULL,
  `CALLE` varchar(100) NOT NULL,
  `NO_EXTERIOR` varchar(45) NOT NULL,
  `NO_INTERIOR` varchar(45) NOT NULL,
  `COLONIA` varchar(45) NOT NULL,
  `DEL_MUNICIPIO` varchar(45) NOT NULL,
  `CIUDAD` varchar(45) NOT NULL,
  `CP` int(10) unsigned NOT NULL,
  `TELEFONO` varchar(45) NOT NULL,
  PRIMARY KEY (`RFC`),
  KEY `FK_diFacturacion_cliente` (`ID_CLIENTE`),
  CONSTRAINT `FK_diFacturacion_cliente` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `cliente` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `difacturacion`
--

/*!40000 ALTER TABLE `difacturacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `difacturacion` ENABLE KEYS */;


--
-- Definition of table `direnvio`
--

DROP TABLE IF EXISTS `direnvio`;
CREATE TABLE `direnvio` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_CLIENTE` varchar(45) NOT NULL,
  `ATENCION` varchar(45) NOT NULL,
  `CALLE` varchar(45) NOT NULL,
  `NO_EXTERIOR` varchar(45) NOT NULL,
  `NO_INTERIOR` varchar(45) NOT NULL,
  `COLONIA` varchar(100) NOT NULL,
  `DEL_MUNICIPIO` varchar(45) NOT NULL,
  `CIUDAD` varchar(45) NOT NULL,
  `CP` int(10) unsigned NOT NULL,
  `REFERENCIA` varchar(100) NOT NULL,
  `TELEFONO` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_dirEnvio_cliente` (`ID_CLIENTE`),
  CONSTRAINT `FK_dirEnvio_cliente` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `cliente` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `direnvio`
--

/*!40000 ALTER TABLE `direnvio` DISABLE KEYS */;
INSERT INTO `direnvio` (`ID`,`ID_CLIENTE`,`ATENCION`,`CALLE`,`NO_EXTERIOR`,`NO_INTERIOR`,`COLONIA`,`DEL_MUNICIPIO`,`CIUDAD`,`CP`,`REFERENCIA`,`TELEFONO`) VALUES 
 (4,'1010','Atencion','sadsad','232','2323','sd','sadsa','sads',2323,'sdad','sadsad');
/*!40000 ALTER TABLE `direnvio` ENABLE KEYS */;


--
-- Definition of table `envio_fisico`
--

DROP TABLE IF EXISTS `envio_fisico`;
CREATE TABLE `envio_fisico` (
  `ID_PEDIDO` int(10) unsigned NOT NULL,
  `ID_ARTICULO` int(10) unsigned NOT NULL,
  `ID_DIR` int(10) unsigned NOT NULL,
  `NO_GUIA` int(10) unsigned NOT NULL,
  `PAQUETERIA` varchar(45) NOT NULL,
  `OBSERVACIONES` varchar(45) NOT NULL,
  PRIMARY KEY (`ID_PEDIDO`,`ID_ARTICULO`),
  KEY `FK_envio_fisico_articulo` (`ID_ARTICULO`),
  KEY `FK_envio_fisico_dirEnvio` (`ID_DIR`),
  CONSTRAINT `FK_envio_fisico_articulo` FOREIGN KEY (`ID_ARTICULO`) REFERENCES `articulo` (`ID`),
  CONSTRAINT `FK_envio_fisico_dirEnvio` FOREIGN KEY (`ID_DIR`) REFERENCES `direnvio` (`ID`),
  CONSTRAINT `FK_envio_fisico_pedido` FOREIGN KEY (`ID_PEDIDO`) REFERENCES `pedido` (`ID_PEDIDO`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `envio_fisico`
--

/*!40000 ALTER TABLE `envio_fisico` DISABLE KEYS */;
/*!40000 ALTER TABLE `envio_fisico` ENABLE KEYS */;


--
-- Definition of table `envioelectronico`
--

DROP TABLE IF EXISTS `envioelectronico`;
CREATE TABLE `envioelectronico` (
  `ID_PEDIDO` int(10) unsigned NOT NULL,
  `ID_ARTICULO` int(10) unsigned NOT NULL,
  `LIGA_DESCARGA` varchar(100) NOT NULL,
  `OBSERVACIONES` varchar(100) NOT NULL,
  PRIMARY KEY (`ID_PEDIDO`,`ID_ARTICULO`),
  KEY `FK_envioElectronico_articulo` (`ID_ARTICULO`),
  CONSTRAINT `FK_envioElectronico_articulo` FOREIGN KEY (`ID_ARTICULO`) REFERENCES `articulo` (`ID`),
  CONSTRAINT `FK_envioElectronico_pedido` FOREIGN KEY (`ID_PEDIDO`, `ID_ARTICULO`) REFERENCES `pedido` (`ID_PEDIDO`, `ID_ARTICULO`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `envioelectronico`
--

/*!40000 ALTER TABLE `envioelectronico` DISABLE KEYS */;
/*!40000 ALTER TABLE `envioelectronico` ENABLE KEYS */;


--
-- Definition of table `enviorealizado`
--

DROP TABLE IF EXISTS `enviorealizado`;
CREATE TABLE `enviorealizado` (
  `ID_PEDIDO` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `FECHA_RECIBO` datetime NOT NULL,
  `OBSERVACIONES` varchar(50) NOT NULL,
  PRIMARY KEY (`ID_PEDIDO`),
  CONSTRAINT `FK_enviorealizado_pedido` FOREIGN KEY (`ID_PEDIDO`) REFERENCES `pedido` (`ID_PEDIDO`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `enviorealizado`
--

/*!40000 ALTER TABLE `enviorealizado` DISABLE KEYS */;
/*!40000 ALTER TABLE `enviorealizado` ENABLE KEYS */;


--
-- Definition of table `estado`
--

DROP TABLE IF EXISTS `estado`;
CREATE TABLE `estado` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `estado`
--

/*!40000 ALTER TABLE `estado` DISABLE KEYS */;
INSERT INTO `estado` (`ID`,`NOMBRE`) VALUES 
 (1,'ACTIVADO'),
 (2,'DESACTIVADO');
/*!40000 ALTER TABLE `estado` ENABLE KEYS */;


--
-- Definition of table `factura`
--

DROP TABLE IF EXISTS `factura`;
CREATE TABLE `factura` (
  `FOLIO` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `RFC` varchar(45) NOT NULL,
  `ID_COMPRA` int(10) unsigned NOT NULL,
  PRIMARY KEY (`FOLIO`,`RFC`),
  KEY `FK_factura_compra` (`ID_COMPRA`),
  KEY `FK_factura_dirfactura` (`RFC`),
  CONSTRAINT `FK_factura_compra` FOREIGN KEY (`ID_COMPRA`) REFERENCES `compra` (`ID`),
  CONSTRAINT `FK_factura_dirfactura` FOREIGN KEY (`RFC`) REFERENCES `difacturacion` (`RFC`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `factura`
--

/*!40000 ALTER TABLE `factura` DISABLE KEYS */;
/*!40000 ALTER TABLE `factura` ENABLE KEYS */;


--
-- Definition of table `factura_general`
--

DROP TABLE IF EXISTS `factura_general`;
CREATE TABLE `factura_general` (
  `FOLIO_GENERAL` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_COMPRA` int(10) unsigned NOT NULL,
  `FECHA` datetime NOT NULL,
  PRIMARY KEY (`FOLIO_GENERAL`),
  KEY `FK_factura_general_compra` (`ID_COMPRA`),
  CONSTRAINT `FK_factura_general_compra` FOREIGN KEY (`ID_COMPRA`) REFERENCES `compra` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `factura_general`
--

/*!40000 ALTER TABLE `factura_general` DISABLE KEYS */;
/*!40000 ALTER TABLE `factura_general` ENABLE KEYS */;


--
-- Definition of table `impuesto`
--

DROP TABLE IF EXISTS `impuesto`;
CREATE TABLE `impuesto` (
  `ID_ARTICULO` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `MONTO_IMPUESTO` decimal(12,2) NOT NULL,
  `DESCRIPCION` varchar(100) NOT NULL,
  PRIMARY KEY (`ID_ARTICULO`),
  CONSTRAINT `FK_impuesto_articulo` FOREIGN KEY (`ID_ARTICULO`) REFERENCES `articulo` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `impuesto`
--

/*!40000 ALTER TABLE `impuesto` DISABLE KEYS */;
/*!40000 ALTER TABLE `impuesto` ENABLE KEYS */;


--
-- Definition of table `pedido`
--

DROP TABLE IF EXISTS `pedido`;
CREATE TABLE `pedido` (
  `ID_PEDIDO` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_ARTICULO` int(10) unsigned NOT NULL,
  `NO_ARTICULO_CATEGORIA` int(10) unsigned NOT NULL,
  `CATEGORIA` varchar(45) NOT NULL,
  PRIMARY KEY (`ID_PEDIDO`,`ID_ARTICULO`),
  KEY `FK_pedido_articulo` (`ID_ARTICULO`),
  CONSTRAINT `FK_pedido_articulo` FOREIGN KEY (`ID_ARTICULO`) REFERENCES `articulo` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pedido`
--

/*!40000 ALTER TABLE `pedido` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedido` ENABLE KEYS */;


--
-- Definition of table `pendiente`
--

DROP TABLE IF EXISTS `pendiente`;
CREATE TABLE `pendiente` (
  `ID_ARTICULO` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NO_ART_SOLICITADOS` int(10) unsigned NOT NULL,
  `FECHA` datetime NOT NULL,
  `OBSERVACONES` varchar(100) NOT NULL,
  `ID_CLIENTE` varchar(45) NOT NULL,
  PRIMARY KEY (`ID_ARTICULO`),
  KEY `FK_pendiente_cliente` (`ID_CLIENTE`),
  CONSTRAINT `FK_pendiente_articulo` FOREIGN KEY (`ID_ARTICULO`) REFERENCES `articulo` (`ID`),
  CONSTRAINT `FK_pendiente_cliente` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `cliente` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pendiente`
--

/*!40000 ALTER TABLE `pendiente` DISABLE KEYS */;
/*!40000 ALTER TABLE `pendiente` ENABLE KEYS */;


--
-- Definition of table `promocion`
--

DROP TABLE IF EXISTS `promocion`;
CREATE TABLE `promocion` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_ARTICULO` int(10) unsigned NOT NULL,
  `PRECIO_PUBLICO` decimal(12,2) NOT NULL,
  `DIA_INICIO` datetime NOT NULL,
  `DIA_FIN` datetime NOT NULL,
  PRIMARY KEY (`ID`,`ID_ARTICULO`),
  KEY `FK_promocion_articulo` (`ID_ARTICULO`),
  CONSTRAINT `FK_promocion_articulo` FOREIGN KEY (`ID_ARTICULO`) REFERENCES `articulo` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `promocion`
--

/*!40000 ALTER TABLE `promocion` DISABLE KEYS */;
INSERT INTO `promocion` (`ID`,`ID_ARTICULO`,`PRECIO_PUBLICO`,`DIA_INICIO`,`DIA_FIN`) VALUES 
 (26,3,'101.00','2011-09-02 02:00:00','2011-09-30 02:00:00');
/*!40000 ALTER TABLE `promocion` ENABLE KEYS */;


--
-- Definition of table `proveedor`
--

DROP TABLE IF EXISTS `proveedor`;
CREATE TABLE `proveedor` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `proveedor`
--

/*!40000 ALTER TABLE `proveedor` DISABLE KEYS */;
INSERT INTO `proveedor` (`ID`,`NOMBRE`) VALUES 
 (1,'PORRRUA'),
 (2,'GOOGLE'),
 (3,'BIBLIOTÈQUE NATIONALE DE FRANCE'),
 (5,'LIBROS DE CHILE PARA EL MUNDO'),
 (10,'YAMIL');
/*!40000 ALTER TABLE `proveedor` ENABLE KEYS */;


--
-- Definition of table `publicacion`
--

DROP TABLE IF EXISTS `publicacion`;
CREATE TABLE `publicacion` (
  `ID_DC` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `PERIODO_MES` datetime NOT NULL,
  `PERIODO_ANIO` datetime NOT NULL,
  `EPOCA` varchar(45) NOT NULL,
  `ANIO` varchar(45) NOT NULL,
  `NUMERO` int(10) unsigned NOT NULL,
  `ISSN` int(10) unsigned NOT NULL,
  `ISBN` varchar(100) NOT NULL,
  `TOMO` varchar(45) NOT NULL,
  `EDITORIAL` varchar(100) NOT NULL,
  PRIMARY KEY (`ID_DC`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `publicacion`
--

/*!40000 ALTER TABLE `publicacion` DISABLE KEYS */;
INSERT INTO `publicacion` (`ID_DC`,`PERIODO_MES`,`PERIODO_ANIO`,`EPOCA`,`ANIO`,`NUMERO`,`ISSN`,`ISBN`,`TOMO`,`EDITORIAL`) VALUES 
 (10,'2011-09-16 02:00:00','2011-10-12 02:00:00','aadsd','1232',12345678,2323,'ISBN','sffd','YAMIL'),
 (14,'2011-09-13 02:00:00','2011-09-06 02:00:00','SDS','1212',12,12,'SDSD','23','2323'),
 (16,'2011-09-16 02:00:00','2011-09-12 02:00:00','ochenta','323',2323,2323,'323','323','sdsd'),
 (17,'2011-09-16 02:00:00','2011-09-12 02:00:00','ochenta','323',2323,2323,'323','323','sdsd'),
 (19,'2011-09-23 02:00:00','2011-09-22 02:00:00','clasica','212',23,23232,'2323','rom','ewsd');
/*!40000 ALTER TABLE `publicacion` ENABLE KEYS */;


--
-- Definition of table `suscripcion`
--

DROP TABLE IF EXISTS `suscripcion`;
CREATE TABLE `suscripcion` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_ARTICULO` int(10) unsigned NOT NULL,
  `ID_DC` int(10) unsigned NOT NULL,
  `NUMERO` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_suscripcion_articulo` (`ID_ARTICULO`),
  KEY `FK_suscripcion_promocion` (`ID_DC`),
  CONSTRAINT `FK_suscripcion_articulo` FOREIGN KEY (`ID_ARTICULO`) REFERENCES `articulo` (`ID`),
  CONSTRAINT `FK_suscripcion_promocion` FOREIGN KEY (`ID_DC`) REFERENCES `publicacion` (`ID_DC`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `suscripcion`
--

/*!40000 ALTER TABLE `suscripcion` DISABLE KEYS */;
/*!40000 ALTER TABLE `suscripcion` ENABLE KEYS */;


--
-- Definition of table `suscripcion_cliente`
--

DROP TABLE IF EXISTS `suscripcion_cliente`;
CREATE TABLE `suscripcion_cliente` (
  `ID_SUSCRIPCION` int(10) unsigned NOT NULL,
  `ID_CLIENTE` varchar(45) NOT NULL,
  `ID_DC` int(10) unsigned NOT NULL,
  `ESTADO` tinyint(1) unsigned NOT NULL,
  `FECHA_ENVIO` datetime NOT NULL,
  PRIMARY KEY (`ID_SUSCRIPCION`,`ID_CLIENTE`),
  KEY `FK_suscripcion_cliente_cliente` (`ID_CLIENTE`),
  KEY `FK_suscripcion_cliente_publicacion` (`ID_DC`),
  CONSTRAINT `FK_suscripcion_cliente_cliente` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `cliente` (`ID`),
  CONSTRAINT `FK_suscripcion_cliente_publicacion` FOREIGN KEY (`ID_DC`) REFERENCES `publicacion` (`ID_DC`),
  CONSTRAINT `FK_suscripcion_cliente_suscripcion` FOREIGN KEY (`ID_SUSCRIPCION`) REFERENCES `suscripcion` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `suscripcion_cliente`
--

/*!40000 ALTER TABLE `suscripcion_cliente` DISABLE KEYS */;
/*!40000 ALTER TABLE `suscripcion_cliente` ENABLE KEYS */;


--
-- Definition of table `suscripcion_envios`
--

DROP TABLE IF EXISTS `suscripcion_envios`;
CREATE TABLE `suscripcion_envios` (
  `ID_PEDIDO` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_SUSCRIPCION` int(10) unsigned NOT NULL,
  `ESTADO_ENVIO` tinyint(1) unsigned NOT NULL,
  `OBSERVACIONES` varchar(50) NOT NULL,
  PRIMARY KEY (`ID_PEDIDO`,`ID_SUSCRIPCION`),
  KEY `FK_suscripcion_envios_suscripcion` (`ID_SUSCRIPCION`),
  CONSTRAINT `FK_suscripcion_envios_pedido` FOREIGN KEY (`ID_PEDIDO`) REFERENCES `pedido` (`ID_PEDIDO`),
  CONSTRAINT `FK_suscripcion_envios_suscripcion` FOREIGN KEY (`ID_SUSCRIPCION`) REFERENCES `suscripcion` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `suscripcion_envios`
--

/*!40000 ALTER TABLE `suscripcion_envios` DISABLE KEYS */;
/*!40000 ALTER TABLE `suscripcion_envios` ENABLE KEYS */;


--
-- Definition of table `tipo_articulo`
--

DROP TABLE IF EXISTS `tipo_articulo`;
CREATE TABLE `tipo_articulo` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `DESCRIPCION` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tipo_articulo`
--

/*!40000 ALTER TABLE `tipo_articulo` DISABLE KEYS */;
INSERT INTO `tipo_articulo` (`ID`,`DESCRIPCION`) VALUES 
 (1,'LIBRO '),
 (5,'PELOTA'),
 (6,'CELULAR'),
 (7,'CUADERNO');
/*!40000 ALTER TABLE `tipo_articulo` ENABLE KEYS */;


--
-- Definition of table `usuarioadministrativo`
--

DROP TABLE IF EXISTS `usuarioadministrativo`;
CREATE TABLE `usuarioadministrativo` (
  `ID_USUARIO` varchar(45) NOT NULL,
  `NOMBRE` varchar(45) NOT NULL,
  `PATERNO` varchar(45) NOT NULL,
  `MATERNO` varchar(45) NOT NULL,
  `PASSWORD` varchar(45) NOT NULL,
  `CARGO` varchar(100) NOT NULL,
  PRIMARY KEY (`ID_USUARIO`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `usuarioadministrativo`
--

/*!40000 ALTER TABLE `usuarioadministrativo` DISABLE KEYS */;
INSERT INTO `usuarioadministrativo` (`ID_USUARIO`,`NOMBRE`,`PATERNO`,`MATERNO`,`PASSWORD`,`CARGO`) VALUES 
 ('admin','YAMIL OMAR','DELGADO','GONZALEZ','ADMIN','ADMINISTRADOR'),
 ('alma123','ALMA ','CABOS','MARTINEZ','ASAS','SECRETARIA'),
 ('root','JARIZ ','DELGADO','GONZALEZ','ADMINROOT','PROGRAMADOR');
/*!40000 ALTER TABLE `usuarioadministrativo` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
