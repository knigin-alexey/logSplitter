/* 1. Разбить имеющийся файл на несколько файлов (5-10 файлов) и залить в одну папку.
* Входные параметры:
* п.1. 2 параметра: <имя_лог_файла>,<постоянная_часть_имени_полученных_файлов> 
*/
package logsplitter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class LogSplitter {
  /**/
  public static void main(String[] args) throws IOException {
    int qFiles = 10;
    args = changeDelim(args);
    
    if (args.length < 2) {
      System.out.println("Количество аргументов должно быть 2.");
      return;
    }
    
    String oldFileName = args[0]; //считываем 1ой арг
    String newFileName = args[1]; //считываем 2ой арг
    int qLines = countLines(oldFileName);
    
    
    int linePerFile = qLines / qFiles;
    int linesToSplit = linePerFile;
    int newFileNum = 0;
    
    try(BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(oldFileName), "CP1251"))) {
      int count = 0;
      //PrintWriter out = new PrintWriter(newFileName + "." + newFileNum);
      PrintWriter out = new PrintWriter(
              new OutputStreamWriter(
                      new FileOutputStream(newFileName + "." + newFileNum), "CP1251"));
      
      for(String line; (line = br.readLine()) != null; ) {
        ++count;
        if (count < linesToSplit) {
          out.println(line);
        } else {
          if (newFileNum < qFiles - 1) {
            out.println(line);
            out.close();
            ++newFileNum;
            linesToSplit += linePerFile;
            out = new PrintWriter(newFileName + "." + newFileNum);
          }
          else {
            out.println(line);
          }
        } 
      }
      out.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    
    System.out.println("Готово.");
  }
  
  public static String[] changeDelim(String[] args){
    StringBuilder strArgs = new StringBuilder("");
    for (int i = 0; i < args.length; i++) {
      strArgs.append(" " + args[i]);
    }
    String[] newArgs = strArgs.toString().split(",");
    for (int i = 0; i < newArgs.length; i++){
      newArgs[i] = newArgs[i].trim();
    }
    return newArgs;
  }
  
  public static int countLines(String filename) throws IOException {
    InputStream is = new BufferedInputStream(new FileInputStream(filename));
    try {
        byte[] c = new byte[1024];
        int count = 0;
        int readChars = 0;
        boolean empty = true;
        while ((readChars = is.read(c)) != -1) {
            empty = false;
            for (int i = 0; i < readChars; ++i) {
                if (c[i] == '\n') {
                    ++count;
                }
            }
        }
        return (count == 0 && !empty) ? 1 : count;
    } finally {
        is.close();
    }
  }   
}
