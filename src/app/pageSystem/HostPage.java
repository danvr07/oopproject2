package app.pageSystem;

import app.Admin;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.info.Announcement;
import app.user.Host;
import app.user.User;

import java.util.List;


public class HostPage implements Page {

    private String username;

    public HostPage(final User user) {
        username = user.getUsername();
    }

    @Override
    public String printPage() {
        Host host = (Host) Admin.getUser(username);

        StringBuilder result = new StringBuilder();

        result.append("Podcasts:\n\t[");
        List<Podcast> podcasts = host.getPodcasts();
        for (Podcast podcast : podcasts) {
            result.append(String.format("%s:\n\t[", podcast.getName()));

            List<Episode> episodes = podcast.getEpisodes();
            for (Episode episode : episodes) {
                result.append(String.format("%s - %s, ", episode
                        .getName(), episode.getDescription()));
            }

            if (!episodes.isEmpty()) {
                result.delete(result.length() - 2, result.length());
            }

            result.append("]\n, ");
        }

        if (!podcasts.isEmpty()) {
            result.delete(result.length() - 2, result.length());
        }

        result.append("]\n\n");

        result.append("Announcements:\n\t[");
        List<Announcement> announcements = host.getAnnouncements();
        for (Announcement announcement : announcements) {
            result.append(String.format("%s:\n\t%s\n, ", announcement
                    .getName(), announcement.getDescription()));
        }

        if (!announcements.isEmpty()) {
            result.delete(result.length() - 2, result.length());
        }

        result.append("]");

        return result.toString();
    } // am cerut ajutor de la IA aici
}
