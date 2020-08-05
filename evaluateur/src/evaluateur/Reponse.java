package evaluateur;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.Type;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public abstract class Reponse { //Cette classe permet de rassembler une requete SQL, son resultat et le nom du fichier permettant de créer la BD nécéssaire à l'execution de la requete
	
	//CallableStatement : Appel procédure/fonction
	//PreparedStatement : adapté pour SELECT, INSERT, UPDATE, DELETE (parametres changeables)
	//Statement : adapté pour CREATE, DROP, ALTER, TRUNCATE et pour le reste si requete FIXE

	//Attributs
	protected String requete;
	protected String standardised = "";
	
	public Reponse(String requete) {
		this.requete = requete;
	}
	
	//Methodes
	public String getRequete () {
		return requete;
	}
	
	public String getStandard () {
		return standardised;
	}
	
	protected void aLaSuite(String nomTest) {
		GsonBuilder gsonB = new GsonBuilder();
		gsonB.registerTypeAdapter(Reponse.class, new InterfaceAdaptater());
		Gson gson = gsonB.create();
		Type listType = new TypeToken<ArrayList<Reponse>>(){}.getType();
		List<Reponse> reponses = null; //On prepare l'arrayList pour récupérer les données dans le json
		try (FileReader reader = new FileReader(nomTest)) { //On lit le json
			reponses = (ArrayList<Reponse>) gson.fromJson(reader, Class.forName("java.util.ArrayList")); //On récupère les données
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
			gson.toJson(reponses, listType, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	protected void premiereTable(String nomTest) {
		GsonBuilder gsonB = new GsonBuilder();
		gsonB.registerTypeAdapter(Reponse.class, new InterfaceAdaptater());
		Type listType = new TypeToken<ArrayList<Reponse>>(){}.getType();
		List<Reponse> buffer = new ArrayList<Reponse>(); //On crée l'arrayList qui va contenir les réponses
		buffer.add(this); //On met la premiere table dans l'array
		try (FileWriter file = new FileWriter(nomTest)) { //On met l'array dans le json
			gsonB.create().toJson(buffer, listType, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	protected void toJSON(String nomTest) {
		File exist = new File(nomTest);
		if (exist.length() == 0) { //Si le fichier ne contient pas de liste
			System.out.println("Le fichier est vide, cette reponse va initialiser un nouvel Array");
			this.premiereTable(nomTest);
		} else { //Le fichier contient deja une liste
			System.out.println("Le fichier contient deja un JSONArray, la table va etre ajoutée a la suite");
			this.aLaSuite(nomTest);
	    }
	}
	
	protected int minimum(int n1, int n2, int n3) {
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
	
	protected int distanceLev(String str1, String str2) { //Tweekd pour utiliser un tableau à 2 lignes au lieu d'un tableau String1 * String2
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
	
	protected Reponse getReponseFromJSON(String nomTest, int numQuestion) {
		GsonBuilder gsonB = new GsonBuilder();
		gsonB.registerTypeAdapter(Reponse.class, new InterfaceAdaptater());
		Gson gson = gsonB.create();
		Type listType = TypeToken.getParameterized(List.class, Reponse.class).getType();
		List<Reponse> reponses = null; //On prepare l'arrayList pour récupérer les données dans le json
		try (FileReader reader = new FileReader(nomTest)) { //On lit le json
			//Class.forName("java.util.ArrayList")
			reponses = (List<Reponse>) gson.fromJson(reader, listType); //Récupère l'array du json
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
	
	public abstract void compareSyntaxe(Reponse reponse);
	
}
