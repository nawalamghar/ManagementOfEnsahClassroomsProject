package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import application.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CalendrierProfController {
	int userId = UserSession.getUserId();
	private Stage stage;
    private Scene scene ;
    private Parent root;
//    @FXML
//    private Button acceuilbtn;
    @FXML
    private Button deconnexionbtn;
    
	
	@FXML private Label l1;@FXML private Label l2;@FXML private Label l3;
	@FXML private Label l4;@FXML private Label l5;@FXML private Label l6;
	@FXML private Label l7;@FXML private Label l8;@FXML private Label l9;
	@FXML private Label l10;@FXML private Label l11;@FXML private Label l12;
	@FXML private Label l13;@FXML private Label l14;@FXML private Label l15;
	@FXML private Label l16;@FXML private Label l17;@FXML private Label l18;
	@FXML private Label l19;@FXML private Label l20;@FXML private Label l21;
	@FXML private Label l22;@FXML private Label l23;@FXML private Label l24;
	List<Label> labels = new ArrayList<>();
	
    @FXML
	private Label salleName;
    
   

    
    @FXML
    private void initialize() {
    	System.out.println("prof:"+UserSession.getUserId());
    	
    	// Ajoutez tous les labels à la liste
        labels.add(l1);labels.add(l2);labels.add(l3);labels.add(l4);labels.add(l5);
        labels.add(l6);labels.add(l7); labels.add(l8);labels.add(l9);labels.add(l10);
        labels.add(l11);labels.add(l12); labels.add(l13);labels.add(l14);labels.add(l15);
        labels.add(l16); labels.add(l17);labels.add(l18);labels.add(l19); labels.add(l20);
        labels.add(l21);labels.add(l22); labels.add(l23);labels.add(l24);
        

        int i = 0; // Commencez à l'indice 0

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ensahjava", "root", "");

            String[] joursSemaine = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};
            String[] plagesHoraires = {"08:30-10:30", "10:30-12:30", "14:30-16:30", "16:30-18:30"};

            for (String jour : joursSemaine) {
                for (String plageHoraire : plagesHoraires) {
                    String sql = "SELECT nom_salle,bloc, nom_cours FROM affectation a " +
                            "INNER JOIN salle p ON p.id_salle = a.id_salle " +
                            "INNER JOIN cours c ON c.id_cours = a.id_cours " +
                            "WHERE id_personne = ? AND jour = ? AND heure_debut = ? AND heure_fin = ?";

                    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    	pstmt.setInt(1, UserSession.getUserId());
                        pstmt.setString(2, jour);
                        pstmt.setString(3, plageHoraire.split("-")[0]); // Heure de début
                        pstmt.setString(4, plageHoraire.split("-")[1]); // Heure de fin

                        try (ResultSet resultSet = pstmt.executeQuery()) {
                            if (i < labels.size() && labels.get(i) != null) {
                                if (resultSet.next()) {
                                    String nomSalle = resultSet.getString("nom_salle");
                                    String bloc = resultSet.getString("bloc");
                                    String nomCours = resultSet.getString("nom_cours");
                                    System.out.println("Updating label " + i + " with data: " + nomSalle + ", " + nomCours);

                                    labels.get(i).setText("  " + nomSalle +"-bloc"+bloc+ "\n  " + nomCours);
                                } else {
                                    labels.get(i).setText(" "); // Clear the label if no data is found
                                }
                            } else {
                                System.out.println("Erreur : Tentative d'accéder à un indice de tableau invalide.");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    i++;
                }
            }

            // Fermez la connexion à la fin.
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    }
    @FXML
    public void btnAcceuil(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/interfaces/AcceuilProf.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
	public void MonEmploibtn(ActionEvent event) {
    	try {
            root = FXMLLoader.load(getClass().getResource("/interfaces/CalendrierProf.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
	    }
    @FXML
    public void btnDemande(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/interfaces/DemandeReservation.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            System.out.println(userId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void btnSuivreDemande(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("/interfaces/SuivreDemande.fxml"));
	        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        scene = new Scene(root);
	        stage.setScene(scene);
	        stage.show();
			
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    }
    @FXML
    public void deconnexionbtn(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("/interfaces/Main.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
	        
			
        } catch (Exception e) {
            e.printStackTrace();
        }
	    }
   
}
