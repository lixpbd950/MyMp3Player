package net.xiaopingli.mymp3player.app;



import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import net.xiaopingli.download.FileUtil;
import net.xiaopingli.model.Mp3Info;

import java.util.*;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class LocalMp3ListFragment extends ListFragment {


    public LocalMp3ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_local_mp3_list, container, false);
        System.out.println("------------>LocalMp3ListFragment--->onCreateView");
        return contentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("------------>LocalMp3ListFragment--->onCreate");
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        FileUtil util = new FileUtil();
        List<Mp3Info> mp3Infos = util.getMp3Files("mp3/");
        SimpleAdapter adapter = buildSimpleAdapter(mp3Infos);
        setListAdapter(adapter);
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
}
