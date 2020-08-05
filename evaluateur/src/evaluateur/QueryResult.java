package evaluateur;

import java.util.ArrayList;

public abstract class QueryResult extends Reponse {
	
	protected String nomFichier; //nom du fichier contenant les requetes pour créer la BD de test
	protected ArrayList<ArrayList<Object>> resRequete; //Resultat de la requete (select sur la table impliquée)
	
	public QueryResult(String requete, String nomFichier) {
		super(requete);
		this.nomFichier = nomFichier;
	}
	
	public ArrayList<ArrayList<Object>> getResRequete () {
		return resRequete;
	}
	
	public void afficheTable() {
		System.out.println(resRequete.toString());
	}
	
	public String getFichier() {
		return nomFichier;
	}
	
	public void comparaisonTable(QueryResult reponse2) { //Methode permettant de comparer la réponse d'un eleve a celle de l'enseignant
		System.out.println(reponse2.getResRequete()); //Affiche la réponse du prof
		System.out.println(resRequete); //Affiche la réponse de l'élève
		if (reponse2.getResRequete().equals(resRequete)) {
			System.out.println("Votre résultat concorde avec celui de l'enseignant, différence de votre requete avec celle de l'enseignant :");
			System.out.println(this.distanceLev(reponse2.getRequete().toUpperCase(), requete.toUpperCase())*100/reponse2.getRequete().length() + "% (Ce pourcentage peut comprendre des différences insignifiantes comme des espaces)");
		} else {
			System.out.println("Le résultat de votre requete diffère avec celui de l'enseignant");
		}
	}
}
