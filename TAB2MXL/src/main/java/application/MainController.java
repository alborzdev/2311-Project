package application;

import java.awt.Desktop;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton.ButtonType;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import test.LineErrorException;
import xmlClasses.Chain;

public class MainController implements Initializable {
	
	private Stage stage;
	private File file;


	@FXML 
	private JFXComboBox<String> KeySig;
	@FXML 
	private JFXComboBox<String> TimeSig;
	@FXML 
	private JFXComboBox<String> InstrumentType;
	
	@FXML 
	private JFXComboBox<String> conversionType;
	
	@FXML
	private JFXTextArea textarea;

	Chain chain;
	/**
	 * This method allows Open/Upload button to select a .txt file and display it in text area
	 * @param event
	 * @throws FileNotFoundException
	 * 
	 */
	public void doOpen(ActionEvent event) throws FileNotFoundException {
		//file chooser
		FileChooser filechooser= new FileChooser(); 
		filechooser.setTitle("Open text file"); 
		filechooser.getExtensionFilters().addAll( new FileChooser.ExtensionFilter(".txt files", "*.txt") );
		file = filechooser.showOpenDialog(stage); 
		textarea.clear();
		
		if(file==null) {
			System.out.println("No file has been selected");
		}
		if(file!=null) {
			//Sends Textarea to Backend to anaylize/parse
			textarea.appendText(tab2mxl.txtAnalyzing.analyze(file.toString()));
		}
	}
	
