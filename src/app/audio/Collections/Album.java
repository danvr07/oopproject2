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
    private List<Song> songs;

    private int likes;

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getLikes() {
        return likes;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public Album(String name, String owner, int releaseYear, List<Song> songs) {
        super(name, owner);
        this.releaseYear = releaseYear;
        this.songs = songs;
    }

    public List<Song> getAllSongs() {
        return songs;
    }


    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(int index) {
      //  List<Song> songGet = new ArrayList<>();
//        for (SongInput song : songs) {
//            songGet.add(new Song(song.getName(), song.getDuration(), song.getAlbum(),
//                    song.getTags(), song.getLyrics(), song.getGenre(),
//                    song.getReleaseYear(), song.getArtist()), song.getLikes());
//
//        }
      //  return songGet.get(index);
        return songs.get(index);
    }
}
