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

public class Artist extends User {

    private static final int MIN_DAY = 1;
    private static final int MAX_DAY = 31;
    private static final int MIN_MONTH = 1;
    private static final int MAX_MONTH = 12;
    private static final int MIN_YEAR = 1900;
    private static final int MAX_YEAR = 2023;
    private static final int MONTH_FEB = 2;

    private static final int DAY_FEB = 28;

    ArrayList<Album> albums;
    ArrayList<Event> events;

    ArrayList<Merch> merchs;

    public int likes = 0;

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }


    @Override
    public boolean isArtist() {
        return true;
    }


    public Artist(String username, int age, String city, boolean online, String type) {
        super(username, age, city, online, type);
        albums = new ArrayList<>();
        events = new ArrayList<>();
        merchs = new ArrayList<>();
    }


    public String addAlbum(Album album) {
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

    public String addEvent(String eventName, String eventDescription, String dateString) {
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

    public String removeEvent(String eventName) {
        for (Event event : events) {
            if (event.getName().equals(eventName)) {
                events.remove(event);
                return getUsername() + " deleted the event successfully.";
            }
        }
        return getUsername() + " doesn't have an event with the given name.";
    }

    public Date validateDate(String dateString) throws ParseException {

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

    public String addMerch(String merchName, String merchDescription, int merchPrice) {
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

    public String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }

    public ArrayList<Merch> getMerchs() {
        return merchs;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public String removeAlbum(String albumName) {
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

    // Metoda pentru a găsi un album după nume
    private Album findAlbumByName(String albumName) {
        for (Artist artist : Admin.getArtists()) {
            for (Album album : artist.getAlbums()) {
                if (album.getName().equals(albumName)) {
                    return album;
                }
            }
        }
        return null;
    }

}
