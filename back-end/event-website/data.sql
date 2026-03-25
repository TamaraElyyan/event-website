-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.40 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping structure for table event_db.city
CREATE TABLE IF NOT EXISTS `city` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `name_ar` varchar(255) NOT NULL,
  `name_en` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_cdpacmkvbejr8ec0ix7akx0f7` (`name_ar`),
  UNIQUE KEY `UK_3swjsvmhmyas7a93vh4hf66qv` (`name_en`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.city: ~0 rows (approximately)
INSERT INTO `city` (`id`, `deleted`, `name_ar`, `name_en`) VALUES
	(1, b'0', 'جنين', 'Jeninin');

-- Dumping structure for table event_db.contact
CREATE TABLE IF NOT EXISTS `contact` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `email` varchar(100) NOT NULL DEFAULT '',
  `message` tinytext NOT NULL,
  `name` varchar(100) NOT NULL DEFAULT '',
  `phone` varchar(100) NOT NULL DEFAULT '',
  `replay` tinytext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.contact: ~1 rows (approximately)
INSERT INTO `contact` (`id`, `deleted`, `email`, `message`, `name`, `phone`, `replay`) VALUES
	(1, b'0', 'john.doe@example.com', 'This is a test message.', 'IsraaMazataa Doe', '+1234567890', NULL),
	(2, b'0', 'john.doe@example.com', 'This is a test message.', 'IsraaMazataa Doe', '+1234567890', NULL),
	(3, b'0', 'Tamara@t.com', '123', 'elyyan', '0562667777', NULL),
	(4, b'0', 'Tamara@t.com', 'test2', 'elyyan', '0562667777', NULL),
	(5, b'0', 'Tamara@t.com', 'test025', 'elyyan', '0562667777', NULL);

-- Dumping structure for table event_db.instructor
CREATE TABLE IF NOT EXISTS `instructor` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `email` varchar(255) NOT NULL,
  `job_title` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_cr0g7gh88hv7sfdx9kqbrbiyw` (`user_id`),
  UNIQUE KEY `UK_h23sspwe1nutb1hkq9rvyh8h8` (`email`),
  CONSTRAINT `FKpyhf3fgtvlqq630u3697wsmre` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.instructor: ~0 rows (approximately)
INSERT INTO `instructor` (`id`, `deleted`, `email`, `job_title`, `name`, `user_id`) VALUES
	(1, b'0', 'johndoe@example.com', 'Senior Instructor', 'John Doe', 48);

