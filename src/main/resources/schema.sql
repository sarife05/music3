CREATE TABLE IF NOT EXISTS Media (
                                     id INTEGER PRIMARY KEY,
                                     name TEXT NOT NULL,
                                     duration INTEGER NOT NULL CHECK (duration > 0),
    type TEXT NOT NULL CHECK (type IN ('SONG', 'PODCAST')),
    creator TEXT NOT NULL,
    UNIQUE(name, type, creator)
    );


CREATE TABLE IF NOT EXISTS Playlist (
                                        id INTEGER PRIMARY KEY,
                                        name TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Playlist_items (
                                              playlist_id INTEGER NOT NULL,
                                              media_id INTEGER NOT NULL,
                                              PRIMARY KEY (playlist_id, media_id),
    FOREIGN KEY (playlist_id) REFERENCES playlist(id) ON DELETE CASCADE,
    FOREIGN KEY (media_id) REFERENCES media(id) ON DELETE CASCADE
    );

INSERT INTO media (id, name, duration, type, creator)
VALUES
    (1, 'Imagine', 180, 'SONG', 'John Lennon'),
    (2, 'Yesterday', 150, 'SONG', 'The Beatles'),
    (3, 'Tech Talk', 1200, 'PODCAST', 'Alice');
