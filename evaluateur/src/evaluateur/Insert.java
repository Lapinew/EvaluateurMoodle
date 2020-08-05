package evaluateur;

import java.sql.Connection;

public class Insert extends Modifiantes {
	
	public Insert(String requete, String nomFichier, Connection connexion, String tabSelect) {
		super(requete, nomFichier, connexion, tabSelect);
	}
	
	public void compareSyntaxe(Reponse reponse) {
		
	}
}
