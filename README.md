# Rxjava-Retrofit-Gloabl_Token_Rresh
集成了rxjava和Retrofit的环信接口实现，实现全局token重试，当token失效以后会刷新token并重试，并简单封装了rxjava的订阅者，在service.impl中实际上就等于是
集成的测试了，并且有后台中很多都用fastJson来做Json转换的，所以自己实现了一个converter，详情见`FastJsonConverterFactory`,
`FastJsonRequestBodyConverter`,`FastJsonResponseBodyConverter`这三个类
## service代码
```
SimpleSubscriber<Map> simpleSubscriber = new SimpleSubscriber<>();//生成一个简单订阅者，这个订阅者返回的是一个map
huanxinApiService.register(new HuanxinRegisterDto(uid.toString(), password)).subscribe(simpleSubscriber);
return !simpleSubscriber.getResponse().containsKey("error");//如果返回的map中有error字段证明调用接口出错
```
## 代码阅读
所有的集成工作都在retrofit这个包中，重点看`HuanxinApiServiceProvider`这个类主要是采用动态代理为生成的所有可订阅者设置一个retryWhen,本次所解决的
全局token刷新就是在这个基础上进行的。
>如有什么问题欢迎提出issue，或者邮件我li_bing_lin@163.com
