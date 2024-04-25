package controllers;

import javafx.beans.property.BooleanProperty;
//import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Salle {
	
	private Integer id_salle;
    private String nom_salle;
    private String type_salle;
    private Integer capacite;
    private String bloc;
    private String description;
    private String timeSlot;

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }
   // private final BooleanProperty selected = new SimpleBooleanProperty(false);


    public Salle(String nom, String type, Integer capacite, String bloc,int id,String description) {
        this.nom_salle = nom;
        this.type_salle = type;
        this.capacite = capacite;
        this.bloc = bloc;
        this.id_salle = id;
        this.description=description;

    }

	public Salle() {
		// TODO Auto-generated constructor stub
	}

	public Salle(String string) {
		// TODO Auto-generated constructor stub
	}

	public String getNom_salle() {
        return nom_salle;
    }

    public void setNom_salle(String nom) {
        this.nom_salle = nom;
    }

    public String getType_salle() {
        return type_salle;
    }

    public void setType_salle(String type) {
        this.type_salle = type;
    }

    public Integer getCapacite() {
        return capacite;
    }

    public void setCapacite(Integer capacite) {
        this.capacite = capacite;
    }

    public String getBloc() {
        return bloc;
    }

    public void setBloc(String bloc) {
        this.bloc = bloc;
    }
    public Integer getid() {
        return id_salle;
    }

    public void setid(int id) {
        this.id_salle = id;
    }


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
}
