package evaluateur;

import java.util.ArrayList;

public abstract class QueryResult extends Reponse {
	
	protected String nomFichier; //nom du fichier contenant les requetes pour créer la BD de test
	protected ArrayList<ArrayList<Object>> table; //Resultat de la requete (select sur la table impliquée)
	
	public QueryResult(String requete) {
		super(requete);
	}
	
	public ArrayList<ArrayList<Object>> getTable () {
		return table;
	}
	
	public void afficheTable() {
		System.out.println(table.toString());
	}
	
	public String getFichier() {
		return nomFichier;
	}
	
	public void comparaisonTable(QueryResult reponse2) { //Methode permettant de comparer la réponse d'un eleve a celle de l'enseignant
		System.out.println(reponse2.getTable()); //Affiche la réponse du prof
		System.out.println(table); //Affiche la réponse de l'élève
		if (reponse2.getTable().equals(table)) {
			System.out.println("Votre résultat concorde avec celui de l'enseignant, différence de votre requete avec celle de l'enseignant :");
			System.out.println(this.distanceLev(reponse2.getRequete().toUpperCase(), requete.toUpperCase())*100/reponse2.getRequete().length() + "% (Ce pourcentage peut comprendre des différences insignifiantes comme des espaces)");
		} else {
			System.out.println("Le résultat de votre requete diffère avec celui de l'enseignant");
		}
	}
}
