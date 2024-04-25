package controllers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ModifierSalleController {
	private Stage stage;
    private Scene scene ;
    private Parent root;
    
	 private Salle salleSelectionne;
	 public ModifierSalleController() {
	      this.salleSelectionne = SharedModel.getSalleSelectionnee();
	    }
    @FXML
    private Button gestionSalletn;
    @FXML
    private Button gestionAffectationbtn;
	@FXML
    private Button enregistrerButton;
    @FXML
    private TextField nomSalle;
    @FXML
    private Label salleName;
    @FXML
    private ComboBox<String> typeSalle;
    @FXML
    private TextField capaciteSalle;
    @FXML
    private TextArea descriptionSalle;
    @FXML
    private ComboBox<String> blocSalle;
    private SharedModel sharedModel;
    private ObservableList<Salle> salleData = FXCollections.observableArrayList();
    
    @FXML
    private void initialize() {
    	sharedModel = new SharedModel();
    	System.out.println("salle a modifier:"+salleSelectionne);
    	System.out.println("nom de selected salle:"+salleSelectionne.getNom_salle());
    	salleName.setText(salleSelectionne.getNom_salle()+" Bloc"+salleSelectionne.getBloc());
        typeSalle.getItems().addAll("Cours", "TD", "Laboratoire", "TP-informatique","Amphi");
        blocSalle.getItems().addAll("A", "B" );
        if ( salleSelectionne!= null) {
        nomSalle.setText(salleSelectionne.getNom_salle());
        typeSalle.setValue(salleSelectionne.getType_salle());  
        Integer capacite = salleSelectionne.getCapacite();
        capaciteSalle.setText(Integer.toString(capacite));
        descriptionSalle.setText(salleSelectionne.getDescription());
        blocSalle.setValue(salleSelectionne.getBloc());
        }
        else
        	System.out.println("selected salle22:"+salleSelectionne);

    }
    @FXML
    public void gestionSallebtn(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("/interfaces/GestionSalles.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
	        
			
        } catch (Exception e) {
            e.printStackTrace();
        }
	    }
    @FXML
    public void gestionAffectationbtn(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("/interfaces/GestionAffectations.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
			
        } catch (Exception e) {
            e.printStackTrace();
        }
	    }
    @FXML
    public void btnEnregistrer(ActionEvent event) {
        try {
        	if (nomSalle.getText().isEmpty() || typeSalle.getValue() == null || capaciteSalle.getText().isEmpty() || descriptionSalle.getText().isEmpty() || blocSalle.getValue() == null) {
                Alert validationFailureDialog = new Alert(Alert.AlertType.ERROR);
                validationFailureDialog.setTitle("Validation Error");
                validationFailureDialog.setHeaderText("Validation Error");
                validationFailureDialog.setContentText("Please enter all required fields.");
                validationFailureDialog.showAndWait();
                return; // Stop further execution
            }
        	
        	modifierSalleFromDatabase(salleSelectionne);
        	SharedModel.callRefreshTable();
         } catch (Exception e) {
            e.printStackTrace();
    }
    }   
    void modifierSalleFromDatabase(Salle salle) {
    	System.out.print(salle.getid());
    	try {
        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ensahjava", "root", "");
        String sql = "UPDATE salle SET nom_salle=?, type_salle=?, capacite=?, description=?, bloc=? WHERE id_salle=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, nomSalle.getText());
        pstmt.setString(2, typeSalle.getValue());
        pstmt.setInt(3, Integer.parseInt(capaciteSalle.getText()));
        pstmt.setString(4, descriptionSalle.getText());
        pstmt.setString(5, blocSalle.getValue());
        pstmt.setInt(6, salle.getid());
        int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                Alert successDialog = new Alert(Alert.AlertType.INFORMATION);
                successDialog.setTitle("Success");
                successDialog.setHeaderText("Success");
                successDialog.setContentText("Salle modified successfully!");
                successDialog.showAndWait();
            	
            } else {
                Alert successDialog = new Alert(Alert.AlertType.INFORMATION);
                successDialog.setTitle("Failure");
                successDialog.setHeaderText("Failure");
                successDialog.setContentText("Échec de modification de la salle!");
                successDialog.showAndWait();               
                }

    }  catch (Exception e) {
        e.printStackTrace();
    	}

   }
 
    
}