# IX ∞ Cloud

[![AUR](https://img.shields.io/github/license/ix-s/ix)](https://github.com/zengkiku/IX_Cloud)
[![](https://img.shields.io/badge/Author-zeng_kiku-orange.svg)](https://github.com/zengkiku)
[![](https://img.shields.io/badge/version-3.0.0-success)](https://github.com/zengkiku/IX_Cloud)


一款基于 Spring Cloud Alibaba 的权限管理系统，集成市面上流行库，可以作用为快速开发的一个框架使用。


一套以微服务架构的脚手架,使用 Spring Cloud Alibaba 系列进行架构,学习并了解它将能快速掌握微服务核心基础。此项目是为了减少业务代码的重复轮子,它具有一个系统该有的通用性核心业务代码,无论是微服务还是单体,都是通用的业务但更多的,是为了学习微服务的理念以及开发 您可以使用它进行网站管理后台，网站会员中心，CMS，CRM，OA 等待系统的开发, 当然,不仅仅是一些小系统,我们可以生产更多的服务模块,不断完善项目。

系统初心是为了能够更快地完成业务的需求，带来更好的体验、更多的时间。它将会用于孵化一些实用的功能点。我们希望它们是轻量级，可移植性高的功能插件。

同时，我们更希望广大开发者能在其中更快地获得更好的解决方案、尽量降低我们的学习成本。由此，我们应当把更多的时间投入到其它更有意义的事情当中，我们深知知识的重要性，但，并不希望仅拥有单一”知识“。去感受/关爱更多光彩，无论人、事、物，它们也将成为你最好的灵感。

后端源码：https://github.com/zengkiku/IX_Cloud

[//]: # (前端源码：https://github.com/ix-s/ix-ui)

[//]: # (技术文档：https://www.ix.cn/docs/)

[//]: # (官方博客：https://www.ix.cn)

## 系统模块

```
com.ix
├── ix-ui              // 前端框架 [80]【主要】
├── ix-gateway         // 网关模块 [88]【主要】
├── ix-nacos           // nacos [8848]
├── ix-auth            // 认证中心 [8888]【主要】
├── ix-api             // 接口模块
│       └── ix-api-system                             // 系统接口
│       └── ix-api-dfs                                // DFS接口
│       └── ix-api-job                                // 定时任务接口
├── ix-framework       // 核心模块
│       └── ix-framework-core                         // 核心模块
│       └── ix-framework-feign                        // 扩展feign模块
│       └── ix-framework-mq                           // 消息队列模块
│       └── ix-framework-log                          // 日志记录
│       └── ix-framework-datascope                    // 数据权限
│       └── ix-framework-jdbc                         // jdbc
│       └── ix-framework-swagger                      // swagger文档
│       └── ix-framework-redis                        // 缓存服务
│       └── ix-framework-security                     // 安全模块
│       └── ix-framework-utils                        // 工具模块
├── ix-server         // 业务模块
│       └── ix-server-system                          // 系统模块 [8081]【主要】
│       └── ix-server-job                             // 定时任务 [8082]
│       └── ix-server-dfs                             //  DFS服务 [8083]
│       └── ix-server-gen                             // 代码生成 [8084]
├── ix-visual        // 图形化管理模块
|       └── ix-visual-sentinel                        // sentinel [8101]
│       └── ix-visual-monitor                         // 监控中心 [8102]
├──pom.xml                // 公共依赖
```

## 内置功能

1. 用户管理：用户是系统操作者，该功能主要完成系统用户配置。
2. 部门管理：配置系统组织机构（公司、部门、小组），树结构展现支持数据权限。
3. 岗位管理：配置系统用户所属担任职务。
4. 菜单管理：配置系统菜单，操作权限，按钮权限标识等。
5. 角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
6. 字典管理：对系统中经常使用的一些较为固定的数据进行维护。
7. 参数管理：对系统动态配置常用参数。
8. 异步：登录日志/系统操作日志/系统登录日志记记录和查询。
9. 定时任务：在线（添加、修改、删除）任务调度包含执行结果日志。
10. 代码生成：一键生成 CRUD 前后端代码，为业务开发提供更快的速度。
11. 服务监控：监视当前系统 CPU、内存、磁盘、堆栈等相关信息。
12. 连接池监视：监视当前系统数据库连接池状态，可进行分析 SQL 找出系统性能瓶颈。
13. 分布式文件储存。
14. Swagger 网关聚合文档。
15. Sentinel 限流中心。
16. Nacos 注册 + 配置中心。

## 演示图

<table>
    <tr>
        <td><img src="https://www.ix.cn/assets/images/ix/1.png"/></td>
        <td><img src="https://www.ix.cn/assets/images/ix/2.png"/></td>
    </tr>
    <tr>
        <td><img src="https://www.ix.cn/assets/images/ix/3.png"/></td>
        <td><img src="https://www.ix.cn/assets/images/ix/4.png"/></td>
    </tr>
    <tr>
        <td><img src="https://www.ix.cn/assets/images/ix/5.png"/></td>
        <td><img src="https://www.ix.cn/assets/images/ix/6.png"/></td>
    </tr>
</table>

## 在线体验

- admin/123456

演示地址：[http://www.zeng_kiku.cloud](http://www.zeng_kiku.cloud)

## 支持 Linux 一件 Docker 启动(最小化启动服务)

内存 > 16 需要自行安装 maven、docker、docker-compose、node、yarn

```shell
# mvn
mvn clean && mvn install
# 进入脚本目录
cd ./docker
# 可执行权限
chmod 751 deploy.sh
# 执行启动（按需执行参数，[init|port|base|server|stop|rm]）
# 初始化
./deploy.sh init
# 基础服务
./deploy.sh base
# 启动ix
./deploy.sh server
# 启动UI
./deploy.sh nginx
```

## ix 微服务交流群

QQ交流群：<a href="https://jq.qq.com/?_wv=1027&k=f3FjlqMu" target='_blank' rel='noreferrer'>796690812</a>
