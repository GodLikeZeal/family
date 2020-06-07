# Family 族谱管理系统
## 介绍
`Family`族谱管理系统是基于`Spring Boot 2.2.4.RELEASE`最新版本构建的，数据库采用`MongoDB`，
采用`Spring Security`来提供安全认证服务，前端渲染模板引擎为比较快的国产`Beetl`，数据图表渲染采用了
蚂蚁金服数据可视化`AntV `来渲染数据。
## 预览体验
[族谱管理系统](https://zhang.zealsay.com)
## 快速部署
### 1、docker部署（推荐）
- docker-compose编排文件
```yaml
version: "3.3"
services:
  family:
    image: registry.cn-qingdao.aliyuncs.com/zealsay/family:latest
    container_name: family
    ports:
      - "8091:8091"
    networks:
      - family
    depends_on:
      - mongo
    environment:
      MONGO_HOST: mongo                     #你的mongo的host地址，这里是内部容器名，你可以配置ip如47.101.43.123
      MONGO_PORT: 27017                     #你的mongo的端口号
      DATABASE: family                #你的项目数据在mongo中存储的空间
      USERNAME: username              #你的mongo中授权的用户名
      PASSWORD: password              #你的mongo中授权的密码
      AUTH_DATABASE: admin            #你的mongo中授权数据库 mongo默认为admin
      ADMIN_USERNAME: admin           #family后台管理系统默认管理员账号
      ADMIN_PASSWORD: 1234            #family后台管理系统默认管理员密码
      USER_PASSWORD: 123              #family后台管理系统添加成员时的默认登录密码
  mongo:
    image: mongo:3.4.10
    container_name: mongo
    networks:
      - family
    ports:
      - "27017:27017"
    volumes:
      - mongo_data:/data/db
    # command: mongod --auth # 启动授权登录
    environment: 
      MONGO_INITDB_ROOT_USERNAME: username  #你的mongo授权username
      MONGO_INITDB_ROOT_PASSWORD: password  #你的mongo授权password
    
networks:
  family:
    driver: bridge

volumes:
  mongo_data:
```
- 执行 `docker-compose up`
### 2、传统部署
> 因为依赖mongo数据库，所以需要你在本地或者远程启动mongo服务，并且最好是开启认证登录的mongo。
- 首先拉取代码到本地
`git clone https://github.com/GodLikeZeal/family` 
- 修改配置文件，将`application.yml`里面的关于prod的配置部分需要自行修改
```yaml
---
spring:
  profiles: prod
  application:
    name: family
  main:
    allow-bean-definition-overriding: true #当遇到相同的名字，是否允许覆盖
  data:
    mongodb:
      host: ${HOST}  #改成你的mongo的host
      port: ${PORT}  #改成你的mongo的port
      database: ${DATABASE}  #改成你的应用在mongo中的存储空间
      username: ${USERNAME}  #改成你的mongo的登录用户名
      password: ${PASSWORD}  #改成你的mongo的登录密码
      authentication-database: ${AUTH_DATABASE}  #改成你的mongo的授权数据库

beetl:
  suffix: html

app:
  username: ${ADMIN_USERNAME}  #配置后台管理员默认账号
  password: ${ADMIN_PASSWORD}  #配置后台管理员默认密码
  default-password: ${USER_PASSWORD}  #配置后台添加新成员时，新成员登录的默认密码

```
- 项目打包
`mvn clean package`
- 启动项目
`java -jar 打包后的jar文件`

## 功能点
- 实现家族树，登录用户节点动态闪烁。
- 分组功能：列表，添加，修改，删除。
- 成员列表分页查询，成员信息修改，删除，添加。
- 添加父节点下拉搜索框。
## 页面截图

- 登录页面
![-w1439](https://pan.zealsay.com/mweb/2020060515913544763818.jpg)

- 群组添加
![-w1439](https://pan.zealsay.com/mweb/2020060515913544763858.jpg)

- 成员添加
![-w1439](https://pan.zealsay.com/mweb/2020060515913544763871.jpg)

- 成员列表
![-w1439](https://pan.zealsay.com/mweb/2020060515913544763883.jpg)

- 族谱树
![-w1437](https://pan.zealsay.com/mweb/2020060515913544763895.jpg)


## 鸣谢
- 感谢[笔下光年Admin](https://gitee.com/yinqi/Light-Year-Admin-Template)
## 交流
- 欢迎加入交流群：189361484
- <img src="https://pan.zealsay.com/20190716214941558000000.jpg" alt="Sample"  width="150" height="200">

