package org.example.musiclibrary.model;

public abstract class Media implements Playable, Validatable {

        protected int id;
        protected String name;
        protected int duration;

        public Media(int id, String name, int duration) {
            this.id = id;
            this.name = name;
            this.duration = duration;
        }

      
        public abstract String getType();
        public abstract String getCreator();


        public void printInfo() {
            System.out.println(
                    "Media: " + name +
                            ", Type: " + getType() +
                            ", Creator: " + getCreator() +
                            ", Duration: " + duration + "s"
            );
        }

        // getters / setters
        public int getId() {
            return id;
        }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    protected void validateBase() {
    if (name == null || name.isEmpty()) {
        throw new IllegalArgumentException("Name cannot be empty");
    }
    if (duration <= 0) {
        throw new IllegalArgumentException("Duration must be positive");
    }
}
}


