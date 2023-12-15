package app.user;


import app.Admin;
import app.audio.Collections.Album;
import app.audio.Files.Song;
import app.info.Event;
import app.info.Merch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import fileio.input.CommandInput;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import static app.Admin.verification;
import static app.Admin.updateSongs;
import static app.Admin.users;

@Getter
@Setter
public class Artist extends User {

    private static final int MIN_DAY = 1;
    private static final int MAX_DAY = 31;
    private static final int MIN_MONTH = 1;
    private static final int MAX_MONTH = 12;
    private static final int MIN_YEAR = 1900;
    private static final int MAX_YEAR = 2023;
    private static final int MONTH_FEB = 2;

    private static final int DAY_FEB = 28;

    private ArrayList<Album> albums;
    private ArrayList<Event> events;

    private ArrayList<Merch> merchs;

    public int likes = 0;

    public int getLikes() {
        return likes;
    }

    public void setLikes(final int likes) {
        this.likes = likes;
    }


    @Override
    public boolean isArtist() {
        return true;
    }


    /**
     * Instantiates a new Artist.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     * @param online   the online
     * @param type     the type
     */
    public Artist(final String username, final int age,
                  final String city, final boolean online, final String type) {
        super(username, age, city, online, type);
        albums = new ArrayList<>();
        events = new ArrayList<>();
        merchs = new ArrayList<>();
    }


    /**
     * Adds a new album based on the provided command input.
     *
     * @param commandInput The input containing information about the album.
     * @return A message indicating the success or failure of the album addition process.
     */
    public String addAlbum(final CommandInput commandInput) {
        List<Song> songsAlbum = new ArrayList<>();
        for (SongInput song : commandInput.getSongs()) {
            songsAlbum.add(new Song(song.getName(), song.getDuration(), song.getAlbum(),
                    song.getTags(), song.getLyrics(), song.getGenre(),
                    song.getReleaseYear(), song.getArtist()));
        }
        Album album = new Album(commandInput.getName(), commandInput.getUsername(),
                commandInput.getReleaseYear(), songsAlbum);
        if (albums.stream().anyMatch(existingAlbum -> existingAlbum.getName()
                .equals(album.getName()))) {
            return " has another album with the same name.";
        } else {
            Set<String> uniqueSongNames = new HashSet<>();
            if (album.getAllSongs().stream().anyMatch(song -> !uniqueSongNames
                    .add(song.getName()))) {
                return " has the same song at least twice in this album.";
            } else {
                Admin.addSongs(album.getAllSongs());
                albums.add(album);
                Admin.addAlbum(album);
                return " has added new album successfully.";
            }
        }
    }

    /**
     * Adds a new event based on the provided command input.
     *
     * @param commandInput The input containing information about the event.
     * @return A message indicating the success or failure of the event addition process.
     */
    public String addEvent(final CommandInput commandInput) {
        String eventName = commandInput.getName();
        String eventDescription = commandInput.getDescription();
        String dateString = commandInput.getDate();
        try {
            // Verificăm validitatea datei
            Date eventDate = validateDate(dateString);
            // System.out.println(eventName);

            // Creăm un nou eveniment și îl adăugăm la lista de evenimente a artistului
            Event event = new Event(eventName, eventDescription, eventDate);
            events.add(event);

            return getUsername() + " has added new event successfully.";
        } catch (ParseException | IllegalArgumentException e) {
            // În cazul în care există o excepție, returnăm un mesaj de eroare
            return "Event for " + getUsername() + " does not have a valid date.";
        }
    }

    /**
     * Removes an event with the specified name.
     *
     * @param eventName The name of the event to be removed.
     * @return A message indicating the success or failure of the event removal process.
     */
    public String removeEvent(final String eventName) {
        for (Event event : events) {
            if (event.getName().equals(eventName)) {
                events.remove(event);
                return getUsername() + " deleted the event successfully.";
            }
        }
        return getUsername() + " doesn't have an event with the given name.";
    }

