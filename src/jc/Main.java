package jc;

import jc.RenrenDownloader;
import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new ExitHandler());

        System.out.println("本程序是开源项目(https://github.com/kingjci/renrenDownloader),可自行判断输入账号密码是否安全");
        ResourceBundle config = ResourceBundle.getBundle("config", Locale.getDefault());

        File imagesFolder = new File(config.getString("imagesFolderPath"));
        if (!imagesFolder.exists()){
            imagesFolder.mkdirs();
        }

        Options options = new Options();
        options.addOption("u", true, "username");
        options.addOption("p", true, "password");
        options.addOption("list", true , "uid list");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try{
            cmd = parser.parse(options, args);
        }catch (ParseException e){
            e.printStackTrace();
        }

        String username = null;
        String password = null;


        if (args.length != 0){



            if (cmd == null){
                System.out.println("参数输入错误, -u 登录邮箱 -p 密码\n本程序是开源项目(https://github.com/kingjci/renrenDownloader),可自行判断输入账号密码是否安全");
                System.exit(-1);
            }

            if (cmd.hasOption("u")){
                username = cmd.getOptionValue("u");
            }else {
                System.out.println("请在参数中指定登陆邮箱 -u a@b.c");
                System.exit(-1);
            }

            if (cmd.hasOption("p")){
                password = cmd.getOptionValue("p");
            }else {
                System.out.println("请在参数中指定登陆密码 -p password");
                System.exit(-1);
            }



        }else {
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入邮箱回车结束:");
            username = scanner.nextLine();
            System.out.println("请输入密码回车结束:");
            password = scanner.nextLine();
        }


        RenrenDownloader renrenDownloader = new RenrenDownloader(username,password,config);

        List<String> uidList;
        if (cmd.hasOption("list")){
            //下载list中uid用户的相册
            uidList = getUidListFromFile(cmd.getOptionValue("list"));
            System.out.println("开始下载指定uid用户相册");
        }else {
            //下载好友相册
            uidList = renrenDownloader.getFriendsList();
            System.out.println("开始下载人人好友相册");
        }



        int personCount = 1;
        for (String uid : uidList){
            System.out.printf("%d.",personCount++);
            renrenDownloader.saveImages(uid, config.getString("imagesFolderPath"));
        }
        System.out.println("---------------------------------------------------");
        System.out.println("下载完毕!");

    }


    public static List<String> getUidListFromFile(String path){

        List<String> list = new ArrayList<String>();

        File file = new File(path);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String lineString = null;

            while ((lineString = reader.readLine()) != null) {
                list.add(lineString);
            }
            reader.close();
        } catch (IOException e) {

            System.out.println("uid文件不存在或者没有读取的权限");
            e.printStackTrace();
            System.exit(-1);

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {

                    System.out.println("uid文件不存在或者没有读取的权限");
                    ex.printStackTrace();
                    System.exit(-1);

                }
            }
        }

        return list;
    }
}
