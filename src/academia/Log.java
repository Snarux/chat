package academia;

import java.io.*;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class Log {

    public static void writting(String incomingString) {

        LocalDate date = LocalDate.now(); // gets the current date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");

        File yourFile = new File(date.format(formatter)+".txt");
        try {
            yourFile.createNewFile(); // if file already exists will do nothing
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(date.format(formatter)+".txt", true)));
                out.println(incomingString);
                out.flush();
                out.close();
            }
            catch (IOException e) {
                System.out.println(e);
        }

    }
}
