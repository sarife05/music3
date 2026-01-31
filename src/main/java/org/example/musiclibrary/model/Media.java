package org.example.musiclibrary.model;

import org.example.musiclibrary.exception.InvalidInputException;

/**
 * Abstract base class for all media types in the music library.
 * Follows OCP (Open-Closed Principle): open for extension via subclasses, closed for modification.
 * Demonstrates polymorphism through abstract methods.
 */
public abstract class Media implements Playable, Validatable<Media> {
    private int id;
    private String name;
    private int duration; // in seconds
    private String creator;
    private MediaType type;

    public enum MediaType {
        SONG, PODCAST
    }

    /**
     * Constructor for creating media without ID (before database insertion)
     */
    public Media(String name, int duration, String creator, MediaType type) {
        this.name = name;
        this.duration = duration;
        this.creator = creator;
        this.type = type;
    }

    /**
     * Constructor for creating media with ID (after database retrieval)
     */
    public Media(int id, String name, int duration, String creator, MediaType type) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.creator = creator;
        this.type = type;
    }

    // Abstract methods - must be implemented by subclasses (LSP - Liskov Substitution Principle)
    public abstract String getDescription();
    public abstract void displayInfo();

    // Concrete method with default implementation
    public String getFormattedDuration() {
        int minutes = duration / 60;
        int seconds = duration % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    // Validation implementation (can be overridden by subclasses)
    @Override
    public void validate() throws InvalidInputException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Media name cannot be empty");
        }
        if (duration <= 0) {
            throw new InvalidInputException("Duration must be greater than 0");
        }
        if (creator == null || creator.trim().isEmpty()) {
            throw new InvalidInputException("Creator name cannot be empty");
        }
    }

    // Playable interface implementation
    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public void play() {
        System.out.println("Now playing: " + name + " by " + creator + " (" + getFormattedDuration() + ")");
    }

    // Encapsulation: getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public MediaType getType() {
        return type;
    }

    protected void setType(MediaType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("%s: %s by %s [%s]",
                type, name, creator, getFormattedDuration());
    }
}