SET SQL_MODE = '';
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

CREATE DATABASE IF NOT EXISTS `penduMicroservice` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `penduMicroservice`;

-- Structure de la table `partie`
DROP TABLE IF EXISTS `partie`;
CREATE TABLE IF NOT EXISTS `partie` (
                                        `id` bigint NOT NULL AUTO_INCREMENT,
                                        `nom_partie` varchar(255) COLLATE utf8_bin NOT NULL,
    `mot_de_passe` varchar(255) COLLATE utf8_bin NOT NULL,
    `mot_secret` varchar(255) COLLATE utf8_bin NOT NULL,
    `etat_mot` varchar(255) COLLATE utf8_bin NOT NULL,
    `erreurs` int NOT NULL,
    `etat_partie` varchar(255) COLLATE utf8_bin NOT NULL,
    `joueur_actuel_id` bigint,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- Structure de la table `joueur`
DROP TABLE IF EXISTS `joueur`;
CREATE TABLE IF NOT EXISTS `joueur` (
                                        `id` bigint NOT NULL AUTO_INCREMENT,
                                        `nom_joueur` varchar(255) COLLATE utf8_bin NOT NULL,
    `tour_joueur` boolean NOT NULL,
    `erreurs` int NOT NULL,
    `partie_id` bigint,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`partie_id`) REFERENCES `partie` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- Structure de la table `lettres_devinees`
DROP TABLE IF EXISTS `lettres_devinees`;
CREATE TABLE IF NOT EXISTS `lettres_devinees` (
                                                  `partie_id` bigint NOT NULL,
                                                  `lettre` char(1) COLLATE utf8_bin NOT NULL,
    FOREIGN KEY (`partie_id`) REFERENCES `partie` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

DROP TABLE IF EXISTS `lettres_ratees`;
CREATE TABLE IF NOT EXISTS `lettres_ratees` (
                                                `partie_id` bigint NOT NULL,
                                                `lettre` char(1) COLLATE utf8_bin NOT NULL,
                                                FOREIGN KEY (`partie_id`) REFERENCES `partie` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

COMMIT;
