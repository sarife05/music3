# Music Library / Playlist Manager API

A comprehensive Java console application demonstrating Object-Oriented Programming (OOP) principles, SOLID architecture, advanced Java features, and JDBC database operations for managing a music library with songs, podcasts, and playlists.

---

## Project Overview

### Purpose
This API provides a complete music library management system with the following capabilities:
- Create, read, update, and delete (CRUD) media items (songs and podcasts)
- Organize media into playlists
- Search and filter media by various criteria
- Sort media using different algorithms
- Validate input data with custom business rules
- Handle errors gracefully with custom exceptions

### Main Entities

#### Media (Abstract Base Class)
- **Song**: Represents music tracks with album, genre, and pricing
- **Podcast**: Represents podcast episodes with host, episode number, and category

#### Playlist
- Collection of media items demonstrating composition
- Supports adding/removing media
- Calculates total duration
- Can play all items sequentially

### Key Relationships
- **Inheritance**: Song and Podcast extend Media
- **Composition**: Playlist contains multiple Media objects
- **Many-to-Many**: Playlists can contain multiple media items, and media can be in multiple playlists

---

## SOLID Principles Documentation

### Single Responsibility Principle (SRP)
Each class has one reason to change:

| Class | Responsibility |
|-------|---------------|
| `Media` | Represents media item data and basic behavior |
| `Song` | Represents song-specific data and behavior |
| `Podcast` | Represents podcast-specific data and behavior |
| `Playlist` | Manages collection of media items |
| `MediaRepositoryImpl` | Database operations for media |
| `PlaylistRepositoryImpl` | Database operations for playlists |
| `MediaServiceImpl` | Business logic and validation for media |
| `PlaylistServiceImpl` | Business logic and validation for playlists |
| `MusicLibraryController` | Coordinates requests between user and services |
| `DatabaseConnection` | Manages database connectivity |

**Example:**
```java
// MediaServiceImpl only handles business logic
public class MediaServiceImpl implements MediaService {
    private final MediaRepository mediaRepository; // Uses repository for data
    
    public Media createMedia(Media media) throws InvalidInputException {
        media.validate();  // Business logic: validation
        // Check duplicates (business rule)
        return mediaRepository.create(media); // Delegates to repository
    }
}
```

### Open-Closed Principle (OCP)
Open for extension, closed for modification:

**Abstract Media Class:**
```java
// Can add new media types (AudioBook, LiveStream) without modifying Media class
public abstract class Media implements Playable, Validatable<Media> {
    // Core fields and methods
    public abstract String getDescription();
    public abstract void displayInfo();
}

// New type can be added without changing Media
public class AudioBook extends Media {
    @Override
    public String getDescription() {
        return "AudioBook: " + getName();
    }
}
```

**Repository Pattern:**
```java
// New repositories can be added without changing existing code
public interface CrudRepository<T> {
    T create(T entity);
    // ... other methods
}
```

### Liskov Substitution Principle (LSP)
Subtypes are substitutable for their base types:

```java
// Media reference can hold Song or Podcast - they behave correctly
Media media1 = new Song("Title", 180, "Artist", "Album", "Rock");
Media media2 = new Podcast("Episode", 1800, "Creator", "Host", 1, "Tech");

// Both work correctly through base class reference
media1.play();  // Calls Song's play method
media2.play();  // Calls Podcast's play method
media1.displayInfo();  // Song-specific display
media2.displayInfo();  // Podcast-specific display
```

All subclasses override abstract methods correctly and maintain the contract defined by Media.

### Interface Segregation Principle (ISP)
Clients depend only on interfaces they use:

```java
// Narrow, focused interfaces
public interface Playable {
    int getDuration();
    void play();
}

public interface PricedItem {
    double getPrice();
    void setPrice(double price);
}

public interface Printable {
    void print();
}

// Song implements all relevant interfaces
public class Song extends Media implements PricedItem {
    // Only implements what it needs
}

// Podcast doesn't need pricing, so it doesn't implement PricedItem
public class Podcast extends Media {
    // Only implements Playable and Validatable (from Media)
}
```

