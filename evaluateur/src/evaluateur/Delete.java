package evaluateur;

import java.sql.Connection;

public class Delete extends Modifiantes {
	
	public Delete(String requete, String nomFichier, Connection connexion, String tabSelect) {
		super(requete, nomFichier, connexion, tabSelect);
	}
	
}
