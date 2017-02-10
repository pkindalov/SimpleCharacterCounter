package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;


import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Controller {


    public Button btnProcess;
    public TextArea textField;
    public TextField pathToFile;
    public TextField pathNameNewFile;
    public Label lblStatusBar;

    public void countChars(ActionEvent actionEvent) throws InterruptedException {

        String userText = textField.getText();
        textField.setText("Your message is: \n\n" + userText + " \n");
        textField.appendText("\n\nYour message has " + userText.length() + " characters");

        Map<Character, Integer> symbols = new HashMap<>();

        if(userText.length() == 0){
            textField.setText("");
            lblStatusBar.setText("You must enter more than 0 characters");
        }else {
            for (Character character : userText.toCharArray()) {

                if(!symbols.containsKey(character)){
                    symbols.put(character, 1);
                }else {
                    symbols.put(character, symbols.get(character) + 1);
                }

            }


            for (Character character : symbols.keySet()) {
                textField.appendText("\n" + character + "-> " + symbols.get(character) + " times");
            }

        }






    }

    public void copyToClipboard(ActionEvent actionEvent) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();

        String textToCopy = textField.getText();
        content.putString(textToCopy);
        clipboard.setContent(content);
        lblStatusBar.setText("copy was successfull");
    }

    public void resetTxtField(ActionEvent actionEvent) {

        textField.clear();
        lblStatusBar.setText("");
    }

    public void writeToFile(ActionEvent actionEvent) {
        final String textToWriteOnFile = textField.getText();
//        final String pathToSaveAndFileName = pathNameNewFile.getText();
        String fileSize = "0";
        long sizeInMB = 0L;
//        String regex = "[^\\.]+(?<=\\/)";
        FileChooser ch = new FileChooser();
        File selectedFile = ch.showSaveDialog(((Button)actionEvent.getSource()).getScene().getWindow());
        String fileName = catcOnlyFileName(selectedFile.toString());
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(pathToSaveAndFileName);


        appendWriteFile(textToWriteOnFile, fileSize, sizeInMB, selectedFile, fileName);

//        writeToFileWithoutAppend(textToWriteOnFile, fileSize, sizeInMB, selectedFile, fileName);





    }

    private void writeToFileWithoutAppend(String textToWriteOnFile, String fileSize, long sizeInMB, File selectedFile, String fileName) {
        try(PrintWriter writer = new PrintWriter(selectedFile)) {

            for (char c : textToWriteOnFile.toCharArray()) {
                writer.write(c);
                sizeInMB++;
            }

            fileSize = String.valueOf(textToWriteOnFile.length());
            writer.close();

        } catch (FileNotFoundException e) {
            lblStatusBar.setText(e.getMessage());
        }

        sizeInMB /= 1024;
        lblStatusBar.setText("Successfully loaded file: " + fileName + " with size: " + fileSize + " Bytes" + "(" + sizeInMB + "MB" + ")");
    }

    private void appendWriteFile(String textToWriteOnFile, String fileSize, long sizeInMB, File selectedFile, String fileName) {
        try(FileOutputStream fos = new FileOutputStream(selectedFile, true)) {

            fos.write('\n');
            for (char c : textToWriteOnFile.toCharArray()) {
                fos.write(c);
                sizeInMB++;
            }

            fileSize = String.valueOf(textToWriteOnFile.length());
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        sizeInMB /= 1024;
        lblStatusBar.setText("Successfully loaded file: " + fileName + " with size: " + fileSize + " Bytes" + "(" + sizeInMB + "MB" + ")");
    }

    public void mostUsedWord(ActionEvent actionEvent) {

        String text = textField.getText();
        String[] input = text.split("[ \n,.]+");
        String mostFrequentWord = "";
        int howManyTimes = Integer.MIN_VALUE;

        if(text.length() <= 1){
            textField.setText("Too short.Enter longer text");
        }else {
            Map<String, Integer> mostFrequentWords = new HashMap<>();

            for (String s : input) {
                if(!mostFrequentWords.containsKey(s)){
                    mostFrequentWords.put(s, 1);
                }else {
                    mostFrequentWords.put(s, mostFrequentWords.get(s) + 1);
                }

            }


            for (String s : mostFrequentWords.keySet()) {
                String currentWord = s;
                int currentWordCount = mostFrequentWords.get(s);

                if(currentWordCount > howManyTimes && currentWord.length() > 1){
                    howManyTimes = currentWordCount;
                    mostFrequentWord = s;
                }

            }


            text = text.replace(mostFrequentWord, mostFrequentWord.toUpperCase());
//        textField.setFont(new Font("Courier New", 22));
            textField.setText(text);

            textField.appendText("\n\n\n Most used word is " + mostFrequentWord.toUpperCase() + " Repeated " + howManyTimes + " times");

        }





    }


    public String catcOnlyFileName(String str){
        String regex = "[^\\/]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        while (matcher.find()){
            str = matcher.group();
        }

        return str;
    }



    public void openFile(ActionEvent actionEvent) {
//        String input = pathToFile.getText();
        StringBuilder sb = new StringBuilder();
        long fileSize = 0L;
        long sizeInMB = 0L;

        FileChooser ch = new FileChooser();
        File selectedFile = ch.showOpenDialog(((Button)actionEvent.getSource()).getScene().getWindow());

        String fileName = catcOnlyFileName(selectedFile.toString());


//        String regex = "[^\\/]+";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(selectedFile.toString());
//        String file = "";
//
//        while (matcher.find()){
//           file = matcher.group();
//        }



        if(selectedFile.length() == 0){
            textField.setText("Must enter path and new file fields. Enter full path and full name of file");
            textField.appendText("\n Example: C:/somedirectory/yourfile.txt");

        }else {


            try(FileInputStream fis = new FileInputStream(selectedFile)
            ) {

                int read = fis.read();
                while (read >= 0){
                    sb.append(((char)read));
                    read = fis.read();
                    fileSize++;
                }


                fis.close();
                textField.setText(sb.toString());




            }  catch (IOException e) {
               textField.setText(e.getMessage());
               lblStatusBar.setText(e.getMessage());
            }

            sizeInMB = fileSize / 1024;
            lblStatusBar.setText("Successfully loaded file: " + fileName + " with size: " + fileSize + " Bytes" + "(" + sizeInMB + "MB" + ")");

//            while (matcher.find()){
//                String fileName = matcher.group();
//                File file = new File(fileName);
//                lblStatusBar.setText("Saved successfully in file " +matcher.group() + " Size of file " + file.length());
//            }



        }




    }


    public void saveWithoutAppend(ActionEvent actionEvent) {

        final String textToWriteOnFile = textField.getText();
//        final String pathToSaveAndFileName = pathNameNewFile.getText();
        String fileSize = "0";
        long sizeInMB = 0L;
//        String regex = "[^\\.]+(?<=\\/)";
        FileChooser ch = new FileChooser();
        File selectedFile = ch.showSaveDialog(((Button)actionEvent.getSource()).getScene().getWindow());
        String fileName = catcOnlyFileName(selectedFile.toString());

        writeToFileWithoutAppend(textToWriteOnFile, fileSize, sizeInMB, selectedFile, fileName);

    }
}
