package evaluateur;

import java.sql.Connection;

public class Update extends Modifiantes {

	public Update(String requete, String nomFichier, Connection connexion, String tabSelect) {
		super(requete, nomFichier, connexion, tabSelect);
	}
	
	public void compareSyntaxe(Reponse reponse) {
		
	}
	
}
