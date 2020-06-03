# Family 族谱管理系统
## 介绍
`Family`族谱管理系统是基于`Spring Boot 2.2.4.RELEASE`最新版本构建的，数据库采用`MongoDB`，
采用`Spring Security`来提供安全认证服务，前端渲染模板引擎为比较快的国产`Beetl`，数据图表渲染采用了
蚂蚁金服数据可视化`AntV `来渲染数据。
## 快速部署
### 1、docker部署（推荐）

```yaml
version: "3.3"
services:
  family:
    image: registry.cn-qingdao.aliyuncs.com/zealsay/family:latest
    container_name: family
    command: redis-server --requirepass zealsay
    expose:
      - 8091
    depends_on:
      - mongo
    environment:
      - HOST= localhost                #你的mongo的host地址，如47.101.43.123
      - PORT=27017                     #你的mongo的端口号
      - DATABASE=family                #你的项目数据在mongo中存储的空间
      - USERNAME=your username         #你的mongo中授权的用户名
      - PASSWORD=your password         #你的mongo中授权的密码
      - AUTH_DATABASE=admin            #你的mongo中授权数据库 mongo默认为admin
      - ADMIN_USERNAME= login username #family后台管理系统默认管理员账号
      - ADMIN_PASSWORD= login password #family后台管理系统默认管理员密码
      - USER_PASSWORD= user password   #family后台管理系统添加成员时的默认登录密码
  mongo:
    image: mongo:3.4.10
    container_name: mongo
    expose:
        - 27017 #mongo默认端口号
    volumes:
      - mongo_data:/data/db
    # command: mongod --auth # 启动授权登录
    environment: 
      MONGO_INITDB_ROOT_USERNAME: username  #你的mongo授权username
      MONGO_INITDB_ROOT_PASSWORD: password  #你的mongo授权password
    

volumes:
  mongo_data:
```