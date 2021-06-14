# 安卓课设特点

## 广播的大量使用

​		`BroadcastReceiver`是android 系统的四大组件之一，本质上就是一个全局的监听器，用于监听系统全局的广播消息，可以方便的实现系统中不同组件之间的通信。

​		广播主要分为两类一类为动态广播，一类为静态广播。

### 动态注册广播

​		动态注册步骤

1.  发送方新建一个intent，同时指定其`action`,发送方也可以通过`putExtra(things)`存放变量，`things`可以为传统的int或者String，也可以为对象，但对象必须继承`Serializable`类。`sendReceiver(intent)`来发送广播。

2.  接受方需要实现自定义广播`myReceiver`。其继承`BroadcastReceiver` 实现`OnReceiver()`方法 ，方法中有两个参数上下文`context` 和`intent`，这里主要是对intent进行筛选`intent.getAction(actionName)`.`actionName`主要是在注册方进行新建 Intent的时候指定。

3.  注册广播`registerReceiver(myReceiver,intentFilter)` 一般是在`onResume`注册 ，当`Activity`或者`Fragment`销毁的时候应该销毁该广播`ungisterReceiver(myReceiver)`。其销毁时机一般是`onPause`。

   

   **如果不销毁注册会发生什么呢?**。会导致内存泄露哦，同时如果在`onDestory`里注销的话可能不会被执行。在`Activity`的生命周期，当资源不足的时候会回收而回收是不会执行`onStop`与`onDestory`的。

     

   **`intentFilter`**的意思就是`意图过滤器`,当我们隐式的启动系统组件的时候，就会根据`IntentFilter`来筛选出合适的进行启动。

   同时可以在`Intent`启动的时候对应设置`Action`、`Category`、`DataAndType`，这里设置的是为了过滤的时候对应`IntentFilter`匹配`action`、`category`、`data`。需要注意的是除了广播的`IntentFilter`是在代码中注册的，其与都是在`AndroidManifest.xml`中通过`<intent-filter> `属性来进行注册。值得注意的是，一个组件可以有多个`IntentFilter`，在过滤的时候只要有一个符合要求的，就会被视为过滤通过。

   ### 静态注册广播

   静态注册的主要步骤是

   1. 写一个`Receiver`继承`BroadcastReceiver`

   2. 在安卓的`AndroidManifest.xml`注册这个广播

   3. 通过`sendReceiver(intent)`发送广播

      

   **安卓8.0(API27)的限制:** 静态广播（应用内）需要为Intent设置`setClassName(String)`。因为安全问题也推出了`LocalBroadcastManager`局部通知管理器，这种通知的好处是安全性高，效率也高，适合局部通信，且只在应用内部进行通信。

   

   ### 静态与动态总结

   1.  动态注册广播不是常驻型广播，也就是说广播跟随Activity的生命周期。注意在Activity结束前，移除广播接收器。

   静态注册是常驻型，也就是说当应用程序关闭后，如果有信息广播来，程序也会被系统调用自动运行。

   2.  当广播为有序广播时：*优先级高的先接收*<u>可以通过这个特性来拦截一些广播</u>（不分静态和动态）。同优先级的广播接收器，动态优先于静态。
   3.  同优先级的同类广播接收器，静态：先扫描的优先于后扫描的，动态：先注册的优先于后注册的。
   4.  当广播为默认广播时：无视优先级，动态广播接收器优先于静态广播接收器。同优先级的同类广播接收器，静态：先扫描的优先于后扫描的，动态：先注册的优先于后册的。

   

   ### `startActivityForResult()`

   这也是一种组件之间通信的方式。

   ```java
   public void startActivityForResult (Intent intent, 
                   int requestCode)
   ```

   他的两个参数，第一个参数指定Intent。通过Intent可以指定目标的`Activity`。`requestCode`为请求码当其>=0的时候会在`onActivityResult`获取这个值。

   在Intent中指定的目标`Activity`可以通过`this.setResult(RESULT_OK, intent)`来设置返回码;RESULT_OK为自定义常量也就是一个返回码。

   最后在目标`Activity`中通过`this.finish()`来关闭这个`Activity`并返回到原来的`Activity`。在原来`Activity`中通过`onActivityResult(int requestCode, int resultCode, Intent data)`来对返回值进行处理

   **在使用`startActivityForResult中遇到的问题`**

   ​	如果在Fragment里面使用这个函数会被父`Activity`所拦截。

   同时这个接口在`API29`中已经被废弃，有了更新的`Activity Results API`。应本人没对其做更多的了解，故不展开解释。

   **`startActivityForResult`和广播哪个更香？**

   ​	一开始我以为都是组件间的通信为啥不用广播呢。后面了解到广播他有一个超时机制，在前台广播其超时时间为*10s*，后台广播为*60s*因此不用其处理复杂逻辑，广播设计的初衷似乎也是为了更新UI。但是在实际编程中发现广播的自由度显然是高于前者的。

   

   

