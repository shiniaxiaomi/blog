<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="/css/bootstrap.min.css">

    <!-- ⚠️生产环境请指定版本号，如 https://cdn.jsdelivr.net/npm/vditor@x.x.x/dist... -->
    <link rel="stylesheet" href="/vditor/index.css" />
<#--    <script src="/vditor/index.min.js" defer></script>-->
    <script src="/vditor/js/method.min.js"></script>


</head>
<body>

<div id="toc"></div>

    <div id="vditor" class="vditor-reset ">

        <p><a href="http://mybatis.org/spring/zh/index.html">http://mybatis.org/spring/zh/index.html</a></p>
        <h2 id="历史原因">历史原因</h2>
        <p>在发布mybatis3.0之前，spring3.0就已经发布了，所以spring并没有整合mybatis3.0，所以是大家合力完成的mybatis-spring整合</p>
        <h2 id="如何与Spring整合">如何与Spring整合</h2>
        <p>添加jar包</p>
        <pre><code class="language-xml">&lt;dependency&gt;
  &lt;groupId&gt;org.mybatis&lt;/groupId&gt;
  &lt;artifactId&gt;mybatis-spring&lt;/artifactId&gt;
  &lt;version&gt;2.0.5&lt;/version&gt;
&lt;/dependency&gt;
</code></pre>
        <p>与spring整合就必须要在spring中存在两个类： <code>SqlSessionFactory</code>  和数据映射器类(即通过xml生成的mapper)</p>
        <h3 id="配置SqlSessionFactory">配置SqlSessionFactory</h3>
        <p>我们可以通过 SqlSessionFactoryBean 类来创建 SqlSessionFactory，通过以下方式配置即可</p>
        <pre><code class="language-java">@Bean
public SqlSessionFactory sqlSessionFactory() throws Exception {
  SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
  factoryBean.setDataSource(dataSource());
  return factoryBean.getObject();
}
</code></pre>
        <h3 id="配置mapper">配置mapper</h3>
        <p>首先定义一个接口</p>
        <pre><code class="language-java">public interface UserMapper {
  @Select(&quot;SELECT * FROM users WHERE id = userId&quot;)
  User getUser(@Param(&quot;userId&quot;) String userId);
}
</code></pre>
        <p>我们可以通过 <code>MapperFactoryBean</code>  类来生成对应的mapper，总共有两种方式：</p>
        <ol>
            <li>
                <p>xml的方式</p>
                <pre><code class="language-xml">&lt;bean id=&quot;userMapper&quot; class=&quot;org.mybatis.spring.mapper.MapperFactoryBean&quot;&gt;
    &lt;property name=&quot;mapperInterface&quot; value=&quot;org.mybatis.spring.sample.mapper.UserMapper&quot; /&gt;
    &lt;property name=&quot;sqlSessionFactory&quot; ref=&quot;sqlSessionFactory&quot; /&gt;
&lt;/bean&gt;
</code></pre>
                <p>指定对应的接口 和 sqlSessionFactory 即可</p>
            </li>
            <li>
                <p>注解的方式</p>
                <pre><code class="language-java">@Bean
public UserMapper userMapper() throws Exception {
    SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory());
    return sqlSessionTemplate.getMapper(UserMapper.class);
}
</code></pre>
                <p>通过SqlSessionTemplate来获取对应的mapper</p>
            </li>
        </ol>
        <hr />
        <h3 id="使用">使用</h3>
        <pre><code class="language-java">@Service
public class FooServiceImpl implements FooService {

  @Autowired
  private final UserMapper userMapper;

  public User doSomeBusinessStuff(String userId) {
    return userMapper.getUser(userId);
  }
}
</code></pre>
        <h2 id="SqlSessionFactoryBean">SqlSessionFactoryBean</h2>
        <p>在基础的Mybatis中，是通过SqlSessionFactoryBuilder根据xml配置文件来创建 <code>SqlSessionFactory</code>  的</p>
        <p>而在mybatis-spring中，则使用SqlSessionFactoryBean来创建</p>
        <hr />
        <p>SqlSessionFactoryBean实现了 <code>FactoryBean</code>  接口，那么在spring中创建的bean就并不是</p>
        <p>SqlSessionFactoryBean本身，而是实现了 <code>FactoryBean</code>  接口中的getObject()方法的返回值， 这种情况下，Spring 将会在应用启动时为你创建 <code>SqlSessionFactory</code>，并使用 <code>sqlSessionFactory</code> 这个名字存储起来。</p>
        <blockquote>
            <p>参考spring官网：<a href="https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-extension-factorybean">https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-extension-factorybean</a></p>
        </blockquote>
        <p>所以，</p>


    </div>

</body>
</html>

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="/js/jquery.min.js"></script>
<script src="/js/bootstrap.min.js"></script>

<script>

    $(function () {
        // new Vditor('vditor', undefined);
        var buf=document.getElementById("vditor");
        // Vditor.mermaidRender(buf);//
        Vditor.highlightRender({},buf);//渲染代码高亮
        Vditor.codeRender(buf);//渲染代码复制
        Vditor.outlineRender(buf,document.getElementById("toc"));//渲染大纲到指定dom


        var arrs=document.getElementsByTagName("h2");
        for(var i=0;i<arrs.length;i++){
            var node = document.createElement("button");
            node.innerText="编辑";
            arrs[i].appendChild(node);
        }
       


    })

</script>
