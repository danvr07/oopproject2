package app.user;


import app.audio.Collections.Album;
import app.info.Event;
import app.info.Merch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Artist extends User  {

    ArrayList<Album> albums;
    ArrayList<Event> events;

    ArrayList<Merch> merchs;


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


    public String addAllbum(Album album) {
        if (albums.stream().anyMatch(existingAlbum -> existingAlbum.getName().equals(album.getName()))) {
            return " has another album with the same name.";
        } else {
            Set<String> uniqueSongNames = new HashSet<>();
            if (album.getAllSongs().stream().anyMatch(song -> !uniqueSongNames.add(song.getName()))) {
                return " has the same song at least twice in this album.";
            } else {
                albums.add(album);
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

    public Date validateDate(String dateString) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);
        Date date = dateFormat.parse(dateString);

        int day = Integer.parseInt(new SimpleDateFormat("dd").format(date));
        int month = Integer.parseInt(new SimpleDateFormat("MM").format(date));
        int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));

        if (day < 1 || day > 31 || month < 1 || month > 12 || year < 1900 || year > 2023) {
            throw new IllegalArgumentException("Invalid date values.");
        }

        // februarie
        if (month == 2 && day > 28) {
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

    public String AlbumsNames() {
        String names = "";
        for (Album album : getAlbums()) {
            names += album.getName() + "\n";
        }
        return names;
    }

}