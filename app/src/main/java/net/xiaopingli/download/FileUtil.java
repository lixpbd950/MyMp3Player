package net.xiaopingli.download;

import android.os.Environment;

import java.io.*;

/**
 * Created by henry on 4/18/2015.
 */
public class FileUtil {
    private String SDPATH;

    public String getPath(){
        return SDPATH;
    }

    public FileUtil(){
        SDPATH = Environment.getExternalStorageDirectory()+File.separator;
    }

    /**
     * create file on SD card
     * @param fileName
     * @return
     * @throws java.io.IOException
     */
    public File createSDFile(String dir,String fileName) throws IOException {
        File file = new File(SDPATH+dir+File.separator+fileName);
        file.createNewFile();
        return file;
    }

    /**
     * create dir on SD card
     * @param directoryName
     * @return
     */
    public File createDirectory(String directoryName){
        File dir = new File(SDPATH+directoryName);
        dir.mkdir();
        return dir;
    }

    /**
     * tell if a file exists on the SD card
     * @param fileName
     * @return
     */
    public boolean isFileExists(String fileName){
        File file = new File(SDPATH+fileName);
        return file.exists();
    }

    public File write2SDFromInputStream(String path, String fileName, InputStream inputStream){
        File file = null;
        OutputStream outputStream = null;

        try {
            createDirectory(path);
            file = createSDFile(path,fileName);
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            int tmp;
            while ((tmp = inputStream.read(buffer))!=-1){
                outputStream.write(buffer,0,tmp);
            }
            outputStream.flush();


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

}
