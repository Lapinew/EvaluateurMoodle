package evaluateur;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class Utility {

	public Utility() {
		
	}
	
	public void executeSQLfile(Connection maConnexion, String nomFichier) {
		int c;
		int back = 0;
		int cpt = 0;
		ArrayList<String> requetes = new ArrayList<String>();
		requetes.add("");
		try (FileReader in = new FileReader(nomFichier)) {
			c = in.read();
			while (c!=-1) { //Tant qu'il reste un character a lire dans le fichier
				if (c == 59) { //Si ";"
					System.out.println(requetes.get(cpt)); //J'affiche la requete que l'on vient de délimiter
					Statement stmtUpdate = maConnexion.createStatement();
					stmtUpdate.executeUpdate(requetes.get(cpt)); //On execute cette fameuse requete
					requetes.add(""); //Je crée une place pour la requete qui suit dans le fichier
					cpt++;
				}
				else if ((c == 32 && back == 32) || c == 10 || c == 13) { //Si double espace ou retour chariot linux/windows
					//Ne rien faire
				} else { //J'ajouter le character trouvé à la suite de la requete en train d'etre construite
					String temp = requetes.get(cpt);
					requetes.set(cpt, temp + Character.toString((char) c));
				}
				back = (char) c;
				c = in.read();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
