-- phpMyAdmin SQL Dump
-- version 4.0.10.18
-- https://www.phpmyadmin.net
--
-- Host: localhost:3306
-- Generation Time: Mar 06, 2018 at 12:56 AM
-- Server version: 5.6.36-cll-lve
-- PHP Version: 5.6.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `oram`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE IF NOT EXISTS `admin` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `aemail` varchar(500) NOT NULL,
  `apassword` varchar(500) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id`, `aemail`, `apassword`) VALUES
(1, 'paddyabhyankar@gmail.com', '827ccb0eea8a706c4c34a16891f84e7b'),
(2, 'admin@gmail.com', '827ccb0eea8a706c4c34a16891f84e7b');

-- --------------------------------------------------------

--
-- Table structure for table `loclog`
--

CREATE TABLE IF NOT EXISTS `loclog` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `lat` text NOT NULL,
  `lon` text NOT NULL,
  `accident` text NOT NULL,
  `updatedon` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `loclog`
--

INSERT INTO `loclog` (`id`, `lat`, `lon`, `accident`, `updatedon`) VALUES
(1, '19.021112', '72.843338', 'B', '06-03-2018 13:25:22');

-- --------------------------------------------------------

--
-- Table structure for table `userdetails`
--

CREATE TABLE IF NOT EXISTS `userdetails` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(500) NOT NULL DEFAULT ' ',
  `password` varchar(500) NOT NULL DEFAULT ' ',
  `deviceid` varchar(500) NOT NULL DEFAULT ' ',
  `lat` varchar(500) NOT NULL DEFAULT '0 ',
  `lon` varchar(500) NOT NULL DEFAULT '0 ',
  `hasoccured` varchar(500) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=10 ;

--
-- Dumping data for table `userdetails`
--

INSERT INTO `userdetails` (`id`, `phone`, `password`, `deviceid`, `lat`, `lon`, `hasoccured`) VALUES
(4, '9321556637', 'd0f6153ae54846cd94504eba6efd1fce', 'abcd', '0 ', '0 ', '0'),
(3, '9619061760', 'cadbe462eb2615fb2f84663f348a66f0', ' abcd', '19.0607', '72.8362', '0'),
(2, '7666474990', 'e10adc3949ba59abbe56e057f20f883e', 'abcd', '19.0607', '72.8362', '1'),
(5, '9022573205', 'ee5a2c7c4ff0e123bc09d5676d694506', 'abcd', '0 ', '0 ', '0'),
(6, '7977242073', '303d8efa3637b797d945042651e00651', 'abcd', '0 ', '0 ', '0'),
(7, '9769834112', '3ae63708361dde5645e7b48cf5679956', 'abcd', '0 ', '0 ', '0'),
(8, '9967511958', 'bf709005906087dc1256bb4449d8774d', 'abcd', '0 ', '0 ', '0'),
(9, '8087555132', '391682ec849887cf2a4c5c2efceffbb0', 'abcd', '0 ', '0 ', '0');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
