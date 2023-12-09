package app;

import app.audio.Collections.Album;
import app.audio.Collections.PlaylistOutput;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.info.Announcement;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.user.Artist;
import app.user.Host;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import fileio.input.EpisodeInput;
import fileio.input.SongInput;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Command runner.
 */
public final class CommandRunner {
    /**
     * The Object mapper.
     */
    private static ObjectMapper objectMapper = new ObjectMapper();

    private CommandRunner() {
    }

    /**
     * Search object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode search(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }

        if (user.isOnline()) {
            Filters filters = new Filters(commandInput.getFilters());
            String type = commandInput.getType();

            ArrayList<String> results = user.search(filters, type);
            String message = "Search returned " + results.size() + " results";

            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", commandInput.getCommand());
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", message);
            objectNode.put("results", objectMapper.valueToTree(results));

            return objectNode;
        } else {
            ObjectNode objectNode = objectMapper.createObjectNode();
            String message = user.getUsername() + " is offline.";
            objectNode.put("command", commandInput.getCommand());
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", message);
            objectNode.put("results", objectMapper.createArrayNode());
            return objectNode;
        }
    }

    /**
     * Select object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode select(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        if (user == null) {
            return null;
        }

        String message = user.select(commandInput.getItemNumber());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Load object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode load(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }
        String message = user.load();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Play pause object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode playPause(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.playPause();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Repeat object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode repeat(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }
        String message = user.repeat();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Shuffle object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode shuffle(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        Integer seed = commandInput.getSeed();
        String message = user.shuffle(seed);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Forward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode forward(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }
        String message = user.forward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Backward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode backward(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.backward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Like object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode like(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user != null && user.isOnline()) {
            String message = user.like();

            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", commandInput.getCommand());
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", message);

            return objectNode;
        } else if (user != null && !user.isOnline()) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            String message = user.getUsername() + " is offline.";
            objectNode.put("command", commandInput.getCommand());
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", message);
            //  objectNode.put("results", objectMapper.createArrayNode());
            return objectNode;
        } else {
            String errorMessage = "User not found for username: " + commandInput.getUsername();
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("error", errorMessage);
            return errorNode;
        }
    }

    /**
     * Next object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode next(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.next();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Prev object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode prev(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.prev();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Create playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode createPlaylist(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }
        String message = user.createPlaylist(commandInput.getPlaylistName(),
                commandInput.getTimestamp());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Add remove in playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addRemoveInPlaylist(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }
        String message = user.addRemoveInPlaylist(commandInput.getPlaylistId());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Switch visibility object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode switchVisibility(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.switchPlaylistVisibility(commandInput.getPlaylistId());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Show playlists object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showPlaylists(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<PlaylistOutput> playlists = user.showPlaylists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Follow object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode follow(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }
        String message = user.follow();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Status object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode status(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }
        PlayerStats stats = user.getPlayerStats();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("stats", objectMapper.valueToTree(stats));

        return objectNode;
    }

    /**
     * Show liked songs object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showLikedSongs(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<String> songs = user.showPreferredSongs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets preferred genre.
     *
     * @param commandInput the command input
     * @return the preferred genre
     */
    public static ObjectNode getPreferredGenre(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String preferredGenre = user.getPreferredGenre();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(preferredGenre));