### Dependency Inversion Principle (DIP)
High-level modules depend on abstractions, not concrete implementations:

**Service Layer depends on Repository Interface:**
```java
public class MediaServiceImpl implements MediaService {
    private final MediaRepository mediaRepository; // Interface, not implementation
    
    public MediaServiceImpl(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository; // Constructor injection
    }
}
```

**Controller depends on Service Interface:**
```java
public class MusicLibraryController {
    private final MediaService mediaService; // Interface, not implementation
    private final PlaylistService playlistService;
    
    public MusicLibraryController(MediaService mediaService, 
                                  PlaylistService playlistService) {
        this.mediaService = mediaService; // Constructor injection
        this.playlistService = playlistService;
    }
}
```

**Main sets up dependency injection:**
```java
// Create implementations
MediaRepository mediaRepo = new MediaRepositoryImpl();
MediaService mediaService = new MediaServiceImpl(mediaRepo);
MusicLibraryController controller = new MusicLibraryController(mediaService, playlistService);
```

---

## OOP Design Documentation

### Abstract Classes and Inheritance

#### Media (Abstract Base Class)
```java
public abstract class Media implements Playable, Validatable<Media> {
    private int id;
    private String name;
    private int duration;
    private String creator;
    private MediaType type;
    
    // Abstract methods - must be implemented by subclasses
    public abstract String getDescription();
    public abstract void displayInfo();
    
    // Concrete method - shared by all subclasses
    public String getFormattedDuration() {
        int minutes = duration / 60;
        int seconds = duration % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
}
```

**Why abstract?**
- Cannot instantiate Media directly (must be Song or Podcast)
- Enforces common structure while allowing subclass-specific behavior
- Demonstrates polymorphism

#### Song and Podcast Subclasses
Both extend Media and provide specific implementations:

```java
public class Song extends Media implements PricedItem {
    private String album;
    private String genre;
    private double price;
    
    @Override
    public String getDescription() {
        return String.format("Song: '%s' by %s from album '%s'", 
            getName(), getCreator(), album);
    }
}

public class Podcast extends Media {
    private String host;
    private int episodeNumber;
    private String category;
    
    @Override
    public String getDescription() {
        return String.format("Podcast: '%s' Episode #%d", 
            getName(), episodeNumber);
    }
}
```

### Interfaces

#### 1. Playable Interface (with default and static methods)
```java
public interface Playable {
    int getDuration();
    void play();
    
    // Default method - can be overridden
    default void pause() {
        System.out.println("Paused");
    }
    
    // Static utility method
    static boolean isValidDuration(int duration) {
        return duration > 0 && duration < 86400;
    }
}
```

#### 2. Validatable<T> Interface (Generic)
```java
public interface Validatable<T> {
    void validate() throws InvalidInputException;
    
    default boolean isValid() {
        try {
            validate();
            return true;
        } catch (InvalidInputException e) {
            return false;
        }
    }
}
```

#### 3. PricedItem Interface
```java
public interface PricedItem {
    double getPrice();
    void setPrice(double price);
    double calculateTotalPrice(int quantity);
    
    default double applyDiscount(double discountPercent) {
        return getPrice() * (1 - discountPercent / 100.0);
    }
}
```

#### 4. Printable Interface
```java
public interface Printable {
    void print();
    
    default void printWithBorder() {
        System.out.println("═".repeat(55));
        print();
        System.out.println("═".repeat(55));
    }
}
```

### Composition & Aggregation

#### Playlist Contains Media (Composition)
```java
public class Playlist implements Validatable<Playlist>, Printable {
    private int id;
    private String name;
    private List<Media> items; // HAS-A relationship
    
    public void addMedia(Media media) {
        items.add(media);
    }
    
    public int getTotalDuration() {
        return items.stream()
                   .mapToInt(Media::getDuration)
                   .sum();
    }
}
```

