package app;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.pageSystem.ArtistPage;
import app.pageSystem.HomePage;
import app.pageSystem.HostPage;
import app.pageSystem.Page;
import app.user.Artist;
import app.user.Host;
import app.user.User;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static app.user.User.lastloaded;
import static app.user.User.userloaded;

/**
 * The type Admin.
 */
public final class Admin {
    private static List<User> users = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    private static int timestamp = 0;
    private static final int LIMIT = 5;


    private Admin() {
    }

    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public static void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
        }
    }

    /**
     * Sets songs.
     *
     * @param songInputList the song input list
     */
    public static void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }


    /**
     * Sets podcasts.
     *
     * @param podcastInputList the podcast input list
     */
    public static void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(),
                        episodeInput.getDuration(),
                        episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    /**
     * Gets songs.
     *
     * @return the songs
     */
    public static List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    /**
     * Gets podcasts.
     *
     * @return the podcasts
     */
    public static List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public static List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @return the user
     */
    public static User getUser(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Update timestamp.
     *
     * @param newTimestamp the new timestamp
     */
    public static void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            user.simulateTime(elapsed);
        }
    }

    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public static List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= LIMIT) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * Gets top 5 playlists.
     *
     * @return the top 5 playlists
     */
    public static List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= LIMIT) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    public static ArrayList<String> getOnlineUsers() {
        ArrayList<String> onlineUsers = new ArrayList<>();
        for (User user : users) {
            if (user.isOnline()) {
                onlineUsers.add(user.getUsername());
            }
        }
        return onlineUsers;
    }

    public static String addUser(String username, int age, String city, boolean online, String type) {
        if (users.stream().anyMatch(existingUser -> existingUser.getUsername().equals(username))) {
            return "The username " + username + " is already taken.";
        } else {
            User user;
            if (type.equals("artist")) {
                user = new Artist(username, age, city, online, type);
                user.setCurrentPage(new ArtistPage(user));
                // user.setCurrentPage(new HomePage(user));
            } else if (type.equals("host")) {
                user = new Host(username, age, city, online, type);
                user.setCurrentPage(new HostPage(user));
            } else {
                user = new User(username, age, city);
                user.setCurrentPage(new HomePage(user));

            }


            users.add(user);


            return "The username " + username + " has been added successfully.";

        }
    }

    public static List<User> getUsers() {
        return users;
    }

    public static List<String> getAllUsers() {
        List<String> allUsers = new ArrayList<>();

        for (User user : users) {
            if (user.getType().equals("regular"))
                allUsers.add(user.getUsername());
        }

        for (User user : users) {
            if (user.getType().equals("artist"))
                allUsers.add(user.getUsername());
        }

        for (User user : users) {
            if (user.getType().equals("host"))
                allUsers.add(user.getUsername());
        }

        return allUsers;
    }

    public static String deleteUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {

                // System.out.println(user.getType());
                if (user.getType().equals("artist")) {
                    Artist artist = (Artist) user;
                    String arstist = ((Song) lastloaded).getArtist();

                    // userloaded.getPlayer().getPaused()

                    // arstist.equals(username) &&

                    if (userloaded.getPlayer().getPaused()) {
//                        getAlbums().removeIf(album -> album.getOwner().equals(username));
//                        songs.removeIf(song -> song.getArtist().equals(username));

//                        for(Album album : artist.getAlbums()){
//                            for(SongInput song : album.getAllSongs()){
//                                updateLikedSongs(song.getName());
//                            }
//                        }
                        updateSongs(artist, username);


                        users.remove(user);
                        return username + " was successfully deleted.";
                    } else {
                        return username + " can't be deleted.";
                    }
                } else {
                    for (Playlist playlist : user.getPlaylists()) {
                        updateFollowedPlaylists(playlist.getName());
                    }
                    users.remove(user);
                    return username + " was successfully deleted.";
                }


            }
        }
        return "The user " + username + " does not exist.";
    }

//    public static String showPodcasts( String username) {
//        for (User user : users) {
//            if (user.getUsername().equals(username)) {
//                Host host = (Host) user;
//                List<String> result = new ArrayList<>();
//                for (Podcast podcast : host.getPodcasts()) {
//
//                }
//                return result.toString();
//
//            }
//        }
//        return "The user " + username + " does not exist.";
//    }

    public static void updateSongs(Artist artist, String username) {
        getAlbums().removeIf(album -> album.getOwner().equals(username));
        songs.removeIf(song -> song.getArtist().equals(username));

        for (Album album : artist.getAlbums()) {
            for (SongInput song : album.getAllSongs()) {
                for (User user : users) {
                    user.updateLikedSongs(song.getName());
                }
            }
        }
    }

    public static void updateFollowedPlaylists(String playlistName) {
        for (User user : users) {
            user.updateFollowedPlaylists(playlistName);
        }
    }

    public static void addSongs(List<SongInput> songsInputList) {
        for (SongInput songInput : songsInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    public static ArrayList<Artist> getArtists() {
        ArrayList<Artist> artists = new ArrayList<>();
        for (User user : users) {
            if (user.getType().equals("artist")) {
                artists.add((Artist) user);
            }
        }
        return artists;
    }

    public static ArrayList<Album> getAlbums() {
        ArrayList<Album> albums = new ArrayList<>();
        for (User user : users) {
            if (user.getType().equals("artist")) {
                albums.addAll(((Artist) user).getAlbums());
            }
        }
        return albums;
    }

    public static void addPodcast(Podcast podcast) {
        podcasts.add(podcast);
    }

    public static ArrayList<Host> getHosts() {
        ArrayList<Host> hosts = new ArrayList<>();
        for (User user : users) {
            if (user.getType().equals("host")) {
                hosts.add((Host) user);
            }
        }
        return hosts;
    }


    /**
     * Reset.
     */
    public static void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        timestamp = 0;
    }
}
