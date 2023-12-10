package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import fileio.input.SongInput;

import java.util.ArrayList;
import java.util.List;

public class Album extends AudioCollection {
    private String name;
    private String artist;
    private int releaseYear;
    private List<SongInput> songs;

    private int likes;

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getLikes() {
        return likes;
    }

    public List<SongInput> getSongs() {
        return songs;
    }

    public Album(String name, String owner, int releaseYear, List<SongInput> songs) {
        super(name, owner);
        this.releaseYear = releaseYear;
        this.songs = songs;
    }

    public List<SongInput> getAllSongs() {
        return songs;
    }


    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(int index) {
        List<Song> songGet = new ArrayList<>();
        for (SongInput song : songs) {
            songGet.add(new Song(song.getName(), song.getDuration(), song.getAlbum(),
                    song.getTags(), song.getLyrics(), song.getGenre(),
                    song.getReleaseYear(), song.getArtist()));

        }
        return songGet.get(index);
    }
}
