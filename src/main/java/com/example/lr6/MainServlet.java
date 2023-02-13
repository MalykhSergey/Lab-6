package com.example.lr6;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "mainServlet", value = "/")
public class MainServlet extends HttpServlet {

    private int[][] daysTemperatures = new int[31][2];
    private float avgTemperature = 0;
    private int daysWasHigher = 0;
    private int daysWasLower = 0;
    private int[][] hotDays = {{0, -100}, {0, -100}, {0, -100}};

    public void init() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\redmik\\IdeaProjects\\LR-6\\data.txt"));
            String line = bufferedReader.readLine();
            int i =0;
            while (line!=null){
                String[] dayTemperature = line.split("\t");
                daysTemperatures[i][0] = Integer.parseInt(dayTemperature[0]);
                daysTemperatures[i][1] = Integer.parseInt(dayTemperature[1]);
                avgTemperature += Integer.parseInt(dayTemperature[1]);
                line = bufferedReader.readLine();
                i++;
            }
            bufferedReader.close();
            avgTemperature = avgTemperature / 30;
            for (int[] dayTemperature : daysTemperatures) {
                if (dayTemperature[1] > avgTemperature) {
                    daysWasHigher++;
                    for (int j = 0; j < 3; j++) {
                        if (dayTemperature[1] > hotDays[j][1]) {
                            hotDays[j] = dayTemperature;
                            break;
                        }
                    }
                } else if (dayTemperature[1] < avgTemperature) daysWasLower++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>LR-6</h1>");
        out.printf("<h2>Средняя температура: %.2f°С</h2>\n", avgTemperature);
        out.println("<h2>" + "Температура была выше средней: " + daysWasHigher + " раз(а)</h2>");
        out.println("<h2>" + "Температура была ниже средней: " + daysWasLower + " раз(а)</h2>");
        out.println("<h2>Самыe тёплые дни:</h2>");
        out.println("<ul>");
        out.println("<li>" + hotDays[0][0] + " числа было: " + hotDays[0][1] + "°С</li>");
        out.println("<li>" + hotDays[1][0] + " числа было: " + hotDays[1][1] + "°С</li>");
        out.println("<li>" + hotDays[2][0] + " числа было: " + hotDays[2][1] + "°С</li>");
        out.println("</ul>");
        out.println("</body></html>");
    }
}