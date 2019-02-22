CREATE TABLE `herbarium_index`.`species` (
  `species_id` INT NOT NULL;
  `latin_name` VARCHAR(45) NOT NULL,
  `is_accepted` TINYINT NOT NULL DEFAULT 0,
  `is_basionym` TINYINT NULL,
  PRIMARY KEY (`latin_name`),
  UNIQUE INDEX `latin_name_UNIQUE` (`latin_name` ASC) VISIBLE);

CREATE TABLE `herbarium_index`.`families` (
  `family_id` INT NOT NULL,
  `family_name` VARCHAR(45) NOT NULL,
  `gbi_index` INT NULL,
  `fe_index` INT NULL,
  `bh_index` INT NULL,
  PRIMARY KEY (`family_id`),
  UNIQUE INDEX `family_id_UNIQUE` (`family_id` ASC) VISIBLE,
  UNIQUE INDEX `family_name_UNIQUE` (`family_name` ASC) VISIBLE);

CREATE TABLE `herbarium_index`.`locations` (
  `geographic_code` VARCHAR(45) NOT NULL,
  `location_name` VARCHAR(45) NULL,
  `collection_name` VARCHAR(45) NULL,
  PRIMARY KEY (`geographic_code`));

ALTER TABLE `herbarium_index`.`species` 
ADD COLUMN `family_id` INT NOT NULL AFTER `is_basionym`,
ADD INDEX `family_id_idx` (`family_id` ASC) VISIBLE;
;
ALTER TABLE `herbarium_index`.`species` 
ADD CONSTRAINT `family_id`
  FOREIGN KEY (`family_id`)
  REFERENCES `herbarium_index`.`families` (`family_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `herbarium_index`.`species` 
ADD COLUMN `family_id` INT NOT NULL AFTER `species_id`,
CHANGE COLUMN `species_id` `species_id` INT(11) NOT NULL AFTER `is_basionym`,
ADD INDEX `family_id_idx` (`family_id` ASC) VISIBLE;
;
ALTER TABLE `herbarium_index`.`species` 
ADD CONSTRAINT `family_id`
  FOREIGN KEY (`family_id`)
  REFERENCES `herbarium_index`.`families` (`family_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `herbarium_index`.`species` 
ADD COLUMN `geo_code` VARCHAR(45) NULL AFTER `family_id`,
CHANGE COLUMN `family_id` `family_id` INT(11) NOT NULL AFTER `species_id`,
ADD INDEX `geographic_code_idx` (`geo_code` ASC) VISIBLE;
;
ALTER TABLE `herbarium_index`.`species` 
ADD CONSTRAINT `geographic_code`
  FOREIGN KEY (`geographic_code`)
  REFERENCES `herbarium_index`.`locations` (`geographic_code`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
