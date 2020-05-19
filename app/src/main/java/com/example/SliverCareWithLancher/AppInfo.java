package com.example.SliverCareWithLancher;

import android.graphics.drawable.Drawable;

public class AppInfo {
    private Drawable icon;
    private String name;
    private String classname;
    private String pakageName;

    public Drawable getIcon(){return icon;}
    public void setIcon(Drawable icon){this.icon=icon;}

    public String getName(){return name;}
    public void setName(String name){this.name=name;}

    public String getClassname(){return classname;}
    public void setClassname(String classname){this.classname=classname;}

    public String getPakageName(){return pakageName;}
    public void setPakageName(String pakageName){this.pakageName=pakageName;}
}
