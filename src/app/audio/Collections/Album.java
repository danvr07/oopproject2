package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;

import java.util.List;

public class Album extends AudioCollection {
    private String name;
    private String artist;
    private int releaseYear;
    private List<Song> songs;

    private int likes;

    /**
     * Sets the number of likes for the album.
     *
     * @param likes The number of likes to set.
     */
    public void setLikes(final int likes) {
        this.likes = likes;
    }

    /**
     * Gets the number of likes for the album.
     *
     * @return The number of likes.
     */
    public int getLikes() {
        return likes;
    }

    public List<Song> getSongs() {
        return songs;
    }

    /**
     * Instantiates a new Album.
     *
     * @param name        the name
     * @param owner       the owner
     * @param releaseYear the release year
     * @param songs       the songs
     */
    public Album(final String name, final String owner,
                 final int releaseYear, final List<Song> songs) {
        super(name, owner);
        this.releaseYear = releaseYear;
        this.songs = songs;
    }

    /**
     * Gets all the songs in the album.
     *
     * @return The list of all songs in the album.
     */
    public List<Song> getAllSongs() {
        return songs;
    }


    /**
     * Gets the number of tracks (songs) in the album.
     *
     * @return The number of tracks in the album.
     */
    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    /**
     * Gets the audio file (song) at the specified index.
     *
     * @param index The index of the song.
     * @return The audio file (song) at the specified index.
     */
    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }
}
