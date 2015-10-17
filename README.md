# 人人相册下载器
============================

###     不知道从什么时候起登陆人人的次数越来越少，但是上面还有很多我和我朋友的照片，这个软件就是帮助你下载人人上所有好友的照片

## 人人相册下载器能干什么？
下载人人网上所有好友的照片或者批量下载指定uid用户的照片

![](http://www.jincheng.link/resources/emotion/d0780c338744ebf88143a7acd9f9d72a6059a708.jpg)
## 人人相册下载器怎么长的这么丑连个界面都没有？
俗话说得好软件长的和软件作者一样，我长得丑还不要脸，所以我的软件也没有脸

## 人人相册下载器需要我的账号吗？
用来登陆你的账号，才能找到你的朋友啊

## 人人相册下载器会盗取我的账号吗？
反正我是不盗你的账号，代码在那里觉得不放心自己看，代码量不多![](http://www.jincheng.link/resources/emotion/image_emoticon25.png)

## 下载与安装
-如果你是个可爱的妹子┑(￣▽ ￣)┍，直接下载人人相册下载器.rar后解压缩运行里面的人人相册下载器.bat。
根据软件的提示输入用户邮箱，密码登陆，然后文件夹images里面就是你和朋友的照片啦


-如果你也是个软件狗，下载renrenDownloader.jar即可(压缩包里多出来的东西是java虚拟机你一定有的)
![](http://www.jincheng.link/resources/emotion/image_emoticon25.png)，在JAVA虚拟机上运行


1.下载所有好友照片

```sh
>>java -jar renrenDownloader -u 你的账号 -p 你的密码
```

2.批量下载指定用户的照片

在当前文件夹下新建一个uid.txt文件，每一行放入一个用户uid。打开指定用户主页，uid就是下图红圈里面的

![](http://www.jincheng.link/resources/emotion/20151017163831.jpg)

```sh
>>java -jar renrenDownloader -u 你的账号 -p 你的密码 -list uid.txt
```

## Contacts
  email:kingjci@sina.com  更新于2015/10/17
