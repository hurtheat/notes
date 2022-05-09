#### Docker在Linux上的安装

##### 一、安装环境

- 要安装Docker Engine，需要Centos7，Centos8，Centos9的维护版本

##### 二、安装前准备（卸载旧版本的Docker）

​	

```shell
 $ sudo yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine
```

##### 三、安装方式

- 使用yum的方式安装，便于安装和后续的升级维护，推荐方法。
- 使用RPM包并手动安装，而且手动管理升级，在特殊的使用场景是很有用的。
- 使用一些自动化脚本安装。

##### 四、使用yum安装

- 确保自己系统上的yum是最新的，使用以下命令更新yum:

  ```shell
    $ yum update
  ```

  

