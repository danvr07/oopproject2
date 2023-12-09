package app.user;

import app.audio.Collections.Podcast;
import app.info.Announcement;

import java.util.ArrayList;

public class Host extends User {


    @Override
    public boolean isHost() {
        return true;
    }


    ArrayList<Podcast> podcasts;
    ArrayList<Announcement> announcements;

    public Host(String username, int age, String city, boolean online, String type) {
        super(username, age, city, online, type);
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
    }

    public String addPodcast(Podcast podcast) {
        if (podcasts.stream().anyMatch(existingPodcast -> existingPodcast.getName().equals(podcast.getName()))) {
            return " has another podcast with the same name.";
        } else {
            podcasts.add(podcast);
            return " has added new podcast successfully.";
        }
    }

    public String addAnnouncement(Announcement announcement) {
        if (announcements.stream().anyMatch(existingAnnouncement -> existingAnnouncement.getName().equals(announcement.getName()))) {
            return " has another announcement with the same name.";
        } else {
            announcements.add(announcement);
            return getUsername() + " has successfully added new announcement.";
        }
    }

    public String removeAnnouncement(String announcementName) {
        if (announcements.stream().noneMatch(existingAnnouncement -> existingAnnouncement.getName().equals(announcementName))) {
            return getUsername() + " has no announcement with the given name.";
        } else {
            announcements.removeIf(existingAnnouncement -> existingAnnouncement.getName().equals(announcementName));
            return getUsername() + " has successfully deleted the announcement.";
        }
    }

    public ArrayList<Podcast> getPodcasts() {
        return podcasts;
    }
}
