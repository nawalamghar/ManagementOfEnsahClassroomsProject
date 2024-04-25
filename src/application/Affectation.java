package application;

public class Affectation {

private int idaffectation ;
private String nomCours	;
private String nomProf ;
private String jour	;
private String duree ;	
private String sallebloctype;



public Affectation(int idaffectation,String nomCours, String nomProf, String jour, String duree, String sallebloctype) {
	this.idaffectation=idaffectation;
	this.nomCours = nomCours;
	this.nomProf = nomProf;
	this.jour = jour;
	this.duree = duree;
	this.sallebloctype = sallebloctype;
}



public int getIdaffectation() {
	return idaffectation;
}



public void setIdaffectation(int idaffectation) {
	this.idaffectation = idaffectation;
}



public String getDuree() {
	return duree;
}
public void setDuree(String duree) {
	this.duree = duree;
}
public String getJour() {
	return jour;
}
public void setJour(String jour) {
	this.jour = jour;
}
public String getNomProf() {
	return nomProf;
}
public void setNomProf(String nomProf) {
	this.nomProf = nomProf;
}
public String getNomCours() {
	return nomCours;
}
public void setNomCours(String nomCours) {
	this.nomCours = nomCours;
}
public String getSallebloctype() {
	return sallebloctype;
}
public void setSallebloctype(String sallebloctype) {
	this.sallebloctype = sallebloctype;
}

@Override
public String toString() {
    return "Affectation [duree=" + duree + ", jour=" + jour + ", nomProf=" +
            nomProf + ", nomCours=" + nomCours + ", sallebloc=" + sallebloctype + "]";
}
	

}
