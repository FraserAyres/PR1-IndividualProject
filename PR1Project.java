import java.io.*; 
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Song {
    private String artist;
    private String title;
    private int playCount;

    public Song(String artist, String title, int playCount) {
        this.artist = artist;
        this.title = title;
        this.playCount = playCount;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int newPlayCount) {
        this.playCount = newPlayCount;
    }

    @Override
    public String toString() {
        return title + " by " + artist + " | Streams: " + playCount;
    }

    public String toFileString() {
        return artist + "," + title + "," + playCount;
    }

    
    public static Song fromFileString(String line) {
        String[] parts = line.split(",");
        return new Song(parts[0], parts[1], Integer.parseInt(parts[2]));
    }
}

public class PR1Project {

    private List<Song> songs;
    private final String FILE_NAME = "songs.txt";

    public PR1Project() {
        songs = new ArrayList<>();
        loadSongsFromFile();
    }

    private void loadSongsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                songs.add(Song.fromFileString(line));
            }
        } catch (IOException e) {
            System.out.println("No previous songs found, starting with an empty list.");
        }
    }

    private void saveSongsToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Song song : songs) {
                bw.write(song.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving songs to file.");
        }
    }

    public void addSong(String artist, String title, int playCount) {
        Song newSong = new Song(artist, title, playCount);
        songs.add(newSong);
        saveSongsToFile(); 
        System.out.println("Added: " + title + " by " + artist + " with " + playCount + " global plays.");
    }

    public void removeSong(String title) {
        songs.removeIf(song -> song.getTitle().equalsIgnoreCase(title));
        saveSongsToFile(); 
        System.out.println("Removed: " + title);
    }

    public void printAllSongs() {
        if (songs.isEmpty()) {
            System.out.println("No songs available.");
            return;
        }
        System.out.println("\nAll Songs:");
        for (Song song : songs) {
            System.out.println(song);
        }
    }

    public void printSongsAbovePlayCount(int playCount) {
        boolean found = false;
        System.out.println("\nSongs with more than " + playCount + " plays:");
        for (Song song : songs) {
            if (song.getPlayCount() > playCount) {
                System.out.println(song);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No songs found with more than " + playCount + " plays.");
        }
    }

    public void updateStreams(String title, int newPlayCount) {
        for (Song song : songs) {
            if (song.getTitle().equalsIgnoreCase(title)) {
                song.setPlayCount(newPlayCount);
                saveSongsToFile(); 
                System.out.println("Updated the play count for " + title + " to " + newPlayCount + " plays.");
                return;
            }
        }
        System.out.println("Song not found.");
    }

    public static void main(String[] args) {
        PR1Project app = new PR1Project();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nPR1 Individual Project - Fraser Ayres");
            System.out.println("1. Add a song");
            System.out.println("2. Remove a song");
            System.out.println("3. List all songs");
            System.out.println("4. List songs with more than a certain number of plays");
            System.out.println("5. Update global play count of a song");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.print("Enter artist name: ");
                    String artist = scanner.nextLine();
                    System.out.print("Enter song title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter the global play count (streams): ");
                    int playCount = scanner.nextInt();
                    scanner.nextLine(); 
                    app.addSong(artist, title, playCount);
                    break;

                case 2:
                    System.out.print("Enter song title to remove: ");
                    String titleToRemove = scanner.nextLine();
                    app.removeSong(titleToRemove);
                    break;

                case 3:
                    app.printAllSongs();
                    break;

                case 4:
                    System.out.print("Enter the number of streams: ");
                    int thresholdPlays = scanner.nextInt();
                    scanner.nextLine(); 
                    app.printSongsAbovePlayCount(thresholdPlays);
                    break;

                case 5:
                    System.out.print("Enter song title to update play count: ");
                    String songToUpdate = scanner.nextLine();
                    System.out.print("Enter the new global play count: ");
                    int newPlayCount = scanner.nextInt();
                    scanner.nextLine(); 
                    app.updateStreams(songToUpdate, newPlayCount);
                    break;

                case 0:
                    System.out.println("Exiting the application.");
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 0);

        scanner.close();
    }
}
