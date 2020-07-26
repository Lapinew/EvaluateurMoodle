package evaluateur;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.reflect.Type;
import java.sql.Connection;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class Table { //Cette classe permet de rassembler une requete SQL, son resultat et le nom du fichier permettant de créer la BD nécéssaire à l'execution de la requete
	
	//CallableStatement : Appel procédure/fonction
	//PreparedStatement : adapté pour SELECT, INSERT, UPDATE, DELETE (parametres changeables)
	//Statement : adapté pour CREATE, DROP, ALTER, TRUNCATE et pour le reste si requete FIXE

	//Attributs
	protected String nomFichier;
	protected ArrayList<ArrayList<Object>> table;
	protected String requete;
	
	public Table(String requete, String nomFichier) {
		this.nomFichier = nomFichier;
		this.requete = requete;
	}
	
	//Methodes
	public ArrayList<ArrayList<Object>> getTable () {
		return table;
	}
	
	public String getRequete () {
		return requete;
	}
	
	public String getFichier () {
		return nomFichier;
	}
	
	public void afficheTable() {
		System.out.println(table.toString());
	}
	
	private void aLaSuite(String nomTest) {
		Gson gson = new Gson();
		ArrayList<Table> reponses = null; //On prepare l'arrayList pour récupérer les données dans le json
		try (FileReader reader = new FileReader(nomTest)) { //On lit le json
			reponses = (ArrayList<Table>) gson.fromJson(reader, Class.forName("java.util.ArrayList")); //On récupère les données
			reponses.add(this); //On ajoute la table qu'on a crée aux données récupérées
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try (FileWriter file = new FileWriter(nomTest)) { //On remplace le contenu du json par le nouveau arrayList
			gson.toJson(reponses, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private void premiereTable(String nomTest) {
		Gson gson = new Gson();
		ArrayList<Table> buffer = new ArrayList<Table>(); //On crée l'arrayList qui va contenir les réponses
		buffer.add(this); //On met la premiere table dans l'array
		try (FileWriter file = new FileWriter(nomTest)) { //On met l'array dans le json
			gson.toJson(buffer, file);
        } catch (IOException e) {
            e.printStackTrace();
        }		
	}
	
	public void toJSON(String nomTest) {
		File exist = new File(nomTest);
		if (exist.length() == 0) { //Si le fichier ne contient pas de liste
			System.out.println("Le fichier est vide, cette table va initialiser un nouveau JSONArray");
			this.premiereTable(nomTest);
		} else { //Le fichier contient deja une liste
			System.out.println("Le fichier contient deja un JSONArray, la table va etre ajoutée a la suite");
			this.aLaSuite(nomTest);
	    }
	}
	
	private int minimum(int n1, int n2, int n3) {
		if (n1 <= n2 && n1 <= n3) {
			return n1;
		}
		else if (n2 <= n1 && n2 <= n3) {
			return n2;
		}
		else {
			return n3;
		}
	}
	
	private int distanceLev(String str1, String str2) { //Tweekd pour utiliser un tableau à 2 lignes au lieu d'un tableau String1 * String2
		char[] arrChar1 = str1.toCharArray();
		char[] arrChar2 = str2.toCharArray();
		int cout;
		boolean pair = true;
		int[][] tab = new int[2][str1.length()]; //Tableau 2 lignes
		for (int i = 0; i < str2.length(); i++) {
			tab[0][i] = i;
		}
		for (int i = 1; i < str2.length(); i++) {
			//On alterne entre les 2 lignes pour calculer les substitutions
			if (pair) { 
				tab[1][0] = i;
				for (int j = 1; j < str1.length(); j++) {
					cout = (arrChar2[i] == arrChar1[j]) ? 0 : 1;
					tab[1][j] = minimum(tab[0][j-1]+cout, tab[1][j-1]+1, tab[0][j]+1);
				}
				pair = false;
			} else {
				tab[0][0] = i;
				for (int j = 1; j < str1.length(); j++) {
					cout = (arrChar2[i] == arrChar1[j]) ? 0 : 1;
					tab[0][j] = minimum(tab[1][j-1]+cout, tab[0][j-1]+1, tab[1][j]+1);
				}
				pair = true;
			}
		}
		if (pair) {
			return tab[0][str1.length()-1];
		} else {
			return tab[1][str1.length()-1];
		}
	}
	
	public Table getTableFromJSON(String nomTest, int numQuestion) {
		Gson gson = new Gson();
		Type listType = new TypeToken<ArrayList<Table>>(){}.getType();
		ArrayList<Table> reponses = null; //On prepare l'arrayList pour récupérer les données dans le json
		try (FileReader reader = new FileReader(nomTest)) { //On lit le json
			//Class.forName("java.util.ArrayList")
			reponses = (ArrayList<Table>) gson.fromJson(reader, listType); //Récupère l'array du json
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		}
		return reponses.get(numQuestion-1);
	}
	
	public void comparaison(Table reponse2) { //Methode permettant de comparer la réponse d'un eleve a celle de l'enseignant
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
