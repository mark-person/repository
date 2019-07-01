
# 安装
* 最近无意间发现Nginx官方提供了Yum源。(默认情况Centos7中无Nginx的源)
* 1. 添加源:sudo rpm -Uvh http://nginx.org/packages/centos/7/noarch/RPMS/nginx-release-centos-7-0.el7.ngx.noarch.rpm
* 1.1. 通过yum search nginx看看是否已经添加源成功 
* 2. 安装Nginx:sudo yum install -y nginx
* 3. 启动Nginx并设置开机自动运行 

```linux
sudo systemctl start nginx.service
sudo systemctl enable nginx.service
```


# 配置
/etc/nginx/nginx.conf

* nginx修改配置后重载: /usr/sbin/nginx -s reload

