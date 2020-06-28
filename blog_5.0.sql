/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : blog

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 28/06/2020 23:52:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `desc` varchar(255) DEFAULT NULL,
  `md` longtext,
  `md_html` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `pid` int DEFAULT NULL COMMENT '记录blog的父节点的id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of blog
-- ----------------------------
BEGIN;
INSERT INTO `blog` VALUES (17, '1', 'dsfdskfjdskl', 'sdfjksldjfsdkljfs\n\n```\npublic void main(){\n    int i=0;\n}\n```\n\nnihao\n\n\n\n\n\njava\n\n\n\nnihel\n\n\n\nwo xiang\n', '<p>sdfjksldjfsdkljfs</p>\n<pre><code>public void main(){\n    int i=0;\n}\n</code></pre>\n<p>nihao</p>\n<p>java</p>\n<p>nihel</p>\n<p>wo xiang</p>\n', '2020-06-26 20:39:20', '2020-06-25 07:38:54', 0);
INSERT INTO `blog` VALUES (18, 'mybatis11111111简单快速反击但是开发绝对是开发都是减肥肯定是减肥的是几点上课见风使舵开发建设的减肥的时刻减肥的时刻', '对双方都舒服\n。对双方都是粉色的\n。输入法推荐使用百度输入法，可以定制化的比较多\n。WiFi测速软件：Speedtest（AppStore中即可下载），尽管好用', '## 介绍111\n\n[主要软件来源](http://www.pc6.com/mac)\n\n## 软件安装记录\n\n对双方都舒服\n\n对双方都是粉色的\n\n输入法推荐使用百度输入法，可以定制化的比较多\n\nWiFi测速软件：Speedtest（AppStore中即可下载），尽管好用，用完赶紧删除掉即可（感觉很占用CPU）\n\nMarkdown编辑器：[Typora](http://www.pc6.com/mac/132924.html)\n\n浏览器：[谷歌浏览器](https://www.google.cn/chrome/)\n\n翻墙：[ShadowsocksX-NG](https://www.maczapp.com/shadowsocksx-ng)（可以去GitHub上下载）\n\nSSH连接工具：[FinalShell](http://www.hostbuf.com/)\n\n代码编辑器：[VS Code](https://code.visualstudio.com/)\n\nJava开发编辑器：[Idea2019](https://www.jetbrains.com/idea/download/)\n\n> [永久激活注册码](https://blog.csdn.net/qq_34801169/article/details/95059368)\n>\n> [激活码2](https://blog.csdn.net/m0_37862829/article/details/93920188)\n\n截图软件：[Snipaste](http://www.pc6.com/mac/542001.html)\n\n翻译：[有道翻译](http://www.pc6.com/mac/110548.html)\n\n非常好用的程序员笔记软件：[Quiver](http://www.pc6.com/mac/130522.html)（有点慢，占用CPU）\n\n应用卸载软件：[App Uninstaller](http://www.pc6.com/mac/267539.html)\n\n压缩软件：[eZip](http://www.pc6.com/mac/629419.html)\n\n修改Mac上的键盘映射规则：[Karabiner](https://mac.softpedia.com/get/System-Utilities/?utm_source=spd&utm_campaign=postdl_redir)\n\n文件搜索工具：EasyFind（AppStore）\n\n谷歌浏览器代理插件：Proxy SwitchyOmega\n\n### 非常好用的软件\n\n1. 快速工具（效率，类似Listary）：[Alfred](https://download.csdn.net/download/sinat_29970905/11634147)\n2. [SpaceLauncher](https://www.maczapp.com/download.php?id=69188&did=52828&action=1573999300&from=15045811251972)\n   通过 `空格`加 `其他按键`来对应快捷键，如打开文件夹，打开应用等\n3. betterTouchTool\n   还是购买正版吧，这个是真的好用，两年需要59元\n4. 测试test\n5. `dsfjdskfjsdkfs`\n6. 对方接受短裤减肥快速的减肥速度快\n\n### 开发相关\n\n1. mysql环境\n   > `brew install mysql`\n   >\n2. [Java-jdk8环境](https://download.csdn.net/download/lc19850921/10957187)：直接安装即可，不需要配置环境变量\n3. [Nodejs环境](http://nodejs.cn/download/)\n4. [git](https://git-scm.com/)\n5. maven(`brew install maven`)\n6. tomcat(`brew install tomcat`\n7. test\n\n### 配置环境变量\n\n配置VsCode的环境变量：\n\n```\nalias code=\"/Applications/Visual\\ Studio\\ Code.app/Contents/Resources/app/bin/code\"\n```\n\n#### 都是减肥的时刻\n\n##### 但是解放军速度快\n\n###### 绝对是开发但是开发的是\n\n但是见风使舵见风使舵里伏牛山\n\n#### 都是减肥的时刻\n\n##### 但是解放军速度快\n\n###### 绝对是开发但是开发的是\n\n#### 都是减肥的时刻\n\n##### 但是解放军速度快\n\n###### 绝对是开发但是开发的是\n\n#### 都是减肥的时刻\n\n##### 但是解放军速度快\n\n###### 绝对是开发但是开发的是\n\n#### 都是减肥的时刻\n\n##### 但是解放军速度快\n\n###### 绝对是开发但是开发的是\n\n###### 绝对是开发但是开发的是\n\n#### 都是减肥的时刻\n\n##### 但是解放军速度快\n\n###### 绝对是开发但是开发的是\n\n#### 都是减肥的时刻\n\n##### 但是解放军速度快\n\n###### 绝对是开发但是开发的是\n', '<h2 id=\"介绍111\">介绍111</h2>\n<p><a href=\"http://www.pc6.com/mac\">主要软件来源</a></p>\n<h2 id=\"软件安装记录\">软件安装记录</h2>\n<p>对双方都舒服</p>\n<p>对双方都是粉色的</p>\n<p>输入法推荐使用百度输入法，可以定制化的比较多</p>\n<p>WiFi测速软件：Speedtest（AppStore中即可下载），尽管好用，用完赶紧删除掉即可（感觉很占用CPU）</p>\n<p>Markdown编辑器：<a href=\"http://www.pc6.com/mac/132924.html\">Typora</a></p>\n<p>浏览器：<a href=\"https://www.google.cn/chrome/\">谷歌浏览器</a></p>\n<p>翻墙：<a href=\"https://www.maczapp.com/shadowsocksx-ng\">ShadowsocksX-NG</a>（可以去GitHub上下载）</p>\n<p>SSH连接工具：<a href=\"http://www.hostbuf.com/\">FinalShell</a></p>\n<p>代码编辑器：<a href=\"https://code.visualstudio.com/\">VS Code</a></p>\n<p>Java开发编辑器：<a href=\"https://www.jetbrains.com/idea/download/\">Idea2019</a></p>\n<blockquote>\n<p><a href=\"https://blog.csdn.net/qq_34801169/article/details/95059368\">永久激活注册码</a></p>\n<p><a href=\"https://blog.csdn.net/m0_37862829/article/details/93920188\">激活码2</a></p>\n</blockquote>\n<p>截图软件：<a href=\"http://www.pc6.com/mac/542001.html\">Snipaste</a></p>\n<p>翻译：<a href=\"http://www.pc6.com/mac/110548.html\">有道翻译</a></p>\n<p>非常好用的程序员笔记软件：<a href=\"http://www.pc6.com/mac/130522.html\">Quiver</a>（有点慢，占用CPU）</p>\n<p>应用卸载软件：<a href=\"http://www.pc6.com/mac/267539.html\">App Uninstaller</a></p>\n<p>压缩软件：<a href=\"http://www.pc6.com/mac/629419.html\">eZip</a></p>\n<p>修改Mac上的键盘映射规则：<a href=\"https://mac.softpedia.com/get/System-Utilities/?utm_source=spd&amp;utm_campaign=postdl_redir\">Karabiner</a></p>\n<p>文件搜索工具：EasyFind（AppStore）</p>\n<p>谷歌浏览器代理插件：Proxy SwitchyOmega</p>\n<h3 id=\"非常好用的软件\">非常好用的软件</h3>\n<ol>\n<li>快速工具（效率，类似Listary）：<a href=\"https://download.csdn.net/download/sinat_29970905/11634147\">Alfred</a></li>\n<li><a href=\"https://www.maczapp.com/download.php?id=69188&amp;did=52828&amp;action=1573999300&amp;from=15045811251972\">SpaceLauncher</a><br />\n通过 <code>空格</code>加 <code>其他按键</code>来对应快捷键，如打开文件夹，打开应用等</li>\n<li>betterTouchTool<br />\n还是购买正版吧，这个是真的好用，两年需要59元</li>\n<li>测试test</li>\n<li><code>dsfjdskfjsdkfs</code></li>\n<li>对方接受短裤减肥快速的减肥速度快</li>\n</ol>\n<h3 id=\"开发相关\">开发相关</h3>\n<ol>\n<li>mysql环境\n<blockquote>\n<p><code>brew install mysql</code></p>\n</blockquote>\n</li>\n<li><a href=\"https://download.csdn.net/download/lc19850921/10957187\">Java-jdk8环境</a>：直接安装即可，不需要配置环境变量</li>\n<li><a href=\"http://nodejs.cn/download/\">Nodejs环境</a></li>\n<li><a href=\"https://git-scm.com/\">git</a></li>\n<li>maven(<code>brew install maven</code>)</li>\n<li>tomcat(<code>brew install tomcat</code></li>\n<li>test</li>\n</ol>\n<h3 id=\"配置环境变量\">配置环境变量</h3>\n<p>配置VsCode的环境变量：</p>\n<pre><code>alias code=&quot;/Applications/Visual\\ Studio\\ Code.app/Contents/Resources/app/bin/code&quot;\n</code></pre>\n<h4 id=\"都是减肥的时刻\">都是减肥的时刻</h4>\n<h5 id=\"但是解放军速度快\">但是解放军速度快</h5>\n<h6 id=\"绝对是开发但是开发的是\">绝对是开发但是开发的是</h6>\n<p>但是见风使舵见风使舵里伏牛山</p>\n<h4 id=\"都是减肥的时刻-\">都是减肥的时刻</h4>\n<h5 id=\"但是解放军速度快-\">但是解放军速度快</h5>\n<h6 id=\"绝对是开发但是开发的是-\">绝对是开发但是开发的是</h6>\n<h4 id=\"都是减肥的时刻--\">都是减肥的时刻</h4>\n<h5 id=\"但是解放军速度快--\">但是解放军速度快</h5>\n<h6 id=\"绝对是开发但是开发的是--\">绝对是开发但是开发的是</h6>\n<h4 id=\"都是减肥的时刻---\">都是减肥的时刻</h4>\n<h5 id=\"但是解放军速度快---\">但是解放军速度快</h5>\n<h6 id=\"绝对是开发但是开发的是---\">绝对是开发但是开发的是</h6>\n<h4 id=\"都是减肥的时刻----\">都是减肥的时刻</h4>\n<h5 id=\"但是解放军速度快----\">但是解放军速度快</h5>\n<h6 id=\"绝对是开发但是开发的是----\">绝对是开发但是开发的是</h6>\n<h6 id=\"绝对是开发但是开发的是-----\">绝对是开发但是开发的是</h6>\n<h4 id=\"都是减肥的时刻-----\">都是减肥的时刻</h4>\n<h5 id=\"但是解放军速度快-----\">但是解放军速度快</h5>\n<h6 id=\"绝对是开发但是开发的是------\">绝对是开发但是开发的是</h6>\n<h4 id=\"都是减肥的时刻------\">都是减肥的时刻</h4>\n<h5 id=\"但是解放军速度快------\">但是解放军速度快</h5>\n<h6 id=\"绝对是开发但是开发的是-------\">绝对是开发但是开发的是</h6>\n', '2020-06-25 07:40:50', '2020-06-28 16:52:22', NULL);
INSERT INTO `blog` VALUES (19, 'test1', 'djsfkdsjfdklsjfsdl\n。dsfjsdkjfkdslfs dfsd\n。;fsd\n。lf', '## djsfsdkf\n\ndjsfkdsjfdklsjfsdl\n\ndsfjsdkjfkdslfs dfsd\n\n;fsd\n\nlfdsfmsd\n\n### dsjfsdkfsd\n\nsdfjsdlkf\n\n## dsjfdskdsfjdsjfhjdsfhdsjfhdsjhfdsjfhdsjfhsjdkfhsdjkfhdskjfs\n\ndsjfdsfjs\n\n### dsjfsdkfsd\n', '<h2 id=\"djsfsdkf\">djsfsdkf</h2>\n<p>djsfkdsjfdklsjfsdl</p>\n<p>dsfjsdkjfkdslfs dfsd</p>\n<p>;fsd</p>\n<p>lfdsfmsd</p>\n<h3 id=\"dsjfsdkfsd\">dsjfsdkfsd</h3>\n<p>sdfjsdlkf</p>\n<h2 id=\"dsjfdskdsfjdsjfhjdsfhdsjfhdsjhfdsjfhdsjfhsjdkfhsdjkfhdskjfs\">dsjfdskdsfjdsjfhjdsfhdsjfhdsjhfdsjfhdsjfhsjdkfhsdjkfhdskjfs</h2>\n<p>dsjfdsfjs</p>\n<h3 id=\"dsjfsdkfsd-\">dsjfsdkfsd</h3>\n', '2020-06-26 19:21:23', '2020-06-28 00:34:28', NULL);
INSERT INTO `blog` VALUES (21, '草稿', '树型\n。', '树型\n\n- 编辑：[sdjfdsk](http://localhost:8081/tree/samples/02_configuration/08_editable.html)\n  - 不用写什么内容，直修改即可\n- 获取选中的内容[http://localhost:8081/tree/samples/03_usage/07_get_checked.html](http://localhost:8081/tree/samples/03_usage/07_get_checked.html)\n- 添加节点：[http://localhost:8081/tree/samples/03_usage/09_add.html](http://localhost:8081/tree/samples/03_usage/09_add.html)\n- 删除节点：[http://localhost:8081/tree/samples/03_usage/11_delete.html](http://localhost:8081/tree/samples/03_usage/11_delete.html)\n- 搜索节点：[http://localhost:8081/tree/samples/03_usage/12_filter.html](http://localhost:8081/tree/samples/03_usage/12_filter.html)\n- 排序节点：\n', '<p>树型</p>\n<ul>\n<li>编辑：<a href=\"http://localhost:8081/tree/samples/02_configuration/08_editable.html\">sdjfdsk</a>\n<ul>\n<li>不用写什么内容，直修改即可</li>\n</ul>\n</li>\n<li>获取选中的内容<a href=\"http://localhost:8081/tree/samples/03_usage/07_get_checked.html\">http://localhost:8081/tree/samples/03_usage/07_get_checked.html</a></li>\n<li>添加节点：<a href=\"http://localhost:8081/tree/samples/03_usage/09_add.html\">http://localhost:8081/tree/samples/03_usage/09_add.html</a></li>\n<li>删除节点：<a href=\"http://localhost:8081/tree/samples/03_usage/11_delete.html\">http://localhost:8081/tree/samples/03_usage/11_delete.html</a></li>\n<li>搜索节点：<a href=\"http://localhost:8081/tree/samples/03_usage/12_filter.html\">http://localhost:8081/tree/samples/03_usage/12_filter.html</a></li>\n<li>排序节点：</li>\n</ul>\n', '2020-06-26 20:21:57', '2020-06-28 17:48:39', NULL);
INSERT INTO `blog` VALUES (22, '关于', 'wqeqwedqwfcasfsafdqw的解放军速度离开就分手啊风刀霜剑法兰克福说放声大哭减肥肯定是啦f是卡德罗夫接受短裤浪费', NULL, NULL, '2020-06-26 20:25:54', '2020-06-26 20:39:08', NULL);
INSERT INTO `blog` VALUES (23, 'test', NULL, 'jdsfdskfjdsklfjlsdfjdskljasfjdskl\n\n\n\ndsfkldsfjklsd\n\n\n\nweuriewouriewo\n', '<p>jdsfdskfjdsklfjlsdfjdskljasfjdskl</p>\n<p>dsfkldsfjklsd</p>\n<p>weuriewouriewo</p>\n', '2020-06-27 00:30:00', '2020-06-27 00:30:11', NULL);
INSERT INTO `blog` VALUES (24, 'dsfs', NULL, NULL, NULL, '2020-06-27 15:20:19', '2020-06-27 15:20:19', NULL);
INSERT INTO `blog` VALUES (25, 'sdfsdfsd', NULL, NULL, NULL, '2020-06-27 15:25:40', '2020-06-27 15:25:40', NULL);
INSERT INTO `blog` VALUES (31, '阿里icons使用', NULL, '选择要使用的icons，添加到购物车\n\n在将购物车里面的icons添加到项目\n\n使用方式：\n\n- 第一种\n  - 使用font class\n  - 复制连接到html\n\n    ```html\n    <link rel=\"stylesheet\" href=\"http://at.alicdn.com/t/font_xxx.css\">\n    ```\n\n\n    使用\n', '<p>选择要使用的icons，添加到购物车</p>\n<p>在将购物车里面的icons添加到项目</p>\n<p>使用方式：</p>\n<ul>\n<li>第一种\n<ul>\n<li>\n<p>使用font class</p>\n</li>\n<li>\n<p>复制连接到html</p>\n<pre><code class=\"language-html\">&lt;link rel=&quot;stylesheet&quot; href=&quot;http://at.alicdn.com/t/font_xxx.css&quot;&gt;\n</code></pre>\n<p>使用</p>\n</li>\n</ul>\n</li>\n</ul>\n', '2020-06-27 17:54:40', '2020-06-27 17:57:38', NULL);
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `userName` varchar(20) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  `visitCount` int DEFAULT NULL COMMENT '博客的总访问次数',
  `tree` text COMMENT '记录blog的层级关系',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_userName_uindex` (`userName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
