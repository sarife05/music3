package org.example.musiclibrary.utils;

import org.example.musiclibrary.model.Media;
import org.example.musiclibrary.model.Playlist;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility class demonstrating Lambda Expressions and functional programming.
 * Provides sorting and filtering operations using lambdas.
 */
public class SortingUtils {

    /**
     * Sort media by name using lambda
     */
    public static void sortByName(List<Media> mediaList) {
        mediaList.sort((m1, m2) -> m1.getName().compareToIgnoreCase(m2.getName()));
        System.out.println("✓ Sorted by name (A-Z)");
    }

    /**
     * Sort media by duration using lambda
     */
    public static void sortByDuration(List<Media> mediaList) {
        mediaList.sort(Comparator.comparingInt(Media::getDuration));
        System.out.println("✓ Sorted by duration (shortest first)");
    }

    /**
     * Sort media by creator using lambda
     */
    public static void sortByCreator(List<Media> mediaList) {
        mediaList.sort((m1, m2) -> m1.getCreator().compareToIgnoreCase(m2.getCreator()));
        System.out.println("✓ Sorted by creator (A-Z)");
    }

    /**
     * Sort media by type and then by name using lambda chaining
     */
    public static void sortByTypeAndName(List<Media> mediaList) {
        mediaList.sort(
                Comparator.comparing(Media::getType)
                        .thenComparing(m -> m.getName().toLowerCase())
        );
        System.out.println("✓ Sorted by type, then by name");
    }

    /**
     * Sort playlists by number of items using lambda
     */
    public static void sortPlaylistsBySize(List<Playlist> playlists) {
        playlists.sort((p1, p2) -> Integer.compare(p2.getItems().size(), p1.getItems().size()));
        System.out.println("✓ Sorted playlists by size (largest first)");
    }

    /**
     * Filter media by type using lambda
     */
    public static List<Media> filterByType(List<Media> mediaList, Media.MediaType type) {
        return mediaList.stream()
                .filter(m -> m.getType() == type)
                .collect(Collectors.toList());
    }

    /**
     * Filter media by creator using lambda
     */
    public static List<Media> filterByCreator(List<Media> mediaList, String creator) {
        return mediaList.stream()
                .filter(m -> m.getCreator().equalsIgnoreCase(creator))
                .collect(Collectors.toList());
    }

    /**
     * Filter media by minimum duration using lambda
     */
    public static List<Media> filterByMinDuration(List<Media> mediaList, int minDuration) {
        return mediaList.stream()
                .filter(m -> m.getDuration() >= minDuration)
                .collect(Collectors.toList());
    }

    /**
     * Generic filter using custom predicate (demonstrates functional interface)
     */
    public static List<Media> filterMedia(List<Media> mediaList, Predicate<Media> condition) {
        return mediaList.stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    /**
     * Search media by name containing keyword using lambda
     */
    public static List<Media> searchByName(List<Media> mediaList, String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return mediaList.stream()
                .filter(m -> m.getName().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    /**
     * Get total duration using lambda reduce
     */
    public static int getTotalDuration(List<Media> mediaList) {
        return mediaList.stream()
                .mapToInt(Media::getDuration)
                .sum();
    }

    /**
     * Count media by type using lambda
     */
    public static long countByType(List<Media> mediaList, Media.MediaType type) {
        return mediaList.stream()
                .filter(m -> m.getType() == type)
                .count();
    }

    /**
     * Get media names as comma-separated string using lambda
     */
    public static String getMediaNames(List<Media> mediaList) {
        return mediaList.stream()
                .map(Media::getName)
                .collect(Collectors.joining(", "));
    }

    /**
     * Display sorted list with custom formatting
     */
    public static void displaySortedList(List<Media> mediaList, String title) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("  " + title);
        System.out.println("═".repeat(60));

        if (mediaList.isEmpty()) {
            System.out.println("  (No items to display)");
        } else {
            mediaList.forEach(media ->
                    System.out.printf("  • %s by %s [%s] - %s%n",
                            media.getName(),
                            media.getCreator(),
                            media.getType(),
                            media.getFormattedDuration())
            );
        }
        System.out.println("═".repeat(60));
    }
}