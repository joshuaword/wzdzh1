package com.dajia.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import com.baidu.navisdk.util.cache.ImageCache;
import com.baidu.navisdk.util.common.LogUtil;
import com.dajia.constant.Constant;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

/**
 * 描述：文件操作类.
 */
@SuppressLint("NewApi")
public class FileUtil {
    
    /** The tag. */
    private static String TAG = "FileUtil";
    
    /** The Constant D. */
    //private static final boolean D = AppData.DEBUG;
    
    /** 默认下载文件地址. */
    private static String downPathRootDir = File.separator  + File.separator;
    
    /** 默认下载图片文件地址. */
    private static String downPathImageDir = downPathRootDir + File.separator;
    
    /** 默认下载文件地址. */
    private static String downPathFileDir = downPathRootDir  + File.separator;
    
    /**MB  单位B*/
    private static int MB = 1024 * 1024;
    
    /** 设置好的图片存储目录*/
    private static String imageDownFullDir = null;
    
    /** 设置好的文件存储目录*/
    private static String fileDownFullDir = null;
    
    /**剩余空间大于200M才使用缓存*/
    private static int freeSdSpaceNeededToCache = 200 * MB;
    
    static {
        initImageDownFullDir();
        initFileDownFullDir();
    }
    
    /**
     * 下载网络文件到SD卡中.如果SD中存在同名文件将不再下载
     * @param url 要下载文件的网络地址
     * @return 下载好的本地文件地址
     */
    public static String downFileToSD(String url, String fullPath) {
        InputStream in = null;
        FileOutputStream fileOutputStream = null;
        HttpURLConnection con = null;
        String downFilePath = null;
        File file = null;
        try {
            if (!isCanUseSD()) {
                return null;
            }
            
            file = new File(fullPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            else {
                return file.getPath();
            }
            downFilePath = file.getPath();
            URL mUrl = new URL(url);
            con = (HttpURLConnection)mUrl.openConnection();
            con.connect();
            in = con.getInputStream();
            fileOutputStream = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int temp = 0;
            while ((temp = in.read(b)) != -1) {
                fileOutputStream.write(b, 0, temp);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "文件下载出错了");
            return null;
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (con != null) {
                    con.disconnect();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            
            try {
                //检查文件大小,如果文件为0B说明网络不好没有下载成功，要将建立的空文件删除
                if (file.length() == 0) {
                    Log.e(TAG, "下载出错了，文件大小为0");
                    file.delete();
                }
                
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return downFilePath;
    }
    
    /**
     * 描述：通过文件的网络地址从SD卡中读取图片，如果SD中没有则自动下载并保存.
     * @param url 文件的网络地址
     * @param type 图片的处理类型（剪切或者缩放到指定大小，参考Constant类）
     * 如果设置为原图，则后边参数无效，得到原图
     * @param width 新图片的宽
     * @param height 新图片的高
     * @return Bitmap 新图片
     */
   
    

    /**
     * 描述：从sd卡中的文件读取到byte[].
     *
     * @param path sd卡中文件路径
     * @return byte[]
     */
    public static byte[] getByteArrayFromSD(String path) {
        byte[] bytes = null;
        ByteArrayOutputStream out = null;
        try {
            File file = new File(path);
            //SD卡是否存在
            if (!isCanUseSD()) {
                return null;
            }
            //文件是否存在
            if (!file.exists()) {
                return null;
            }
            
            long fileSize = file.length();
            if (fileSize > Integer.MAX_VALUE) {
                return null;
            }
            
            FileInputStream in = new FileInputStream(path);
            out = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];
            int size = 0;
            while ((size = in.read(buffer)) != -1) {
                out.write(buffer, 0, size);
            }
            in.close();
            bytes = out.toByteArray();
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (out != null) {
                try {
                    out.close();
                }
                catch (Exception e) {
                }
            }
        }
        return bytes;
    }
    
    /**
     * 描述：将byte数组写入文件.
     *
     * @param path the path
     * @param content the content
     * @param create the create
     */
    public static void writeByteArrayToSD(String path, byte[] content, boolean create) {
        
        FileOutputStream fos = null;
        try {
            File file = new File(path);
            //SD卡是否存在
            if (!isCanUseSD()) {
                return;
            }
            //文件是否存在
            if (!file.exists()) {
                if (create) {
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                        file.createNewFile();
                    }
                }
                else {
                    return;
                }
            }
            fos = new FileOutputStream(path);
            fos.write(content);
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (fos != null) {
                try {
                    fos.close();
                }
                catch (Exception e) {
                }
            }
        }
    }
    
    /**
     * 描述：SD卡是否能用.
     *
     * @return true 可用,false不可用
     */
    public static boolean isCanUseSD() {
        try {
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 描述：获得当前下载的地址.
     * @return 下载的地址（默认SD卡download）
     */
    public static String getDownPathImageDir() {
        return downPathImageDir;
    }
    
    /**
     * 描述：设置图片文件的下载保存路径（默认SD卡download/cache_images）.
     * @param downPathImageDir 图片文件的下载保存路径
     */
    public static void setDownPathImageDir(String downPathImageDir) {
        FileUtil.downPathImageDir = downPathImageDir;
        initImageDownFullDir();
    }
    
    /**
     * Gets the down path file dir.
     *
     * @return the down path file dir
     */
    public static String getDownPathFileDir() {
        return downPathFileDir;
    }
    
    /**
     * 描述：设置文件的下载保存路径（默认SD卡download/cache_files）.
     * @param downPathFileDir 文件的下载保存路径
     */
    public static void setDownPathFileDir(String downPathFileDir) {
        FileUtil.downPathFileDir = downPathFileDir;
        initFileDownFullDir();
    }
    
    /**
     * 描述：获取默认的图片保存全路径.
     *
     * @return 完成的存储目录
     */
    private static void initImageDownFullDir() {
        String pathDir = null;
        try {
            if (!isCanUseSD()) {
                return;
            }
            //初始化图片保存路径
            File fileRoot = Environment.getExternalStorageDirectory();
            File dirFile = new File(fileRoot.getAbsolutePath() + downPathImageDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            pathDir = dirFile.getPath();
            imageDownFullDir = pathDir;
        }
        catch (Exception e) {
        }
    }
    
    /**
     * 描述：获取默认的文件保存全路径.
     *
     * @return 完成的存储目录
     */
    private static void initFileDownFullDir() {
        String pathDir = null;
        try {
            if (!isCanUseSD()) {
                return;
            }
            //初始化图片保存路径
            File fileRoot = Environment.getExternalStorageDirectory();
            File dirFile = new File(fileRoot.getAbsolutePath() + downPathFileDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            pathDir = dirFile.getPath();
            fileDownFullDir = pathDir;
        }
        catch (Exception e) {
        }
    }
  
    

    
    /**
     * 计算sdcard上的剩余空间
     */
    public static int freeSpaceOnSD() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = ((double)stat.getAvailableBlocks() * (double)stat.getBlockSize()) / MB;
        return (int)sdFreeMB;
    }
    
    /**
     * 根据文件的最后修改时间进行排序
     */
    public static class FileLastModifSort implements Comparator<File> {
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            }
            else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            }
            else {
                return -1;
            }
        }
    }
    
    /**
     * 
     * 描述：剩余空间大于多少B才使用缓存
     * @return
     * @throws 
     */
    public static int getFreeSdSpaceNeededToCache() {
        return freeSdSpaceNeededToCache;
    }
    
    /**
     * 
     * 描述：剩余空间大于多少B才使用缓存
     * @param freeSdSpaceNeededToCache
     * @throws 
     */
    public static void setFreeSdSpaceNeededToCache(int freeSdSpaceNeededToCache) {
        FileUtil.freeSdSpaceNeededToCache = freeSdSpaceNeededToCache;
    }
    
    /**
     * 删除所有缓存文件
    */
    public static boolean removeAllFileCache() {
        
        try {
            if (!isCanUseSD()) {
                return false;
            }
            
            File path = Environment.getExternalStorageDirectory();
            File fileDirectory = new File(path.getAbsolutePath() + downPathImageDir);
            File[] files = fileDirectory.listFiles();
            if (files == null) {
                return true;
            }
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    /**
     * 
     * 描述：读取Assets目录的文件内容
     * @param context
     * @param name
     * @return
     * @throws 
     */
    public static String readAssetsByName(Context context, String name, String encoding) {
        String text = null;
        InputStreamReader inputReader = null;
        BufferedReader bufReader = null;
        try {
            inputReader = new InputStreamReader(context.getAssets().open(name));
            bufReader = new BufferedReader(inputReader);
            String line = null;
            StringBuffer buffer = new StringBuffer();
            while ((line = bufReader.readLine()) != null) {
                buffer.append(line);
            }
            text = new String(buffer.toString().getBytes(), encoding);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (bufReader != null) {
                    bufReader.close();
                }
                if (inputReader != null) {
                    inputReader.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return text;
    }
    
    /**
     * 
     * 描述：读取Raw目录的文件内容
     * @param context
     * @param id
     * @return
     * @throws 
     */
    public static String readRawByName(Context context, int id, String encoding) {
        String text = null;
        InputStreamReader inputReader = null;
        BufferedReader bufReader = null;
        try {
            inputReader = new InputStreamReader(context.getResources().openRawResource(id));
            bufReader = new BufferedReader(inputReader);
            String line = null;
            StringBuffer buffer = new StringBuffer();
            while ((line = bufReader.readLine()) != null) {
                buffer.append(line);
            }
            text = new String(buffer.toString().getBytes(), encoding);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (bufReader != null) {
                    bufReader.close();
                }
                if (inputReader != null) {
                    inputReader.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return text;
    }
    
    public static String getImageDownFullDir() {
        return imageDownFullDir;
    }
    
    public static void setImageDownFullDir(String imageDownFullDir) {
        FileUtil.imageDownFullDir = imageDownFullDir;
    }
    
    public static String getFileDownFullDir() {
        return fileDownFullDir;
    }
    
    public static void setFileDownFullDir(String fileDownFullDir) {
        FileUtil.fileDownFullDir = fileDownFullDir;
    }
    
    public static boolean isSDCARDMounted() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }
    
    public static boolean isExistFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return false;
        }
        return true;
    }
    
    /**
     * 将文件流转成String
     * 
     * @param is
     * @return
     */
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                is.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    

    /***
     * ��URL�л�ȡ�ļ���
     * 
     * @param s
     * @return
     */
    public static String getFileNameFromUrl(String s) {
        int i = s.lastIndexOf("\\");
        if (i < 0 || i >= s.length() - 1) {
            i = s.lastIndexOf("/");
            if (i < 0 || i >= s.length() - 1)
                return s;
        }
        return s.substring(i + 1);
    }
    
    /***
     * ��ȡ�ļ�MIME����
     * 
     * @param f
     * @return
     */
    public static String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg")
            || end.equals("wav")) {
            type = "audio";
        }
        else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video";
        }
        else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            type = "image";
        }
        else if (end.equals("apk")) {
            type = "application/vnd.android.package-archive";
        }
        else if (end.equals("doc") || end.equals("docx")) {
            type = "application/msword";
        }
        else if (end.equals("xls") || end.equals("xlsx")) {
            type = "application/vnd.ms-excel";
        }
        else if (end.equals("ppt") || end.equals("pptx")) {
            type = "application/vnd.ms-powerpoint";
        }
        else if (end.equals("pdf")) {
            type = "application/pdf";
        }
        else {
            type = "*";
        }
        if (type.indexOf("/") == -1) {
            type += "/*";
        }
        return type;
    }
    
    /**
     * ��ȡȨ��
     * 
     * @param permission
     *            Ȩ��
     * @param path
     *            ·��
     */
    public static void chmod(String permission, String path) {
        try {
            String command = "chmod " + permission + " " + path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /***
     * ɾ���ļ�
     * 
     * @param filename
     */
    public static void deleteFile(String filename) {
        File myFile = new File(filename);
        if (myFile.exists()) {
            myFile.delete();
        }
    }
    
    /***
     * ��ȡ�ļ���չ��
     * 
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }
    
    /***
     * ��ȡ������չ����ļ���
     * 
     * @param filename
     * @return
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }
    
    /***
     * 获取程序的缓存
     * 
     * @param filesize
     * @return
     */
    public static String convertFileSize(long filesize) {
        String strUnit = "B";
        String strAfterComma = "";
        int intDivisor = 1;
        if (filesize >= 1024 * 1024 * 1024) {
            strUnit = "GB";
            intDivisor = 1024 * 1024 * 1024;
        }
        else if (filesize >= 1024 * 1024) {
            strUnit = "MB";
            intDivisor = 1024 * 1024;
        }
        else if (filesize >= 1024) {
            strUnit = "KB";
            intDivisor = 1024;
        }
        if (intDivisor == 1)
            return filesize + " " + strUnit;
        strAfterComma = "" + 100 * (filesize % intDivisor) / intDivisor;
        if (strAfterComma == "")
            strAfterComma = ".0";
        return filesize / intDivisor + "." + strAfterComma + " " + strUnit;
    }
    
    public static double getDirSize(File file) {
        //�ж��ļ��Ƿ����
        if (file.exists()) {
            //�����Ŀ¼��ݹ���������ݵ��ܴ�С    
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            }
            else {//������ļ���ֱ�ӷ������С,�ԡ��ס�Ϊ��λ   
                double size = (double)file.length();
                return size;
            }
        }
        else {
            return 0.0;
        }
    }
    
    public static long getSDCardSize(String path) {
        File pathFile = new File(path); // ȡ��sdcard�ļ�·��
        android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());
        long nTotalBlocks = statfs.getBlockCount(); // ��ȡSDCard��BLOCK����
        long nBlocSize = statfs.getBlockSize(); // ��ȡSDCard��ÿ��block��SIZE
        long nAvailaBlock = statfs.getAvailableBlocks(); // ��ȡ�ɹ�����ʹ�õ�Block������
        long nFreeBlock = statfs.getFreeBlocks(); // ��ȡʣ�µ�����Block������(����Ԥ����һ������޷�ʹ�õĿ�)
        long nSDTotalSize = nTotalBlocks * nBlocSize / 1024 / 1024; // ����SDCard
                                                                    // ��������СMB
        return nSDTotalSize;
    }
    
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //ɾ����������������
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //ɾ����ļ���
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            }
            else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//��ɾ���ļ���������ļ�
                delFolder(path + "/" + tempList[i]);//��ɾ����ļ���
                flag = true;
            }
        }
        return flag;
    }
    
    public static String getFileFromUri(final Context context, final Uri uri, String tempfile) {
        
        final boolean isKitKat = Build.VERSION.SDK_INT >= 19;//Build.VERSION_CODES.KITKAT;
        if (isKitKat) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                FileOutputStream out = new FileOutputStream(tempfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                tempfile = null;
            }
            return tempfile;
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    
    /**
     * 把一个文件的内容复制另一个文件
     * @param orginalFile
     * @param newFile
     */
	public static void copyFile(File orginalFile, File newFile) {
		InputStream fis = null;
		OutputStream fos = null;
		try {
			fis = new FileInputStream(orginalFile);
			fos = new FileOutputStream(newFile);
			byte[] buf = new byte[2028];
			int i;
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf, 0, i);

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	
	/**
	 * 初始化存储图片的文件夹
	 */
	public static void initPictureDir() {
		File dir = new File(FileUtil.getImageDownFullDir()+File.separator);
		if (!dir.exists()) {
			dir.mkdirs();
		} else {
			FileUtil.clearDirFiles(dir);
		}
	}
	
	/**
	 * 删除文件夹子文件
	 * @param dir
	 */
	public static void clearDirFiles(File dir){
		File[] files = dir.listFiles();
		for(File f : files){
			f.delete();
		}
	}
	
	/**
	 * 删除剪裁后的图片
	 */
	public static void clearCropedImages(){
		String SDCARD_PAHT = Environment.getExternalStorageDirectory()
				.getPath() + "/ass";
		File dirFile = new File(SDCARD_PAHT);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		String[] paths = dirFile.list();
		for (String s : paths) {
			new File(SDCARD_PAHT+File.separator+s).delete();
		}
	}
	/**
	 * 从相册得到的url转换为SD卡中图片路径
	 */
	public static String getPath(final Context context, final Uri uri) {  
		  
	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;  
	  
	    // DocumentProvider  
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {  
	        // ExternalStorageProvider  
	        if (isExternalStorageDocument(uri)) {  
	            final String docId = DocumentsContract.getDocumentId(uri);  
	            final String[] split = docId.split(":");  
	            final String type = split[0];  
	            if ("primary".equalsIgnoreCase(type)) {  
	                return Environment.getExternalStorageDirectory() + "/" + split[1];  
	            }  
	  
	            // TODO handle non-primary volumes  
	        }  
	        // DownloadsProvider  
	        else if (isDownloadsDocument(uri)) {  
	  
	            final String id = DocumentsContract.getDocumentId(uri);  
	            final Uri contentUri = ContentUris.withAppendedId(  
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));  
	  
	            return getDataColumn(context, contentUri, null, null);  
	        }  
	        // MediaProvider  
	        else if (isMediaDocument(uri)) {  
	            final String docId = DocumentsContract.getDocumentId(uri);  
	            final String[] split = docId.split(":");  
	            final String type = split[0];  
	  
	            Uri contentUri = null;  
	            if ("image".equals(type)) {  
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
	            } else if ("video".equals(type)) {  
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;  
	            } else if ("audio".equals(type)) {  
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  
	            }  
	  
	            final String selection = "_id=?";  
	            final String[] selectionArgs = new String[] {  
	                    split[1]  
	            };  
	  
	            return getDataColumn(context, contentUri, selection, selectionArgs);  
	        }  
	    }  
	    // MediaStore (and general)  
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {  
	  
	        // Return the remote address  
	        if (isGooglePhotosUri(uri))  
	            return uri.getLastPathSegment();  
	  
	        return getDataColumn(context, uri, null, null);  
	    }  
	    // File  
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {  
	        return uri.getPath();  
	    }  
	  
	    return null;  
	}  
	/** 
	 * Get the value of the data column for this Uri. This is useful for 
	 * MediaStore Uris, and other file-based ContentProviders. 
	 * 
	 * @param context The context. 
	 * @param uri The Uri to query. 
	 * @param selection (Optional) Filter used in the query. 
	 * @param selectionArgs (Optional) Selection arguments used in the query. 
	 * @return The value of the _data column, which is typically a file path. 
	 */  
	public static String getDataColumn(Context context, Uri uri, String selection,  
	        String[] selectionArgs) {  
	  
	    Cursor cursor = null;  
	    final String column = "_data";  
	    final String[] projection = {  
	            column  
	    };  
	  
	    try {  
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,  
	                null);  
	        if (cursor != null && cursor.moveToFirst()) {  
	            final int index = cursor.getColumnIndexOrThrow(column);  
	            return cursor.getString(index);  
	        }  
	    } finally {  
	        if (cursor != null)  
	            cursor.close();  
	    }  
	    return null;  
	}  
	  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is ExternalStorageProvider. 
	 */  
	public static boolean isExternalStorageDocument(Uri uri) {  
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is DownloadsProvider. 
	 */  
	public static boolean isDownloadsDocument(Uri uri) {  
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is MediaProvider. 
	 */  
	public static boolean isMediaDocument(Uri uri) {  
	    return "com.android.providers.media.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is Google Photos. 
	 */  
	public static boolean isGooglePhotosUri(Uri uri) {  
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());  
	}  
//图片转为文件
	private File bitmapToFile(Bitmap photo, String filename) {
		if (photo == null) {
			return null;
		} else {
			try {
				FileOutputStream out = new FileOutputStream(filename);
				photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new File(filename);
		}
	}
	
}
