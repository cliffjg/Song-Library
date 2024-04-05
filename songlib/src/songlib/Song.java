
package songlib;

public class Song {
	
	public String name;
	public String artist;
	public String album;
	public String year;
	
	public Song() {
		
	}
	
	
	public Song(String name, String artist) {
		this.name = name; 
		this.artist = artist;
		
	}
	
	public Song(String name, String artist, String album, String year) {
		this.name = name; 
		this.artist = artist;
		this.album = album;
		this.year = year;
		
	}
	
	public String getName() {
		return name;
	}
	
	
	public String getArtist() {
		return artist;
	}
	
	
	public String toString() {
		return name + "\t\t\t" + artist;
	}
	
	
	
	

}
