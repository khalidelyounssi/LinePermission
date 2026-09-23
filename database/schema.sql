PRAGMA foreign_keys = ON;
CREATE TABLE IF NOT EXISTS users (

    id INTEGER PRIMARY KEY AUTOINCREMENT,

    login TEXT NOT NULL UNIQUE,

    password_hash TEXT NOT NULL
);


CREATE TABLE IF NOT EXISTS fichiers (

    id INTEGER PRIMARY KEY AUTOINCREMENT,

    nom TEXT NOT NULL UNIQUE,

    owner_id INTEGER NOT NULL,

    droits TEXT NOT NULL DEFAULT 'rwd|---',

    FOREIGN KEY (owner_id)
        REFERENCES users(id)
        ON DELETE RESTRICT
);


CREATE TABLE IF NOT EXISTS logs (

    id INTEGER PRIMARY KEY AUTOINCREMENT,

    user_id INTEGER NOT NULL,

    fichier_id INTEGER NOT NULL,

    action TEXT NOT NULL
        CHECK (action IN ('LECTURE','ECRITURE','SUPPRESSION')
        ),

    resultat TEXT NOT NULL
        CHECK (resultat IN ('OK','REFUSE')
        ),

    date_heure TEXT NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE RESTRICT,

    FOREIGN KEY (fichier_id)
        REFERENCES fichiers(id)
        ON DELETE RESTRICT
);