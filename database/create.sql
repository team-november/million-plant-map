CREATE TABLE `families_bh` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `family_name` varchar(45) NOT NULL,
  `family_number` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `name_idx` (`family_name`)
) ENGINE=InnoDB AUTO_INCREMENT=565 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `families_fe` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `family_name` varchar(45) NOT NULL,
  `family_number` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `name_idx` (`family_name`)
) ENGINE=InnoDB AUTO_INCREMENT=196 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `families_gbi` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `family_name` varchar(45) NOT NULL,
  `family_number` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `name_idx` (`family_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `genera_bh` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `genus_name` varchar(45) NOT NULL,
  `family_number` varchar(45) DEFAULT NULL,
  `genus_number` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `genus_name_idx` (`genus_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `genera_fe` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `genus_name` varchar(45) NOT NULL,
  `family_number` varchar(45) DEFAULT NULL,
  `genus_number` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `genus_name_idx` (`genus_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1240 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `genera_gbi` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `genus_name` varchar(45) NOT NULL,
  `family_number` varchar(45) DEFAULT NULL,
  `genus_number` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `genus_name_idx` (`genus_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `synonyms` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `species_name` varchar(45) NOT NULL,
  `family_name` varchar(45) NOT NULL,
  `index_scheme` varchar(45) DEFAULT NULL,
  `family_number` varchar(45) NOT NULL,
  `genus_number` varchar(45) DEFAULT NULL,
  `is_accepted` tinyint(1) DEFAULT NULL,
  `is_basionym` tinyint(1) DEFAULT NULL,
  `note` mediumtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