    /**
     * Validates a date string and converts it into a Date object.
     *
     * @param dateString The string representation of the date to be validated and parsed.
     * @return The parsed Date object.
     * @throws ParseException If the date string is notin the correct format
     *                        or contains invalid values.
     */
    public Date validateDate(final String dateString) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);
        Date date = dateFormat.parse(dateString);

        int day = Integer.parseInt(new SimpleDateFormat("dd").format(date));
        int month = Integer.parseInt(new SimpleDateFormat("MM").format(date));
        int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));

        if (day < MIN_DAY || day > MAX_DAY || month < MIN_MONTH
                || month > MAX_MONTH || year < MIN_YEAR || year > MAX_YEAR) {
            throw new IllegalArgumentException("Invalid date values.");
        }

        // februarie
        if (month == DAY_FEB && day > MONTH_FEB) {
            throw new IllegalArgumentException("Invalid date for February.");
        }

        return date;
    }

    /**
     * Adds new merchandise based on the provided command input.
     *
     * @param commandInput The input containing information about the merchandise.
     * @return A message indicating the success or failure of the merchandise addition process.
     */
    public String addMerch(final CommandInput commandInput) {
        String merchName = commandInput.getName();
        String merchDescription = commandInput.getDescription();
        int merchPrice = commandInput.getPrice();

        if (merchs.stream().anyMatch(existingMerch -> existingMerch.getName().equals(merchName))) {
            return getUsername() + " has merchandise with the same name.";
        } else {
            if (merchPrice < 0) {
                return "Price for merchandise can not be negative.";
            } else {
                merchs.add(new Merch(merchName, merchDescription, merchPrice));
                return getUsername() + " has added new merchandise successfully.";
            }
        }
    }


    /**
     * Formats the given date into a string with the pattern "dd-MM-yyyy".
     *
     * @param date The date to be formatted.
     * @return A string representation of the formatted date.
     */
    public String formatDate(final Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }


    /**
     * Removes an album with the specified name if conditions allow.
     *
     * @param albumName The name of the album to be removed.
     * @return A message indicating the success or failure of the removal process.
     */
    public String removeAlbum(final String albumName) {
        List<String> loadedSongs = new ArrayList<>();

        // Iterăm prin utilizatorii cu player ne-pausat și adăugăm numele melodiilor în loadedSongs
        for (User user : Admin.getUsers()) {
            if (user.getPlayer().getSource() != null && !user.getPlayer().getPaused()) {
                String loadedSongName = user.getPlayer().getSource().getAudioFile().getName();
                if (!loadedSongs.contains(loadedSongName)) {
                    loadedSongs.add(loadedSongName);
                }
            }
        }

        // Verificăm dacă melodiile din loadedSongs se află în albumul cu numele dat
        Album albumToRemove = findAlbumByName(albumName);
        if (albumToRemove != null) {
            for (String loadedSongName : loadedSongs) {
                for (Song song : albumToRemove.getSongs()) {
                    if (song.getName().equals(loadedSongName)
                            || albumToRemove.getName().equals(albumloaded.getName())) {
                        return getUsername() + " can't delete this album.";
                    }
                }
            }
            // Dacă nu s-a găsit nicio melodie încărcată din album, atunci albumul poate fi șters
            Admin.getSongs().removeAll(albumToRemove.getSongs());
            Admin.getAlbums().remove(albumToRemove);
            return getUsername() + "deleted the album successfully.";

        } else {
            return getUsername() + " doesn't have an album with the given name.";
        }
    }

    /**
     * Finds an album with the specified name.
     *
     * @param albumName The name of the album to be found.
     * @return The album with the specified name or null if no such album exists.
     */
    private Album findAlbumByName(final String albumName) {
        for (Artist artist : Admin.getArtists()) {
            for (Album album : artist.getAlbums()) {
                if (album.getName().equals(albumName)) {
                    return album;
                }
            }
        }
        return null;
    }


    /**
     * Deletes an artist from the application based on the provided username.
     *
     * @param username  The username of the artist to be deleted.
     * @param pageVisit The page visit status of the artist.
     * @return A message indicating the success or failure of the operation.
     */
    @Override
    public String deleteThis(final String username, final String pageVisit) {
        String verificationResult = verification(username);

        if ("album".equals(verificationResult) || !"no".equals(pageVisit)) {
            return username + " can't be deleted.";
        }

        User user = Admin.getUser(username);

        if (user != null) {
            Artist artist = (Artist) user;
            updateSongs(artist, username);
            users.remove(user);
            return username + " was successfully deleted.";
        }

        return "The username " + username + " doesn't exist.";
    }

}
