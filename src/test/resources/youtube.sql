-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb`;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`videos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`videos` (
  `id` INT NOT NULL,
  `title` VARCHAR(100) NULL,
  `date` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`channels`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`channels` (
  `id` INT NOT NULL,
  `channel_name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;