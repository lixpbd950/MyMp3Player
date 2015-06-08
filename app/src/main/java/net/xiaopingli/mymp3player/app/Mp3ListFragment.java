package net.xiaopingli.mymp3player.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.view.*;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import net.xiaopingli.download.HttpDownloader;
import net.xiaopingli.model.Mp3Info;
import net.xiaopingli.service.DownloadService;
import net.xiaopingli.xml.Mp3ListContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class Mp3ListFragment extends ListFragment {
    private static final int UPDATE = 1;
    private static final int ABOUT = 2;
    private List<Mp3Info> mp3Infos = null;

    ArrayList<HashMap<String,String>> list = null;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Mp3ListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_mp3_list,container,false);
        return contentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        updateView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mp3_list, menu);
        menu.add(0,UPDATE,1,R.string.mp3list_update);
        menu.add(0,ABOUT,2,R.string.mp3list_about);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id==UPDATE){
            updateView();

        }else if(id==ABOUT){
            //about
        }
        return super.onOptionsItemSelected(item);
    }
    public void updateView(){
        DownloadXMLThread downloadXMLThread = new DownloadXMLThread();
        downloadXMLThread.start();
    }
    class DownloadXMLThread extends Thread{
        @Override
        public void run() {
            Looper.prepare();
            String xml = downloadXML("http://xiaopingli.net/mp3/resources.xml");
            Message m = mHandler.obtainMessage();
            m.obj = xml;
            m.sendToTarget();
            Looper.loop();
        }
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String xml = (String) msg.obj;
            mp3Infos = parse(xml);
            SimpleAdapter simpleAdapter = buildSimpleAdapter(mp3Infos);
            setListAdapter(simpleAdapter);
        }
    };
    public String downloadXML(String urlString){
        HttpDownloader httpDownloader = new HttpDownloader();
        String result = httpDownloader.downloadText(urlString);
        return result;
    }

    public List<Mp3Info> parse(String xmlStr){
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        List<Mp3Info> infos = new ArrayList<Mp3Info>();
        try {
            XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
            Mp3ListContentHandler mp3ListContentHandler = new Mp3ListContentHandler(infos);
            xmlReader.setContentHandler(mp3ListContentHandler);
            xmlReader.parse(new InputSource(new StringReader(xmlStr)));
            for (Iterator iterator = infos.iterator();iterator.hasNext();){
                Mp3Info mp3Info = (Mp3Info) iterator.next();
                System.out.println(mp3Info);
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return infos;
    }

    public SimpleAdapter buildSimpleAdapter(List<Mp3Info> mp3Infos){
        List<Map<String,String>> list = new ArrayList<Map<String, String>>();
        for (Iterator iterator = mp3Infos.iterator();iterator.hasNext();) {
            Mp3Info mp3Info = (Mp3Info) iterator.next();
            Map<String,String> map = new HashMap<String, String>();
            map.put("mp3_name",mp3Info.getMp3Name());
            map.put("mp3_size",mp3Info.getMp3Size());
            list.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),list,R.layout.mp3_info_item,
                new String[]{"mp3_name","mp3_size"},new int[]{R.id.mp3_name,R.id.mp3_size});
        return simpleAdapter;
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Mp3Info mp3Info = mp3Infos.get(position);
        Intent intent = new Intent();
        intent.putExtra("mp3Info",mp3Info);
        intent.setClass(getActivity(), DownloadService.class);
        //System.out.println("prepare to start service");
        getActivity().startService(intent);
        String mp3Name = mp3Info.getMp3Name();
        Toast.makeText(getActivity(), mp3Name, Toast.LENGTH_SHORT).show();
    }

}
