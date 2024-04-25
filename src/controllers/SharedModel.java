package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SharedModel {
    private static Salle salleSelectionne;
    private static ObservableList<Salle> salleData = FXCollections.observableArrayList();
    private static GestionSAllesController gestionSalleController; // Ajout de la référence

    public ObservableList<Salle> getSalleData() {
        return salleData;
    }

    public void setSalleData(ObservableList<Salle> salleData) {
        this.salleData = salleData;
    }

    public static Salle getSalleSelectionnee() {
        return salleSelectionne;
    }

    public static void setSalleSelectionnee(Salle salle) {
    	salleSelectionne = salle;
    }

    public void refreshTable(javafx.scene.control.TableView<Salle> salleTable) {
        try {
            // Effacer les données actuelles de la table
            salleData.clear();

            // Recharger les données depuis la base de données (remplacez fetchSalleData() par votre logique de chargement)
            salleData.addAll(fetchSalleData());

            // Rafraîchir la table avec les nouvelles données
            salleTable.setItems(salleData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ObservableList<Salle> fetchSalleData() {
        return FXCollections.observableArrayList();
    }

    public static void setGestionSalleController(GestionSAllesController controller) {
        gestionSalleController = controller;
    }

    public static void callRefreshTable() {
        if (gestionSalleController != null) {
            gestionSalleController.refreshTable();
        }
    }
}
