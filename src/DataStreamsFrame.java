import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.CREATE;

public class DataStreamsFrame extends JFrame
{

    JPanel mainPnl;
    JPanel textPnl;
    JPanel searchPnl;
    JPanel buttonPnl;

    JTextArea unfilteredTA;
    JScrollPane unfilteredSP;
    JTextArea filteredTA;
    JScrollPane filteredSP;

    JTextField searchTF;

    JButton loadBtn;
    JButton searchBtn;
    JButton quitBtn;

    Path file;


    public DataStreamsFrame()
    {

        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());
        setTitle("Word Filter");

        createTextPnl();
        mainPnl.add(textPnl,BorderLayout.NORTH);
        createSearchPnl();
        mainPnl.add(searchPnl,BorderLayout.CENTER);
        creatButtonPnl();
        mainPnl.add(buttonPnl,BorderLayout.SOUTH);
        add(mainPnl);

        setSize(400,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    private void createTextPnl()
    {

        textPnl = new JPanel();
        textPnl.setLayout(new GridLayout(1,2));

        unfilteredTA = new JTextArea();
        unfilteredTA.setEditable(false);
        unfilteredSP = new JScrollPane(unfilteredTA);
        unfilteredSP.setBorder(new TitledBorder(new EtchedBorder(), "Unfiltered text"));
        unfilteredSP.setPreferredSize(new Dimension(40,700));
        textPnl.add(unfilteredSP);

        filteredTA = new JTextArea();
        filteredTA.setEditable(false);
        filteredSP = new JScrollPane(filteredTA);
        filteredSP.setBorder(new TitledBorder(new EtchedBorder(), "Filtered text"));
        filteredSP.setPreferredSize(new Dimension(40,700));
        textPnl.add(filteredSP);

    }

    private void createSearchPnl()
    {

        searchPnl = new JPanel();
        searchPnl.setLayout(new BorderLayout());
        searchTF = new JTextField();
        searchTF.setFont(new Font("Roboto", Font.PLAIN, 24));
        searchPnl.setBorder(new TitledBorder(new EtchedBorder(), "Search keyword"));
        searchPnl.add(searchTF, BorderLayout.CENTER);


    }
    private void creatButtonPnl()
    {

        buttonPnl = new JPanel();
        buttonPnl.setLayout(new GridLayout(1,3));

        JFileChooser fileChoose = new JFileChooser();

        loadBtn = new JButton("Load");
        loadBtn.setFont(new Font("Roboto", Font.PLAIN, 24));
        loadBtn.addActionListener((ActionEvent ae) ->
        {
            try {
                File workingDirectory = new File(System.getProperty("user.dir"));

                fileChoose.setCurrentDirectory(workingDirectory);

                if (fileChoose.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File toImport = fileChoose.getSelectedFile();
                    file = toImport.toPath();
                    InputStream in = new BufferedInputStream(Files.newInputStream(file, CREATE));
                } else {
                    System.out.println("Failed to choose file. Try again.");
                    System.exit(0);
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found!!!");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }




        });
        buttonPnl.add(loadBtn);

        searchBtn = new JButton("Search");
        searchBtn.setFont(new Font("Roboto", Font.PLAIN, 24));
        searchBtn.addActionListener((ActionEvent ae) ->
        {
            try (Stream<String> lines = Files.lines(Paths.get(String.valueOf(file))))
            {
                unfilteredTA.append(lines.collect(Collectors.joining("\n")));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Invalid");
            }

            try (Stream<String> lines = Files.lines(Paths.get(String.valueOf(file))))
            {
                filteredTA.append(lines.filter(w -> w.contains(searchTF.getText())).collect(Collectors.joining("\n")));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Invalid");
            }
            unfilteredTA.append("\n");
            filteredTA.append("\n");
        });

        buttonPnl.add(searchBtn);

        quitBtn = new JButton("Quit");
        quitBtn.setFont(new Font("Roboto", Font.PLAIN, 24));
        quitBtn.addActionListener((ActionEvent ae) ->
        {int quit = JOptionPane.showConfirmDialog(null,"Are you sure you want to quit?","Quit Confirm", JOptionPane.YES_NO_OPTION);
            if(quit == JOptionPane.YES_OPTION)
            {
                System.exit(0);
            }
        });

        buttonPnl.add(quitBtn);
    }

}
