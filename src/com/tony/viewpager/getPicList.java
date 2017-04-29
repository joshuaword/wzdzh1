package com.tony.viewpager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;

public class getPicList 
{
	private Button getbtn;
	private ImageView image;
	private List<String> urllist;
	private List<String> fiellist;
	private Bitmap bitmap;
	private String picURL = "";
	private final static String ALBUM_PATH = Environment
			.getExternalStorageDirectory() + "/mypic_data/";


        public void CreatePic(List<String> url)
        {
        	urllist=new ArrayList<String>();
        	urllist.addAll(url);
        	fiellist=getFileNameList(url);
	
		// TODO Auto-generated method stub
		new Thread(new Runnable() 
		{
            
			public void run() 
            {
				// TODO Auto-generated method stub
			
				try {
					for(int i=0;i<urllist.size();i++ )
					{
					URL url = new URL(urllist.get(i).toString());
					InputStream is = url.openStream();
					bitmap = BitmapFactory.decodeStream(is);
					String filename=fiellist.get(i).toString();
					saveFile(bitmap, filename);
					is.close();
					}
					
				} catch (MalformedURLException e) 
				{
					// TODO Auto-generated catch block
					
					e.printStackTrace();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
			
					e.printStackTrace();
				}
			}
		}).start();
		
      }

	// 指定保存的路径：
	private void saveFile(Bitmap bm, String fileName) throws IOException {
		File dirFile = new File(ALBUM_PATH);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		File myCaptureFile = new File(ALBUM_PATH + fileName);
		if(!myCaptureFile.exists())
		{ 
			
			myCaptureFile.createNewFile();
		}
	
			
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
	}
	private List<String> getFileNameList(List<String> url)
	{
		List<String> file=new ArrayList<String>();
		for(int i=0;i<url.size();i++)
		{
			String temp=url.get(i).toString();
			temp=temp.replace("/", "a");
			temp=temp.replace(":", "b");
			file.add(temp);
		}
		return file;
		
	}
	public List<String> getFilePathList(List<String> url)
	{
		List<String> file=new ArrayList<String>();
		for(int i=0;i<url.size();i++)
		{
			String temp=url.get(i).toString();
			temp=temp.replace("/", "a");
			temp=temp.replace(":", "b");
			temp=ALBUM_PATH+temp;
			file.add(temp);
		}
		return file;		
	}


}
