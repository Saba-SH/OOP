import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Semaphore;
import javax.swing.*;

public class WebWorker implements Runnable {

    private String urlString;
    private int rowNumber;
    private WebFrame frame;
    private Semaphore sem;

 	private String download() {
        InputStream input = null;
        StringBuilder contents = null;
        long beginTime = System.currentTimeMillis();
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            // Set connect() to throw an IOException
            // if connection does not succeed in this many msecs.
            connection.setConnectTimeout(5000);

            connection.connect();
            input = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            char[] array = new char[1000];
            int len;
            contents = new StringBuilder(1000);
            while ((len = reader.read(array, 0, array.length)) > 0) {
                if(Thread.currentThread().isInterrupted()) {
                    return "interrupted";
                }
                contents.append(array, 0, len);
                Thread.sleep(100);
            }

            // Successful download if we get here
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            return sdf.format(new Date()) + " " + (System.currentTimeMillis() - beginTime) + "ms " + contents.toString().length() + " bytes";

        }
        // Otherwise control jumps to a catch...
        catch (MalformedURLException ignored) {
            return "err";
        } catch (InterruptedException exception) {
            return "err";
            // YOUR CODE HERE
            // deal with interruption
        } catch (IOException exception) {
            return "err";
        }
        // "finally" clause, to close the input stream
        // in any case
        finally {
            try {
                if (input != null) input.close();
            } catch (IOException ignored) {
            }
        }

//        return "";
    }

    @Override
    public void run() {
        try {
            sem.acquire();
        } catch (InterruptedException e) {
            return;
        }

        frame.updateRunningThreads(1);
        frame.updateTable(this.rowNumber, download());
        frame.updateRunningThreads(-1);
        sem.release();
    }


	public WebWorker(String urlString, int rowNumber, WebFrame frame, Semaphore sem) {
        this.urlString = urlString;
        this.rowNumber = rowNumber;
        this.frame = frame;
        this.sem = sem;
    }
}
