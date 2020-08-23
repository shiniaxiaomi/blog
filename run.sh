# 关闭应用
#应用名称
appName="blog"
app=$(jps | grep $appName | awk '{print $1}')
if test ${app}null != "null"
then
    kill -9 $app
fi

# 启动应用
projectPath=/root/code/$appName
cd $projectPath/target
name=$(ls |grep jar$)
nohup java -jar $projectPath/target/$name --spring.profiles.active=prod --server.port=80 >$projectPath/out &
tail -f $projectPath/out