**Why composition?**
- Playlist "has-a" list of media items
- Playlist manages its media collection
- Media items can exist independently
- Demonstrates object relationships beyond inheritance

### Polymorphism Examples

#### 1. Method Overriding
```java
// Base class reference to different subclass objects
List<Media> library = new ArrayList<>();
library.add(new Song("Title", 180, "Artist", "Album", "Rock"));
library.add(new Podcast("Episode", 1800, "Host", "Host", 1, "Tech"));

// Polymorphic calls - correct method runs based on actual object type
for (Media media : library) {
    media.displayInfo(); // Calls Song.displayInfo() or Podcast.displayInfo()
    System.out.println(media.getDescription()); // Different for each subclass
}
```

#### 2. Interface Polymorphism
```java
// All Media objects can be treated as Playable
Playable playable = new Song("Title", 180, "Artist", "Album", "Rock");
playable.play(); // Calls play() through Playable interface
```

### Encapsulation

All entity classes use private fields with public getters/setters:
```java
public class Media {
    private int id;           // Private - cannot be accessed directly
    private String name;      // Private
    private int duration;     // Private
    
    // Public getter
    public String getName() {
        return name;
    }
    
    // Public setter with validation
    public void setName(String name) {
        this.name = name;
    }
}
```

---

## Advanced Java Features

### 1. Generics

#### Generic Repository Interface
```java
public interface CrudRepository<T> {
    T create(T entity) throws DatabaseOperationException;
    List<T> getAll() throws DatabaseOperationException;
    T getById(int id) throws ResourceNotFoundException;
    T update(int id, T entity) throws ResourceNotFoundException;
    boolean delete(int id) throws ResourceNotFoundException;
}

// Specific implementations
public interface MediaRepository extends CrudRepository<Media> { }
public interface PlaylistRepository extends CrudRepository<Playlist> { }
```

#### Generic Validatable Interface
```java
public interface Validatable<T> {
    void validate() throws InvalidInputException;
    
    static <T extends Validatable<T>> boolean validateAll(T... entities) {
        for (T entity : entities) {
            entity.validate();
        }
        return true;
    }
}
```

**Benefits:**
- Type safety at compile time
- Code reusability
- Eliminates casting

### 2. Lambda Expressions

#### Sorting with Lambdas
```java
// Sort by name
mediaList.sort((m1, m2) -> m1.getName().compareToIgnoreCase(m2.getName()));

// Sort by duration
mediaList.sort(Comparator.comparingInt(Media::getDuration));

// Complex sorting
mediaList.sort(
    Comparator.comparing(Media::getType)
             .thenComparing(m -> m.getName().toLowerCase())
);
```

#### Filtering with Lambdas
```java
// Filter by type
List<Media> songs = mediaList.stream()
    .filter(m -> m.getType() == Media.MediaType.SONG)
    .collect(Collectors.toList());

// Filter by duration
List<Media> longMedia = mediaList.stream()
    .filter(m -> m.getDuration() >= 300)
    .collect(Collectors.toList());

// Custom predicate
List<Media> filtered = SortingUtils.filterMedia(mediaList, 
    media -> media.getDuration() > 200 && media.getCreator().contains("Artist")
);
```

#### Functional Operations
```java
// Map and collect
String names = mediaList.stream()
    .map(Media::getName)
    .collect(Collectors.joining(", "));

// Count with condition
long songCount = mediaList.stream()
    .filter(m -> m.getType() == Media.MediaType.SONG)
    .count();

// Sum durations
int totalDuration = mediaList.stream()
    .mapToInt(Media::getDuration)
    .sum();
```

### 3. Reflection & RTTI (Run-Time Type Information)

