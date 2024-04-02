import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class WebFrame extends JFrame {

    private static String FILENAME = "links.txt";

    private Thread launcher;
    private List<Thread> currThreads;
    private Semaphore sem;
    private Integer runningThreads;
    private Integer fetchedUrls;

    private TableModel model;
    private JTable table;
    private JPanel panel;

    private JButton singleThrBTN, concBTN, stopBTN;
    private JTextField textField;
    private JLabel runningJL, completedJL, elapsedJL;

    private JProgressBar progressBar;

    /**
     * updates the count of running threads. thread-safe.
     * @param change the change in total amount of running threads.
     * */
    public void updateRunningThreads(int change) {
        synchronized (runningThreads) {
            runningThreads += change;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    runningJL.setText("Running:" + runningThreads);
                }
            });
        }
    }

    /**
     * signals that process for the url at given index of table has been completed
     * */
    public void updateTable(int index, String status) {
        synchronized (fetchedUrls) {
            fetchedUrls++;
            model.setValueAt((Object) status, index, 1);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    completedJL.setText("Completed:" + fetchedUrls);
                    progressBar.setValue(fetchedUrls);
                }
            });
        }
    }

    private void reset() {
        runningThreads = 0;
        fetchedUrls = 0;
        completedJL.setText("Completed:0");
        runningJL.setText("Running:0");
        elapsedJL.setText("Elapsed:");

        for(int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt((Object) "", i, 1);
        }
    }

    private void launch(int numThreads) {
        launcher = new Thread(() -> {
            reset();
            progressBar.setMaximum(model.getRowCount());
            sem = new Semaphore(numThreads);
            long startTime = System.currentTimeMillis();
            updateRunningThreads(1);
            currThreads = new ArrayList<>();
            for(int i = 0; i < model.getRowCount(); i++) {
                synchronized (currThreads) {
                    if(launcher.isInterrupted()) {
                        break;
                    }
                    Thread thr = new Thread(new WebWorker((String) model.getValueAt(i, 0), i, this, sem));
                    currThreads.add(thr);
                    thr.start();
                }
            }
            try {
                for (Thread thr : currThreads) {
                    try {
                        thr.join();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }catch (ConcurrentModificationException ex){ex.printStackTrace();}
            updateRunningThreads(-1);

            singleThrBTN.setEnabled(true);
            concBTN.setEnabled(true);
            stopBTN.setEnabled(false);

            double elapsed = ((double) (System.currentTimeMillis() - startTime)) / 1000;
            elapsedJL.setText("Elapsed:" + elapsed);
        });
        launcher.start();
    }

    private void stopLaunch() {
        synchronized (currThreads) {
            launcher.interrupt();
            for (Thread thr : currThreads) {
                thr.interrupt();
            }
        }
    }

    private void addListeners() {
        this.singleThrBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                singleThrBTN.setEnabled(false);
                concBTN.setEnabled(false);
                stopBTN.setEnabled(true);
                launch(1);
            }
        });

        this.concBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                singleThrBTN.setEnabled(false);
                concBTN.setEnabled(false);
                stopBTN.setEnabled(true);
                try {
                    launch(Integer.parseInt(textField.getText()));
                } catch (NumberFormatException e) {
                    singleThrBTN.setEnabled(true);
                    concBTN.setEnabled(true);
                    stopBTN.setEnabled(false);
                }
            }
        });

        this.stopBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                singleThrBTN.setEnabled(true);
                concBTN.setEnabled(true);
                stopBTN.setEnabled(false);
                stopLaunch();
            }
        });
    }

    public WebFrame() {
        super("WebLoader");

        runningThreads = 0;
        fetchedUrls = 0;

        model = new DefaultTableModel(new String[]{"url", "status"}, 0);
        table = new JTable(model);
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                tableModel.addRow(new Object[]{line, ""});
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollpane = new JScrollPane(table);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        scrollpane.setPreferredSize(new Dimension(600, 300));
        panel.add(scrollpane);

        singleThrBTN = new JButton("Single Thread Fetch");
        concBTN = new JButton("Concurrent Fetch");
        stopBTN = new JButton("Stop");
        textField = new JTextField();
        textField.setMinimumSize(new Dimension(50, 10));
        textField.setMaximumSize(new Dimension(50, 10));

        runningJL = new JLabel("Running:");
        completedJL = new JLabel("Completed:");
        elapsedJL = new JLabel("Elapsed:");

        panel.add(singleThrBTN);
        panel.add(concBTN);

        panel.add(textField);

        panel.add(runningJL);
        panel.add(completedJL);
        panel.add(elapsedJL);

        progressBar = new JProgressBar();
        panel.add(progressBar);

        panel.add(stopBTN);
        stopBTN.setEnabled(false);

        add(panel);

        addListeners();

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        WebFrame frame = new WebFrame();
    }
}
