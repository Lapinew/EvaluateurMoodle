package evaluateur;

import java.sql.Connection;
import java.util.ArrayList;

public class Insert extends Modifiantes {
	
	private boolean allField;
	private ArrayList<String> fields = new ArrayList<String>();
	private ArrayList<String> values = new ArrayList<String>();
	
	public Insert(String requete, String nomFichier, Connection connexion, String tabSelect) {
		super(requete, nomFichier, connexion, tabSelect);
		allField = false;
		String standardised = "";
		int currentChar;
		int lastChar = 0;
		for (int i = 0; i < this.requete.length(); i++) {
			currentChar = this.requete.charAt(i);
			if (i == this.requete.length()-1) { //Au bout de la requete on separe le ";"
				standardised += ((char) currentChar);
			} else if (currentChar == 44) { //Si virgule on remplace par un espace
				if (lastChar != 32) {
					standardised += " ";
				}
				lastChar = 32;
				continue;
			} else if (currentChar == 40 || currentChar == 41) { //Si parenthèse
				if (lastChar == 32 && this.requete.charAt(i) == 32) {
					i++;
				} else if (lastChar == 32 || this.requete.charAt(i) == 32) {
				} else {
					standardised += " ";
				}
				lastChar = 32;
				continue;
			} else if (currentChar == 32 && lastChar == 32) { //Si double espace on fait rien
			} else if (currentChar == 34) { //Si guillemets on met des quotes a la place
				standardised += (char) 39;
			} else {
				standardised += ((char) currentChar);
			}
			lastChar = currentChar;
		}
		this.cleanRequete = standardised.toUpperCase();
		System.out.println("Standard : " + this.cleanRequete);
		
		String[] requeteSplit = this.cleanRequete.split(" ");
		
		if (requeteSplit[3].equals("VALUES")) {
			allField = true;
		}
		
		int valuesPos = 4;
		if (!allField) {
			for (int i = 4; i < requeteSplit.length; i++) {
				if (requeteSplit[i].equals("VALUES")) {
					valuesPos = i + 1;
					break;
				} else {
					fields.add(requeteSplit[i]);
				}
			}
		}
		for (int i = valuesPos; i < requeteSplit.length; i++) {
			if (requeteSplit[i].equals(";")) {
				break;
			} else {
				values.add(requeteSplit[i]);
			}
		}
	}
	
	public void compareSyntaxe(Reponse reponse) {
		boolean sameItems = true;
		if (!allField) {
			System.out.println("Comparaison des champs ajoutés :");
			if (fields.size()>((Insert) reponse).getFields().size()) {
				System.out.println("Vous avez indiquez plus de champs que nécéssaires");
			} else if (fields.size()<((Insert) reponse).getFields().size()) {
				System.out.println("Vous avez indiquez moins de champs que nécéssaire");
			} else {
				System.out.println("Vous avez indiquez le bon nombre de champs");
			}
			sameItems = true;
			for (String item : fields) {
				if (!((Insert) reponse).getFields().contains(item)) {
					System.out.println(item + " n'apparait pas dans la réponse du prof");
					sameItems = false;
				}
			}
			if (sameItems) {
				System.out.println("Vos champs sont identiques à ceux du prof");
			}
		}
		System.out.println("Comparaison des valeurs indiquées : ");
		if (values.size()>((Insert) reponse).getValues().size()) {
			System.out.println("Vous avez indiquez plus de valeurs que nécéssaires");
		} else if (values.size()<((Insert) reponse).getValues().size()) {
			System.out.println("Vous avez indiquez moins de valeurs que nécéssaire");
		} else {
			System.out.println("Vous avez indiquez le bon nombre de valeurs");
		}
		sameItems = true;
		for (String item : values) {
			if (!((Insert) reponse).getValues().contains(item)) {
				System.out.println(item + " n'apparait pas dans la réponse du prof");
				sameItems = false;
			}
		}
		if (sameItems) {
			System.out.println("Vos valeurs sont identiques à ceux du prof");
		}
	}
	
	public ArrayList<String> getFields() {
		return fields;
	}
	
	public ArrayList<String> getValues() {
		return values;
	}
}
