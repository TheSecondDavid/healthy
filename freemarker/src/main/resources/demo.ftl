<html>
<head>
    <meta charset="utf-8">
    <title>Freemarker入门</title>
</head>
<body>
    <#include "head.ftl">
    <#assign linkman="周先生">
    <#assign info={"mobile":"13812345678",'address':'北京市昌平区'}>
    联系人：${linkman}
    <br/>
    电话：${info.mobile}  地址：${info.address}
    <br/>
    <#--我只是一个注释，我不会有任何输出  -->
    ${name}你好，${message}
    <br/>
    <#if success=true>
        认证成功
    <#else>
        您好，您未实名认证
    </#if>
    <#list goods as good>
        <br/>
        商品名称： ${good.name}  价格：${good.price}
    </#list>
</body>
</html>
