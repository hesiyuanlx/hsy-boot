# 导入基础镜像
#  FROM harbor.xxxxxxxxx.com/xxxx/openjdk-8:8u212

# 设置镜像时间
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime &&  echo $TZ > /etc/timezone

# 创建服务目录
RUN mkdir -p /opt/app/audit-admin

# 设置服务名环境变量
ARG APP_NAME=audit-admin

# 把文件复制到镜像
ADD audit-admin.tar.gz /opt/app/boot-admin/

# WORKDIR指令设置Dockerfile中的任何RUN，CMD，ENTRPOINT，COPY和ADD指令的工作目录
WORKDIR /opt/app/boot-admin

# 创建日志目录并赋予所有用户可执行权限
RUN mkdir /opt/app/logs && chmod a+x /opt/app/boot-admin/bin/boot-admin-daemon.sh

# 容器启动时要运行的命令
CMD ["/opt/app/audit-admin/bin/boot-admin-daemon.sh", "start", "-conf /opt/app/boot-admin/conf", "-logs /opt/app/logs"]