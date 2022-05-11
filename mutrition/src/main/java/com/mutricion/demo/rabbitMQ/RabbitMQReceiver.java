package com.mutricion.demo.rabbitMQ;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import com.google.gson.Gson;
import com.mutricion.demo.modelo.Dieta;
import com.mutricion.demo.modelo.Receta;
import com.mutricion.demo.modelo.User;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class RabbitMQReceiver {
    public static final String RECEIVE_METHOD_NAME = "receiveMessage";

    @Autowired
    RestTemplate rTemplate;
    Gson gson;

    public RabbitMQReceiver() {
        gson = new Gson();
    }

    public void receiveMessage(byte[] message) {
        //message == user + receta
        try {
            parseoBytesDieta(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseoBytesDieta(byte[] body) throws IOException {
        JSONObject testV = new JSONObject(new String(body));
        
        Integer idUser = (Integer) testV.get("idUser");
        Integer idReceta = (Integer) testV.get("idReceta");

        Dieta dieta = crearDieta(String.valueOf(idUser), String.valueOf(idReceta));

        User user = getUser(String.valueOf(idUser));
        Receta receta = getReceta(String.valueOf(idReceta));
        
        String path = "/var/jenkins_home/dieta/"+user.getId()+"/"+receta.getId();

        File theDir = new File(path);
        if (!theDir.exists()){
            theDir.mkdirs();
        }

        generarDocumento(user, receta, dieta);
    }

    private void generarDocumento(User user, Receta receta, Dieta dieta) {
        PrintWriter pw = null;
        String path = "/var/jenkins_home/dieta/"+user.getId()+"/"+receta.getId();
        String dieta_path = "/dieta"+dieta.getId()+".tex";
        try {
            pw = new PrintWriter(new FileWriter(path+dieta_path));
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        String text = "\\documentclass{beamer}\n"
                + "\\usepackage[size=a1,orientation=portrait,scale=1.8]{beamerposter}\n" +

                "\\usepackage[utf8]{inputenc}\n" + "\\usepackage[T1]{fontenc}\n" + "\\usepackage{libertine}\n"
                + "\\usepackage[scaled=0.92]{inconsolata}\n" + "\\usepackage[libertine]{newtxmath}\n"
                + "\\usepackage{tabularx}\n" + "\\usepackage{caption}\n" + "\\usepackage{subcaption}\n" +

                "\\definecolor{VioletRed}         {RGB}{215, 0, 0} \n"
                + "\\setbeamercolor{block title}{fg=VioletRed,bg=white}\n"
                + "\\setbeamercolor{block body}{fg=black,bg=white}\n"
                + "\\setbeamercolor{local structure}{fg=VioletRed}\n"
                + "\\setbeamercolor{bibliography structure}{fg=VioletRed}\n"
                + "\\setbeamercolor{bibliography item}{fg=black,bg=white}\n"
                + "\\setbeamercolor{bibliography entry author}{fg=black,bg=white}\n"
                + "\\setbeamercolor{bibliography item}{fg=black,bg=white}\n"
                + "\\setbeamercolor{bibliography entry location}{fg=black} \n"
                + "\\setbeamercolor{bibliography entry note}{fg=black} \n"

                + "\\author[ffs@ime.usp.br]{User: "+user.getName()+" "+user.getLastname()+"}\n" + "\\title{MUTRITION S.L}\n"
                + "\\institute{RECETA:}\n"

                + "        \\addtobeamertemplate{headline}{} {\n" + " \\leavevmode\n" + "  \\begin{columns}[T]\n"
                + "        \\begin{column}{.25\\linewidth}\n" + "        \\vskip0.5cm\n" + "        \\hskip1cm\n"
                + "        \\includegraphics[width=1.2\\linewidth]{/var/jenkins_home/Logo_Mondragon_Unibertsitatea.png}\n"
                + "        \\end{column}\n" + "    \\begin{column}{.5\\linewidth}\n" + "         \\vskip3cm\n"
                + "        \\centering\n"
                + "        \\usebeamercolor{title in headline}{\\color{black}\\Huge{\\textbf{\\inserttitle}}\\\\[0.5ex]}\n"
                + "        \\vskip.5cm\n"
                + "        \\usebeamercolor{subtitle in headline}{\\color{black}\\huge{\\insertsubtitle}\\\\[0.5ex]}\n"
                + "        \\vskip.5cm\n"
                + "        \\usebeamercolor{author in headline}{\\color{fg}\\Large{\\textbf{\\insertauthor}}\\\\[1ex]}\n"
                + "        \\usebeamercolor{institute in headline}{\\color{fg}\\Large{\\insertinstitute}\\\\[1ex]}\n"
                + "        \\vskip1cm\n" + "    \\end{column}\n" + "    \\begin{column}{.25\\linewidth}\n"
                + "        \\vskip 2.1cm\n" + "        \\begin{flushleft}\n"
                + "        \\includegraphics[width=\\linewidth]{/var/jenkins_home/mutrition_logo.png}\n"
                + "        \\end{flushleft}\n" + "        \\hskip1cm\n" 
                + "        \\end{column}       \n" + "   \\vspace{0.1cm}\n" + "  \\end{columns}\n" + " \\vspace{0.1cm}\n"
                + "        \\hspace{0.1cm}\\begin{beamercolorbox}[wd=47in,colsep=0.15cm]{black}\\end{beamercolorbox}\n"
                + "        \\vspace{0.1in}\n" + "}\n" + "\\begin{document}\n" + "\\begin{frame}[fragile]\\centering\n" 
                + "        \\begin{table}[]\n" + "\\large\n" + "\\begin{tabular}{|p{0.45\\textwidth}|p{0.45\\textwidth}|}\n" + "\\hline\n" 
                
                + "Receta                           & "+ receta.getName()+"  \\\\ \\hline\n"

                + "\\vskip 1cm\n"
                + "\\vskip 1cm\n" + "\\begin{flushleft}\n" + "Tu IA Favorita: \n" + "\\vskip 0.25cm\n"
                + "\\hskip2cm\\includegraphics[width=0.1\\linewidth]{/var/jenkins_home/firma.png}\n" + "\\end{flushleft}\n"
                + "\\end{frame}\n" + "\\end{document}\n";

        pw.write(text);
        pw.close();

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(
                    "pdflatex -interaction=nonstopmode -output-directory "+path+" "+path+dieta_path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Dieta crearDieta(String idUser, String idReceta) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        JSONObject dieta_json = new JSONObject();
        dieta_json.put("dieta", "dieta [" + formatter.format(date).toString() + "]");
        dieta_json.put("fecha_hora", formatter.format(date));
        dieta_json.put("usuario_id", Integer.valueOf(idUser));
        dieta_json.put("receta_id", Integer.valueOf(idReceta));
        dieta_json.put("url","/var/jenkins_home/dieta/"+idUser+"/"+idReceta);

        String query = dieta_json.toString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(query, headers);
        String uri = "http://172.17.0.1:1880/addDieta";

        String body = rTemplate.postForObject(uri, entity, String.class);
        System.out.println("dieta post --> "+body);
        JSONObject bodyObject = new JSONObject(body);
        Dieta dieta = gson.fromJson(bodyObject.getJSONObject("entity").toString(), Dieta.class);

        return dieta;
    }

    public Receta getReceta(String idReceta) {
        final String uri = "http://172.17.0.1:1880/getReceta/";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri).path(idReceta);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        System.out.println(builder.toUriString());

        HttpEntity<String> response = rTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);

        JSONObject obj = new JSONObject(response);
        String body = obj.getString("body");
        JSONObject bodyObject = new JSONObject(body);
        Receta receta = gson.fromJson(bodyObject.getJSONObject("entity").toString(), Receta.class);

        return receta;
    }

    public User getUser(String idUser) {
        final String uri = "http://172.17.0.1:1880/getUser/";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri).path(idUser);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        System.out.println(builder.toUriString());

        HttpEntity<String> response = rTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);

        System.out.println(response);

        JSONObject obj = new JSONObject(response);
        String body = obj.getString("body");
        JSONObject bodyObject = new JSONObject(body);
        User user = gson.fromJson(bodyObject.getJSONObject("entity").toString(), User.class);

        return user;
    }

}