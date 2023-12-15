package app.user;

import app.Admin;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.info.Announcement;

import java.util.ArrayList;

import fileio.input.CommandInput;
import fileio.input.EpisodeInput;
import lombok.Getter;

import static app.Admin.users;
import static app.Admin.verification;

@Getter
public final class Host extends User {


    @Override
    public boolean isHost() {
        return true;
    }


    private final ArrayList<Podcast> podcasts;
    private final ArrayList<Announcement> announcements;

    /**
     * Instantiates a new Host.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     * @param online   the online
     * @param type     the type
     */
    public Host(final String username, final int age,
                final String city, final boolean online, final String type) {
        super(username, age, city, online, type);
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
    }

    /**
     * Add podcast.
     */
    public String addPodcast(final CommandInput commandInput) {
        ArrayList<Episode> episodes = new ArrayList<>();
        for (EpisodeInput episodeInput : commandInput.getEpisodes()) {
            episodes.add(new Episode(episodeInput.getName(),
                    episodeInput.getDuration(),
                    episodeInput.getDescription()));
        }
        Podcast podcast = new Podcast(commandInput.getName(),
                commandInput.getUsername(), episodes);
        Admin.addPodcast(podcast);
        if (podcasts.stream().anyMatch(existingPodcast -> existingPodcast
                .getName().equals(podcast.getName()))) {
            return " has another podcast with the same name.";
        } else {
            podcasts.add(podcast);
            return " has added new podcast successfully.";
        }
    }

    /**
     * Remove podcast.
     *
     * @param podcastName the podcast name
     */
    public String removePodcast(final String podcastName) {
        if (podcasts.stream().noneMatch(existingPodcast -> existingPodcast
                .getName().equals(podcastName))) {
            return getUsername() + " doesn't have a podcast with the given name.";
        } else {
            if (podcastPlayed(podcastName).equals("da")) {
                return getUsername() + " can't delete this podcast.";
            } else {
                podcasts.removeIf(existingPodcast -> existingPodcast
                        .getName().equals(podcastName));
                return getUsername() + " deleted the podcast successfully.";
            }
        }
    }


    /**
     * Checks if the specified podcast is currently being played by any user.
     *
     * @param podcastName The name of the podcast to be checked.
     * @return "da" if the podcast is being played, "nu" otherwise.
     */
    public String podcastPlayed(final String podcastName) {
        for (User user : Admin.getUsers()) {
            if (user.getPlayer().getSource() != null
                    && user.getPlayer().getSource().getAudioCollection()
                    .getName().equals(podcastName)) {
                return "da";
            }
        }
        return "nu";
    }

    /**
     * Add announcement.
     */
    public String addAnnouncement(final CommandInput commandInput) {
        Announcement announcement
                = new Announcement(commandInput.getName(), commandInput.getDescription());
        if (announcements.stream().anyMatch(existingAnnouncement -> existingAnnouncement
                .getName().equals(announcement.getName()))) {
            return " has another announcement with the same name.";
        } else {
            announcements.add(announcement);
            return getUsername() + " has successfully added new announcement.";
        }
    }

    /**
     * Remove announcement.
     *
     * @param announcementName the announcement name
     */
    public String removeAnnouncement(final String announcementName) {
        if (announcements.stream().noneMatch(existingAnnouncement -> existingAnnouncement
                .getName().equals(announcementName))) {
            return getUsername() + " has no announcement with the given name.";
        } else {
            announcements.removeIf(existingAnnouncement -> existingAnnouncement
                    .getName().equals(announcementName));
            return getUsername() + " has successfully deleted the announcement.";
        }
    }

    /**
     * Deletes a host from the application based on the provided username.
     *
     * @param username The username of the host to be deleted.
     * @return A message indicating the success or failure of the operation.
     */
    @Override
    public String deleteThis(final String username, final String pageVisit) {
        if (verification(username).equals("podcast") || !pageVisit.equals("no")) {
            return username + " can't be deleted.";
        }

        User user = Admin.getUser(username);

        if (user != null) {
            users.remove(user);
            return username + " was successfully deleted.";
        }

        return "The username " + username + " doesn't exist.";
    }

}
