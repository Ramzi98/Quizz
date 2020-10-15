package com.example.quizz;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.quizz.DataBase.Proposition;
import com.example.quizz.DataBase.Question;
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
    QuizzsGestion ma;

    public DOMQuizz(QuizzsGestion ma) {
        this.ma = ma ;
        httpTitle = new HttpTitle() ;
    }

    public void execute() {
        httpTitle.execute() ;
    }

    class HttpTitle  extends AsyncTask<Void, Void, List<String>> {
        private List<String> titles = new ArrayList<>();
        ArrayList<com.example.quizz.DataBase.Quizz> Quizz = new ArrayList<>();
        ArrayList<Question> Questions = new ArrayList<>();
        ArrayList<Proposition> Propositions = new ArrayList<>();
        String question;
        String type ="vide";
        ArrayList<String> props;
        int nombre = 0;
        int reponse = 0;
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
                            NodeList propositionList = ((Element) propositionsNode).getElementsByTagName("Proposition");
                            props = new ArrayList<>();

                            // Boucle pour récuperer les toutes les propositions d'une question
                            for (int k=0 ; k<propositionList.getLength() ; k++)
                            {
                                //Récuperer un element depuis la liste des proposition(de réponse)
                                // puis le transformer en Element
                                Node propositionNode = propositionList.item(k);
                                Element propositionElement = (Element) propositionNode;

                                props.add(propositionElement.getTextContent());
                            }
                            for(int k2=0;k2<props.size();k2++)
                            {
                                props.set(k2,props.get(k2).replaceAll("\\R", ""));
                                props.set(k2,props.get(k2).replaceAll("\t", ""));
                            }
                            //Récuperer un element depuis la liste des Réponse puis le transformer en type Element
                            NodeList reponseList = questionElement.getElementsByTagName("Reponse");
                            Node reponseNode = reponseList.item(0);
                            Element reponseElement = (Element) reponseNode ;
                            //Affecter la valeur récuperer vers le variable nombre
                            reponse = Integer.parseInt(reponseElement.getAttribute("valeur"));
                            //sauvgarder toutes les valeur dans un Arraylist<Quizz>
                            Quizz.add(new Quizz(type));
                            Questions.add(new Question(question,reponse,nombre));
                            Propositions.add(new Proposition(props));
                        }

                    }


                }
                else {
                    Toast.makeText(ma.getApplicationContext(),"Probleme de Connexion HTTP", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
            ma.Add_New_Quizz(Quizz,Questions,Propositions);
        }
    }

}
