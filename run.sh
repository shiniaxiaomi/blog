# 关闭应用
#应用名称
appName="blog-java"
app=$(pgrep -f java)
if test ${app}null != "null"
then
    kill -9 $app
fi

# 启动应用
projectPath=/root/code/$appName
cd $projectPath/target
name=$(ls |grep jar$)
nohup java -jar $projectPath/target/$name --spring.profiles.active=prod >$projectPath/log &
tail -f $projectPath/log
