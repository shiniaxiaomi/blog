# 构建应用
appName="blog"
projectPath=/root/code/$appName
cd $projectPath
mvn package -Dmaven.test.skip=true