        return objectNode;
    }

    public static ObjectNode addUser(CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        String message = Admin.addUser(commandInput.getUsername(), commandInput.getAge(), commandInput.getCity(), false, commandInput.getType());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);


        return objectNode;

    }

    public static ObjectNode addAlbum(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        if (user != null) {
            if (user.getType().equals("artist")) {
                Artist artist = (Artist) user;
                Admin.addSongs(commandInput.getSongs());
                Album album = new Album(commandInput.getName(), commandInput.getUsername(), commandInput.getReleaseYear(), commandInput.getSongs());
                objectNode.put("command", commandInput.getCommand());
                objectNode.put("user", commandInput.getUsername());
                objectNode.put("timestamp", commandInput.getTimestamp());
                objectNode.put("message", commandInput.getUsername() + artist.addAllbum(album));

            } else {

                objectNode.put("message", "The username " + commandInput.getUsername() + " is not an artist.");
            }
        }


        return objectNode;
    }

    public static ObjectNode addPodcast(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user != null) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            if (user.getType().equals("host")) {
               // System.out.println("da");
                Host host = (Host) user;

                ArrayList<Episode> episodes = new ArrayList<>();
                for (EpisodeInput episodeInput : commandInput.getEpisodes()) {
                    episodes.add(new Episode(episodeInput.getName(),
                            episodeInput.getDuration(),
                            episodeInput.getDescription()));
                }
                Podcast podcast = new Podcast(commandInput.getName(), commandInput.getUsername(), episodes);
                Admin.addPodcast(podcast);
                objectNode.put("command", commandInput.getCommand());
                objectNode.put("user", commandInput.getUsername());
                objectNode.put("timestamp", commandInput.getTimestamp());
                objectNode.put("message", commandInput.getUsername() + host.addPodcast(podcast));
                return objectNode;
            } else {
                objectNode.put("command", commandInput.getCommand());
                objectNode.put("user", commandInput.getUsername());
                objectNode.put("timestamp", commandInput.getTimestamp());
                objectNode.put("message", commandInput.getUsername() + " is not a host.");
                return objectNode;
            }
        } else {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("message", "The username " + commandInput.getUsername() + " doesn't exist.");
            return  objectNode;
        }


    }



    public static ObjectNode getAllUsers(CommandInput commandInput) {
        List<String> users = Admin.getAllUsers();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(users));

        return objectNode;
    }

    public static ObjectNode deleteUser(CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        String message = Admin.deleteUser(commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }


    public static ObjectNode printCurrentPage(CommandInput commandInput) {


        ObjectNode objectNode = objectMapper.createObjectNode();
        User user = Admin.getUser(commandInput.getUsername());
        if (user.isOnline()) {
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("command", commandInput.getCommand());
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", user.getCurrentPage().printPage());

            return objectNode;
        } else {
            String message = user.getUsername() + " is offline.";
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("command", commandInput.getCommand());
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", message);

            return objectNode;

        }
    }

    public static ObjectNode removeAnnouncement(CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        User user = Admin.getUser(commandInput.getUsername());
        if (user != null) {
            if (user.getType().equals("host")) {
                Host host = (Host) user;
                String message = host.removeAnnouncement(commandInput.getName());
                objectNode.put("command", commandInput.getCommand());
                objectNode.put("user", commandInput.getUsername());
                objectNode.put("timestamp", commandInput.getTimestamp());
                objectNode.put("message", message);
                return objectNode;
            } else {
                objectNode.put("command", commandInput.getCommand());
                objectNode.put("user", commandInput.getUsername());
                objectNode.put("timestamp", commandInput.getTimestamp());
                objectNode.put("message", commandInput.getUsername() + " is not a host.");
                return objectNode;
            }
        } else {
            objectNode.put("command", commandInput.getCommand());
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", "The username " + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }


    }

    public static ObjectNode showPodcasts(CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        User user = Admin.getUser(commandInput.getUsername());
        if (user != null) {
            if (user.getType().equals("host")) {
                Host host = (Host) user;

                objectNode.put("command", commandInput.getCommand());
                objectNode.put("user", commandInput.getUsername());
                objectNode.put("timestamp", commandInput.getTimestamp());

                ArrayNode resultArray = objectMapper.createArrayNode();
                List<Podcast> podcasts = host.getPodcasts();
                for (Podcast podcast : podcasts) {
                    ObjectNode podcastNode = objectMapper.createObjectNode();
                    podcastNode.put("name", podcast.getName());

                    ArrayNode episodesArray = objectMapper.createArrayNode();
                    for (Episode episode : podcast.getEpisodes()) {
                        episodesArray.add(episode.getName());
                    }
                    podcastNode.put("episodes", episodesArray);
                    resultArray.add(podcastNode);
                }
                objectNode.set("result", resultArray);
                return objectNode;
            } else {
                objectNode.put("command", commandInput.getCommand());
                objectNode.put("user", commandInput.getUsername());
                objectNode.put("timestamp", commandInput.getTimestamp());
                objectNode.put("message", commandInput.getUsername() + " is not a host.");
                return objectNode;
            }
        } else {
            objectNode.put("command", commandInput.getCommand());
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", "The username " + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }
    }

    public static ObjectNode showAlbums(CommandInput commandInput) {

        Artist artist = (Artist) Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        ArrayNode resultArray = objectMapper.createArrayNode();
        List<Album> albums = artist.getAlbums();
        for (Album album : albums) {
            ObjectNode albumNode = objectMapper.createObjectNode();
            albumNode.put("name", album.getName());

            ArrayNode songsArray = objectMapper.createArrayNode();
            for (SongInput song : album.getAllSongs()) {
                songsArray.add(song.getName());
            }
            albumNode.put("songs", songsArray);

            resultArray.add(albumNode);
        }

        objectNode.set("result", resultArray);

        return objectNode;
    }

    public static ObjectNode addEvent(CommandInput commandInput) {

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        User user = Admin.getUser(commandInput.getUsername());
        if (user != null) {
            if (user.getType().equals("artist")) {
                Artist artist = (Artist) user;
                String message = artist.addEvent(commandInput.getName(), commandInput.getDescription(), commandInput.getDate());
//                for(Event event: artist.getEvents()) {
//                    System.out.println(event.getName() + " " + event.getDescription() + " " + event.getDate());
//                }

                objectNode.put("message", message);
            } else {
                objectNode.put("message", commandInput.getUsername() + " is not an artist.");
            }
        } else {
            objectNode.put("message", "The username " + commandInput.getUsername() + " doesn't exist.");
        }


        return objectNode;
    }

    public static ObjectNode addAnnouncement(CommandInput commandInput) {

            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", commandInput.getCommand());
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("timestamp", commandInput.getTimestamp());
            User user = Admin.getUser(commandInput.getUsername());
            if (user != null) {
                if (user.getType().equals("host")) {
                    Host host = (Host) user;
                    Announcement announcement = new Announcement(commandInput.getName(), commandInput.getDescription());
                    String message = host.addAnnouncement(announcement);
                    objectNode.put("message", message);
                }
            }
            return objectNode;
    }

    public static ObjectNode addMerch(CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        User user = Admin.getUser(commandInput.getUsername());

        if (user != null) {
            if (user.getType().equals("artist")) {
                Artist artist = (Artist) user;
                String message = artist.addMerch(commandInput.getName(), commandInput.getDescription(), commandInput.getPrice());

                objectNode.put("message", message);
            } else {
                objectNode.put("message", commandInput.getUsername() + " is not an artist.");
            }
        } else {
            objectNode.put("message", "The username " + commandInput.getUsername() + " doesn't exist.");
        }

        return objectNode;


    }

    public static ObjectNode switchConnectionStatus(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        // System.out.println();
        ObjectNode objectNode = objectMapper.createObjectNode();
        if (user != null) {
            user.switchConnectionStatus();

            objectNode.put("command", commandInput.getCommand());
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", commandInput.getUsername() + " has changed status successfully.");

        } else {
            objectNode.put("command", commandInput.getCommand());
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", "The username " + commandInput.getUsername() + " doesn't exist.");
        }
        return objectNode;
    }

    public static ObjectNode getOnlineUsers(CommandInput commandInput) {
        List<String> onlineUsers = Admin.getOnlineUsers();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(onlineUsers));

        return objectNode;
    }


    /**
     * Gets top 5 songs.
     *
     * @param commandInput the command input
     * @return the top 5 songs
     */
    public static ObjectNode getTop5Songs(final CommandInput commandInput) {
        List<String> songs = Admin.getTop5Songs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets top 5 playlists.
     *
     * @param commandInput the command input
     * @return the top 5 playlists
     */
    public static ObjectNode getTop5Playlists(final CommandInput commandInput) {
        List<String> playlists = Admin.getTop5Playlists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }
}