	/**
	 * Lets user specify name and path of xml file
	 * @param event
	 * @throws Exception 
	 */
	private File loc;
	public void convertFile(ActionEvent event) throws Exception {
		//xmlClasses.ObjectToMxl.mxlMaker();
		FileChooser saver = new FileChooser();
		
		FileChooser.ExtensionFilter extFilter = 
                new FileChooser.ExtensionFilter("xml files (*.xml)", "*.xml");
           saver.getExtensionFilters().add(extFilter);
        	loc = saver.showSaveDialog(stage);	//get file path specified by user
        FileWriter write;

        
        //COMMENTED OUT THE OPTION TO FORCE THE CHAIN TO USE THE TEXT AREA -aidan
//      if(file!=null)chain = new Chain(file, getTitle(), getLyricist(),getComposer(), loc.getAbsolutePath(), getTimeSig(), getKey(), getType(),getConversionType());
//      else { System.out.println(textarea.getText());
        chain = new Chain(textarea.getText(), getTitle(), getLyricist(),getComposer(), getTimeSig(), getKey(), getType(),getConversionType());     	
//      }
        
        //CHAIN CALLS w/ ERROR HANDLING
        boolean errorEvent = false;
        //T2P
        try { chain.TABtoPART(); } 
        catch ( LineErrorException e ) {
			errorEvent = ErrorHandling.errorEventHighlight(
					"Conversion was unsuccessful :(",
					e, textarea, "|"+e.getString()+"|");
        }
        catch ( Exception e ) {
			errorEvent = ErrorHandling.errorEvent(
					"Conversion was unsuccessful :(",
					e.getMessage(), e);
        }
        //I2P
        try { chain.INFOtoPARTWISE(); } 
        catch( Exception e ) {
        	errorEvent = ErrorHandling.errorEvent(
					"Conversion was unsuccessful :(",
					"Some attributes are incorrect", e);
        }
        //M2X
		try { chain.MARSHtoXML(); }
		catch ( Exception e ) {
			errorEvent = ErrorHandling.errorEvent(
					"Conversion was unsuccessful :(",
					"Your tab format is correct, something went wrong on our end! Please try again.", e);
		}
        
		
        if(loc==null) {
        	System.out.println("Exporting has been cancelled");
        }
        if(errorEvent) {
        	AlertType type = AlertType.ERROR; 
			Alert alert = new Alert(type, "Conversion was unsuccessful :("); 
			alert.getDialogPane().setContentText("Exporting was cancelled due to previous errors.");
			alert.showAndWait();
        }
        else {
	        try {
				write = new FileWriter(loc);
				//SHOULD RECIEVE XML FROM BACKEND
				write.write(chain.getXML());
	       	  	write.close();
	       	  	Alert conf = new Alert(AlertType.CONFIRMATION,  
	                 "Conversion was successful!"); 
	       	  	conf.setContentText("A MusicXML file has been exported.");
	       	 	conf.showAndWait(); 
			} catch (IOException e) { 
				AlertType type = AlertType.ERROR; 
				Alert alert = new Alert(type, "Conversion was unsuccessful :("); 
				alert.getDialogPane().setContentText("Exporting was cancelled. Please try again."); 
				alert.showAndWait();
				e.printStackTrace();	}
        }
	}
	//
	/**
	 * 
	 *
	 */
	@FXML
	private MenuItem help;
	public void UserManual(ActionEvent event) {
		File usermanual = new File("Documentation/User Manual.pdf");
		if (Desktop.isDesktopSupported()) {
	        new Thread(() -> {
	            try {
	                Desktop.getDesktop().open(usermanual);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }).start();
	    }
	}
	
	
	/* Exit button - exit app
	 * */
	public void doExit(ActionEvent event) {
		Platform.exit();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		KeySig.getItems().add("C Major");
//		KeySig.getItems().add("G Major");
//		KeySig.getItems().add("D Major");
//		KeySig.getItems().add("A Major");
//		KeySig.getItems().add("E Major");
//		KeySig.getItems().add("B Major");
//		KeySig.getItems().add("F# Major");
//		KeySig.getItems().add("C# Major");
		
		TimeSig.getItems().add("3/4");
		TimeSig.getItems().add("4/4");
//		TimeSig.getItems().add("5/4");
//		TimeSig.getItems().add("6/8");
//		TimeSig.getItems().add("7/8");
//		TimeSig.getItems().add("12/8");
		
		InstrumentType.getItems().add("Guitar");
		InstrumentType.getItems().add("Drums - not completely implemented");
		InstrumentType.getItems().add("Bass - not completely implemented");
		
		conversionType.getItems().add("Tab");
		conversionType.getItems().add("Sheet Music");
	}

	public void init(Stage primaryStage) {
		this.stage = primaryStage;
		
	}
		
	
	/**
	 * GETTERS FOR ADDITIONAL INFORMATION
	 */
	public String getConversionType() {
		String s;
		if(conversionType.getSelectionModel().isEmpty()==false) {
			s = conversionType.getSelectionModel().getSelectedItem().toString();
			switch(s) {
			case "Sheet Music": return"G";
			case "Tab":
			default: return "TAB";
			}
		}
			else {
				s="TAB";
				AlertType type = AlertType.WARNING; 
				Alert alert = new Alert(type, "Required Information missing"); 
				alert.getDialogPane().setContentText("Conversion type left empty (default = TAB)"); 
				alert.showAndWait();
			}
		return s;
	}
	
	//default = Guitar
	public String getType() {
		String s;
		if(InstrumentType.getSelectionModel().isEmpty()==false) {
			s =new String(InstrumentType.getSelectionModel().getSelectedItem().toString());
			switch(s){
			case "Drums - not completely implemented": return "drums";
			case "Guitar": return "Guitar"; 
			default: return "Bass";
			}
		}
		else {s="Guitar";
		AlertType type = AlertType.WARNING; 
		Alert alert = new Alert(type, "Required Information missing"); 
		alert.getDialogPane().setContentText("Instrument type left empty (default = Guitar)"); 
		alert.showAndWait();
		
		}
		
		return s;
	}
	//default = CMajor
	public String getKey() {
		String s;
		if(KeySig.getSelectionModel().isEmpty()==false) {
			s =new String(KeySig.getSelectionModel().getSelectedItem().toString());
		System.out.println("selected key sig "+s);
		}
		else {s= "C Major";
		AlertType type = AlertType.WARNING; 
		Alert alert = new Alert(type, "Required Information missing"); 
		alert.getDialogPane().setContentText("Key Signature empty (default = C Major)"); 
		alert.showAndWait();
		}
		
		return s;
	}
	//default = 4/4
	public int getTimeSig() {
		int indx = TimeSig.getSelectionModel().getSelectedIndex();
		switch(indx) {
		case 0: return 34;
		case 1: return 44;
		//case 2: return 54;
		//case 3: return 68;
		//case 4: return 78;
		//case 5: return 128;
		default: return 44;
		}
	}
	
	@FXML
	private TextField composer = new TextField("");
	@FXML
	private TextField lyricist=new TextField("");
	@FXML
	private TextField title=new TextField("");

	public String getComposer() throws IOException {
		String s=new String(composer.getText());
		return s;
	}
	public String getLyricist() throws IOException {
		String s=new String(lyricist.getText());
		return s;
	}
	public String getTitle() throws IOException {
		String s=new String(title.getText());
		return s;
	}
	
}
