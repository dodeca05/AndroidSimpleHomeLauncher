package com.example.SliverCareWithLancher;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AppListViewAdapter extends BaseAdapter {

    private ArrayList<AppInfo>mItems=new ArrayList<>();
    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public AppInfo getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.app_list_layout, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        ImageView iv_img = (ImageView) convertView.findViewById(R.id.appicon) ;
        TextView tv_name = (TextView) convertView.findViewById(R.id.appname) ;


        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        AppInfo myItem = getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        iv_img.setImageDrawable(myItem.getIcon());
        tv_name.setText(myItem.getName());


        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */

        return convertView;
    }
    public void addItem(Drawable icon,String name,String pakageName,String classname)
    {
        AppInfo appInfo=new AppInfo();

        appInfo.setIcon(icon);
        appInfo.setName(name);
        appInfo.setClassname(classname);
        appInfo.setPakageName(pakageName);

        mItems.add(appInfo);

    }
}
