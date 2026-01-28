# Music Library / Playlist Manager API

## A. Project Overview

This project is a console-based Java API for managing a music library with Songs, Podcasts, and Playlists using JDBC and a layered architecture.

The purpose of the API is to demonstrate:
- Object-Oriented Programming principles
- CRUD operations with JDBC
- Custom exception handling
- Multi-layer design (controller → service → repository)

### Main Entities

- Media (abstract base entity)
- Song (extends Media)
- Podcast (extends Media)
- Playlist (composition: contains Media)

Relationships:
- Song and Podcast are stored in one table `media` using a discriminator column `type`.
- Playlist can contain multiple Media objects (composition).

---

## B. OOP Design Documentation

### Abstract Class and Subclasses

- `Media` is an abstract base class with fields:
  - `id`
  - `name`
  - `duration`
  - `type`
  - `creator`

Abstract / common behavior:
- Validation through `validate()`
- Common getters and setters

Subclasses:
- `Song` extends `Media`
- `Podcast` extends `Media`

Each subclass:
- Overrides `getType()`
- Implements its own validation rules

### Interfaces

- `Playable`  
  - Method: `int getDuration()`

- `Validatable`  
  - Method: `void validate()`

Implemented in:
- `Media`, `Song`, `Podcast`

### Composition / Aggregation

- `Playlist` contains a list of `Media` objects:

```javф
private List<Media> items;
Tables
media
id INTEGER PRIMARY KEY
name TEXT NOT NULL
duration INTEGER NOT NULL
type TEXT NOT NULL ('SONG' or 'PODCAST')
creator TEXT NOT NULL
UNIQUE(name, type, creator)
playlists
id INTEGER PRIMARY KEY
name TEXT NOT NULL UNIQUE
playlist_items
playlist_id INTEGER (FK → playlists.id)
media_id INTEGER (FK → media.id)
PRIMARY KEY (playlist_id, media_id)
Foreign Keys
playlist_items.playlist_id → playlists.id
playlist_items.media_id → media.id
Controller 
The Main class demonstrates:
Creating Song and Podcast
Reading all Media from database
Reading Media by ID
Updating Song
Deleting Podcast
Polymorphism using Media reference
Composition using Playlist
Validation and exception handling

I Learned how to design an API using layered architecture
How to use JDBC with PreparedStatement
How to apply OOP principles in a real project
How to implement polymorphism with database mapping
Challenges Faced
Designing a correct abstract class hierarchy
Mapping one table to multiple subclasses
Handling SQL exceptions properly

Benefits of JDBC and Multi-layer Design
Clear separation of concerns
Easier testing and maintenance
Better error handling
Scalable architecture for future extensions

[music.pdf](https://github.com/user-attachments/files/24837190/music.pdf)
<img width="1280" height="800" alt="Снимок экрана 2026-01-28 в 13 25 33" src="https://github.com/user-attachments/assets/021f0131-3c76-4d25-b823-8b8180a82763" />

<img width="1280" height="800" alt="Снимок экрана 2026-01-28 в 13 25 37" src="https://github.com/user-attachments/assets/98b0c921-071c-4884-9596-ab876c70b6ab" />

