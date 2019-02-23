CREATE TABLE `herbarium_index`.`synonyms` (
  `name` VARCHAR(45) NOT NULL,
  `species_id` INT NOT NULL,
  `family_name` VARCHAR(45) NOT NULL,
  `is_accepted` TINYINT NOT NULL DEFAULT 0,
  `is_basionym` TINYINT NULL DEFAULT 0,
  `note` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`name`),
  UNIQUE INDEX `species_name_UNIQUE` (`name` ASC) VISIBLE);

CREATE TABLE `herbarium_index`.`families` (
  `name` VARCHAR(45) NOT NULL,
  `gbi_index` VARCHAR(45) NULL,
  `fe_index` VARCHAR(45) NULL,
  `bh_index` VARCHAR(45) NULL,
  `true_index` VARCHAR(45) NULL,
  PRIMARY KEY (`name`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE);


CREATE TABLE `herbarium_index`.`locations` (
  `code` VARCHAR(45) NOT NULL,
  `code_definition` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`code`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC) VISIBLE);

ALTER TABLE `herbarium_index`.`synonyms` 
ADD INDEX `name_idx` (`family_name` ASC) VISIBLE;
;
ALTER TABLE `herbarium_index`.`synonyms` 
ADD CONSTRAINT `name`
  FOREIGN KEY (`family_name`)
  REFERENCES `herbarium_index`.`families` (`name`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

