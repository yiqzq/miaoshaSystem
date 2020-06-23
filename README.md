# miaoshaSystem

基于Springboot的电商秒杀系统

使用技术栈：mysql+springboot+Redis+RabbitMQ

前端略丑，见谅

## 使用须知

1. 更新`application.properties`中mysql，Redis，RabbitMQ的地址
2. `miaoshaSystem\src\main\java\com\yiqzq\miaoshasystem\config\StaticResourceLocationConfig.java`中需要配置一下存放图片的本地链接

```java
//根据环境自行更改
//windows引用
registry.addResourceHandler("/userimg/**").addResourceLocations("file:F:/image/");
//linux引用
registry.addResourceHandler("/userimg/**").addResourceLocations("file:/usr/local/img/");
```

同时在`miaoshaSystem\src\main\java\com\yiqzq\miaoshasystem\service\impl\FileServiceImpl.java`中关于存储路径的设置和上面要修改一致

```java
public String validateImage(MultipartFile file) {
        ....
             //filePath为实际存储的位置
            //String filePath = "F:\\image\\";
            String filePath = "/usr/local/img/";
            String path = filePath + fileName;
 ...
    }
```

在`miaoshaSystem\src\main\java\com\yiqzq\miaoshasystem\service\impl\RegisterUserServiceImpl.java`中有设置默认头像，可以自行更改

```java
public Result<User> insertUser(RegisterUser registerUser) {
     ...
    	//默认头像   
    	user.setHead("/userimg/img.jpg");
      ...
    }
```

3. 项目效果可以查看http://139.9.128.222:8080/
4. 有效期至2020/11/1

<img src="https://i.loli.net/2020/06/23/IeW598TCvcwfFmy.png" alt="image-20200623155314100" style="zoom:80%;" />

<img src="https://i.loli.net/2020/06/23/PvOu4FVsn3LJIrE.png" alt="image-20200623155343722" style="zoom:80%;" />

<img src="https://i.loli.net/2020/06/23/PVAvZ2Qa7y8ztEB.png" alt="image-20200623155419324" style="zoom:80%;" />