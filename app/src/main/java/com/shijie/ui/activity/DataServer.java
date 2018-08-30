package com.shijie.ui.activity;

import com.shijie.entity.ResponestBean.MySection;
import com.shijie.entity.tableBean.VideoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haoge on 2018/8/23.
 */

public class DataServer {
    public static List<MySection> getSampleData(){
        List<MySection> list = new ArrayList<>();
        list.add(new MySection(true, "Section 1", true));
        list.add(new MySection(new VideoBean("标题1","内容1",1)));
        list.add(new MySection(new VideoBean("标题2","内容2",2)));
        list.add(new MySection(new VideoBean("标题3","内容3",3)));
        list.add(new MySection(new VideoBean("标题4","内容4",4)));
        list.add(new MySection(true, "Section 2", false));
        list.add(new MySection(new VideoBean("标题1","内容1",1)));
        list.add(new MySection(new VideoBean("标题2","内容2",2)));
        list.add(new MySection(new VideoBean("标题3","内容3",3)));
        list.add(new MySection(new VideoBean("标题4","内容4",4)));
        return list;
    }
}
