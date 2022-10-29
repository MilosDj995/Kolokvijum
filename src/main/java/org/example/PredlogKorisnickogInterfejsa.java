package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PredlogKorisnickogInterfejsa extends JDialog {
    private JPanel contentPane;
    private JButton buttonClose;
    private JButton buttonSave;
    private JButton buttonOpenTop;
    private JButton topToNewButton;
    private JTextArea textAreaTop;
    private JTextArea textAreaBottom;
    private JTextArea textAreaNew;
    private JButton bottomToNewButton;
    private JButton buttonOpenBottom;

    private String directory;

    public PredlogKorisnickogInterfejsa() {
        setContentPane(contentPane);
        setModal(true);
        //    getRootPane().setDefaultButton(buttonOpen);

        buttonOpenTop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onButtonOpen(textAreaTop);
            }
        });

        buttonOpenBottom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onButtonOpen(textAreaBottom);
            }
        });

        topToNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyToNew(textAreaTop);
            }
        });
        bottomToNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyToNew(textAreaBottom);
            }
        });
        buttonSave.addActionListener(new OnSaveButtonAction());

        buttonClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onButtonClose();
            }
        });

        // call onCancel() when cross is clicked
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onButtonClose();
            }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private class OnSaveButtonAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            onButtonSave();
        }
    }

    private void copyToNew(JTextArea area) {
        String selectedText = area.getSelectedText();
        if(selectedText != null) {
            String currentText = textAreaNew.getText();
            textAreaNew.setText(currentText + selectedText);
        }
    }

    private void onButtonClose() {
        // add your code here if necessary
        dispose();
    }

    private void onButtonOpen(JTextArea textArea) {
        // Create a file dialog box to prompt for a new file to display
        FileDialog f = new FileDialog(this, "Otvori fajl", FileDialog.LOAD);
        f.setDirectory(directory); // Set the default directory
        // Display the dialog and wait for the user's response
        f.setVisible(true);
        directory = f.getDirectory(); // Remember new default directory
        loadAndDisplayFile(directory, f.getFile(), textArea); // Load and display selection
        f.dispose(); // Get rid of the dialog box
    }

    public void loadAndDisplayFile(String directory, String filename, JTextArea textArea) {
        if ((filename == null) || (filename.length() == 0)) {
            return;
        }
        File file;
        FileReader in = null;
        // Read and display the file contents. Since we're reading text, we
        // use a FileReader instead of a FileInputStream.
        try {
            file = new File(directory, filename); // Create a file object
            in = new FileReader(file); // And a char stream to read it
            char[] buffer = new char[4096]; // Read 4K characters at a time
            int len; // How many chars read each time
            textArea.setText(""); // Clear the text area
            while ((len = in.read(buffer)) != -1) { // Read a batch of chars
                String s = new String(buffer, 0, len); // Convert to a string
                textArea.append(s); // And display them
            }
            this.setTitle("FileViewer: " + filename); // Set the window title
            textArea.setCaretPosition(0); // Go to start of file
        }
        // Display messages if something goes wrong
        catch (IOException e) {
            textAreaTop.setText(e.getClass().getName() + ": " + e.getMessage());
            this.setTitle("FileViewer: " + filename + ": I/O Exception");
        }
        // Always be sure to close the input stream!
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public static void main(String[] args) {
        PredlogKorisnickogInterfejsa dialog = new PredlogKorisnickogInterfejsa();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void onButtonSave() {
        // Create a file dialog box to prompt for a new file to display
        FileDialog f = new FileDialog(this, "Otvori fajl", FileDialog.SAVE);
        f.setDirectory(directory); // Set the default directory
        // Display the dialog and wait for the user's response
        f.setVisible(true);
        directory = f.getDirectory(); // Remember new default directory
        saveFile(directory, f.getFile()); // Load and display selection
        f.dispose(); // Get rid of the dialog box
    }

    public void saveFile(String directory, String filename) {
        if ((filename == null) || (filename.length() == 0)) {
            return;
        }
        File file;
        FileWriter out = null;
        try {
            file = new File(directory, filename); // Create a file object
            out = new FileWriter(file); // And a char stream to write it
            textAreaNew.getLineCount(); // Get text from the text area
            String s = textAreaNew.getText();
            out.write(s);
        }
        // Display messages if something goes wrong
        catch (IOException e) {
            textAreaNew.setText(e.getClass().getName() + ": " + e.getMessage());
            this.setTitle("FileViewer: " + filename + ": I/O Exception");
        }
        // Always be sure to close the input stream!
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
            }
        }
    }

}