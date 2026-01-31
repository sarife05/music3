package org.example.musiclibrary;

import org.example.musiclibrary.controller.MusicLibraryController;
import org.example.musiclibrary.model.*;
import org.example.musiclibrary.repository.*;
import org.example.musiclibrary.service.*;
import org.example.musiclibrary.utils.*;

import java.util.List;

/**
 * Main application demonstrating all OOP principles, SOLID architecture,
 * advanced Java features, and JDBC operations.
 *
 * This application showcases:
 * - Abstract classes and inheritance (Media -> Song, Podcast)
 * - Interfaces with default/static methods (Playable, Validatable, PricedItem, Printable)
 * - Polymorphism (Media references to Song/Podcast objects)
 * - Composition (Playlist HAS-A list of Media)
 * - SOLID principles (SRP, OCP, LSP, ISP, DIP)
 * - Generics (CrudRepository<T>, Validatable<T>)
 * - Lambda expressions (sorting, filtering)
 * - Reflection/RTTI (runtime type inspection)
 * - Custom exception hierarchy
 * - JDBC with PreparedStatements
 * - Multi-layer architecture (controller -> service -> repository)
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║     MUSIC LIBRARY / PLAYLIST MANAGER API                     ║");
        System.out.println("║     Demonstrating OOP, SOLID, and Advanced Java Features     ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝\n");

        try {

            // Setup dependency injection (DIP)
            MediaRepository mediaRepo = new MediaRepositoryImpl();
            PlaylistRepository playlistRepo = new PlaylistRepositoryImpl();

            MediaService mediaService = new MediaServiceImpl(mediaRepo);
            PlaylistService playlistService = new PlaylistServiceImpl(playlistRepo, mediaRepo);

            MusicLibraryController controller = new MusicLibraryController(mediaService, playlistService);

            // Run all demonstrations
            System.out.println("\n" + "═".repeat(60));
            System.out.println("SECTION 1: BASIC CRUD OPERATIONS");
            System.out.println("═".repeat(60));
            demonstrateCRUD(controller);

            System.out.println("\n" + "═".repeat(60));
            System.out.println("SECTION 2: POLYMORPHISM & INTERFACES");
            System.out.println("═".repeat(60));
            demonstratePolymorphism(controller);

            System.out.println("\n" + "═".repeat(60));
            System.out.println("SECTION 3: COMPOSITION (PLAYLISTS)");
            System.out.println("═".repeat(60));
            demonstrateComposition(controller);

            System.out.println("\n" + "═".repeat(60));
            System.out.println("SECTION 4: LAMBDA EXPRESSIONS & SORTING");
            System.out.println("═".repeat(60));
            demonstrateLambdas(controller);

            System.out.println("\n" + "═".repeat(60));
            System.out.println("SECTION 5: REFLECTION & RTTI");
            System.out.println("═".repeat(60));
            demonstrateReflection(controller);

            System.out.println("\n" + "═".repeat(60));
            System.out.println("SECTION 6: EXCEPTION HANDLING");
            System.out.println("═".repeat(60));
            demonstrateExceptionHandling(controller);

            System.out.println("\n" + "═".repeat(60));
            System.out.println("SECTION 7: INTERFACE FEATURES (Default & Static Methods)");
            System.out.println("═".repeat(60));
            demonstrateInterfaceFeatures(controller);

            System.out.println("\n\n╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                 ALL DEMONSTRATIONS COMPLETED                 ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");

        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    /**
     * Demonstrate basic CRUD operations (ASM-3 requirement)
     */
    private static void demonstrateCRUD(MusicLibraryController controller) {
        System.out.println("\n▶ Creating Songs...");
        Media song1 = controller.createSong("Bohemian Rhapsody", 354, "Queen", "A Night at the Opera", "Rock", 1.29);
        Media song2 = controller.createSong("Imagine", 183, "John Lennon", "Imagine", "Pop", 0.99);
        Media song3 = controller.createSong("Stairway to Heaven", 482, "Led Zeppelin", "Led Zeppelin IV", "Rock", 1.29);

        System.out.println("\n▶ Creating Podcasts...");
        Media podcast1 = controller.createPodcast("The Joe Rogan Experience", 7200, "Joe Rogan", "Joe Rogan", 1987, "Comedy");
        Media podcast2 = controller.createPodcast("Hardcore History", 14400, "Dan Carlin", "Dan Carlin", 68, "History");

        System.out.println("\n▶ Displaying All Media:");
        controller.displayAllMedia();

        System.out.println("\n▶ Retrieving Media by ID:");
        if (song1 != null) {
            controller.displayMediaInfo(song1.getId());
        }

        System.out.println("\n▶ Updating Media:");
        if (song2 != null) {
            song2.setName("Imagine (Remastered)");
            controller.updateMedia(song2.getId(), song2);
        }

        System.out.println("\n▶ Deleting Media:");
        if (song3 != null) {
            controller.deleteMedia(song3.getId());
        }

        System.out.println("\n▶ Final Media List:");
        controller.displayAllMedia();
    }

    /**
     * Demonstrate polymorphism and abstract classes (ASM-3 & ASM-4 requirement)
     */
    private static void demonstratePolymorphism(MusicLibraryController controller) {
        System.out.println("\n▶ Demonstrating Polymorphism:");
        System.out.println("Using Media reference to call overridden methods...\n");

        List<Media> allMedia = controller.getAllMedia();

        // Polymorphism: calling overridden methods through base class reference
        for (Media media : allMedia) {
            System.out.println("─".repeat(50));
            System.out.println("Processing: " + media.getClass().getSimpleName());

            // Abstract method implementation (polymorphic call)
            System.out.println(media.getDescription());

            // Playable interface method
            media.play();

            // Concrete method from base class
            System.out.println("Formatted duration: " + media.getFormattedDuration());

            // Default interface method
            media.pause();
            media.stop();
        }
    }

    /**
     * Demonstrate composition with Playlists (ASM-3 & ASM-4 requirement)
     */
    private static void demonstrateComposition(MusicLibraryController controller) {
        System.out.println("\n▶ Creating Playlists (Composition):");

        Playlist rockPlaylist = controller.createPlaylist("Classic Rock Hits", "Best rock songs from the 70s and 80s");
        Playlist podcastPlaylist = controller.createPlaylist("Informative Podcasts", "Educational and entertaining podcasts");

        System.out.println("\n▶ Adding Media to Playlists:");
        List<Media> allMedia = controller.getAllMedia();

        for (Media media : allMedia) {
            if (media.getType() == Media.MediaType.SONG) {
                if (rockPlaylist != null) {
                    controller.addMediaToPlaylist(rockPlaylist.getId(), media.getId());
                }
            } else if (media.getType() == Media.MediaType.PODCAST) {
                if (podcastPlaylist != null) {
                    controller.addMediaToPlaylist(podcastPlaylist.getId(), media.getId());
                }
            }
        }

        System.out.println("\n▶ Displaying Playlists:");
        controller.displayAllPlaylists();

        System.out.println("\n▶ Detailed Playlist Information:");
        if (rockPlaylist != null) {
            controller.displayPlaylistInfo(rockPlaylist.getId());
        }

        if (podcastPlaylist != null) {
            Playlist playlist = controller.getPlaylistById(podcastPlaylist.getId());
            if (playlist != null) {
                System.out.println("\n▶ Playing Entire Playlist:");
                playlist.playAll();
            }
        }
    }

    /**
     * Demonstrate lambda expressions and functional programming (ASM-4 requirement)
     */
    private static void demonstrateLambdas(MusicLibraryController controller) {
        System.out.println("\n▶ Demonstrating Lambda Expressions:");

        List<Media> allMedia = controller.getAllMedia();

        if (allMedia.isEmpty()) {
            System.out.println("No media available for sorting demonstration");
            return;
        }

        // Create copies for different sorts
        List<Media> mediaByName = List.copyOf(allMedia);
        List<Media> mediaByDuration = List.copyOf(allMedia);
        List<Media> mediaByType = List.copyOf(allMedia);

        System.out.println("\n1. Sorting by Name (using lambda):");
        java.util.ArrayList<Media> nameList = new java.util.ArrayList<>(mediaByName);
        SortingUtils.sortByName(nameList);
        SortingUtils.displaySortedList(nameList, "Sorted by Name");

        System.out.println("\n2. Sorting by Duration (using lambda):");
        java.util.ArrayList<Media> durationList = new java.util.ArrayList<>(mediaByDuration);
        SortingUtils.sortByDuration(durationList);
        SortingUtils.displaySortedList(durationList, "Sorted by Duration");

        System.out.println("\n3. Filtering by Type (using lambda):");
        List<Media> songs = SortingUtils.filterByType(allMedia, Media.MediaType.SONG);
        System.out.println("Songs only: " + songs.size() + " items");
        songs.forEach(media -> System.out.println("  • " + media.getName()));

        System.out.println("\n4. Custom Filter (duration > 300 seconds using lambda):");
        List<Media> longMedia = SortingUtils.filterByMinDuration(allMedia, 300);
        System.out.println("Media longer than 5 minutes: " + longMedia.size() + " items");
        longMedia.forEach(media ->
                System.out.println("  • " + media.getName() + " - " + media.getFormattedDuration())
        );

        System.out.println("\n5. Search by Name (using lambda):");
        List<Media> searchResults = SortingUtils.searchByName(allMedia, "the");
        System.out.println("Media containing 'the': " + searchResults.size() + " items");
        searchResults.forEach(media -> System.out.println("  • " + media.getName()));
    }

    /**
     * Demonstrate reflection and RTTI (ASM-4 requirement)
     */
    private static void demonstrateReflection(MusicLibraryController controller) {
        System.out.println("\n▶ Demonstrating Reflection & RTTI:");

        // Create sample objects
        Song song = new Song("Sample Song", 200, "Sample Artist", "Sample Album", "Pop");
        Podcast podcast = new Podcast("Sample Podcast", 1800, "Sample Creator", "Host Name", 1, "Tech");
        Playlist playlist = new Playlist("Sample Playlist", "Sample description");

        System.out.println("\n1. Inspecting Song class:");
        ReflectionUtils.inspectClass(song);

        System.out.println("\n2. Inspecting Podcast class:");
        ReflectionUtils.inspectClass(podcast);

        System.out.println("\n3. Inspecting Playlist class:");
        ReflectionUtils.inspectClass(playlist);

        // Demonstrate field access
        System.out.println("\n4. Accessing fields using reflection:");
        Object duration = ReflectionUtils.getFieldValue(song, "duration");
        System.out.println("Song duration (via reflection): " + duration);

        // Demonstrate method checking
        System.out.println("\n5. Checking for methods:");
        System.out.println("Song has 'play' method: " + ReflectionUtils.hasMethod(Song.class, "play"));
        System.out.println("Song has 'validate' method: " + ReflectionUtils.hasMethod(Song.class, "validate"));
    }

    /**
     * Demonstrate exception handling (ASM-3 & ASM-4 requirement)
     */
    private static void demonstrateExceptionHandling(MusicLibraryController controller) {
        System.out.println("\n▶ Demonstrating Exception Handling:");

        System.out.println("\n1. InvalidInputException - Empty name:");
        controller.createSong("", 180, "Artist", "Album", "Genre", 0.99);

        System.out.println("\n2. InvalidInputException - Negative duration:");
        controller.createSong("Test Song", -100, "Artist", "Album", "Genre", 0.99);

        System.out.println("\n3. DuplicateResourceException - Duplicate media:");
        controller.createSong("Bohemian Rhapsody", 354, "Queen", "A Night at the Opera", "Rock", 1.29);
        controller.createSong("Bohemian Rhapsody", 354, "Queen", "A Night at the Opera", "Rock", 1.29);

        System.out.println("\n4. ResourceNotFoundException - Non-existent ID:");
        controller.getMediaById(99999);

        System.out.println("\n5. ResourceNotFoundException - Delete non-existent:");
        controller.deleteMedia(99999);

        System.out.println("\n6. DuplicateResourceException - Duplicate playlist:");
        controller.createPlaylist("Test Playlist", "Description");
        controller.createPlaylist("Test Playlist", "Different description");

        System.out.println("\n✓ Exception handling demonstration completed");
    }

    /**
     * Demonstrate interface default and static methods (ASM-4 requirement)
     */
    private static void demonstrateInterfaceFeatures(MusicLibraryController controller) {
        System.out.println("\n▶ Demonstrating Interface Features:");

        // Get a song
        List<Media> allMedia = controller.getAllMedia();
        Song song = null;
        for (Media media : allMedia) {
            if (media instanceof Song) {
                song = (Song) media;
                break;
            }
        }

        if (song != null) {
            System.out.println("\n1. Default method from Playable interface:");
            song.play();    // Abstract method
            song.pause();   // Default method
            song.stop();    // Default method

            System.out.println("\n2. Static method from Playable interface:");
            System.out.println("Is 300 seconds valid? " + Playable.isValidDuration(300));
            System.out.println("Is -10 seconds valid? " + Playable.isValidDuration(-10));
            System.out.println("Formatted duration (3665 seconds): " + Playable.formatDuration(3665));

            System.out.println("\n3. Default method from Validatable interface:");
            System.out.println("Is song valid? " + song.isValid());

            System.out.println("\n4. Methods from PricedItem interface:");
            System.out.println("Original price: $" + song.getPrice());
            System.out.println("Price with 20% discount: $" + String.format("%.2f", song.applyDiscount(20)));
            System.out.println("Total for 5 items: $" + String.format("%.2f", song.calculateTotalPrice(5)));

            System.out.println("\n5. Static method from PricedItem interface:");
            Song anotherSong = new Song("Another Song", 200, "Artist", "Album", "Genre");
            anotherSong.setPrice(1.99);
            int comparison = PricedItem.comparePrice(song, anotherSong);
            System.out.println("Price comparison result: " + comparison);
            System.out.println("(negative = first cheaper, zero = equal, positive = first more expensive)");
        }

        System.out.println("\n6. Printable interface with default method:");
        Playlist playlist = controller.getAllPlaylists().stream().findFirst().orElse(null);
        if (playlist != null) {
            playlist.printWithBorder(); // Default method with border
        }
    }
}