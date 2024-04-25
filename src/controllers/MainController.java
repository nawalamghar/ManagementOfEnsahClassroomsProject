package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.UserSession;

public class MainController {

    private Stage stage;
    private Scene scene ;
    private Parent root;
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button connectButton;

    @FXML
    private Label errorLabel;

    private String isValid(String emailtest, String passwordtest) {
        try {
            String url = "jdbc:mysql://localhost:3306/ensahjava";
            Connection connection = DriverManager.getConnection(url, "root", "");
            Statement statement = connection.createStatement();
            String sql = "SELECT id_personne,email, password,role FROM personne";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int id=resultSet.getInt("id_personne");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String role=resultSet.getString("role");
                if (email.equals(emailtest) && password.equals(passwordtest)) {
                	int loggedInUserId = id ; // Get the user's ID from the login process
                    UserSession.setUserId(loggedInUserId);
                    resultSet.close();
                    statement.close();
                    connection.close();
                     return role;
                }
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

        
    }

    @FXML
    public void btnConnecter(ActionEvent event) {
    	    try {
    	        String role = isValid(emailField.getText(), passwordField.getText());
    	        if (role != null) {
    	            //System.out.println("Connected");
    	            if ("admin".equals(role)) {
    	                root = FXMLLoader.load(getClass().getResource("/interfaces/Acceuil.fxml"));
    	                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	                scene = new Scene(root);
    	                stage.setScene(scene);
    	                stage.show();
    	            } else if ("prof".equals(role)) {
    	                root = FXMLLoader.load(getClass().getResource("/interfaces/AcceuilProf.fxml"));
    	                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	                scene = new Scene(root);
    	                stage.setScene(scene);
    	                stage.show();
    	            }} else {
    	                System.out.println("Not Connected");
    	                errorLabel.setText("Invalid credentials");
    	            }
    	        }
    	     catch (Exception e) {
    	        e.printStackTrace();
    	    }
    	}

}