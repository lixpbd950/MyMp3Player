package net.xiaopingli.download;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by henry on 4/17/2015.
 */
public class HttpDownloader {

    private URL url = null;

    public String downloadText(String urlStr){
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader br = null;

        try {
            InputStream inputStream = getInputStringFromUrl(urlStr);
            br = new BufferedReader(new InputStreamReader(inputStream));
            while((line = br.readLine())!=null){
                sb.append(line);
            }

        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        } /*finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        return sb.toString();

    }

    /**
     * return -1 means download failure;
     * return 0 means download success;
     * return 1 means file already exists;
     * @param path
     * @param fileName
     * @param urlStr
     * @return
     */
    public int downloadFile(String path, String fileName, String urlStr){
        InputStream inputStream = null;
        //StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        String line = null;
        try {
            FileUtil util = new FileUtil();
            if(util.isFileExists(path+fileName)){
                return 1;
            }else {
                inputStream = getInputStringFromUrl(urlStr);
                /*br = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = br.readLine())!=null){
                    sb.append(line);
                }
                System.out.println(sb.toString());*/
                File resultFile = util.write2SDFromInputStream(path,fileName,inputStream);
                inputStream.close();
                if (resultFile==null){
                    return -1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                inputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return -1;
        }
        return 0;

    }


    public InputStream getInputStringFromUrl(String urlStr) throws IOException {
        url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream inputStream = conn.getInputStream();
        return inputStream;
    }
}
