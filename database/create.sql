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

ALTER TABLE `herbarium_index`.`synonyms` 
DROP FOREIGN KEY `name`;
ALTER TABLE `herbarium_index`.`synonyms` 
DROP INDEX `name_idx` ;
;

ALTER TABLE `herbarium_index`.`families` 
ADD COLUMN `family_id` INT NOT NULL AUTO_INCREMENT FIRST,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`family_id`),
ADD UNIQUE INDEX `id_UNIQUE` (`family_id` ASC) VISIBLE,
DROP INDEX `name_UNIQUE` ;
;

CREATE TABLE `herbarium_index`.`families_bh` (
  `name` VARCHAR(45) NOT NULL,
  `index` VARCHAR(45) NOT NULL);

CREATE TABLE `herbarium_index`.`families_gbi` (
  `name` VARCHAR(45) NOT NULL,
  `value` VARCHAR(45) NOT NULL,
  INDEX `name_idx` (`name` ASC) VISIBLE);


CREATE TABLE `herbarium_index`.`families_fe` (
  `name` VARCHAR(45) NOT NULL,
  `value` VARCHAR(45) NOT NULL,
  INDEX `name_idx` (`name` ASC) VISIBLE);


ALTER TABLE `herbarium_index`.`families` 
DROP COLUMN `bh_index`,
DROP COLUMN `fe_index`,
DROP COLUMN `gbi_index`,
DROP COLUMN `family_id`,
ADD COLUMN `type` VARCHAR(45) NULL AFTER `name`,
CHANGE COLUMN `true_index` `value` VARCHAR(45) NOT NULL ,
ADD INDEX `name_idx` (`name` ASC) VISIBLE,
DROP INDEX `id_UNIQUE` ,
DROP PRIMARY KEY;
, RENAME TO  `herbarium_index`.`families_true` ;


ALTER TABLE `herbarium_index`.`synonyms` 
DROP COLUMN `species_id`,
ADD COLUMN `family_index_type` VARCHAR(45) NULL AFTER `family_name`,
ADD COLUMN `family_index_value` VARCHAR(45) NOT NULL AFTER `family_index_type`,
ADD COLUMN `genus_index_type` VARCHAR(45) NULL AFTER `family_index_value`,
ADD COLUMN `genus_index_value` VARCHAR(45) NULL AFTER `genus_index_type`;


ALTER TABLE `herbarium_index`.`synonyms` 
ADD COLUMN `id` INT NOT NULL AUTO_INCREMENT FIRST,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`),
ADD UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
DROP INDEX `species_name_UNIQUE` ;
;

CREATE TABLE `herbarium_index`.`genera_bh` (
  `name` VARCHAR(45) NOT NULL,
  `value` VARCHAR(45) NOT NULL,
  INDEX `name_idx` (`name` ASC) VISIBLE);

CREATE TABLE `herbarium_index`.`genera_fe` (
  `name` VARCHAR(45) NOT NULL,
  `value` VARCHAR(45) NOT NULL,
  INDEX `name_idx` (`name` ASC) VISIBLE);

CREATE TABLE `herbarium_index`.`genera_gbi` (
  `name` VARCHAR(45) NOT NULL,
  `value` VARCHAR(45) NOT NULL,
  INDEX `name_idx` (`name` ASC) VISIBLE);
