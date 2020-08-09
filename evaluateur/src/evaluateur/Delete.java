package evaluateur;

import java.sql.Connection;
import java.util.ArrayList;

public class Delete extends Modifiantes {
	
	private boolean boolWhere;
	private ArrayList<String> conditions = new ArrayList<String>();
	
	public Delete(String requete, String nomFichier, Connection connexion, String tabSelect) {
		super(requete, nomFichier, connexion, tabSelect);
		boolWhere = false;
		String standardised = "";
		int currentChar;
		int lastChar = 0;
		for (int i = 0; i < this.requete.length(); i++) {
			currentChar = this.requete.charAt(i);
			if (i == this.requete.length()-1) { //Au bout de la requete on separe le ";"
				standardised += " " + ((char) currentChar);
			} else if (currentChar == 32 && lastChar == 32) { //Si double espace on fait rien
			} else if (currentChar == 34) { //On remplace les guillemets par des quotes
				standardised += (char) 39;
			} else {
				standardised += ((char) currentChar);
			}
			lastChar = currentChar;
		}
		this.cleanRequete = standardised.toUpperCase();
		System.out.println("Standard : " + this.cleanRequete);
		
		String[] requeteSplit = this.cleanRequete.split(" ");
		
		if (requeteSplit.length > 4) { //Si on a une clause WHERE
			boolWhere = true;
			String currentItem = ""; //Permet d'accumuler les elements séparés par un espace : age > 40 --> age>40;
			for (int i = 4; i < requeteSplit.length; i++) {
				if (requeteSplit[i].equals("AND")) { //Si on tombe sur AND on ajoute l'argument précédant dans l'array et on prepare currentItem pour le prochain argument
					conditions.add(currentItem);
					currentItem = "";
				} else if (requeteSplit[i].equals(";")) { //On est arrivé au bout de la requete (on l'aura séparé du reste de la requete pendant la phase de standardisation)
					conditions.add(currentItem);
					break;
				} else {
					currentItem += requeteSplit[i]; //On accumule les morceaux de chaine dans currentItem;
				}
			}
		}
	}
	
	public void compareSyntaxe(Reponse reponse) {
		if (boolWhere) {
			System.out.println("Comparaison des conditions des requetes :");
			if (conditions.size()>((Delete) reponse).getConditions().size()) {
				System.out.println("Vous avez indiquez plus de conditions que nécéssaires");
			} else if (conditions.size()<((Delete) reponse).getConditions().size()) {
				System.out.println("Vous avez indiquez moins de conditions que nécéssaire");
			} else {
				System.out.println("Vous avez indiquez le bon nombre de conditions");
			}
			boolean sameItems = true;
			for (String item : conditions) {
				if (!((Delete) reponse).getConditions().contains(item)) {
					System.out.println(item + " n'apparait pas dans la réponse du prof");
					sameItems = false;
				}
			}
			if (sameItems) {
				System.out.println("Vos conditions sont identiques à celles du prof");
			}
		}
	}
	
	public ArrayList<String> getConditions() {
		return conditions;
	}
}