-- Dumping structure for table event_db.organization
CREATE TABLE IF NOT EXISTS `organization` (
  `id` int NOT NULL AUTO_INCREMENT,
  `contact_email` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `description_picture` varchar(250) DEFAULT NULL,
  `organization_name` varchar(255) NOT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  `website_url` varchar(255) DEFAULT NULL,
  `user_id` int NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_q13wkhwj7885gg61lwiyanwis` (`user_id`),
  UNIQUE KEY `UK_pw8gr9xtgl634fnv4tvqev5nn` (`organization_name`),
  UNIQUE KEY `UK_8thvnct556hs6ewck9ibsv82e` (`contact_email`),
  CONSTRAINT `FKq0435723w14233u7xu6r92xev` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.organization: ~1 rows (approximately)
INSERT INTO `organization` (`id`, `contact_email`, `deleted`, `description_picture`, `organization_name`, `phone_number`, `website_url`, `user_id`, `description`) VALUES
	(1, 'JHF@techinnovators.com', b'0', '/static/pictures/878534d8-628e-482b-bb6d-4993ca24cc9f.png', 'JHF .', '+1234567890', 'https://www.techinnovators.com', 45, 'East Jerusalem becomes a Hi-tech destination competing at the global level and empowered by a dynamic, collaborative & thriving community'),
	(2, 'Gaza@GazaSkyGeeks.com', b'0', '/static/pictures/4451a1bb-1a66-4841-bf12-e0c5fe5c2ead.png', 'GazaSkyGeeks .', '+1234567890', 'https://gazaskygeeks.com', 46, 'Since 2011, we have been propelling Palestine’s digital economy and future technology leaders to transcend borders through cutting edge peer-led education and true grit.');

-- Dumping structure for table event_db.picture
CREATE TABLE IF NOT EXISTS `picture` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `listing_id` int NOT NULL,
  `upload_date` datetime(6) NOT NULL,
  `url` varchar(250) NOT NULL,
  `is_user` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.picture: ~34 rows (approximately)
INSERT INTO `picture` (`id`, `deleted`, `listing_id`, `upload_date`, `url`, `is_user`) VALUES
	(1, b'0', 1, '2025-01-27 11:41:37.583195', '/static/pictures/499bd684-53eb-43ad-aa3c-9513737a7ef4.png', b'1'),
	(2, b'0', 3, '2025-01-31 14:04:58.321155', '/static/pictures/css.png', b'0'),
	(3, b'0', 4, '2025-01-31 14:05:14.379192', '/static/pictures/html.png', b'0'),
	(4, b'0', 2, '2025-01-31 14:29:27.203535', '/static/pictures/cac893a2-ae36-4bf5-9dd0-064347cf8c64.png', b'1'),
	(5, b'0', 3, '2025-01-31 14:51:43.738779', '/static/pictures/1a6aad1f-15a3-46c5-9d51-ebab74f4d911.png', b'1'),
	(6, b'0', 7, '2025-01-31 14:55:31.276297', '/static/pictures/4c9734c0-f3ce-476b-93e4-738dc4c22f70.png', b'1'),
	(7, b'0', 33, '2025-01-31 15:01:00.174986', '/static/pictures/29b4f951-b2b8-4bbe-82e7-e9f34d5e70bd.png', b'1'),
	(8, b'0', 8, '2025-01-31 15:01:00.207767', '/static/pictures/42f131bc-7575-48cf-aeed-ad2f3e96fa31.png', b'1'),
	(9, b'0', 34, '2025-01-31 15:08:13.468270', '/static/pictures/ebb81b5d-181e-491d-87e4-3b38b1b0b306.png', b'1'),
	(10, b'0', 9, '2025-01-31 15:08:13.528122', '/static/pictures/7a6e03a0-79d4-4b19-894a-bc5cbd5ce040.png', b'1'),
	(11, b'0', 35, '2025-01-31 15:09:09.297820', '/static/pictures/cfa33797-0f2c-40bf-8e79-bb4d23569253.png', b'1'),
	(12, b'0', 10, '2025-01-31 15:09:09.331079', '/static/pictures/b17ce39a-7304-4de3-b803-8a74f302aba9.png', b'1'),
	(13, b'0', 36, '2025-01-31 15:09:36.735440', '/static/pictures/c0298302-8ffa-40fc-855f-1425fc89feb5', b'1'),
	(14, b'0', 11, '2025-01-31 15:09:36.755597', '/static/pictures/0c966d61-adce-4ac1-b125-670dac2ef30b', b'1'),
	(15, b'0', 38, '2025-01-31 15:10:14.265254', '/static/pictures/68ec4776-ae4a-467c-aa6c-d5e2ac8b0f30', b'1'),
	(16, b'0', 12, '2025-01-31 15:10:14.285162', '/static/pictures/3e93080b-33cc-4400-bcbe-50f8d1176a0e', b'1'),
	(17, b'0', 39, '2025-01-31 15:11:45.520119', '/static/pictures/45d3c9be-8321-461b-86ce-5fcdf713b574', b'1'),
	(18, b'0', 13, '2025-01-31 15:11:45.555483', '/static/pictures/e37d0e8b-51fa-4b34-9bd8-7906779ab06f', b'1'),
	(19, b'0', 42, '2025-01-31 15:15:03.346785', '/static/pictures/671c62c0-5204-40ee-8708-9ceb5836fd6d', b'1'),
	(20, b'0', 14, '2025-01-31 15:15:03.428390', '/static/pictures/9a43119f-f4c9-4df0-b69f-76fe52421bdc', b'1'),
	(21, b'0', 45, '2025-01-31 15:48:52.311601', '/static/pictures/6d4b2f54-7b68-4a2b-af1d-a898cdd859a3.png', b'1'),
	(22, b'0', 1, '2025-01-31 15:48:52.335870', '/static/pictures/java.png', b'0'),
	(23, b'0', 46, '2025-01-31 16:09:41.471347', '/static/pictures/e49b6f5a-2eb6-4f02-8677-f88fb6ea29f6.png', b'1'),
	(24, b'0', 2, '2025-01-31 16:09:41.508870', '/static/pictures/React-icon.png', b'0'),
	(25, b'0', 12, '2025-02-01 02:42:08.823318', '/static/pictures/c5ab807f-7f70-4390-bae2-f2010bceb202.png', b'0'),
	(26, b'0', 12, '2025-02-01 02:42:08.848102', '/static/pictures/dd36c628-e312-4510-985e-c330034c41fb.png', b'0'),
	(27, b'0', 13, '2025-02-01 04:03:37.395429', '/static/pictures/dfe842ce-a3b7-43a2-a86c-d341a65c6338.png', b'0'),
	(28, b'0', 13, '2025-02-01 04:03:37.417740', '/static/pictures/8b2553c3-b2fb-41e3-bed2-38a5c0510978.png', b'0'),
	(29, b'0', 16, '2025-02-01 04:52:50.997399', '/static/pictures/db9d25d3-10c5-4fb9-ada7-32fc38df7c1b.png', b'0'),
	(30, b'0', 16, '2025-02-01 04:52:51.010401', '/static/pictures/66b8766a-45bf-4184-8b09-72687f4f829a.png', b'0'),
	(31, b'0', 5, '2025-02-01 07:47:23.000000', '/static/pictures/sql.png', b'0'),
	(32, b'0', 12, '2025-02-01 07:50:33.000000', '/static/pictures/network.png', b'0'),
	(33, b'0', 15, '2025-02-01 07:51:13.000000', '/static/pictures/vue.png', b'0'),
	(34, b'0', 13, '2025-02-01 07:52:01.000000', '/static/pictures/spring.png', b'0');

-- Dumping structure for table event_db.registration_student
CREATE TABLE IF NOT EXISTS `registration_student` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `enrolled` bit(1) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `student_id` int NOT NULL,
  `training_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKkfvhdyb1531qtocr7j01cp55u` (`student_id`,`training_id`),
  KEY `FKq5w5xxs61rwwph9iryq8gnp38` (`training_id`),
  CONSTRAINT `FK66jnnwnke41vaaldrysp3imn3` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`),
  CONSTRAINT `FKq5w5xxs61rwwph9iryq8gnp38` FOREIGN KEY (`training_id`) REFERENCES `training` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.registration_student: ~0 rows (approximately)
