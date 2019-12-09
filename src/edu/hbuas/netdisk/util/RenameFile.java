package edu.hbuas.netdisk.util;

import java.io.File;

public class RenameFile {
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        File dir=new File("/Users/tengsir/workspace/java/idea/NetDisk/resources/imgs/headimages");

        File[] files=dir.listFiles();
        int n=1;
        for(File f:files) {
            if(f.isFile()) {
                System.out.println(f.getName());
                String type=f.getName().substring(f.getName().lastIndexOf("."),f.getName().length());


                f.renameTo(new File(dir.getAbsolutePath(),"/Head"+n+type));
                n++;
            }
        }
    }
}
