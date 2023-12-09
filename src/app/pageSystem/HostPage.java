package app.pageSystem;

import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.info.Announcement;
import app.user.Host;
import app.user.User;

import java.util.List;

import static app.Admin.getUser;

public class HostPage extends Page {

    String username;

    List<Podcast> podcasts;

    List<Announcement> announcements;

    public HostPage(User user) {
        super(user);
        username = user.getUsername();
        podcasts = ((Host) user).getPodcasts();
        announcements = ((Host) user).getAnnouncements();
    }


    @Override
    public String printPage() {
        Host host = (Host) getUser(username);

        StringBuilder result = new StringBuilder();

        result.append("Podcasts:\n\t[");
        List<Podcast> podcasts = host.getPodcasts();
        for (Podcast podcast : podcasts) {
            result.append(String.format("%s:\n\t[", podcast.getName()));

            List<Episode> episodes = podcast.getEpisodes();
            for (Episode episode : episodes) {
                result.append(String.format("%s - %s, ", episode.getName(), episode.getDescription()));
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
            result.append(String.format("%s:\n\t%s\n, ", announcement.getName(), announcement.getDescription()));
        }

        if (!announcements.isEmpty()) {
            result.delete(result.length() - 2, result.length());
        }

        result.append("]");

        return result.toString();
    } // am cerut ajutor de la IA aici
}