#### ReflectionUtils Class
```java
public class ReflectionUtils {
    public static void inspectClass(Object obj) {
        Class<?> clazz = obj.getClass();
        
        // Get class information
        System.out.println("Class Name: " + clazz.getName());
        System.out.println("Superclass: " + clazz.getSuperclass().getSimpleName());
        
        // List interfaces
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> iface : interfaces) {
            System.out.println("Implements: " + iface.getSimpleName());
        }
        
        // List fields
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("Field: " + field.getName() + 
                             " (" + field.getType().getSimpleName() + ")");
        }
        
        // List methods
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("Method: " + method.getName());
        }
    }
}
```

**Usage:**
```java
Song song = new Song("Title", 180, "Artist", "Album", "Rock");
ReflectionUtils.inspectClass(song);

// Output shows:
// - Class name: model.Song
// - Superclass: Media
// - Interfaces: PricedItem
// - Fields: album, genre, price
// - Methods: getDescription, displayInfo, getPrice, etc.
```

### 4. Interface Default and Static Methods

#### Default Methods
Provide default implementation that can be overridden:
```java
public interface Playable {
    void play(); // Abstract
    
    default void pause() {  // Default implementation
        System.out.println("Paused");
    }
}
```

#### Static Methods
Utility methods that don't require an instance:
```java
public interface Playable {
    static boolean isValidDuration(int duration) {
        return duration > 0 && duration < 86400;
    }
    
    static String formatDuration(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        return String.format("%d:%02d", hours, minutes);
    }
}

// Usage:
boolean valid = Playable.isValidDuration(300);
String formatted = Playable.formatDuration(3665);
```

---

## Database Design

### Schema Overview

#### Entity-Relationship Diagram
```
┌─────────────┐         ┌──────────────────┐         ┌──────────────┐
│  playlists  │◄────────│  playlist_items  │────────►│    media     │
│─────────────│  1    ∞ │──────────────────│  ∞   1  │──────────────│
│ id (PK)     │         │ playlist_id (FK) │         │ id (PK)      │
│ name (UQ)   │         │ media_id (FK)    │         │ name         │
│ description │         │ position         │         │ duration     │
└─────────────┘         └──────────────────┘         │ type         │
                                                      │ creator      │
                                                      │ ...          │
                                                      └──────────────┘
```

### Table Structures

#### 1. media Table
Stores all media items using a discriminator pattern:

```sql
CREATE TABLE media (
                      id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                      name TEXT NOT NULL,
                      duration INTEGER NOT NULL CHECK (duration > 0),
                      type TEXT NOT NULL CHECK (type IN ('SONG', 'PODCAST')),
                      creator TEXT NOT NULL,

   -- Song-specific fields
                      album TEXT,
                      genre TEXT,
                      price NUMERIC(5,2) DEFAULT 0.99 CHECK (price >= 0),

   -- Podcast-specific fields
                      host TEXT,
                      episode_number INTEGER DEFAULT 0 CHECK (episode_number >= 0),
                      category TEXT,

                      UNIQUE (name, type, creator)
);
```

**Constraints:**
- `PRIMARY KEY`: Auto-incrementing ID
- `CHECK`: Duration must be positive
- `CHECK`: Type must be SONG or PODCAST
- `CHECK`: Price must be non-negative
- `UNIQUE`: No duplicate media with same name, type, and creator

#### 2. playlists Table
```sql
CREATE TABLE playlists (
                          id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          name TEXT NOT NULL UNIQUE,
                          description TEXT
);
```

**Constraints:**
- `PRIMARY KEY`: Auto-incrementing ID
- `UNIQUE`: Playlist names must be unique

#### 3. playlist_items Table (Junction Table)
```sql
CREATE TABLE IF NOT EXISTS playlist_items (
                                             playlist_id BIGINT NOT NULL,
                                             media_id BIGINT NOT NULL,
                                             position INTEGER DEFAULT 0 CHECK (position >= 0),

   PRIMARY KEY (playlist_id, media_id),

   CONSTRAINT fk_playlist
   FOREIGN KEY (playlist_id)
   REFERENCES playlists (id)
   ON DELETE CASCADE,

   CONSTRAINT fk_media
   FOREIGN KEY (media_id)
   REFERENCES media (id)
   ON DELETE CASCADE
   );
```

