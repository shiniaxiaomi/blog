# blog-v5.0
第五版个人博客

todo
1. 新建一个admin管理页面，左侧是一个blog的文件夹树状结构
（最顶上有一个tag按钮，管理对应blog的tag），
右侧是对应的blog的相关的编辑内容

---

初始化数据库：
1. 手动创建一个用户
    ```sql
    insert into user (name,password,tree) values ("luyingjie","1","[]");
    ```
2. 手动创建一个草稿blog
3. 手动创建一个根blog（待整理），作为一个根目录

