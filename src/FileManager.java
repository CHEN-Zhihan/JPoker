import java.io.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by zhihan on 2/9/17.
 */
public abstract class FileManager {
    protected final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    protected final Lock read = readWriteLock.readLock();
    protected final Lock write = readWriteLock.writeLock();
    protected ObjectOutputStream output;
    protected File file;
    protected void writeAll() {
        try {
            File temp = new File("./" + file.getName() + ".temp");
            file.createNewFile();
            output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            subWrite();
            output.close();
            temp.renameTo(file);
        } catch (IOException e) {
            System.err.println("Error saving to file: " + file.getName() + " " + e);
        }
    }

    protected abstract void subWrite() throws IOException;
}
