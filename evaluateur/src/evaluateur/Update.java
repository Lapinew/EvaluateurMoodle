package evaluateur;

import java.sql.Connection;
import java.util.ArrayList;

public class Update extends Modifiantes {
	
	private boolean boolWhere;
	private ArrayList<String> champs = new ArrayList<String>();
	private ArrayList<String> conditions = new ArrayList<String>();

	public Update(String requete, String nomFichier, Connection connexion, String tabSelect) {
		super(requete, nomFichier, connexion, tabSelect);
		boolWhere = false;
		String standardised = "";
		int currentChar;
		int lastChar = 0;
		for (int i = 0; i < this.requete.length(); i++) {
			currentChar = this.requete.charAt(i);
			if (i == this.requete.length()-1) { //Au bout de la requete on separe le ";"
				standardised += " " + ((char) currentChar);
			} else if (currentChar == 44) { //Si virgule on remplace par un espace
				if (lastChar != 32) {
					standardised += " ";
				}
				lastChar = 32;
				continue;
			} else if (currentChar == 32 && lastChar == 32) { //Si double espace on fait rien
			} else if (currentChar == 61) { //Si "=" on enleve les espaces qui pourraient le separer de ses arguments
				if (lastChar == 32) {
					standardised = standardised.substring(0, standardised.length()-1) + ((char) currentChar);
				}
				else {
					standardised += ((char) currentChar);
				}
				if (this.requete.charAt(i+1) == 32) {
					i++;
				}
			} else if (currentChar == 34) {
				standardised += (char) 39;
			} else {
				standardised += ((char) currentChar);
			}
			lastChar = currentChar;
		}
		this.cleanRequete = standardised.toUpperCase();
		System.out.println("Standard : " + this.cleanRequete);
		
		String[] requeteSplit = this.cleanRequete.split(" ");
		
		int WherePos = 0; //Va permettre de récupérer la position du from dans la requete pour commencer a partir de cette position dans le methode suivante (optimisation nb calcul)
		for (int i = 3; i < requeteSplit.length; i++) {
			if (requeteSplit[i].equals("WHERE")) {
				WherePos = i;
				boolWhere = true;
				break;
			} else if (requeteSplit[i].equals(";")) {
				break;
			} else {
				champs.add(requeteSplit[i]);
			}
		}
		
		if (boolWhere == true) {
			String currentItem = ""; //Permet d'accumuler les elements séparés par un espace : age > 40 --> age>40;
			for (int i = WherePos + 1; i < requeteSplit.length; i++) {
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
	
	public ArrayList<String> getChamps() {
		return champs;
	}

	public ArrayList<String> getConditions() {
		return conditions;
	}
	
	public void compareSyntaxe(Reponse reponse) {
		//Comparaison des champs modifiés
		System.out.println("Comparaison des champs modifiés : ");
		if (champs.size()>((Update) reponse).getChamps().size()) {
			System.out.println("Vous avez indiquez plus de modifications que nécéssaires");
		} else if (champs.size()<((Update) reponse).getChamps().size()) {
			System.out.println("Vous avez indiquez moins de modifications que nécéssaire");
		} else {
			System.out.println("Vous avez indiquez le bon nombre de modifications");
		}
		boolean sameItems = true;
		for (String item : champs) {
			if (!((Update) reponse).getChamps().contains(item)) {
				System.out.println(item + " n'apparait pas dans la réponse du prof");
				sameItems = false;
			}
		}
		if (sameItems) {
			System.out.println("Vos champs sont identiques à ceux du prof");
		}
		
		//Comparaison des conditions s'il y en a
		System.out.println("Comparaison des elements suivant WHERE : ");
		if (conditions.size()>((Update) reponse).getConditions().size()) {
			System.out.println("Vous avez indiquez plus de conditions que nécéssaires");
		} else if (conditions.size()<((Update) reponse).getConditions().size()) {
			System.out.println("Vous avez indiquez moins de conditions que nécéssaire");
		} else {
			System.out.println("Vous avez indiquez le bon nombre de conditions");
		}
		sameItems = true;
		for (String item : conditions) {
			if (!((Update) reponse).getConditions().contains(item)) {
				System.out.println(item + " n'apparait pas dans la réponse du prof");
				sameItems = false;
			}
		}
		if (sameItems) {
			System.out.println("Vos conditions sont identiques à celles du prof");
		}
	}
	
}
