package objLoader;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

/**
 * Iterates other an obj file to extract the exact obj commands
 *
 * @author Alessandro Martinelli
 */
public class ObjFileIterator implements Iterator<String> {

    private BufferedReader reader;
    private String nextLine;

    public ObjFileIterator(Context context, String filename) {
        try {
            InputStream stream = context.getAssets().open(filename);
            reader = new BufferedReader(new InputStreamReader(stream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasNext() {
        try {
            String line = reader.readLine();

            if (line == null)
                return false;
            this.nextLine = "";
            while (line != null) {
                line = line.trim();
                if (line.endsWith("\\")) {
                    nextLine = nextLine + line.substring(0, line.length() - 1);
                    line = reader.readLine();
                } else {
                    nextLine = nextLine + line;
                    line = null;
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String next() {
        return nextLine;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