## 生命周期的再次理解

​    

![生命周期](https://developer.android.google.cn/guide/components/images/activity_lifecycle.png?hl=zh-cn)

接下来我们对每一个生命周期的职责再次进行重复，并将对一些我所遇到的问题和解决方案进行深入的展开。

### `onCreate()`

`onCreate()` 方法一般用来初始化某些变量，例如声明界面（在 XML 布局文件中定义）、定义成员变量，以及配置某些界面。

#### `onCreate()`中的关键变量和函数

##### `savedInstanceState`

这是一个`bundle类型`的变量，`bundle`类型其实是一种类似Map的用来存储K,V对的一种数据结构。在阅读源码后发现其内部用来存储K,V的也正是`ArrayMap<String, Object>`类型的变量。同时我们在使用广播时也正是使用这个完成了组件间的通信。



**`savedInstanceState`有啥用？**他主要是在`Activity`结束之前可以通过调用`onsaveInstanceState`来保存一些简短的信息。

且这个保存到的信息应该不是存在进程中的，因为其在应用被关闭后仍能获取到这个变量。

##### `setContentView`

其主要是用来将View加载到根View上去。本质是为了给View分配内存。阅读源码后发现了一个比较有趣的点。`setContentView`其实是被一个`Window`类型的变量所调用。这个**`Window`又是啥呢?**



在Android的window机制中，每个`view树`（就是我们写的Layout文件中的结构）都可以看成一个`window`。可以理解成**view是window的存在形式，window是view的载体**。现在理清了Window和View,那`Activity`呢?

Window 有三种类型，分别是**应用 Window**、**子 Window** 和**系统 Window**。应用类 Window 对应一个 `Acitivity`，子 Window 不能单独存在，需要依附在特定的父 Window 中（我们大量使用的Dialog其实就是子Window）。

总结一下：Activity在初始化时生成了一个Window类型的实例，然后在执行Activity的`setContentView()时`，调用了`Window实例`的`setContentView()`



#####  资源类R类

当 Android 应用程序被编译，会自动生成一个 R 类，其中包含了所有 `res/ `目录下资源的 ID，如布局文件，资源文件，图片（values下所有文件）的ID等。在需要使用这些资源的时候就可以使用R类来进行调用。这些可供调用的也都是int类型的静态常量。*（亲身踩坑，在`gradle5.0`以后其取消了`final`关键字将资源文件变成了变量，用SWITCH语句可能会报错）*。再来瞅瞅这个东西是怎么生成的。

`这些资源索引由Android的工具AAPT（Android Asset Packing Tool）生成的八位十六进制整数型。`

![img](https://img-blog.csdnimg.cn/20190807144733138.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTg4Mjc0,size_16,color_FFFFFF,t_70)

需要注意的是如果res目录中的某个资源在应用中没有被使用到，在该应用被编译的时候系统就不会把对应的资源编译到该应用的`APK`包中。



### `onStart()`

当 Activity 进入“已开始”状态时，系统会调用此回调。`onStart()` 调用使 Activity 对用户可见，因为应用会为 Activity 进入前台并支持互动做准备。例如，应用通过此方法来初始化维护界面的代码。

在这里Activity正式进入了可见状态，也可以反过来理解，当Activity进入可见状态（前台）就会调用`onStart()`。在这里可以进行UI的渲染（那些onCreate()无法重新渲染的问题）。

