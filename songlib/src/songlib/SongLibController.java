

package songlib;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SongLibController {
	
	@FXML
    private Button addButton;

    @FXML
    private TextField albumTextField;

    @FXML
    private TextField artistTextField;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Button saveButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField yearTextField;
    
    @FXML
    private ListView listView;
    
    
    private static ObservableList<Song> obsList;
    
    public Song song;
    

    public void start() throws IOException {
    	
    	File file = new File("DataFile.txt");
    	
    	//if file exist and not empty select first item
    	//if file exist but is empty disable to listView screen
    	if(file.exists() ) {
    		System.out.println("Exists");
    		readFile();
    		
    		
    		if (!obsList.isEmpty()) {
    		//GOING TO HAVE TO ERROR CHECK THIS WHEN I GET A CHANCE WHEN LIST IS EMPTY
    		listView.getSelectionModel().select(0);
    		Song selection = (Song) listView.getSelectionModel().getSelectedItem();
    		setPromptTextField(selection.name.trim(), selection.artist.trim(), selection.album.trim(), selection.year.trim());
    		
    		
    		System.out.println("\n\n");
    		}else {
    			listView.setDisable(true);
    		}
    		
    	}else {
    		 System.out.println("Does not Exists");
    		 file.createNewFile();
    		 obsList = FXCollections.observableArrayList();
    	}  

  
    	
    }
    
    @FXML
    void listClick(MouseEvent event) {
    	Song selection = (Song) listView.getSelectionModel().getSelectedItem();
    	int index =  listView.getSelectionModel().getSelectedIndex(); 
    	clearTextField();
    	
    	setPromptTextField(selection.name,selection.artist,selection.album,selection.year);
    	System.out.println(
    			"Name: " + selection.name +
    			"\nArtist: " + selection.artist +
    			"\nAlbum: " + selection.album +
    			"\nYear: " + selection.year + 
    			"\nIndex: " + index + "\n");
    }
    

    @FXML
    void addSong(ActionEvent event) throws IOException {
    	boolean isInList = false;
    	boolean noExtraData = false;
    	boolean extraData = true;
    	
//    	if(verticalBarPresent()) {
//    		
//    		alertPopUpBox("Error! Input Character Error!","Vertical Bar '|' character is not allowed!", "", null, null, null, null);
//    		//start();
//    	}
    	
    	//checks to see if the nameTextField and artistTextField is empty
    	if(!(nameTextField.getText().trim().isEmpty()) && !(artistTextField.getText().trim().isEmpty()) ) {
    		
    		//loops the list to check if there is a duplicate if true breaks and displays alert
    		// also checks to see if there is a vertical bar if true breaks
    		for (Song song : obsList) {
    			
        		if(song.name.equalsIgnoreCase(nameTextField.getText().trim()) && song.artist.equalsIgnoreCase(artistTextField.getText().trim()) ){
        			
        			alertPopUpBox("Error! Duplicate Entry!","Item is already in list!", "", null, null, null, null);

        			System.out.println("Yes it is in list");
        			isInList = true;
        			clearTextField();
        			break;
        		}else if(verticalBarPresent()){
        			alertPopUpBox("Error! Input Character Error!","Vertical Bar '|' character is not allowed!", "", null, null, null, null);
        			System.out.println("Vertical Bar detected!");
        			isInList = true;
        			clearTextField();
        			break;
        		}

      	   }
    		//if for loop returns false(meaning not in list) then enter in list
    		if(!isInList && isYearValid(yearTextField.getText().trim())) {
    			
    			//Alert box with prompt Confirm or Cancel
    			//Press Confirm than it adds it to list
    			//Press Cancel it clears input box and doesn't add to list
    			if(alertPopUpBox("Add Song","Do you want to add this new song details","add",
    					nameTextField.getText().trim(), 
    					artistTextField.getText().trim(), 
    					albumTextField.getText().trim(), 
    					yearTextField.getText().trim()  
    			).get().getButtonData() == ButtonData.OK_DONE ) {
    				
    				insertIntoList(nameTextField.getText().trim(), artistTextField.getText().trim(), albumTextField.getText().trim(), yearTextField.getText().trim() );
    				listView.setDisable(false);
    				save();
    				
    			}else {
    				clearTextField();
    			}
    			
    			
	
    		}

    		
        	
        	
        //}else if(name.getText().trim().isEmpty() && artist.getText().trim().isEmpty())  {	
    	}else if(nameTextField.getText().trim().isEmpty() || artistTextField.getText().trim().isEmpty())  {
    		clearTextField();
    		System.out.println("Input Error! Both Name and Artist must be entered!");
    		
    		alertPopUpBox("Input Error! ", "Both Name and Artist must be entered!", "", null, null, null, null);

    	}
    	

    }
    
    //Used to add data into Song object -> into Observable list -> listView
    public void insertIntoList(String name, String artist, String album, String year)  {
    	song = new Song(name.trim(), artist.trim(), album.trim(), year.trim());
		obsList.add(song);
    	
    	obsList.sort(Comparator.comparing(Song::getName, String.CASE_INSENSITIVE_ORDER)
    			.thenComparing(Song::getArtist, String.CASE_INSENSITIVE_ORDER));
    	listView.setItems(obsList);
    	listView.getSelectionModel().select(song);
    	
    	clearTextField();
    	setPromptTextField(song.name,song.artist,song.album,song.year);
    	
    }
    
    //method for all the alert pop ups
    public Optional<ButtonType> alertPopUpBox(String title, String headerText,String extraData, 
    		String name, String artist, String album, String year) {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	
    	if(extraData.equals("add") || extraData.equals("delete")) {
    		//Alert alert = new Alert(AlertType.INFORMATION);
        	alert.setTitle(title);
        	alert.setHeaderText(headerText);
        	
        	ButtonType okButton = new ButtonType("Confirm", ButtonData.OK_DONE);
        	ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        	alert.getButtonTypes().setAll(cancelButton, okButton);
        	
        	alert.setContentText("Name:\t" + name + 
        			"\nArtist:\t" + artist + 
        			"\nAlbum:\t" + album + 
        			"\nYear:\t" + year);
        	
        	
        	
        	Optional<ButtonType> result = alert.showAndWait();
        	return result;
	
     	
    	}else{
    		//Alert alert = new Alert(AlertType.INFORMATION);
        	alert.setTitle(title);
        	alert.setHeaderText(headerText);
        	Optional<ButtonType> result = alert.showAndWait();
        	return result;
    	}
    	
    	
    }
    
    //set textField prompt with information passed
    public void setPromptTextField(String name, String artist, String album, String year) {
    	
    	nameTextField.setPromptText(name);
    	artistTextField.setPromptText(artist);
    	albumTextField.setPromptText(album);
    	yearTextField.setPromptText(year);
    }
	
    
    
    //clears textField when information is entered
    public void clearTextField() {
    	nameTextField.setText("");
    	artistTextField.setText("");
    	albumTextField.setText("");
    	yearTextField.setText("");
    }

    public void deleteSong(ActionEvent event) throws IOException {
		if (obsList.isEmpty()) {
			haveError("There are no songs to delete!");
		}else{
			int deleteIndex = listView.getSelectionModel().getSelectedIndex();
			

			
			Song selection = (Song) listView.getSelectionModel().getSelectedItem();
	    	

	    	

//			if (delete.get() == ButtonType.OK) {
			if(alertPopUpBox("CAREFULL","Do you want to delete this song?","delete",
					selection.name.trim(), 
					selection.artist.trim(), 
					selection.album.trim(), 
					selection.year.trim()  
			).get().getButtonData() == ButtonData.OK_DONE ) {
				obsList.remove(deleteIndex);

				if (deleteIndex == obsList.size() - 1) {
					listView.getSelectionModel().select(deleteIndex--);
					selection = (Song) listView.getSelectionModel().getSelectedItem();
					setPromptTextField(selection.name.trim(), selection.artist.trim(), selection.album.trim(), selection.year.trim());
				} else {
					listView.getSelectionModel().select(deleteIndex++);
					selection = (Song) listView.getSelectionModel().getSelectedItem();
					if(selection != null) {
						setPromptTextField(selection.name.trim(), selection.artist.trim(), selection.album.trim(), selection.year.trim());
						
					}else {
						clearTextField();
						listView.setDisable(true);
						setPromptTextField("Enter Song Name", "Enter Artist Name", "Enter Album Name", "Enter Song Year");
					}
						
				}
				
				save();
				
				
			}
			
		}

	}
    

    
    void haveError(String error) {
		String errorType = error;
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Error");
		alert.setContentText(errorType);
		alert.showAndWait();
	}
    

    //checks that year is a positive integer
    private boolean isYearValid(String year) {
		if (!year.isEmpty()) {
			try {
				int c = Integer.parseInt(year.trim());

				if (c <= 0) {
					haveError("Please enter a positive integer for year!");
					return false;
				}
			} catch (Exception e) {
				haveError("Please enter a positve integer for year!");
				return false;
			}
		}
		return true;
	}


    @FXML
    void editSong(ActionEvent event) {
    	
    	if (obsList.isEmpty()) {
			haveError("There are no songs to edit");
		}else{
			boolean extraData = true;
			
			//used to enable hidden buttons
			saveCancelButtonEnabled();
	    	
   	
	    	Song selection = (Song) listView.getSelectionModel().getSelectedItem();
	    	
	    	nameTextField.setText(selection.name);
	    	artistTextField.setText(selection.artist);
	    	albumTextField.setText(selection.album);
	    	yearTextField.setText(selection.year);
	    	

		
		}
    	
    
    }
    
    
    @FXML
    public void cancelButton(ActionEvent event) {
    	
    	//used to disable hidden buttons
        saveCancelButtonDisabled();
    		
    	
    	Song selection = (Song) listView.getSelectionModel().getSelectedItem();
    	

    	clearTextField();
    	
    	setPromptTextField(selection.name, selection.artist, selection.album, selection.year);
    	
    }
    
    @FXML
    void saveButton(ActionEvent event) throws IOException {
    	
    	boolean isInList = false;
    	boolean noExtraData = false;
    	boolean extraData = true;
    	
    	//checks to see if the nameTextField and artistTextField is empty
    	if(!(nameTextField.getText().trim().isEmpty()) && !(artistTextField.getText().trim().isEmpty()) ) {
    		
    		//loops the list to check if there is a duplicate if true breaks and displays alert
    		for (Song song : obsList) {
    			
        		if(song.name.equalsIgnoreCase(nameTextField.getText().trim()) && song.artist.equalsIgnoreCase(artistTextField.getText().trim()) ){
        			
        			if(verticalBarPresent()){
            			alertPopUpBox("Error! Input Character Error!","Vertical Bar '|' character is not allowed!", "", null, null, null, null);
            			System.out.println("Vertical Bar detected!");
            			isInList = true;
            			//clearTextField();
            			break;
            		}else if(!song.album.equalsIgnoreCase(albumTextField.getText().trim()) || !song.year.equalsIgnoreCase(yearTextField.getText().trim()) ) {
        				continue;
        			}else {
        				alertPopUpBox("Error! Duplicate Entry!","Item is already in list!", "", null, null, null, null);

            			System.out.println("Yes it is in list");
            			isInList = true;
            			//clearTextField();
            			break;
        			}
        			
        			
        		}

      	   }
    		

    		//if for loop returns false(meaning not in list) then enter in list
    		if(!isInList && isYearValid(yearTextField.getText().trim())) {
    			
    			//Alert box with prompt OK or Cancel
    			//Press Confirm than it adds it to list
    			//Press Cancel it clears input box and doesn't add to list
    			//Checks to see if vertical bar is present 
    			if(verticalBarPresent()){
        			alertPopUpBox("Error! Input Character Error!","Vertical Bar '|' character is not allowed!", "", null, null, null, null);
        			System.out.println("Vertical Bar detected!");
        			isInList = true;
        			//clearTextField();
        			//break;
        			
        		}else if(alertPopUpBox("Edit Song","Do you want to edit the song details?","add",
    					nameTextField.getText().trim(), 
    					artistTextField.getText().trim(), 
    					albumTextField.getText().trim(), 
    					yearTextField.getText().trim()  
    			).get().getButtonData() == ButtonData.OK_DONE ) {
    				
    				//isYearValid(yearTextField.getText().trim());
    				int deleteIndex = listView.getSelectionModel().getSelectedIndex();
    				obsList.remove(deleteIndex);
    				
    				insertIntoList(nameTextField.getText().trim(), artistTextField.getText().trim(), albumTextField.getText().trim(), yearTextField.getText().trim() );
    				
    				
    				save();
    				
    				//used to disable hidden buttons
    			    saveCancelButtonDisabled();
		    	
    		    	
    		    	Song selection = (Song) listView.getSelectionModel().getSelectedItem();
    		    	

    		    	clearTextField();
    		    	
    		    	setPromptTextField(selection.name, selection.artist, selection.album, selection.year);

    				
    			}else {
    				//clearTextField();
    			}
    			
    			
	
    		}

    		
        	
        	
        //}else if(name.getText().trim().isEmpty() && artist.getText().trim().isEmpty())  {	
    	}else if(nameTextField.getText().trim().isEmpty() || artistTextField.getText().trim().isEmpty())  {
    		clearTextField();
    		System.out.println("Input Error! Both Name and Artist must be entered!");
    		
    		alertPopUpBox("Input Error! ", "Both Name and Artist must be entered!", "", null, null, null, null);
    		
    		clearTextField();
    		
    		//used to enable hidden buttons
    		saveCancelButtonEnabled();
  	
	    	Song selection = (Song) listView.getSelectionModel().getSelectedItem();
	    	

	    	clearTextField();
	    	
	    	nameTextField.setText(selection.name);
	    	artistTextField.setText(selection.artist);
	    	albumTextField.setText(selection.album);
	    	yearTextField.setText(selection.year);

    	}

    }
    
    //used to enable hidden buttons
    public void saveCancelButtonEnabled(){
    	listView.setDisable(true);
    	addButton.setDisable(true);
    	deleteButton.setDisable(true);
    	editButton.setDisable(true);
    	saveButton.setDisable(false);
    	saveButton.setVisible(true);
    	cancelButton.setDisable(false);
    	cancelButton.setVisible(true);
    	
    	
    }
    
    //used to disable hidden buttons
    public void saveCancelButtonDisabled(){
    	listView.setDisable(false);
    	addButton.setDisable(false);
    	deleteButton.setDisable(false);
    	editButton.setDisable(false);
    	cancelButton.setDisable(true);
    	cancelButton.setVisible(false);
    	saveButton.setDisable(true);
    	saveButton.setVisible(false);
    }
    
    //checks to see if vertical bar is in TextField from user inputed data
    private boolean verticalBarPresent() {
    	if(nameTextField.getText().trim().contains("|") ||
    			artistTextField.getText().trim().contains("|") ||
    			albumTextField.getText().trim().contains("|") 
    			) {
    		return true;
    	}else {
    		return false;
    	}
    	
    }
    
    //saves the file to a text file 
    public static void save() throws IOException {
    	System.out.println("Saving data...");
        
        File file = new File("DataFile.txt");
 	      
        file.createNewFile();
        
        FileWriter fileWrite = new FileWriter(file); 
 	   
 	   	for (Song song : obsList) {
 		   fileWrite.write("Name: " + song.name + "\n" + 
 				   "Artist: " +song.artist + "\n" + 
 				   "Album: " + song.album + "\n" + 
 				   "Year: " + song.year + "\n\n" );
 	   	}
 	   
 	   	fileWrite.flush();
 	   	fileWrite.close();
 	   
 	   	System.out.println("Saving Completed!");
    }
    
    
    //reads in the data from a text file
    public void readFile() throws FileNotFoundException {
    	String name;
    	String artist;
    	String album;
    	String year;
    	String newline;
    	obsList = FXCollections.observableArrayList();
    	
    	File file = new File("DataFile.txt");
        Scanner sc = new Scanner(file);
        
        System.out.println("Reading File...");
     
       while(sc.hasNext()) {
        	//sc.useDelimiter("Name: ");
            name = sc.nextLine();
            artist = sc.nextLine();
            album = sc.nextLine();
            if(album.equals("Album: "))
            	album = "";
            year = sc.nextLine();
            if(year.equals("Year: "))
            	year = "";
            
            sc.nextLine();
            
            System.out.println(name.trim().replace("Name: ", ""));
            System.out.println(artist.trim().replace("Artist: ", ""));
            System.out.println(album.trim().replace("Album: ", ""));
            System.out.println(year.trim().replace("Year: ", ""));
            System.out.println("\n");
            
            
            
            insertIntoList(name.trim().replace("Name: ", ""), 
            		artist.trim().replace("Artist: ", ""), 
            		album.trim().replace("Album: ", ""),
            		year.trim().replace("Year: ", "") );

            
        }
       System.out.println("Reading File Completed!");

        sc.close();
        
    }
    
    


}