INSERT INTO `registration_student` (`id`, `deleted`, `enrolled`, `notes`, `student_id`, `training_id`) VALUES
	(1, b'0', b'0', 'Student is registered for the event, pending approval.', 1, 1),
	(2, b'0', b'1', 'Student is registered for the event, pending approval.', 3, 1),
	(3, b'0', b'1', 'Student is registered for the event, pending approval.', 3, 3),
	(4, b'0', b'1', 'Student is registered for the event, pending approval.', 3, 2),
	(6, b'0', b'1', 'Student is registered for the event, pending approval.', 3, 4),
	(8, b'0', b'1', 'Student is registered for the event, pending approval.', 3, 12),
	(9, b'0', b'1', 'Student is registered for the event, pending approval.', 3, 5),
	(11, b'0', b'1', 'Student is registered for the event, pending approval.', 3, 13),
	(14, b'0', b'0', 'Student is registered for the event, pending approval.', 1, 2),
	(16, b'0', b'0', 'Student is registered for the event, pending approval.', 1, 3),
	(17, b'0', b'1', 'Student is registered for the event, pending approval.', 1, 12),
	(18, b'0', b'1', 'Student is registered for the event, pending approval.', 1, 4);

-- Dumping structure for table event_db.student
CREATE TABLE IF NOT EXISTS `student` (
  `id` int NOT NULL AUTO_INCREMENT,
  `city` int DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `email` varchar(255) NOT NULL,
  `job_title` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  `specialization` varchar(255) DEFAULT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_bkix9btnoi1n917ll7bplkvg5` (`user_id`),
  UNIQUE KEY `UK_fe0i52si7ybu0wjedj6motiim` (`email`),
  CONSTRAINT `FKk5m148xqefonqw7bgnpm0snwj` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.student: ~0 rows (approximately)
INSERT INTO `student` (`id`, `city`, `deleted`, `email`, `job_title`, `name`, `phone_number`, `specialization`, `user_id`) VALUES
	(1, 1, b'0', 'Mohamed23@gmail.com', 'full stack', 'israaStu israaStu', '05923558', NULL, 50),
	(3, 1, b'0', 'israabzu@gmail.com', 'full stack', 'Israa MAzaraa', '05923558', NULL, 7);

-- Dumping structure for table event_db.training
CREATE TABLE IF NOT EXISTS `training` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `end_date` date DEFAULT NULL,
  `end_registration` date DEFAULT NULL,
  `max_number_of_students` int DEFAULT NULL,
  `number_of_students_enrolled` int DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `training_description` tinytext,
  `training_name` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.training: ~15 rows (approximately)
INSERT INTO `training` (`id`, `deleted`, `end_date`, `end_registration`, `max_number_of_students`, `number_of_students_enrolled`, `start_date`, `training_description`, `training_name`, `type`) VALUES
	(1, b'0', NULL, '2025-02-01', 10, 1, NULL, 'A confasderence about the latest in technology.', 'JAVA ', 'TRAINING_COURSE'),
	(2, b'0', NULL, '2025-02-01', 10, 0, NULL, 'A confasderence about the latest in technology.', 'ReactJs ', 'TRAINING_COURSE'),
	(3, b'0', NULL, '2025-02-01', 10, 0, NULL, 'A confasderence about the latest in technology.', 'CSS ', 'TRAINING_COURSE'),
	(4, b'0', NULL, '2025-02-01', 10, 0, NULL, 'A confasderence about the latest in technology.', 'HTML ', 'TRAINING_COURSE'),
	(5, b'0', NULL, '2025-02-01', 10, 0, NULL, 'A confasderence about the latest in technology.', 'SQL ', 'TRAINING_COURSE'),
	(6, b'0', '2025-03-01', '2025-02-28', 50, 25, '2025-02-01', 'Learn the fundamentals of Java programming, including object-oriented concepts and basic data structures.', 'Java Programming ', 'EVENT'),
	(7, b'0', '2025-04-15', '2025-04-10', 100, 50, '2025-03-15', 'A comprehensive course on web development with React, covering components, state management, and hooks.', 'React Web Development ', 'EVENT'),
	(8, b'0', '2025-05-10', '2025-05-05', 30, 15, '2025-04-20', 'Introduction to machine learning, including supervised and unsupervised learning techniques.', 'Machine Learning Basics', 'EVENT'),
	(9, b'0', '2025-06-01', '2025-05-28', 40, 20, '2025-05-10', 'A beginner-friendly course on digital marketing strategies, SEO, and content creation for businesses.', 'Digital Marketing ', 'EVENT'),
	(11, b'0', '2025-07-01', '2025-06-25', 60, 30, '2025-06-01', 'Advanced techniques in data visualization using tools like Tableau and Power BI to create interactive reports.', ' Data Visualization', 'EVENT'),
	(12, b'0', NULL, '2025-02-01', 10, 0, NULL, 'A confasderence about the latest in technology.', 'Network', 'TRAINING_COURSE'),
	(13, b'0', NULL, '2025-02-01', 10, 0, NULL, 'A confasderence about the latest in technology.', 'SPRING BOOT', 'TRAINING_COURSE'),
	(14, b'0', NULL, '2025-02-01', 0, 0, NULL, 'فسس', 'فثسف', 'EVENT'),
	(15, b'0', NULL, '2025-02-01', 10, 0, NULL, 'Angular is a web framework that empowers developers to build fast, reliable applications..', 'Vue js', 'TRAINING_COURSE'),
	(16, b'0', NULL, '2025-02-01', 10, 0, NULL, 'Angular is a web framework that empowers developers to build fast, reliable applications..', 'Angular course', 'TRAINING_COURSE');

-- Dumping structure for table event_db.training_instructor
CREATE TABLE IF NOT EXISTS `training_instructor` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `instructor_id` int DEFAULT NULL,
  `training_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlayplm0kx4r7ot32q6ojohirt` (`instructor_id`),
  KEY `FK3866cdooh3twxbimsxevbxe64` (`training_id`),
  CONSTRAINT `FK3866cdooh3twxbimsxevbxe64` FOREIGN KEY (`training_id`) REFERENCES `training` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKlayplm0kx4r7ot32q6ojohirt` FOREIGN KEY (`instructor_id`) REFERENCES `instructor` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.training_instructor: ~0 rows (approximately)
INSERT INTO `training_instructor` (`id`, `deleted`, `instructor_id`, `training_id`) VALUES
	(1, b'0', 1, 3),
	(2, b'0', 1, 4),
	(3, b'0', 1, 5),
	(4, b'0', 1, 12),
	(5, b'0', 1, 13),
	(6, b'0', 1, 14),
	(7, b'0', 1, 15),
	(8, b'0', 1, 16);

-- Dumping structure for table event_db.training_organization
CREATE TABLE IF NOT EXISTS `training_organization` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `organization_id` int NOT NULL,
  `training_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3veyg4l35kanb3npffylnkwvo` (`organization_id`),
  KEY `FK2i5d8go16usw16l8tqlq8yale` (`training_id`),
  CONSTRAINT `FK2i5d8go16usw16l8tqlq8yale` FOREIGN KEY (`training_id`) REFERENCES `training` (`id`),
  CONSTRAINT `FK3veyg4l35kanb3npffylnkwvo` FOREIGN KEY (`organization_id`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.training_organization: ~0 rows (approximately)
INSERT INTO `training_organization` (`id`, `deleted`, `organization_id`, `training_id`) VALUES
	(1, b'0', 1, 1),
	(2, b'0', 1, 2),
	(3, b'0', 1, 3),
	(4, b'0', 1, 4),
	(5, b'0', 1, 5),
	(6, b'0', 1, 12),
	(7, b'0', 1, 13),
	(8, b'0', 1, 14),
	(9, b'0', 1, 15),
	(10, b'0', 1, 16);

-- Dumping structure for table event_db.user
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `deleted` bit(1) NOT NULL,
  `email` varchar(50) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `is_enabled` bit(1) NOT NULL,
  `is_verified` bit(1) NOT NULL,
  `last_login` datetime(6) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `password` varchar(72) NOT NULL,
  `phone_number` varchar(50) DEFAULT NULL,
  `profile_picture_url` varchar(250) DEFAULT NULL,
  `role` varchar(255) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `username` varchar(50) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.user: ~34 rows (approximately)
INSERT INTO `user` (`user_id`, `created_at`, `deleted`, `email`, `first_name`, `is_enabled`, `is_verified`, `last_login`, `last_name`, `password`, `phone_number`, `profile_picture_url`, `role`, `updated_at`, `username`) VALUES
	(1, '2025-01-27 09:38:14.259455', b'0', 'Super@Admin.com', 'Israa', b'1', b'0', '2025-01-31 09:21:43.010644', 'Mazaraa', '$2a$10$tFWhHMaz7sHoxB/bpxC9cefzNUvx3dVNi1.0P0c9Kiieeeuvxe4FW', '059235684', NULL, 'SUPER_ADMIN', '2025-01-31 09:21:43.112203', 'SuperAdmin'),
	(2, '2025-01-27 09:40:54.697112', b'0', 'Admin@Admin.com', 'Israa', b'1', b'0', '2025-02-01 04:24:35.381001', 'Mazaraa', '$2a$10$a1Q138uhVaSDzBWt/eh4.edjcWlx.F9kMro2Cm/PPOYcpZ.um038S', '059235684', '/static/pictures/e38ef484-cd1d-432e-ba86-bad3dbdf4488.png', 'ADMIN', '2025-02-01 04:24:35.381998', 'Admin'),
	(4, '2025-01-27 09:42:11.562134', b'1', 'Tamara@gmail.com', 'Tamara', b'1', b'0', NULL, 'refaee', '$2a$10$/X7TO9Tw7eP8X1hLjzktk.rHRY9aBwJKOXdd4rZfBWxIN6pkatPc6', '+503 733 790 774', NULL, 'INSTRUCTOR', '2025-01-31 11:43:18.607418', 'Tamara'),
	(7, '2025-01-27 09:43:14.553857', b'0', 'israaStu@gmail.com', 'israaStu', b'1', b'0', '2025-02-01 04:24:13.603889', 'israaStu', '$2a$10$PC8qu5v5A491Ol3VHmV15OUVC.ZtxWQk2Gv35GfG58nrq2cuOtpVa', '+503 733 790 774', NULL, 'STUDENT', '2025-02-01 04:24:13.644672', 'israaStu'),
	(8, '2025-01-27 11:41:37.542607', b'0', 'contact@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', 'adminPassword123', '+1234567890', 'https://www.techinnovators.com/images/johndoe.jpg', 'ORGANIZATION', '2025-01-27 11:41:37.542607', 'ccc'),
	(12, '2025-01-31 14:29:27.141298', b'0', 'contactdd@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', 'adminPassword123', '+1234567890', 'https://www.techinnovators.com/images/johndoe.jpg', 'ORGANIZATION', '2025-01-31 14:29:27.141298', 'test'),
	(13, '2025-01-31 14:33:28.202122', b'0', 'vv@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', 'adminPassword123', '+1234567890', 'https://www.techinnovators.com/images/johndoe.jpg', 'ORGANIZATION', '2025-01-31 14:33:28.202122', 'vv'),
	(14, '2025-01-31 14:34:50.744076', b'0', 'vddv@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', 'adminPassword123', '+1234567890', 'https://www.techinnovators.com/images/johndoe.jpg', 'ORGANIZATION', '2025-01-31 14:34:50.744076', 'dd'),
	(15, '2025-01-31 14:38:24.874672', b'0', 'vddxxv@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', 'adminPassword123', '+1234567890', 'https://www.techinnovators.com/images/johndoe.jpg', 'ORGANIZATION', '2025-01-31 14:38:24.874672', 'dxxd'),
	(16, '2025-01-31 14:40:29.264892', b'0', 'cccccccccccvv@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', 'adminPassword123', '+1234567890', 'https://www.techinnovators.com/images/johndoe.jpg', 'ORGANIZATION', '2025-01-31 14:40:29.264892', 'vccccccccccccccccccccccv'),
	(17, '2025-01-31 14:42:13.998606', b'0', 'cccccccccccvv@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', 'adminPassword123', '+1234567890', 'https://www.techinnovators.com/images/johndoe.jpg', 'ORGANIZATION', '2025-01-31 14:42:13.998606', 'cc234'),
	(20, '2025-01-31 14:44:21.385418', b'0', 'v21@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', 'adminPassword123', '+1234567890', 'https://www.techinnovators.com/images/johndoe.jpg', 'ORGANIZATION', '2025-01-31 14:44:21.385418', '123'),
	(22, '2025-01-31 14:46:47.213931', b'0', 'v21@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', 'adminPassword123', '+1234567890', 'https://www.techinnovators.com/images/johndoe.jpg', 'ORGANIZATION', '2025-01-31 14:46:47.213931', '1bb23'),
	(23, '2025-01-31 14:49:16.135802', b'0', '689@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', 'adminPassword123', '+1234567890', 'https://www.techinnovators.com/images/johndoe.jpg', 'ORGANIZATION', '2025-01-31 14:49:16.135802', 'xsdew'),
	(25, '2025-01-31 14:51:33.780047', b'0', '68c9@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', 'adminPassword123', '+1234567890', 'https://www.techinnovators.com/images/johndoe.jpg', 'ORGANIZATION', '2025-01-31 14:51:33.780047', 'xsderty'),
	(26, '2025-01-31 14:53:11.336234', b'0', '68c9@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', 'adminPassword123', '+1234567890', 'https://www.techinnovators.com/images/johndoe.jpg', 'ORGANIZATION', '2025-01-31 14:53:11.338311', '89xx'),
	(29, '2025-01-31 14:54:00.855927', b'0', '68c9@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', 'adminPassword123', '+1234567890', 'https://www.techinnovators.com/images/johndoe.jpg', 'ORGANIZATION', '2025-01-31 14:54:00.855927', '8974'),
	(31, '2025-01-31 14:54:38.086053', b'0', '68c9@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', 'adminPassword123', '+1234567890', 'https://www.techinnovators.com/images/johndoe.jpg', 'ORGANIZATION', '2025-01-31 14:54:38.086053', '89874'),
	(32, '2025-01-31 14:55:31.224156', b'0', '68c899@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', 'adminPassword123', '+1234567890', 'https://www.techinnovators.com/images/johndoe.jpg', 'ORGANIZATION', '2025-01-31 14:55:31.224156', '8987489'),
	(33, '2025-01-31 15:01:00.117175', b'0', '68c84599@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', 'adminPassword123', '+1234567890', '/static/pictures/29b4f951-b2b8-4bbe-82e7-e9f34d5e70bd.png', 'ORGANIZATION', '2025-01-31 15:01:00.196139', '898457489'),
	(34, '2025-01-31 15:08:13.392795', b'0', '68c8324599@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', 'adminPassword123', '+1234567890', '/static/pictures/ebb81b5d-181e-491d-87e4-3b38b1b0b306.png', 'ORGANIZATION', '2025-01-31 15:08:13.499701', '89843457489'),
	(35, '2025-01-31 15:09:09.265551', b'0', '68c834424599@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', 'adminPassword123', '+1234567890', '/static/pictures/cfa33797-0f2c-40bf-8e79-bb4d23569253.png', 'ORGANIZATION', '2025-01-31 15:09:09.316275', '8984443457489'),
	(36, '2025-01-31 15:09:36.725603', b'0', '68c83444424599@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', 'adminPassword123', '+1234567890', '/static/pictures/c0298302-8ffa-40fc-855f-1425fc89feb5', 'ORGANIZATION', '2025-01-31 15:09:36.745571', '44423'),
	(38, '2025-01-31 15:10:14.249466', b'0', '68c8w3444424599@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', 'adminPassword123', '+1234567890', '/static/pictures/68ec4776-ae4a-467c-aa6c-d5e2ac8b0f30', 'ORGANIZATION', '2025-01-31 15:10:14.275294', '4w4423'),
	(39, '2025-01-31 15:11:45.475486', b'0', '6822c8w3444424599@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', 'adminPassword123', '+1234567890', '/static/pictures/45d3c9be-8321-461b-86ce-5fcdf713b574', 'ORGANIZATION', '2025-01-31 15:11:45.543300', '4w422423'),
	(42, '2025-01-31 15:14:32.925785', b'0', '6822c8w344vv4424599@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', 'adminPassword123', '+1234567890', '/static/pictures/671c62c0-5204-40ee-8708-9ceb5836fd6d', 'ORGANIZATION', '2025-01-31 15:15:03.412701', '4w4vv22423'),
	(43, '2025-01-31 15:16:24.750964', b'0', '6822c8w3c44vv4424599@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', 'adminPassword123', '+1234567890', NULL, 'ORGANIZATION', '2025-01-31 15:16:24.750964', '4w4vcv22423'),
	(45, '2025-01-31 15:48:52.280802', b'0', 'JHF@techinnovators.com', 'John', b'1', b'0', NULL, 'Doe', '$2a$10$eqvByLuNBJuW4pPI.l9K8.EYbCAuQhKAu4IweFbQuNwtbfqZ5LDCC', '+1234567890', '/static/pictures/6d4b2f54-7b68-4a2b-af1d-a898cdd859a3.png', 'ORGANIZATION', '2025-01-31 15:48:52.327758', 'JHF'),
	(46, '2025-01-31 16:09:41.423966', b'0', 'Gaza@GazaSkyGeeks.com', 'John', b'1', b'0', NULL, 'Doe', '$2a$10$ySuAzPNB2kJ6C.X0TMnVnu1O2mPdbd6eC7vjqYtzeJsuH.wHgtR/a', '+1234567890', '/static/pictures/e49b6f5a-2eb6-4f02-8677-f88fb6ea29f6.png', 'ORGANIZATION', '2025-01-31 16:09:41.493424', 'GazaSkyGeeks'),
	(48, '2025-01-31 16:32:34.240538', b'0', 'johndoe@example.com', 'John', b'1', b'0', NULL, 'Doe', '123456', '1234567890', 'https://example.com/profile/johndoe.jpg', 'INSTRUCTOR', '2025-01-31 16:32:34.240538', 'johndoe'),
	(50, '2025-01-31 16:35:38.788352', b'0', 'israaMazaraa96@gmail.com', 'israaStu', b'1', b'0', '2025-02-01 04:08:48.839495', 'israaStu', '$2a$10$YPdpTsAo9Ofhp9b6ShKcm.CHgns5Z6MrpYpI.9kG3bEp2q4WUiEri', '+503 733 790 774', NULL, 'STUDENT', '2025-02-01 04:08:48.846498', 'IsraaMAzaraa'),
	(52, '2025-01-31 20:27:50.102053', b'0', 'TamaraElyyan@gmail.com', 'Tamara', b'1', b'0', '2025-01-31 20:28:03.282860', 'Elyyan', '$2a$10$i6p8Jja2hUiqmCQgB2AmXeOmcQg39WdGLW87e53Pkium.8Rxmak1e', NULL, NULL, 'STUDENT', '2025-01-31 20:28:03.283865', 'Tamara123'),
	(53, '2025-02-01 00:57:56.715472', b'0', 'Intel@gmail.com', 'Israa', b'1', b'0', NULL, 'MAzaraa', '$2a$10$BD0QijSs2XVtzvvG7f/Hmu4o7ppzstDiUvsr04WEDznAFBoc.mr/G', NULL, NULL, 'ORGANIZATION', '2025-02-01 00:57:56.715472', 'Intel'),
	(54, '2025-02-01 00:58:24.131316', b'0', 'Asal@gmail.com', 'Israa', b'1', b'0', NULL, 'MAzaraa', '$2a$10$xboABZItd9zoRtu.XuJk8.JBnD6Ph9M3vmPBCnOa1ydd8c3BNjrdu', NULL, NULL, 'ORGANIZATION', '2025-02-01 00:58:24.131316', 'Asal');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
