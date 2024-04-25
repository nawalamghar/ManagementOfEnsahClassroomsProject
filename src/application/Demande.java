package application;

import java.time.LocalDateTime;

public class Demande {

    private int idDemande;
    private String nomProf;
    private String cours ;
    private String typeSalle;
    private LocalDateTime momentDemande;
    private String jourDemande;
    private String heureDebutDemande;
    private String heureFinDemande;
    private String etatDemande;
    private String commentaire;

    // Constructors, getters, and setters

    public Demande(int idDemande, String nomProf, String cours, String typeSalle, LocalDateTime momentDemande, String jourDemande, String heureDebutDemande, String heureFinDemande, String etatDemande ,String commentaire) {
        this.idDemande = idDemande;
        this.nomProf = nomProf;
        this.cours = cours;
        this.typeSalle = typeSalle;
        this.momentDemande = momentDemande;
        this.jourDemande = jourDemande;
        this.heureDebutDemande = heureDebutDemande;
        this.heureFinDemande = heureFinDemande;
        this.etatDemande = etatDemande;
        this.commentaire = commentaire;

    }

    public int getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(int idDemande) {
        this.idDemande = idDemande;
    }

    public String getNomProf() {
        return nomProf;
    }

    public void setNomProf(String nomProf) {
        this.nomProf = nomProf;
    }

    public String getCours() {
        return cours;
    }

    public void setCours(String cours) {
        this.cours = cours;
    }

    public String getTypeSalle() {
        return typeSalle;
    }

    public void setTypeSalle(String typeSalle) {
        this.typeSalle = typeSalle;
    }

    public LocalDateTime getMomentDemande() {
        return momentDemande;
    }

    public void setMomentDemande(LocalDateTime momentDemande) {
        this.momentDemande = momentDemande;
    }

    public String getJourDemande() {
        return jourDemande;
    }

    public void setJourDemande(String jourDemande) {
        this.jourDemande = jourDemande;
    }

    public String getHeureDebutDemande() {
        return heureDebutDemande;
    }

    public void setHeureDebutDemande(String heureDebutDemande) {
        this.heureDebutDemande = heureDebutDemande;
    }

    public String getHeureFinDemande() {
        return heureFinDemande;
    }

    public void setHeureFinDemande(String heureFinDemande) {
        this.heureFinDemande = heureFinDemande;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getEtatDemande() {
        return etatDemande;
    }

    public void setEtatDemande(String etatDemande) {
        this.etatDemande = etatDemande;
    }
    
    @Override
    public String toString() {
        return "Demande{" +
                "idDemande=" + idDemande +
                ", nomProf='" + nomProf + '\'' +
                ", cours='" + cours + '\'' +
                ", typeSalle='" + typeSalle + '\'' +
                ", momentDemande=" + momentDemande +
                ", jourDemande='" + jourDemande + '\'' +
                ", heureDebutDemande='" + heureDebutDemande + '\'' +
                ", heureFinDemande='" + heureFinDemande + '\'' +
                ", etatDemande='" + etatDemande + '\'' +
                ", commentaire='" + commentaire + '\'' +
                '}';
    }
}
