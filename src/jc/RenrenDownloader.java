package jc;

import com.alibaba.fastjson.JSON;
import jc.entity.*;
import org.apache.http.NameValuePair;
import org.apache.http.client.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RenrenDownloader {

    private String username;
    private String password;
    private String uid;
    private List<Map<String, Object>> imagesList;

    private CloseableHttpClient httpclient;
    private HttpGet httpget;
    private ResourceBundle config;
    private ResponseHandler<byte[]> byteResponseHandler = new ByteResponseHandler();

    public RenrenDownloader(String username,String password,ResourceBundle config) {

        this.username = username;
        this.password = password;
        this.config = config;
        httpclient = HttpClients.createDefault();
        httpget = new HttpGet();
        imagesList = new ArrayList<Map<String, Object>>();

        login();

    }

    //返回值获取到的图片总数
    public int saveImages(String uid, String path){

        String username = getNameFromUid(uid);
        System.out.println(String.format("开始下载%s的相册",username));
        //  http://photo.renren.com/photo/320967759/albumlist/v7#
        System.out.println(String.format("  开始获取%s的相册地址", username));
        List<Album> albumList = getImageAlbums(uid);
        System.out.println(String.format("  开始获取%s的照片地址",username));
        List<List<Photo>> photoList = getPhotosFromAlbums(uid, albumList);
        int totalcount = 0;

        //为每个用户uid建立一个文件夹
        File uidDir = new File(path + username);
        if (!uidDir.exists()){
            uidDir.mkdir();
        }


        for (int i = 0; i < albumList.size(); i++) {

            Album album = albumList.get(i);
            if (album.getSourceControl() == 4){
                continue;
            }

            List<Photo> list = photoList.get(i);

            //为每个相册建立一个文件夹，文件夹名称为相册的名称.相册名中可能存在\/*?<>|等不合法符号无法作为文件名，使用X替换不合法符号
            String albumName = Utils.getSafeFolderName(album.getAlbumName());
            File albumDir = new File(path + username + '/' + albumName);
            albumDir.mkdir();

            System.out.println(String.format("  开始下载相册:%s", albumName));
            for(Photo photo : list){
                saveImage(photo.getUrl(), albumDir.toString());
                totalcount++;
            }
        }

        System.out.println(String.format("%s相册全部下载完毕,共下载%d相册%d张照片！",username, albumList.size(), totalcount));
        return totalcount;
    }

    public List<List<Photo>> getPhotosFromAlbums(String uid, List<Album> albumList){

        //每个list里面是一个album
        List<List<Photo>> list = new ArrayList<List<Photo>>();
        
        for (Album album : albumList){
            // http://photo.renren.com/photo/320967759/album-1074897301/bypage/ajax/v7?page=5&pageSize=20

            List<Photo> photoAlbum = new ArrayList<Photo>();

            // 4代表文件夹加密
            // 0代表好友可见，即使是好友也会访问失败，原因不明
            if (album.getSourceControl() == 4){
                //文件夹有密码，限制访问
                list.add(photoAlbum);
                continue;
            }


            int photoCount = album.getPhotoCount();

            for (int i = 1; i <= photoCount/100 + 1 ; i++){
                String requestURl = "http://photo.renren.com/photo/" + uid + "/album-" +album.getAlbumId()
                        + "/bypage/ajax/v7?page=" + i + "&pageSize=100";
                PhotoList photoList = null;
                try{
                    String temp = ajax(requestURl);
                    //原因不明，部分只有好友可见相册会出现这个问题
                    String regexTemp = "您没有操作本资源的权限";

                    Pattern pattern = Pattern.compile(regexTemp);
                    Matcher matcher= pattern.matcher(temp);
                    if (matcher.find()){
                        continue;
                    }


                    photoList = JSON.parseObject(temp, PhotoList.class);
                }catch (Exception e){
                    System.out.println("网络连接出现问题无法连接到人人服务器");
                    e.printStackTrace();
                    System.exit(-1);
                }
                List<Photo> photos = photoList.getPhotoList();
                photoAlbum.addAll(photos);
            }

            list.add(photoAlbum);

        }
        
        return list;
    }
    
    
    public List<Album> getImageAlbums(String uid){

        List<Album> albumList = null;

        try{
            httpget.setURI(new URI("http://photo.renren.com/photo/" + uid + "/albumlist/v7#"));
            String albumListString = new String(httpclient.execute(httpget, byteResponseHandler), "UTF-8");
            //  nx\.data\.photo\s?=\s?(\{.*\});
            String regexAlbumList = "nx\\.data\\.photo\\s?=\\s?(\\{\\s[\\s\\S]*\\});";
            Pattern pattern = Pattern.compile(regexAlbumList);
            Matcher matcher = pattern.matcher(albumListString);
            if (matcher.find()){

                OuterAlbumList outerAlbumList = JSON.parseObject(matcher.group(1), OuterAlbumList.class);
                albumList = outerAlbumList.getAlbumList().getAlbumList();

            }

        }catch (URISyntaxException e){
            System.out.println("相册地址错误");
            e.printStackTrace();
            System.exit(-1);

        }catch (ClientProtocolException e){
            System.out.println("相册地址错误");
            e.printStackTrace();
            System.exit(-1);

        }catch (IOException e){
            System.out.println("网络连接出现问题无法连接到人人服务器");
            System.exit(-1);

            e.printStackTrace();
        }

        return albumList;

    }

    public boolean saveImage (String url, String path) {

        try{

            httpget.setURI(new URI(url));

            byte[] responseBody = httpclient.execute(httpget, this.byteResponseHandler);

            File file = new File(path + '/' + getRandomString(10) + ".jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(responseBody);

        }catch (Exception e){
            System.out.println("网络访问故障无法连接到人人服务器");
            e.printStackTrace();

            System.exit(-1);

        }

        return true;
    }

    //to get json from the ajax
    public String ajax(String url){

        String ajaxString = null;

        try{
            httpget.setURI(new URI(url));
            byte[] responseBody = this.httpclient.execute(httpget, this.byteResponseHandler);
            ajaxString = new String(responseBody, "UTF-8");
        }catch (UnsupportedEncodingException e){
            System.out.println("不支持的网络编码格式");
            e.printStackTrace();
            System.exit(-1);
        }catch (URISyntaxException e){
            System.out.println("相册地址错误");
            e.printStackTrace();
            System.exit(-1);
        }catch (ClientProtocolException e){
            System.out.println("网络访问协议错误");
            e.printStackTrace();
            System.exit(-1);
        }catch (IOException e){
            System.out.println("网络连接出现问题无法连接人人服务器");
            e.printStackTrace();
            System.exit(-1);
        }

        return ajaxString;
    }

    public String getNameFromUid(String uid){

        String username = null;

            try{
                //www.renren.com/320967759/profile
                httpget.setURI(new URI("http://www.renren.com/" + uid + "/profile"));
                String profileString = new String(httpclient.execute(httpget, byteResponseHandler), "UTF-8");
                // profileOwnerName = 'name'
                String regex = "profileOwnerName\\s?=\\s?'(.*)'";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(profileString);
                if (matcher.find()){
                    username = matcher.group(1);
                }

            }catch (URISyntaxException e){
                System.out.println("相册地址错误");
                e.printStackTrace();
                System.exit(-1);
            }catch (ClientProtocolException e){
                System.out.println("相册地址错误");
                e.printStackTrace();
                System.exit(-1);
            }catch (IOException e){
                System.out.println("网络连接出现问题无法连接人人服务器");
                e.printStackTrace();
                System.exit(-1);
            }

            if (username == null){
                System.out.println("连接错误，请重新运行");
                System.exit(-1);
            }



        return username;
    }

    //to get a random string for the image name
    public static String getRandomString(int length) { //length��ʾ�����ַ����ĳ���
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    //login to get cookies
    public void login(){

        String publicKeyJSON = ajax(config.getString("encryptUrl"));
        PublicKey publicKey = JSON.parseObject(publicKeyJSON, PublicKey.class);
        String rawPassword = password;

        BigInteger m = new BigInteger(Utils.getReverseHexString(rawPassword), 16);
        BigInteger n = new BigInteger(publicKey.getN(), 16);
        BigInteger e = new BigInteger(publicKey.getE(), 16);
        BigInteger result = m.modPow(e, n);
        String encryptPassword = result.toString(16);

        HttpPost httpPost = new HttpPost(config.getString("loginUrl"));

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", username));
        params.add(new BasicNameValuePair("origURL", config.getString("origURL")));
        params.add(new BasicNameValuePair("domain", config.getString("domain")));
        params.add(new BasicNameValuePair("key_id", config.getString("key_id")));
        params.add(new BasicNameValuePair("captcha_type", config.getString("captcha_type")));
        params.add(new BasicNameValuePair("password", encryptPassword));
        params.add(new BasicNameValuePair("rkey", publicKey.getRkey()));
        params.add(new BasicNameValuePair("f", config.getString("f")));

        String response = null;
        try{
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            response =new String(httpclient.execute(httpPost, byteResponseHandler));
        }catch (UnsupportedEncodingException ex){
            System.out.println("不支持的网络编码格式");
            ex.printStackTrace();
            System.exit(-1);
        }catch (ClientProtocolException ex){
            System.out.println("网络协议错误");
            ex.printStackTrace();
            System.exit(-1);
        }catch (IOException ex){
            System.out.println("网络访问错误，无法连接人人服务器");
            ex.printStackTrace();
            System.exit(-1);
        }

        String regexLogin = "\"code\":true";
        Pattern pattern = Pattern.compile(regexLogin);
        Matcher matcher = pattern.matcher(response);

        if (matcher.find()){
            System.out.println("登陆成功!");
        }else {
            System.out.println(String.format("登陆失败!请检查用户名:%s 密码:%s 或者稍后再试",username,password));
            System.exit(-1);
        }


    }

    public List<String> getFriendsList(){

        HttpGet httpGet = new HttpGet(config.getString("friendUrl"));

        String friendsString = null;
        try{
            friendsString = new String(httpclient.execute(httpGet, byteResponseHandler), "UTF-8");
        }catch (UnsupportedEncodingException ex){
            System.out.println("不支持的网络编码格式");
            ex.printStackTrace();
            System.exit(-1);
        }catch (ClientProtocolException ex){
            System.out.println("相册地址错误");
            ex.printStackTrace();
            System.exit(-1);
        }catch (IOException ex){
            System.out.println("网络访问错误，无法连接人人服务器");
            ex.printStackTrace();
            System.exit(-1);
        }

        List<String> list = new ArrayList<String>();
        //String regexAlbumList = "nx\\.data\\.photo\\s?=\\s?(\\{\\s[\\s\\S]*\\});";
        String regexFriendList = "\"friends\":\\s(\\[\\{\"[\\s\\S]*\\}\\]),";
        Pattern pattern = Pattern.compile(regexFriendList);
        Matcher matcher = pattern.matcher(friendsString);
        if (matcher.find()){

            List<Friend> friendList = JSON.parseArray(matcher.group(1), Friend.class);
            for (Friend friend:friendList){
                list.add(friend.getFid());
            }
        }

        return list;
    }


    //setters and getters
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public CloseableHttpClient getHttpclient() {
        return httpclient;
    }

    public void setHttpclient(CloseableHttpClient httpclient) {
        this.httpclient = httpclient;
    }
}
