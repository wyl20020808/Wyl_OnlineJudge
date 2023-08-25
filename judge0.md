# 环境配置

## 在ubuntu中安装Judge0：

确保安装了docker和docker compose
看教程就完事了，官方文档写得很好

**参考**：

[安装docker compose](https://docs.docker.com/compose/install/linux/#install-using-the-repository)

[judge0自建版官方安装教程](https://github.com/judge0/judge0/blob/master/CHANGELOG.md#deployment-procedure)

# judge0安装遇到的问题记录

执行docker-compose up -d命令时报错。

**解决方法**：按提示的来（没啥困难的）

```shell
root@hecs-278615:~/judge0/judge0-v1.13.0# docker-compose up -d

Command 'docker-compose' not found, but can be installed with:

snap install docker          # version 20.10.17, or
apt  install docker-compose  # version 1.25.0-1

See 'snap info docker' for additional versions.

```

# judge0的API



