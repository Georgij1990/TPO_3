package zad1.client;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI {

    public ClientGUI() {

    }
    public void showGUI() {
        JFrame frame = new JFrame("TPO Zadanie 3");
        frame.setSize(600, 400);

        JPanel panel = new JPanel(new GridBagLayout());
        frame.add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);


        JLabel wordToTranslateLabel = new JLabel("Enter a word to translate:");
        JLabel languageCodeLabel = new JLabel("Language code:");
        JLabel translationLabel = new JLabel("Translation");

        JTextField wordToTranslateField = new JTextField();
        wordToTranslateField.setPreferredSize(new Dimension(200, 25));
        JTextField languageCodeField = new JTextField();
        languageCodeField.setPreferredSize(new Dimension(200, 25));
        JTextField translationField = new JTextField();
        translationField.setPreferredSize(new Dimension(200, 25));
        translationField.setEnabled(false);

        translationField.setForeground(Color.BLACK);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        JButton translateButton = new JButton("Translate");
        JButton clearButton = new JButton("Clear");
        JButton closeButton = new JButton("Close");

        translateButton.addActionListener(translateWord(wordToTranslateField, languageCodeField, translationField));
        clearButton.addActionListener(clearFrame(frame, wordToTranslateField, languageCodeField, translationField));
        closeButton.addActionListener(closeFrame(frame));

        buttonsPanel.add(translateButton);
        buttonsPanel.add(clearButton);
        buttonsPanel.add(closeButton);

        panel.add(wordToTranslateLabel, gbc);
        gbc.gridx++;
        panel.add(wordToTranslateField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(languageCodeLabel, gbc);
        gbc.gridx++;
        panel.add(languageCodeField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(translationLabel, gbc);
        gbc.gridx++;
        panel.add(translationField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(buttonsPanel, gbc);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private ActionListener clearFrame(JFrame frame, JTextField wordToTranslateField, JTextField languageCodeField, JTextField translationField) {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wordToTranslateField.setText("");
                languageCodeField.setText("");
                translationField.setText("");
            }
        };
        return al;
    }

    public ActionListener translateWord(JTextField wordToTranslateField, JTextField languageCodeField, JTextField translationField) {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String wordToTranslateFieldValue = wordToTranslateField.getText();
                String languageCodeFieldValue = languageCodeField.getText();
                Client client = new Client(wordToTranslateFieldValue, languageCodeFieldValue);
                String response = client.sendRequest();
                translationField.setText(response);
            }
        };
        return al;
    }
    public ActionListener closeFrame(JFrame jFrame) {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.dispose();
            }
        };
        return al;
    }
}
