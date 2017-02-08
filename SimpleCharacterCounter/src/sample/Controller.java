package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;


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
        final String pathToSaveAndFileName = pathNameNewFile.getText();
//        String regex = "[^\\.]+(?<=\\/)";
        String regex = "[^\\/]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pathToSaveAndFileName);

        while (matcher.find()){
            textField.setText("Saved successfully in file " +matcher.group());
        }



        try(PrintWriter writer = new PrintWriter(pathToSaveAndFileName)) {

            for (char c : textToWriteOnFile.toCharArray()) {
                writer.write(c);

            }

            writer.close();

        } catch (FileNotFoundException e) {
            textField.setText(e.getMessage());
        }



    }

    public void mostUsedWord(ActionEvent actionEvent) {

        String text = textField.getText();
        String[] input = text.split("[ \n,.]+");
        String mostFrequentWord = "";
        int howManyTimes = Integer.MIN_VALUE;

        if(text.length() <= 1){
            textField.setText("Too short.Enter more long text");
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

    public void openFile(ActionEvent actionEvent) {
        String input = pathToFile.getText();
        StringBuilder sb = new StringBuilder();

        if(input.length() == 0){
            textField.setText("Must enter path and new file fields. Enter full path and full name of file");
            textField.appendText("\n Example: C:/somedirectory/yourfile.txt");
        }else {


            try(FileInputStream fis = new FileInputStream(input);
            ) {

                int read = fis.read();
                while (read >= 0){
                    sb.append(((char)read));
                    read = fis.read();
                }

                fis.close();
                textField.setText(sb.toString());




            }  catch (IOException e) {
               textField.setText(e.getMessage());
            }


        }




    }
}
