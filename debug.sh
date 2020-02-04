# 以远程debug模式启动

# 构建项目
bash build.sh

# 关闭应用
#应用名称
appName="blog-v4.0"
app=$(pgrep -f blog)
if test ${app}null != "null"
then
    kill -9 $app
fi

# 启动应用
projectPath=/root/code/$appName
cd $projectPath/target
name=$(ls |grep jar$)
nohup java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9200,suspend=n -jar $projectPath/target/$name --spring.profiles.active=prod >$projectPath/out &
tail -f $projectPath/out


