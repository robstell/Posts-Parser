package pacote;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Posts_Parser
{
    public static String linkInicial;
    public static Document pagina_HTML_Capturada;
    public static ArrayList<String> listaLinksIntermediarios;
    public static ArrayList<String> listaLinksFinais;
    public static ArrayList<String> listaSetBody;
    public static int posicao = 0;
    
    public static int posts = 0;
    
    public static String extensao = ".html";
    public static String caminho = "H:/Temp/";
    public static String nome = "Gaga Games Random Link";

    
    public static void main(String[] args) throws IOException, InterruptedException
    {
        
        linkInicial = "http://www.gagagames.com.br/";
        
    
        if (!linkInicial.endsWith("/"))                                  	//Verificador de contra barra (negação)
        {
            linkInicial = linkInicial + "/";
        }
            
        System.out.println("Link inicial: " + linkInicial);
        capturaPagina_HTML(linkInicial);
        System.out.println("Página HTML capturada");
        
        
        capturaLinksIntermediarios(pagina_HTML_Capturada);                      //roda 1 vez apenas
        System.out.println("");
        System.out.println("LINKS INTERMEDIÁRIOS adicionados: " + listaLinksIntermediarios);
        System.out.println("");
              
        
        int cont=1;                                                       	//Percorre LISTA DE LINKS INTERMEDIÁRIOS (Nesse caso, pela segunda vez)
        //listaLinksIntermediarios = new ArrayList<>();
        listaSetBody = new ArrayList<>();                                       //Criação prévia de LISTA 
        
        for (String i : listaLinksIntermediarios)
        {
            System.out.println("");
            System.out.println("Link " + cont + "/" + listaLinksIntermediarios.size() + ": " + i);
            String link = i;
            
            capturaPagina_HTML(link);
            capturaLinksFinais(pagina_HTML_Capturada, link);
            //InsereHTML(listaLinksFinais);
           
            cont++;
        }
        
        geraHTML(listaSetBody);
    }
    
    public static Document capturaPagina_HTML(String link) throws IOException
    {
        pagina_HTML_Capturada = Jsoup.connect(link).get();
        
    return pagina_HTML_Capturada;
    }
    
    public static ArrayList<String> capturaLinksIntermediarios(Document pagina_HTML_Capturada) throws IOException
    {

        Elements linkElements = pagina_HTML_Capturada.select(".widget_archive").select("li > a"); 	//1.Captura LINK-ELEMENTOS da PAGINA HTML capturada
        listaLinksIntermediarios = new ArrayList<>();
        

        for (Element i : linkElements)                                                                  //2.Transforma os LINK-ELEMENTOS em LINKS PUROS 
        {
            if (i.toString().matches(".*/[0-9][0-9]/.*"))		
            {		
                listaLinksIntermediarios.add(i.attr("href"));                                           //3.Coloca dentro de uma LISTA
                //System.out.println(i.attr("href"));
            }
        }
        
     return listaLinksIntermediarios;
    }
    
    public static ArrayList<String> capturaLinksFinais(Document paginaHTML, String link) throws IOException, InterruptedException
    {

        int pag = 1;                                                            //1.Captura LINK-ELEMENTOS da PAGINA HTML capturada
        int cont = 1;
        Elements linkElements = paginaHTML.select("header h2 a");
        listaLinksFinais = new ArrayList<>();
        
        for (Element i : linkElements) 						//2.Transforma os LINK-ELEMENTOS em LINKS PUROS 
        {
            
            System.out.println(cont + ": " + i.attr("href"));
            listaLinksFinais.add(i.attr("href"));                               //3.Coloca dentro de uma LISTA
            //Collections.sort(listaLinksFinais);

            posts++;
            cont++;
        }
        
        System.out.println(""); System.out.println("Posts = " + posts);
//        System.out.println("Aperte qualquer tecla...");
//        Scanner in = new Scanner (System.in); in.nextLine();
        
//        System.out.println();
//        Elements classNext = pagina_HTML_Capturada.getElementsByClass("previous");
//        
//        while (classNext.hasText() == true )
//        {
////Gerando Links-Elementos da página (próxima página)
//            pag++;
//            cont = 1;
//            String linkNextPage = link + "page/" + pag;
//            
//            System.out.println(linkNextPage);
//            
//            pagina_HTML_Capturada = Jsoup.connect(linkNextPage).get();
//            linkElements = pagina_HTML_Capturada.select(".entry-title > a");
//            
////Transformando cada Links-Elemento em link puro (próxima página)
//            for (Element i : linkElements) 
//            {
//                System.out.println(cont + ": " + i.attr("href"));
//                listaLinksFinais.add(i.attr("href"));
//                
//                posts++;
//                cont++;
//            }
//            
//            System.out.println("");System.out.println("Posts = " + posts);
////            System.out.println("Aperte qualquer tecla...");
////            in = new Scanner (System.in); in.nextLine();
//            
//            System.out.println();
//            classNext = pagina_HTML_Capturada.getElementsByClass("previous");
//        }
     

     char semicolon = ';';

     for ( String i : listaLinksFinais)
     {
        listaSetBody.add("     links [" + posicao + "] = \"" + i + "\"" + semicolon);
        posicao++;
     }

     return listaSetBody;
    }
    
//    public static ArrayList<String> InsereHTML(ArrayList<String> listaLinksFinais) throws IOException
//    {
//       char semicolon = ';';
//       
//       for ( String i : listaLinksFinais)
//       {
//           listaSetBody.add("     links [" + posicao + "] = \"" + i + "\"" + semicolon);
//           posicao++;
//       }
//       
//     return listaSetBody;
//    }
    
    public static void geraHTML(ArrayList<String> setBody) throws FileNotFoundException, IOException
    {
        String headerHTML = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<body>\n" +
                    "<script type=\"text/javascript\">\n" + 
                    "     var links = new Array();\n";
        
        String footerHTML = "     var random = Math.floor(Math.random()*links.length);\n" +
                    "     window.location = links[random];\n" + 
                    "</script>\n" +
                    "</body>\n" +
                    "</html>\n";
        
        String fileName = caminho +         //Caminho
                          nome +            //Nome
                          extensao;         //Extensão
        
        try (FileWriter file = new FileWriter(fileName))
        {
            file.write(headerHTML);
        
            for (String i : setBody)
            {
                file.write(i);
                file.write("\n");
            }

            file.write(footerHTML);
            file.close();
        }
        
        System.out.println("");
        System.out.println("HTML gerado em " + fileName + " com sucesso!");
    }
}           
