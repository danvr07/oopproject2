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
import lombok.Getter;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Admin.
 */


//factory pattern
interface PageFactory {
    Page createPage(User user);
}

class ArtistPageFactory implements PageFactory {
    @Override
    public Page createPage(final User user) {
        return new ArtistPage(user);
    }
}

class HostPageFactory implements PageFactory {
    @Override
    public Page createPage(final User user) {
        return new HostPage(user);
    }
}

class HomePageFactory implements PageFactory {
    @Override
    public Page createPage(final User user) {
        return new HomePage(user);
    }
}

public final class Admin {
    @Getter
    public static List<User> users = new ArrayList<>();
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

    /**
     * Adds a new album to the list of albums in the application.
     *
     * @param album The album to be added.
     */
    public static void addAlbum(final Album album) {
        if (albums.stream().anyMatch(existingAlbum -> existingAlbum
                .getName().equals(album.getName()))) {
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

    /**
     * Gets the top 5 artists based on the number of likes.
     *
     * @return The list of top 5 artists.
     */
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

    /**
     * Gets the top 5 albums based on the number of likes.
     *
     * @return The list of top 5 albums.
     */
    public static List<String> getTop5Albums() {
        List<Album> sortedAlbums = new ArrayList<>(getAlbums());
        Set<String> uniqueAlbumNames = new HashSet<>();

        for (Album album : sortedAlbums) {
            if (uniqueAlbumNames.contains(album.getName())) {
                continue;
            }

            for (Song iter : songs) {
                if (iter.getAlbum().equals(album.getName())) {
                    album.setLikes(album.getLikes() + iter.getLikes());
                }
            }
            uniqueAlbumNames.add(album.getName());
        }

        sortedAlbums.sort(Comparator
                .comparingInt(Album::getLikes)
                .reversed()
                .thenComparing(Album::getName));

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

    /**
     * Gets a list of online users.
     *
     * @return The list of online users.
     */
    public static ArrayList<String> getOnlineUsers() {
        ArrayList<String> onlineUsers = new ArrayList<>();
        for (User user : users) {
            if (user.isOnline()) {
                onlineUsers.add(user.getUsername());
            }
        }
        return onlineUsers;
    }

    /**
     * Adds a new user to the application based on the provided information.
     *
     * @param username The username of the new user.
     * @param age      The age of the new user.
     * @param city     The city of the new user.
     * @param online   The online status of the new user.
     * @param type     The type of the new user (e.g., "regular", "artist", "host").
     * @return A message indicating the success or failure of the operation.
     */
    public static String addUser(final String username,
                                 final int age, final String city,
                                 final boolean online, final String type) {
        if (users.stream().anyMatch(existingUser -> existingUser.getUsername().equals(username))) {
            return "The username " + username + " is already taken.";
        } else {
            User user;
            PageFactory pageFactory;
            // in functie de tipul userului se creeaza pagina corespunzatoare
            if (type.equals("artist")) {
                user = new Artist(username, age, city, online, type);
                pageFactory = new ArtistPageFactory();
            } else if (type.equals("host")) {
                user = new Host(username, age, city, online, type);
                pageFactory = new HostPageFactory();
            } else {
                user = new User(username, age, city);
                pageFactory = new HomePageFactory();
            }
            user.setCurrentPage(pageFactory.createPage(user));

            users.add(user);


            return "The username " + username + " has been added successfully.";

        }
    }

    /**
     * Gets all users in the application.
     *
     * @return The list of all users.
     */
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

    /**
     * Deletes a user from the application based on the provided username.
     *
     * @param username The username of the user to be deleted.
     * @return A message indicating the success or failure of the operation.
     */
    public static String deleteAllType(final String username) {
        User user = getUser(username);

        if (user == null) {
            return "The username " + username + " doesn't exist.";
        }
        String pageVisit = getPageVisitStatus(username);
        // In functie de tipul userului se apeleaza metoda corespunzatoare
        // Override
        return user.deleteUser(username, pageVisit);

    }

    /**
     * Gets the current page visit status for a user.
     *
     * @param username The username of the user to be checked.
     * @return "pageVisit" if the user is currently on a page, "noPagevisit" otherwise.
     */
    private static String getPageVisitStatus(final String username) {
        User userToCheck = getUser(username);
        String pageVisit = "no";

        for (User user : users) {
            if (userToCheck.getCurrentPage().equals(user.getCurrentPage())
                    && !user.getName().equals(username)) {
                pageVisit = "yes";
                break;
            }
        }

        return pageVisit;
    }

    /**
     * Checks if a user can be deleted based on the loaded content.
     *
     * @param username The username of the user to be checked.
     * @return A message indicating if the user can be deleted or not.
     */
    public static String verification(final String username) {
        for (User user : users) {

            if (user.getPlayer().getSource() != null && !user.getPlayer().getPaused()) {
                if (user.getPlayer().getSource().getAudioFile().getClass().equals(Song.class)) {
                    Song song = (Song) user.getPlayer().getSource().getAudioFile();
                    // Se verifica daca exista vreun user care aculta
                    // o meldodie a artistului care urmeaza sa fie sters
                    if (song.getArtist().equals(username)) {
                        return "album";
                    } else {
                        // Se verifica daca exista vreun user care asculta
                        // un playlist al userului care urmeaza sa fie sters
                        for (Playlist playlist : Admin.getUser(username).getPlaylists()) {
                            if (playlist.getSongs().stream().anyMatch(song1 -> song1
                                    .getName().equals(user.getPlayer()
                                            .getSource().getAudioFile().getName()))) {
                                return "playlist";
                            }
                        }
                    }
                } else if (user.getPlayer().getSource().getAudioCollection()
                        .getClass().equals(Podcast.class)) {
                    // Se verifica daca exista vreun user care asculta
                    // un podcast al hostului care urmeaza sa fie sters
                    Podcast podcast = (Podcast) user.getPlayer().getSource().getAudioCollection();
                    if (podcast.getOwner().equals(username)) {
                        return "podcast";
                    }
                }
            }
        }
        return "no";
    }

    /**
     * Decreases the number of followers for playlists that were followed by the deleted user.
     *
     * @param deletedUser The user being deleted.
     */
    public static void decrementFollowers(final User deletedUser) {
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

    /**
     * Updates the liked songs for users when an artist is deleted.
     *
     * @param artist   The artist being deleted.
     * @param username The username of the artist.
     */
    public static void updateSongs(final Artist artist, final String username) {
        // Se sterg toate melodiile artistului din lista de melodii
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

    /**
     * Updates the followed playlists for users when a user is deleted.
     *
     * @param deletedUser The user being deleted.
     */
    public static void updateFollowedPlaylists(final User deletedUser) {
        //Se sterg toate urmaririle userului din listele de urmariri
        for (Playlist playlist : deletedUser.getPlaylists()) {
            for (User user : users) {
                user.updateFollowedPlaylistsUser(playlist.getName());
            }
        }
    }

    /**
     * Adds a list of songs to the global list of songs.
     *
     * @param songsList The list of songs to be added.
     */
    public static void addSongs(final List<Song> songsList) {
        for (Song song : songsList) {
            songs.add(song);
        }
    }

    /**
     * Gets a list of artists in the application.
     *
     * @return The list of artists.
     */
    public static ArrayList<Artist> getArtists() {
        ArrayList<Artist> artists = new ArrayList<>();
        for (User user : users) {
            if (user.getType().equals("artist")) {
                artists.add((Artist) user);
            }
        }
        return artists;
    }

    /**
     * Gets a list of albums in the application.
     *
     * @return The list of albums.
     */
    public static ArrayList<Album> getAlbums() {
        ArrayList<Album> albumsArray = new ArrayList<>();
        for (User user : users) {
            if (user.getType().equals("artist")) {
                if ((albumsArray.containsAll(((Artist) user).getAlbums()))) {
                    continue;
                }
                albumsArray.addAll(((Artist) user).getAlbums());
            }
        }
        return albumsArray;
    }

    /**
     * Adds a new podcast to the list of podcasts in the application.
     *
     * @param podcast The podcast to be added.
     */
    public static void addPodcast(final Podcast podcast) {
        podcasts.add(podcast);
    }

    /**
     * Gets a list of hosts in the application.
     *
     * @return The list of hosts.
     */
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
