


需求:作为一个Web开发者，我们有时候会需要临时地将一个本地的Web网站部署到外网，以供他人体验评价或协助调试等等


ngrok + 云服务器，自己搭配

ngrok官网
ngrok 是一个开源程序,官网服务在国外,国内访问国外速度慢.
国内有相应的ngrok服务 natapp，frp，nat123端口映射，内网通 等


直接使用:
ngrok基本使用
从https://ngrok.com/  下载ngrok 然后注册一个账号 获取官方分配一个密钥

Your Tunnel Authtoken:7vSS4vDczrT2g8boL6KSZ_2TAZUmH7BiREJooxGUu1N


ngrok authtoken 7vSS4vDczrT2g8boL6KSZ_2TAZUmH7BiREJooxGUu1N




# 搭建 
yum install gcc -y
yum install git -y

安装go语言环境
yum install -y mercurial git bzr subversion golang golang-pkg-windows-amd64 golang-pkg-windows-386

检查环境安装 
git --version //( >= 1.7 )
go version

git clone https://github.com/inconshreveable/ngrok.git

cd ngrok

// http://www.ppx123.xyz/auto/test/test
// ppx123.xyz

export NGROK_DOMAIN="ppx123.xyz"

openssl genrsa -out rootCA.key 2048

openssl req -x509 -new -nodes -key rootCA.key -subj "/CN=$NGROK_DOMAIN" -days 5000 -out rootCA.pem

openssl genrsa -out device.key 2048

openssl req -new -key device.key -subj "/CN=$NGROK_DOMAIN" -out device.csr

openssl x509 -req -in device.csr -CA rootCA.pem -CAkey rootCA.key -CAcreateserial -out device.crt -days 5000


# 将新生成的证书替换
cp rootCA.pem assets/client/tls/ngrokroot.crt

cp device.crt assets/server/tls/snakeoil.crt

cp device.key assets/server/tls/snakeoil.key


# 编译生成ngrokd（服务端）
GOOS=linux GOARCH=amd64 make release-server
生成在~/ngrok/bin/目录中
# 编译生成ngrok（客户端）
GOOS=windows GOARCH=amd64 make release-client
生成在~/ngrok/bin/windows_amd64/目录中



cd ngrok

	

./bin/ngrokd -domain="134.175.231.104" -httpAddr=":80" -httpsAddr=":443" -tunnelAddr=":8083"
./bin/ngrokd -domain="87284200.qcloud.la" -httpAddr=":80" -httpsAddr=":443" -tunnelAddr=":8083" 
./bin/ngrokd -domain="17ee3.gz.1253864162.clb.myqcloud.com" -httpAddr=":80" -httpsAddr=":443" -tunnelAddr=":8083"


./bin/ngrokd -domain="ppx123.xyz" -httpAddr=":80" -httpsAddr=":443" -tunnelAddr=":8083"


ngrok -config=ngrok.cfg start http https ssh mstsc


ngrok -config=ngrok.cfg start http


./bin/ngrokd -tlsKey=device.key -tlsCrt=device.crt -domain=ppx123.xyz -httpAddr=:80 -httpsAddr=:443 -tunnelAddr=:8083



