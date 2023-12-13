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

import java.util.*;
import java.util.stream.Collectors;

import static app.user.User.*;

/**
 * The type Admin.
 */
public final class Admin {
    private static List<User> users = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();

    private static List<Album> albums = new ArrayList<>();
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

    public static void addAlbum(Album album) {
        if (albums.stream().anyMatch(existingAlbum -> existingAlbum.getName().equals(album.getName()))) {
            return;
        }
        albums.add(album);
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

    public static List<String> getTop5Artists() {
        List<Artist> sortedArtists = new ArrayList<>(getArtists());
        for (Artist artist : sortedArtists) {
            for (Song iter : songs) {
                if (iter.getArtist().equals(artist.getUsername())) {
                    artist.setLikes(artist.getLikes() + iter.getLikes());
                }
            }
        }
        sortedArtists.sort(Comparator.comparingInt(Artist::getLikes).reversed());
        List<String> topArtists = new ArrayList<>();
        int count = 0;
        for (Artist artist : sortedArtists) {
            if (count >= LIMIT) {
                break;
            }
            topArtists.add(artist.getUsername());
            count++;
        }
        return topArtists;
    }

    public static List<String> getTop5Albums() {
        List<Album> sortedAlbums = new ArrayList<>(getAlbums());
        Set<String> uniqueAlbumNames = new HashSet<>();

        for (Album album : sortedAlbums) {
            if (uniqueAlbumNames.contains(album.getName())) {
                continue;  // Skip processing if the album name has already been encountered
            }

            for (Song iter : songs) {
                if (iter.getAlbum().equals(album.getName())) {
                    album.setLikes(album.getLikes() + iter.getLikes());
                }
            }

            uniqueAlbumNames.add(album.getName());  // Mark the album name as encountered
        }

        sortedAlbums.sort(Comparator
                .comparingInt(Album::getLikes)
                .reversed()
                .thenComparing(Album::getName));  // Secondary sort lexicographically

        List<String> topAlbums = new ArrayList<>();

        int count = 0;
        for (Album album : sortedAlbums) {
            if (count >= LIMIT) {
                break;
            }
            topAlbums.add(album.getName());
            count++;
        }
        return topAlbums;
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

    public static String addUser(String username,
                                 int age, String city, boolean online, String type) {
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
            if (user.getType().equals("regular")) {
                allUsers.add(user.getUsername());
            }
        }

        for (User user : users) {
            if (user.getType().equals("artist")) {
                allUsers.add(user.getUsername());
            }
        }

        for (User user : users) {
            if (user.getType().equals("host")) {
                allUsers.add(user.getUsername());
            }
        }

        return allUsers;
    }


    public static String deleteUser(String username) {
        User user1 = getUser(username);
        String pageVisit = "noPagevisit";

        for (User user : users) {
            if (user1.getCurrentPage().equals(user.getCurrentPage()) && !user.getName().equals(username)) {
                pageVisit = user1.getName();
            }
        }
        if (user1.getType().equals("artist")) {
            String verificare = verificare(username);

            if (verificare.equals("album")) {
                return username + " can't be deleted.";
            } else {
                if (!pageVisit.equals("noPagevisit")) {
                    return username + " can't be deleted.";
                } else {
                    for (User user : users) {
                        if (user.getUsername().equals(username)) {
                            Artist artist = (Artist) user;
                            updateSongs(artist, username);
                            users.remove(user);
                            return username + " was successfully deleted.";
                        }
                    }
                }

            }
        } else if (user1.getType().equals("host")) {
            if (verificare(username).equals("podcast")) {
                return username + " can't be deleted.";
            } else {
                if (!pageVisit.equals("noPagevisit")) {
                    return username + " can't be deleted.";
                } else {
                    for (User user : users) {
                        if (user.getUsername().equals(username)) {
                            Host host = (Host) user;
                            users.remove(user);
                            return username + " was successfully deleted.";
                        }
                    }
                }

            }
        } else {
            if (verificare(username).equals("playlist")) {
                return username + " can't be deleted.";
            } else {
                for (User user : users) {
                    if (user.getUsername().equals(username)) {
                        updateFollowedPlaylists(user);
                        decrementFollowers(user);
                        //  users.remove(user);
                        return username + " was successfully deleted.";
                    }
                }
            }
//
        }

        return "The user";


    }

    public static String verificare(String username) {
        for (User user : users) {
            if (user.getPlayer().getSource() != null && !user.getPlayer().getPaused()) {
                //   System.out.println(timestamp);
                if (user.getPlayer().getSource().getAudioFile().getClass().equals(Song.class)) {
                    Song song = (Song) user.getPlayer().getSource().getAudioFile();
                    if (song.getArtist().equals(username)) {
                        //System.out.println(timestamp);
                        return "album";
                    } else {
                        for (Playlist playlist : Admin.getUser(username).getPlaylists()) {
                            if (playlist.getSongs().stream().anyMatch(song1 -> song1.getName().equals(user.getPlayer().getSource().getAudioFile().getName()))) {
                                return "playlist";
                            }
                        }
                    }
                } else if (user.getPlayer().getSource().getAudioCollection().getClass().equals(Podcast.class)) {
                    Podcast podcast = (Podcast) user.getPlayer().getSource().getAudioCollection();
                    if (podcast.getOwner().equals(username)) {
                        return "podcast";
                    }
                } else {
                    Song songCheck = (Song) user.getPlayer().getSource().getAudioFile();
                    // if (songCheck.getArtist().equals(username)) {
                    //  String OwnerSong = ((Song) user.getPlayer().getSource().getAudioFile()).getArtist();

                    for (Playlist playlist : Admin.getUser(username).getPlaylists()) {
                        System.out.println(1);
                        if (playlist.getSongs().stream().anyMatch(song -> song.getName().equals(user.getPlayer().getSource().getAudioFile().getName()))) {
                            System.out.println(1);
                            return "da";
                        }
                    }


                }
            }
        }
        return "nu";
    }


    public static void decrementFollowers(User deletedUser) {
        for (User user : users) {
            for (Playlist playlist : deletedUser.getFollowedPlaylists()) {
                for (Playlist playlist1 : user.getPlaylists()) {
                    if (playlist.getName().equals(playlist1.getName())) {
                        playlist1.setFollowers(playlist1.getFollowers() - 1);
                    }
                }
            }
        }
    }

    public static void updateSongs(Artist artist, String username) {
        getAlbums().removeIf(album -> album.getOwner().equals(username));
        songs.removeIf(song -> song.getArtist().equals(username));

        for (Album album : artist.getAlbums()) {
            for (Song song : album.getAllSongs()) {
                for (User user : users) {
                    user.updateLikedSongs(song.getName());
                }
            }
        }
    }

    public static void updateFollowedPlaylists(User deletedUser) {
        for (Playlist playlist : deletedUser.getPlaylists()) {
            for (User user : users) {
                user.updateFollowedPlaylists(playlist.getName());
            }
        }
    }

    public static void addSongs(List<Song> songsList) {
        for (Song song : songsList) {
            songs.add(song);
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
                if ((albums.containsAll(((Artist) user).getAlbums()))) {
                    continue;
                }
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
