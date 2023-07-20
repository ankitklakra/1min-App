package com.startop.a1min;

// Define a class called ModelStory.
public class ModelStory {

    // Declare public fields to store story information.
    public String title = "";          // Story title.
    public String description = "";    // Story description.
    public String imageUri = "";       // URI of the image associated with the story.
    public long timestart = 0;         // Start time of the story (timestamp in milliseconds).
    public long timeend = 0;           // End time of the story (timestamp in milliseconds).
    public String storyid = "";        // Unique identifier for the story.
    public String userid = "";         // User ID associated with the story.

    // Constructor to initialize the ModelStory object with provided values.
    public ModelStory(String imageUri, long timestart, long timeend, String storyid, String userid, String title, String description) {
        this.imageUri = imageUri;
        this.timestart = timestart;
        this.timeend = timeend;
        this.storyid = storyid;
        this.userid = userid;
        this.title = title;
        this.description = description;
    }

    // Default constructor (no arguments) required for Firebase database operations.
    public ModelStory() {

    }

    // Getter method for image URI.
    public String getImageUri() {
        return imageUri;
    }

    // Setter method for image URI.
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    // Getter method for the story's start time.
    public long getTimestart() {
        return timestart;
    }

    // Setter method for the story's start time.
    public void setTimestart(long timestart) {
        this.timestart = timestart;
    }

    // Getter method for the story's end time.
    public long getTimeend() {
        return timeend;
    }

    // Setter method for the story's end time.
    public void setTimeend(long timeend) {
        this.timeend = timeend;
    }

    // Getter method for the story ID.
    public String getStoryid() {
        return storyid;
    }

    // Setter method for the story ID.
    public void setStoryid(String storyid) {
        this.storyid = storyid;
    }

    // Getter method for the user ID associated with the story.
    public String getUserid() {
        return userid;
    }

    // Setter method for the user ID associated with the story.
    public void setUserid(String userid) {
        this.userid = userid;
    }

    // Getter method for the story title.
    public String getTitle() {
        return title;
    }

    // Setter method for the story title.
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter method for the story description.
    public String getDescription() {
        return description;
    }

    // Setter method for the story description.
    public void setDescription(String description) {
        this.description = description;
    }

}