**Constraints:**
- `PRIMARY KEY`: Composite key (playlist_id, media_id)
- `FOREIGN KEY`: References playlists table with CASCADE delete
- `FOREIGN KEY`: References media table with CASCADE delete

**Relationship:**
- Many-to-many between playlists and media
- A playlist can contain many media items
- A media item can be in many playlists
- CASCADE delete ensures referential integrity

### Sample Data

#### Songs
```sql
INSERT INTO media (name, duration, type, creator, album, genre, price) VALUES
('Bohemian Rhapsody', 354, 'SONG', 'Queen', 'A Night at the Opera', 'Rock', 1.29),
('Imagine', 183, 'SONG', 'John Lennon', 'Imagine', 'Pop', 0.99),
('Stairway to Heaven', 482, 'SONG', 'Led Zeppelin', 'Led Zeppelin IV', 'Rock', 1.29);
```

#### Podcasts
```sql
INSERT INTO media (name, duration, type, creator, host, episode_number, category) VALUES
('The Joe Rogan Experience', 7200, 'PODCAST', 'Joe Rogan', 'Joe Rogan', 1987, 'Comedy'),
('Hardcore History', 14400, 'PODCAST', 'Dan Carlin', 'Dan Carlin', 68, 'History');
```

### Indexes (Performance Optimization)
```sql
CREATE INDEX idx_media_type ON media(type);
CREATE INDEX idx_media_creator ON media(creator);
CREATE INDEX idx_media_name ON media(name);
CREATE INDEX idx_playlist_items_playlist ON playlist_items(playlist_id);
CREATE INDEX idx_playlist_items_media ON playlist_items(media_id);
```

---

## Architecture

### Layered Architecture (Multi-tier Design)

```
┌─────────────────────────────────────────┐
│         CONTROLLER LAYER                │
│  (User Interface / Request Handling)    │
│  - MusicLibraryController               │
└────────────────┬────────────────────────┘
                 │ delegates to
┌────────────────▼────────────────────────┐
│          SERVICE LAYER                  │
│  (Business Logic / Validation)          │
│  - MediaServiceImpl                     │
│  - PlaylistServiceImpl                  │
└────────────────┬────────────────────────┘
                 │ uses
┌────────────────▼────────────────────────┐
│        REPOSITORY LAYER                 │
│  (Data Access / JDBC Operations)        │
│  - MediaRepositoryImpl                  │
│  - PlaylistRepositoryImpl               │
└────────────────┬────────────────────────┘
                 │ accesses
┌────────────────▼────────────────────────┐
│          DATABASE                       │
│  (PostgreSQL - musiclibrary.db)         │
│  - media table                          │
│  - playlists table                      │
│  - playlist_items table                 │
└─────────────────────────────────────────┘
```

### Layer Responsibilities

#### 1. Controller Layer
**Purpose:** Coordinate requests and responses
- **NO business logic**
- Delegates all operations to service layer
- Handles user input/output
- Formats responses

**Example:**
```java
public class MusicLibraryController {
    private final MediaService mediaService;
    
    public Media createSong(...) {
        try {
            Song song = new Song(...);
            return mediaService.createMedia(song); // Delegates to service
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }
}
```

#### 2. Service Layer
**Purpose:** Implement business logic and validation
- Validates input data
- Enforces business rules
- Coordinates between multiple repositories if needed
- Throws appropriate exceptions

**Example:**
```java
public class MediaServiceImpl implements MediaService {
    private final MediaRepository mediaRepository;
    
    public Media createMedia(Media media) throws InvalidInputException {
        // Validation
        media.validate();
        
        // Business rule: check duplicates
        if (mediaRepository.existsByNameAndTypeAndCreator(...)) {
            throw new DuplicateResourceException("Media already exists");
        }
        
        // Business rule: duration limit
        if (media.getDuration() > 86400) {
            throw new InvalidInputException("Duration cannot exceed 24 hours");
        }
        
        return mediaRepository.create(media);
    }
}
```

