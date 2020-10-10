package com.example.quizz;

import android.os.AsyncTask;
import android.util.Log;

import com.example.quizz.DataBase.Quizz;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

class DOMQuizz {
    HttpTitle httpTitle ;
    MainActivity ma;

    public DOMQuizz(MainActivity ma) {
        this.ma = ma ;
        httpTitle = new HttpTitle() ;
    }

    public void execute() {
        httpTitle.execute() ;
    }

    class HttpTitle  extends AsyncTask<Void, Void, List<String>> {
        private List<String> titles = new ArrayList<String>();
        ArrayList<com.example.quizz.DataBase.Quizz> Quizz = new ArrayList<com.example.quizz.DataBase.Quizz>();
        String question;
        String type ="vide";
        ArrayList<String> props = new ArrayList<String>();
        int nombre = 0;
        int reponse = 0;
        int current = 0;
        ArrayList<String> propositions = new ArrayList<String>();
        private void getPage(String adresseURL) {
            BufferedReader bufferedReader = null;
            HttpURLConnection urlConnection = null;
            try {
                //Rendre le String passer dans les parammetre un URL
                URL url = new URL(adresseURL);

                // faire la connexion vers l'URL donnez
                urlConnection = (HttpURLConnection) url.openConnection();
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = urlConnection.getInputStream();
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse (inputStream);
                    doc.getDocumentElement().normalize ();

                    //Récuperer toutes la liste des noued Quizz
                    NodeList quizzList = doc.getElementsByTagName("Quizz");

                    //Boucle pour parcourir tous les quizz
                    for (int i=0;i<quizzList.getLength();i++)
                    {
                        //Récuperer un element depuis la liste de quizz puis le rendre en type Element
                        Node quizzNode = quizzList.item(i);
                        Element quizzElement = (Element) quizzNode ;
                        //Récuperer le type de Quizz
                        type = quizzElement.getAttribute("type");
                        //Récuperer toutes la liste des noued Question
                        NodeList questionsList = quizzElement.getElementsByTagName("Question") ;

                        //Boucle pour parcourir tous les questions de quizz(i)
                        for(int j = 0; j < questionsList.getLength(); j++)
                        {
                            //Récuperer un element depuis la liste des questions
                            // est sont premier fils qui est la question et enlever les sautes de ligne et les espaces
                            question = questionsList.item(j).getFirstChild().getNodeValue();
                            question = question.replaceAll("\\R", "");
                            question = question.replaceAll("\t", "");

                            //Transformer le Node question en element
                            Node questionNode = questionsList.item(j);
                            Element questionElement = (Element) questionNode;

                            //Récuperer un element depuis la liste des propositions et le transformer en type Element
                            NodeList propositionsList = questionElement.getElementsByTagName("Propositions");
                            Node propositionsNode = propositionsList.item(0);
                            Element propositionsElement = (Element) propositionsNode;

                            //Récuperer un element depuis la liste des Nombre puis le transformer en type Element
                            NodeList nombreList = propositionsElement.getElementsByTagName("Nombre");
                            Node nombreNode = nombreList.item(0);
                            Element nombreElement = (Element) nombreNode;

                            //Récuperer le type de Réponse
                            nombre = Integer.parseInt(nombreElement.getAttribute("valeur"));

                            //Récuperer la liste des proposition
                            NodeList propositionList = propositionsElement.getElementsByTagName("Proposition");

                            // Boucle pour récuperer les toutes les propositions d'une question
                            for (int k=0 ; k<propositionList.getLength() ; k++)
                            {
                                //Récuperer un element depuis la liste des proposition(de réponse)
                                // puis le transformer en Element
                                Node propositionNode = propositionList.item(k);
                                Element propositionElement = (Element) propositionNode;
                                //Ajouter l'element réponse dans l'ArrayList
                                Log.d("k", "k = "+k);
                                props.add(propositionElement.getTextContent());
                            }
                            /*
                            Log.d("boucle k", "current : "+current);
                            Log.d("boucle k", "nombre : "+nombre);
                            Log.d("boucle k", "current+nombre : "+(current+nombre));
                            for (int k2=current;k2<(current+nombre);k2++)
                            {
                                propositions.add(props.get(k2));
                            }
                            current =current+nombre;
                             */
                            //Récuperer un element depuis la liste des Réponse puis le transformer en type Element
                            NodeList reponseList = questionElement.getElementsByTagName("Reponse");
                            Node reponseNode = reponseList.item(0);
                            Element reponseElement = (Element) reponseNode ;
                            //Affecter la valeur récuperer vers le variable nombre
                            reponse = Integer.parseInt(reponseElement.getAttribute("valeur"));
                            //sauvgarder toutes les valeur dans un Arraylist<Quizz>
                            Quizz.add(new Quizz(i+1,question,reponse,nombre,props,type));
                        }

                    }


                }
                else { }

            } catch (Exception e) { }
            finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) { }
                }
                if (urlConnection != null) urlConnection.disconnect();
            }
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            getPage("https://dept-info.univ-fcomte.fr/joomla/images/CR0700/Quizzs.xml");
            return titles;
        }

        @Override
        protected void onPostExecute(List<String> titles) {
            ma.Add_New_Quizz(Quizz);
        }
    }

}
