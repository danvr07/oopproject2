package fileio.input;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Command input.
 */
@Getter
@Setter
public final class CommandInput {
    private String command;
    private String username;
    private String city;
    private Integer timestamp;
    private String type;
    private FiltersInput filters;
    private Integer itemNumber;
    private Integer repeatMode;
    private Integer playlistId;
    private String playlistName;
    private Integer seed;
    private String description;
    private Integer releaseYear;
    private String name;
    private Integer age;
    private Integer price;
    private String nextPage;
    private List<EpisodeInput> episodes;
    private List<SongInput> songs;
    private String date;
    public CommandInput() {
    }

    @Override
    public String toString() {
        return "CommandInput{"
                + "command='" + command + '\''
                + ", username='" + username + '\''
                + ", timestamp=" + timestamp
                + ", type='" + type + '\''
                + ", filters=" + filters
                + ", itemNumber=" + itemNumber
                + ", repeatMode=" + repeatMode
                + ", playlistId=" + playlistId
                + ", playlistName='" + playlistName + '\''
                + ", seed=" + seed
                + '}';
    }
}