#### 3. Repository Layer
**Purpose:** Handle database operations
- **NO business logic**
- CRUD operations using JDBC
- Prepared statements for SQL injection prevention
- Maps between database records and objects

**Example:**
```java
public class MediaRepositoryImpl implements MediaRepository {
    public Media create(Media entity) throws DatabaseOperationException {
        String sql = "INSERT INTO media (name, duration, ...) VALUES (?, ?, ...)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, entity.getName());
            pstmt.setInt(2, entity.getDuration());
            // ... set other parameters
            
            pstmt.executeUpdate();
            return entity;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to create media", e);
        }
    }
}
```

#### 4. Database Layer
**Purpose:** Persistent data storage
- SQLite database file
- Tables with constraints
- Foreign key relationships
- Indexes for performance

### Directory Structure

```
music-library-api/
├── src/
│   ├── controller/
│   │   └── MusicLibraryController.java
│   ├── service/
│   │   ├── interfaces/
│   │   │   ├── MediaService.java
│   │   │   └── PlaylistService.java
│   │   ├── MediaServiceImpl.java
│   │   └── PlaylistServiceImpl.java
│   ├── repository/
│   │   ├── interfaces/
│   │   │   ├── CrudRepository.java
│   │   │   ├── MediaRepository.java
│   │   │   └── PlaylistRepository.java
│   │   ├── MediaRepositoryImpl.java
│   │   └── PlaylistRepositoryImpl.java
│   ├── model/
│   │   ├── Media.java (abstract)
│   │   ├── Song.java
│   │   ├── Podcast.java
│   │   ├── Playlist.java
│   │   ├── Playable.java (interface)
│   │   ├── Validatable.java (interface)
│   │   ├── PricedItem.java (interface)
│   │   └── Printable.java (interface)
│   ├── exception/
│   │   ├── InvalidInputException.java
│   │   ├── DuplicateResourceException.java
│   │   ├── ResourceNotFoundException.java
│   │   └── DatabaseOperationException.java
│   ├── utils/
│   │   ├── DatabaseConnection.java
│   │   ├── ReflectionUtils.java
│   │   └── SortingUtils.java
│   └── Main.java
├── resources/
│   └── schema.sql
├── docs/
│   └── screenshots/
├── .gitignore
└── README.md
```

---

## API Operations

### Media Operations

#### Create Song
```java
Media song = controller.createSong(
    "Bohemian Rhapsody",  // name
    354,                   // duration in seconds
    "Queen",               // creator
    "A Night at the Opera", // album
    "Rock",                // genre
    1.29                   // price
);
```

#### Create Podcast
```java
Media podcast = controller.createPodcast(
    "The Joe Rogan Experience", // name
    7200,                        // duration
    "Joe Rogan",                 // creator
    "Joe Rogan",                 // host
    1987,                        // episode number
    "Comedy"                     // category
);
```

#### Read Operations
```java
// Get all media
List<Media> allMedia = controller.getAllMedia();

// Get by ID
Media media = controller.getMediaById(1);

// Search by name
List<Media> results = controller.searchMedia("Bohemian");

// Filter by type
List<Media> songs = controller.getMediaByType(Media.MediaType.SONG);
```

#### Update Media
```java
Media media = controller.getMediaById(1);
media.setName("Updated Name");
controller.updateMedia(media.getId(), media);
```

#### Delete Media
```java
boolean deleted = controller.deleteMedia(1);
```

### Playlist Operations

#### Create Playlist
```java
Playlist playlist = controller.createPlaylist(
    "My Favorites",           // name
    "Best songs of all time"  // description
);
```

#### Add Media to Playlist
```java
controller.addMediaToPlaylist(playlistId, mediaId);
```

