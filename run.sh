# 关闭应用
#应用名称
appName="blog-v4.0"
app=$(pgrep -f SNAPSHOT.jar)
if test ${app}null != "null"
then
    kill -9 $app
fi

# 启动应用
projectPath=/root/code/$appName
cd $projectPath/target
name=$(ls |grep jar$)
nohup java -jar $projectPath/target/$name --spring.profiles.active=prod >$projectPath/out &
tail -f $projectPath/out
