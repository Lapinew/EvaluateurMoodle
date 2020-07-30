package evaluateur;

import java.util.ArrayList;

public abstract class QueryResult extends Reponse {
	
	protected String nomFichier; //nom du fichier contenant les requetes pour cr�er la BD de test
	protected ArrayList<ArrayList<Object>> table; //Resultat de la requete (select sur la table impliqu�e)
	
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
	
	public void comparaisonTable(QueryResult reponse2) { //Methode permettant de comparer la r�ponse d'un eleve a celle de l'enseignant
		System.out.println(reponse2.getTable()); //Affiche la r�ponse du prof
		System.out.println(table); //Affiche la r�ponse de l'�l�ve
		if (reponse2.getTable().equals(table)) {
			System.out.println("Votre r�sultat concorde avec celui de l'enseignant, diff�rence de votre requete avec celle de l'enseignant :");
			System.out.println(this.distanceLev(reponse2.getRequete().toUpperCase(), requete.toUpperCase())*100/reponse2.getRequete().length() + "% (Ce pourcentage peut comprendre des diff�rences insignifiantes comme des espaces)");
		} else {
			System.out.println("Le r�sultat de votre requete diff�re avec celui de l'enseignant");
		}
	}
}