#### Display Playlist
```java
Playlist playlist = controller.getPlaylistById(1);
playlist.print();  // Formatted display
playlist.playAll(); // Play all items
```

### Error Handling Examples

```java
// InvalidInputException
try {
    controller.createSong("", 180, "Artist", "Album", "Genre", 0.99);
} catch (InvalidInputException e) {
    System.err.println("Invalid input: " + e.getMessage());
}

// DuplicateResourceException
try {
    controller.createSong("Existing Song", 180, "Artist", "Album", "Genre", 0.99);
    controller.createSong("Existing Song", 180, "Artist", "Album", "Genre", 0.99);
} catch (DuplicateResourceException e) {
    System.err.println("Duplicate: " + e.getMessage());
}

// ResourceNotFoundException
try {
    controller.getMediaById(99999);
} catch (ResourceNotFoundException e) {
    System.err.println("Not found: " + e.getMessage());
}
```

---

## Installation & Setup

### Prerequisites
- Java Development Kit (JDK) 17 or higher
- PostgreSQL
- PostgreSQL JDBC Driver

### Compilation

#### Option 1: Command Line (Linux/Mac)
```bash
# Navigate to project directory
cd music-library-api

# Compile all Java files
javac -d bin -cp ".:postgresql-42.7.3.jar" $(find src -name "*.java")

# Run the application
java -cp "bin:postgresql-42.7.3.jar" Main
```

#### Option 2: Command Line (Windows)
```cmd
# Navigate to project directory
cd music-library-api

# Compile
javac -d bin -cp ".;postgresql-42.7.3.jar" src\**\*.java

# Run
java -cp "bin;postgresql-42.7.3.jar" Main
```

#### Option 3: IDE (IntelliJ IDEA / Eclipse)
1. Import project as Java project
2. Add PostgreSQL JDBC driver to classpath
   Download from: https://jdbc.postgresql.org/download/
3. Set src/ as source folder
4. Run Main.java

### Database Setup
- PostgreSQL database
- Database schema is created automatically on first run
- Connection parameters are configured in DatabaseConnection class
- Sample data is NOT inserted automatically (to demonstrate CRUD operations)

---

## Usage Examples

### Example 1: Creating and Managing Media

```java
// Create songs
Media song1 = controller.createSong(
    "Bohemian Rhapsody", 354, "Queen", 
    "A Night at the Opera", "Rock", 1.29
);

// Display info
controller.displayMediaInfo(song1.getId());

// Update
song1.setName("Bohemian Rhapsody (Remastered)");
controller.updateMedia(song1.getId(), song1);

// Delete
controller.deleteMedia(song1.getId());
```

### Example 2: Working with Playlists

```java
// Create playlist
Playlist playlist = controller.createPlaylist(
    "Road Trip", "Songs for long drives"
);

// Add songs
controller.addMediaToPlaylist(playlist.getId(), song1.getId());
controller.addMediaToPlaylist(playlist.getId(), song2.getId());

// Display playlist
controller.displayPlaylistInfo(playlist.getId());

// Play all
Playlist loadedPlaylist = controller.getPlaylistById(playlist.getId());
loadedPlaylist.playAll();
```

### Example 3: Searching and Filtering

```java
// Search by name
List<Media> results = controller.searchMedia("Love");

// Filter by type
List<Media> songs = controller.getMediaByType(Media.MediaType.SONG);
List<Media> podcasts = controller.getMediaByType(Media.MediaType.PODCAST);

// Sort using lambdas
List<Media> allMedia = controller.getAllMedia();
SortingUtils.sortByName(allMedia);
SortingUtils.displaySortedList(allMedia, "Sorted by Name");
```

### Example 4: Polymorphism in Action

```java
List<Media> library = controller.getAllMedia();

// Polymorphic calls - each subclass behaves differently
for (Media media : library) {
    media.displayInfo();      // Calls Song or Podcast version
    media.play();             // Playable interface method
    System.out.println(media.getDescription()); // Abstract method
}
```

