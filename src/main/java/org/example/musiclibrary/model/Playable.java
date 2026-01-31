package org.example.musiclibrary.model;

/**
 * Playable interface defines the contract for any media that can be played.
 * Demonstrates ISP (Interface Segregation Principle): clients depend only on play behavior.
 * Includes default and static methods (Advanced Java feature).
 */
public interface Playable {

    /**
     * Get the duration of the playable media in seconds
     */
    int getDuration();

    /**
     * Play the media
     */
    void play();

    /**
     * Default method: Pause simulation
     * This is a default implementation that can be overridden
     */
    default void pause() {
        System.out.println("⏸️  Paused");
    }

    /**
     * Default method: Stop simulation
     */
    default void stop() {
        System.out.println("⏹️  Stopped");
    }

    /**
     * Static method: Check if duration is valid
     * Utility method that doesn't require an instance
     */
    static boolean isValidDuration(int duration) {
        return duration > 0 && duration < 86400; // Max 24 hours
    }

    /**
     * Static method: Format duration to human-readable string
     */
    static String formatDuration(int seconds) {
        if (seconds < 0) return "Invalid duration";

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, secs);
        } else {
            return String.format("%d:%02d", minutes, secs);
        }
    }
}