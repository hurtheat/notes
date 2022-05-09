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

  

- 安装`yum-utils`包（提供`yum-config-manager` 实用程序）

  ```shell
   $ sudo yum install -y yum-utils
  ```

- 设置稳定的存储库

  ```shell
  $ sudo yum-config-manager \
      --add-repo \
      https://download.docker.com/linux/centos/docker-ce.repo
  ```

  

- 安装Docker引擎

  ```shell
  $ sudo yum install docker-ce docker-ce-cli containerd.io docker-compose-plugin
  ```

  Ps: 这条命令会安装最新的版本，如果需要指定的版本，可以使用下面命令列出：

  ```shell
  $ yum list docker-ce --showduplicates | sort -r
  ```

  然后使用以下命令安装指定版本：

  ```shell
  $ sudo yum install docker-ce-<VERSION_STRING> docker-ce-cli-<VERSION_STRING> containerd.io docker-compose-plugin
  ```

- 启动Docker

  ```shell
  $ sudo systemctl start docker
  ```

- 测试是否安装成功

  ```shell
  $ docker version
  Client: Docker Engine - Community
   Version:           20.10.15
   API version:       1.41
   Go version:        go1.17.9
   Git commit:        fd82621
   Built:             Thu May  5 13:16:58 2022
   OS/Arch:           linux/amd64
   Context:           default
   Experimental:      true
  
  Server: Docker Engine - Community
   Engine:
    Version:          20.10.15
    API version:      1.41 (minimum version 1.12)
    Go version:       go1.17.9
    Git commit:       4433bf6
    Built:            Thu May  5 13:15:18 2022
    OS/Arch:          linux/amd64
    Experimental:     false
   containerd:
    Version:          1.6.4
    GitCommit:        212e8b6fa2f44b9c21b2798135fc6fb7c53efc16
   runc:
    Version:          1.1.1
    GitCommit:        v1.1.1-0-g52de29d
   docker-init:
    Version:          0.19.0
    GitCommit:        de40ad0
  ```

##### 五、通过包安装



##### 六、通过自动化脚本安装



##### 七、卸载Docker

- 卸载 Docker Engine、CLI、Containerd 和 Docker Compose 软件包：

  ```shell
  $  sudo yum remove docker-ce docker-ce-cli containerd.io
  ```

- 主机上的映像、容器、卷或自定义配置文件不会自动删除。要删除所有映像、容器和卷：

  ```shell
  $ sudo rm -rf /var/lib/docker
  $ sudo rm -rf /var/lib/containerd
  ```

  

更多细节可以参考官网安装教程，可以包括选择Linux（Centos,Ubantu），Windows,Macos的安装步骤:

[]: https://docs.docker.com/engine/install/	"官网安装文档"