### Example 5: Using Interface Features

```java
Song song = new Song("Title", 180, "Artist", "Album", "Rock");
song.setPrice(1.99);

// Interface default methods
song.play();
song.pause();
song.stop();

// PricedItem interface
double discounted = song.applyDiscount(20); // 20% off
double total = song.calculateTotalPrice(5); // Price for 5 copies

// Static methods
boolean valid = Playable.isValidDuration(300);
String formatted = Playable.formatDuration(3665);
```

---

## Screenshots

*To be added: Screenshots demonstrating:*
1. Successful CRUD operations
2. Validation failures with error messages
3. Reflection utility output
4. Sorted lists using lambdas
5. Playlist composition
6. Polymorphism in action
[oop33.pdf](../../Desktop/oop33.pdf)
---

## Reflection

### What I Learned

#### Technical Skills
1. **SOLID Principles in Practice**
    - How to apply SRP by separating concerns into layers
    - DIP through interface-based design and constructor injection
    - OCP by using abstract classes and interfaces for extension
    - ISP by creating focused, narrow interfaces
    - LSP by ensuring subclasses behave correctly

2. **Advanced Java Features**
    - Generics for type-safe code reuse
    - Lambda expressions for functional programming
    - Reflection for runtime type inspection
    - Default and static methods in interfaces

3. **Database Design**
    - Using JDBC with PreparedStatements
    - Implementing the repository pattern
    - Handling many-to-many relationships
    - Ensuring referential integrity with foreign keys

4. **Architecture**
    - Multi-layer design for maintainability
    - Separation of concerns (controller → service → repository)
    - Dependency injection for loose coupling

#### Design Patterns Applied
- **Repository Pattern**: Data access abstraction
- **Service Layer Pattern**: Business logic encapsulation
- **Dependency Injection**: Loose coupling between layers
- **Template Method**: Abstract base class with concrete/abstract methods
- **Strategy Pattern**: Different implementations of interfaces

### Challenges Faced

1. **Database Design with Inheritance**
    - **Challenge**: How to store Song and Podcast in one table
    - **Solution**: Used discriminator pattern with 'type' column
    - **Learning**: Single table inheritance vs. table-per-subclass trade-offs

2. **Managing Dependencies**
    - **Challenge**: Circular dependencies between services and repositories
    - **Solution**: Used interfaces and dependency injection
    - **Learning**: Importance of programming to interfaces

3. **Exception Hierarchy**
    - **Challenge**: Deciding which exceptions should extend which
    - **Solution**: Created hierarchy: InvalidInputException → DuplicateResourceException
    - **Learning**: Exception hierarchy should reflect business logic relationships

4. **Lambda Expressions with Different Types**
    - **Challenge**: Creating reusable sorting/filtering with generics and lambdas
    - **Solution**: Used Predicate<T> and Function<T, R> functional interfaces
    - **Learning**: Combining generics with lambdas for maximum flexibility

5. **Reflection Performance**
    - **Challenge**: Reflection is slower than direct method calls
    - **Solution**: Used reflection only for utility/debugging purposes
    - **Learning**: Reflection is powerful but should be used judiciously

### Benefits of JDBC and Multi-Layer Design

#### JDBC Benefits
1. **Direct Database Control**: Fine-grained control over SQL queries
2. **Performance**: Can optimize queries for specific use cases
3. **Portability**: Works with any SQL database (SQLite, PostgreSQL, MySQL)
4. **PreparedStatements**: Built-in SQL injection prevention
5. **Transaction Support**: Can manage complex transactions

#### Multi-Layer Architecture Benefits
1. **Maintainability**: Changes to one layer don't affect others
2. **Testability**: Each layer can be tested independently
3. **Reusability**: Services and repositories can be reused
4. **Scalability**: Easy to add new features without breaking existing code
5. **Team Collaboration**: Different developers can work on different layers
6. **Clear Separation**: Business logic separate from data access and presentation

