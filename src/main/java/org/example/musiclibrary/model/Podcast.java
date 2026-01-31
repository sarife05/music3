package org.example.musiclibrary.model;

import org.example.musiclibrary.exception.InvalidInputException;

/**
 * Podcast entity representing an audio podcast episode.
 * Demonstrates LSP: can substitute Media in all contexts.
 */
public class Podcast extends Media {
    private String host;
    private int episodeNumber;
    private String category;

    public Podcast(String name, int duration, String creator) {
        super(name, duration, creator, MediaType.PODCAST);
        this.host = creator; // By default, creator is the host
    }

    public Podcast(String name, int duration, String creator, String host, int episodeNumber, String category) {
        super(name, duration, creator, MediaType.PODCAST);
        this.host = host;
        this.episodeNumber = episodeNumber;
        this.category = category;
    }

    public Podcast(int id, String name, int duration, String creator, String host, int episodeNumber, String category) {
        super(id, name, duration, creator, MediaType.PODCAST);
        this.host = host;
        this.episodeNumber = episodeNumber;
        this.category = category;
    }

    @Override
    public String getDescription() {
        return String.format("Podcast: '%s' hosted by %s (Episode #%d, Category: %s)",
                getName(), host, episodeNumber,
                category != null ? category : "General");
    }

    @Override
    public void displayInfo() {
        System.out.println("═══════════════════════════════════════");
        System.out.println("🎙️  PODCAST DETAILS");
        System.out.println("═══════════════════════════════════════");
        System.out.println("Title    : " + getName());
        System.out.println("Creator  : " + getCreator());
        System.out.println("Host     : " + host);
        System.out.println("Episode  : #" + episodeNumber);
        System.out.println("Category : " + (category != null ? category : "N/A"));
        System.out.println("Duration : " + getFormattedDuration());
        System.out.println("═══════════════════════════════════════");
    }

    @Override
    public void validate() throws InvalidInputException {
        super.validate(); // Call parent validation
        if (host == null || host.trim().isEmpty()) {
            throw new InvalidInputException("Podcast host cannot be empty");
        }
        if (episodeNumber < 0) {
            throw new InvalidInputException("Episode number cannot be negative");
        }
    }

    // Getters and setters
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}