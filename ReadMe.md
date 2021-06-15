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

​     

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

当 Activity 进入“已开始”状态时，系统会调用此回调。`onStart()` 调用使 Activity 对用户可见，因为应用会为 Activity 进入前台并支持互动做准备。例如，应用通过此方法来初始化维护界面的代码。概括一下就是可见但不可交互。真正的交互要到下一步`onResume`才会执行。

在这里Activity正式进入了可见状态，也可以反过来理解，当Activity进入可见状态（前台）就会调用`onStart()`。在这里可以进行UI的渲染（那些`onCreate()`无法重新渲染的问题）。



### `onResume()`

这时`Activity`已进入了可交互的阶段。

Activity 会在进入“已恢复”状态时来到前台，然后系统调用 `onResume()` 回调。这是应用与用户互动的状态。应用会一直保持这种状态，直到某些事件发生，让焦点远离应用。此类事件包括接到来电、用户导航到另一个 Activity，或设备屏幕关闭。

举个简单的例子，一个页面具有三个`editText`,当我们不进行任何指定时，我们在输入时并不知道输入的是哪个框，而通过焦点我们能够选择哪个被我们编辑的框。





Activity中的` handleResumeActivity()`.通过对下面这段代码的分析我们可以了解到其实是在`Resume`阶段才完成了UI的渲染。

```java
    @Override
    public void handleResumeActivity() {
        //onResume
        final ActivityClientRecord r = performResumeActivity(token, finalStateRequest, reason);
        //addView
        if (r.window == null && !a.mFinished && willBeVisible) {
            wm.addView(decor, l);
        }
    }
```



**什么时候会调用他**

1. 当再次回到这个Activity,且这个Activity已被创建。此时会按照`onStart->onResume`的顺序执行

2. 如果 Activity 从“已暂停”状态返回“已恢复”状态，系统将再次调用 `onResume()` 方法。因此，您应实现 `onResume()`，以初始化在 [`onPause()`](https://developer.android.google.cn/reference/android/app/Activity?hl=zh-cn#onPause()) 期间释放的组件，并执行每次 Activity 进入“已恢复”状态时必须完成的任何其他初始化操作。

3. 当 Activity 进入已恢复状态时，与 Activity 生命周期相关联的所有生命周期感知型组件都将收到 [`ON_RESUME`](https://developer.android.google.cn/reference/androidx/lifecycle/Lifecycle.Event?hl=zh-cn#ON_RESUME) 事件。这时，生命周期组件可以启用在组件可见且位于前台时需要运行的任何功能，例如启动相机预览。

4. 当发生中断事件时，Activity 进入“已暂停”状态，系统调用 `onPause()` 回调。

   且会先对当前的Activity进行`onPause（）`再去执行下一个Activity的`onResume()`

   

   ## Handler

   `Handler`是一个消息分发对象，进行发送和处理消息。主要用于线程之间的通信，举个具体的例子：由于子线程无法更新UI所以，在子线程中通过`Message.obtain()`(用户获取消息池，和传统的new 方法相比更节省资源)获取一个消息池。并发送。而主线程中则通过`Looper.loop()`来获取消息并做进一步的处理。

   #### Handler通信机制

   

   ![img](https://upload-images.jianshu.io/upload_images/1289721-b355ff5279721171.png?imageMogr2/auto-orient/strip|imageView2/2/format/webp)



且`MessageQueue`与`Looper`和线程都是一对一的关系。在`Looper`对象中通过`sThreadLocal`就可以找到其绑定的线程,在调用了`Looper.prepare()`方法之后，当前线程和`Looper`就进行了双向的绑定。而`Looper.loop()`是真的将消息队列与Looper结合在一起

```java
public static void loop() {
        final Looper me = myLooper();
        if (me == null) {
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        //注意下面这行
        final MessageQueue queue = me.mQueue;

        // Make sure the identity of this thread is that of the local process,
        // and keep track of what that identity token actually is.
        Binder.clearCallingIdentity();
        final long ident = Binder.clearCallingIdentity();

        //注意下面这行
        for (;;) {
            //注意下面这行
            Message msg = queue.next(); // might block
            if (msg == null) {
                // No message indicates that the message queue is quitting.
                return;
            }

            // This must be in a local variable, in case a UI event sets the logger
            Printer logging = me.mLogging;
            if (logging != null) {
                logging.println(">>>>> Dispatching to " + msg.target + " " +
                        msg.callback + ": " + msg.what);
            }

            //注意下面这行
            msg.target.dispatchMessage(msg);

            if (logging != null) {
                logging.println("<<<<< Finished to " + msg.target + " " + msg.callback);
            }

            // Make sure that during the course of dispatching the
            // identity of the thread wasn't corrupted.
            final long newIdent = Binder.clearCallingIdentity();
            if (ident != newIdent) {
                Log.wtf(TAG, "Thread identity changed from 0x"
                        + Long.toHexString(ident) + " to 0x"
                        + Long.toHexString(newIdent) + " while dispatching to "
                        + msg.target.getClass().getName() + " "
                        + msg.callback + " what=" + msg.what);
            }

            msg.recycleUnchecked();
        }
}
```

上述是`Looper.loop`的源代码。我们可以从中了解到如何处理消息：通过`queue.next()`来提取消息。若这个消息为空则进行阻塞。





### `WeakReference`与`SoftReference`

我在安卓自动生成的一些代码中发现了**软引用**。同时在处理Handler的内存泄露问题时使用到了**弱引用**。那么这些引用有什么用呢？

其实和这些引用直接挂钩的是垃圾回收机制。

> 如果一个对象持有软引用，那么如果内存空间足够，垃圾回收器就不会回收它；如果内存空间不足了，就会回收这些对象的内存。只要垃圾回收器没有回收它，该对象就可以被程序使用。
>
> 如果一个对象持有弱引用，那么在垃圾回收器线程扫描的过程中，一旦发现了只具有弱引用的对象，不管当前内存空间足够与否，都会回收它的内存。不过，由于垃圾回收器是一个优先级很低的线程，因此不一定会很快发现那些只具有弱引用的对象。



### Volley异步网络请求框架



Volley 是 Google 推出的 Android 异步网络请求框架和图片加载框架。

#### Volley 的主要特点

(1). 扩展性强。Volley 中大多是基于接口的设计，可配置性强。
 (2). 一定程度符合 `Http` 规范，包括返回` ResponseCode(2xx、3xx、4xx、5xx）`的处理，请求头的处理，缓存机制的支持等。并支持重试及优先级定义。
 (3). 默认 `Android2.3 `及以上基于 `HttpURLConnection`，2.3 以下基于 `HttpClient `实现，这两者的区别及优劣在4.2.1 Volley中具体介绍。
 (4). 提供简便的图片加载工具。



我们将Volley进行了二次封装以便我们更好的调用。我们实现了基于`懒汉模式双重锁机制`的获取实例方法。

而Volley的调用则十分简单。首先我们需要一个`RequestQueue`类型的对象，`RequestQueue`是一个请求队列对象，它可以缓存所有的`HTTP`请求，然后按照一定的算法并发地发出这些请求。而后根据URL和一些获取结果后的回调新建一个`Request`类的对象。并将这个对象加入`RequestQueue`。





### 签名

`签名`在` APK` 中写入一个「指纹」。指纹写入以后，`APK `中有任何修改，都会导致这个指纹无效，Android 系统在安装 `APK` 进行签名校验时就会不通过，从而保证了安全性。



### 华为推送服务

其主要是通过Service常驻的特点来检测是否有消息。其内部其实也是通过Handler实现的。

推送服务主要支持两种消息一种为常规的通知栏消息，另一种为透传消息。我们使用的是后者。

**透传消息**是开发者负责处理的消息，透传消息可自定义更多推送样式，从而帮助开发者更灵活地使用消息推送通道。终端设备收到透传消息后不直接展示，而是将数据传递给应用，由您的应用自主解析内容，并触发相关动作。

透传消息的到达率受Android系统和应用是否驻留在后台影响，华为推送服务不保证透传消息的高到达率。

透传消息的常用场景：好友邀请、VoIP呼叫、语音播报等。

在本项目中我们使用了**透传消息**来实现好友添加的功能。通过`下行消息`（开发者通过调用华为推送服务REST API，向终端设备发送的消息。）向终端发送一个带有用户名的消息。客户端在接收到消息后。会对消息进行处理。这里的处理指的是判断消息的类型，并通过广播的形式向其他Fragment或Activity发送消息。而接受者通过消息内容显示一个对话框。并进行相应的逻辑处理。



### Context

Android的应用模型是基于组件的应用设计模式，组件能够运行需要一个完整的工程环境。同时，各个组件拥有自己独立的场景(Context)，并不能采用new的方式创建一个组件对象。Context是维持Android程序中各组件能够正常工作的一个核心功能类。Context提供了关于应用环境全局信息的接口。它是一个抽象类，它的执行被Android系统所提供。它允许获取以应用为特征的资源和类型，是一个统领一些资源（应用程序环境变量等）的上下文。通过它我们可以获取应用程序的资源和类（包括应用级别操作，如启动Activity，发广播，接受Intent等）。



并且我们常用的Activity与Service均继承自Context。但是将Service和Activity作为Context时要考虑内存泄露的问题。

|                               | Application | Activity | Service |
| :---------------------------: | :---------: | :------: | :-----: |
|          show dialog          |      N      |    Y     |    N    |
|        start Activity         |      N      |    Y     |    N    |
|       Layout Inflation        |      N      |    Y     |    N    |
|         start service         |      Y      |    Y     |    Y    |
|       send a BroadCast        |      Y      |    Y     |    Y    |
| register a BroadCast Receiver |      Y      |    Y     |    Y    |
|         load Resource         |      Y      |    Y     |    Y    |



在启动一个Activity和Dialog时，不推荐使用Application和Service。Android系统出于安全原因的考虑，是不允许Activity或Dialog凭空出现的，一个Activity的启动必须要建立在另一个Activity的基础之上，也就是以此形成的返回栈。而Dialog则必须在一个Activity上面弹出（除非是System Alert类型的Dialog），因此在这种场景下，我们只能使用Activity类型的Context。且与UI相关的都推荐用Activity的Context。

### `FragmentActivity`与`Activity`与`AppCompatActivity`

`FragmentActivity`继承自`Activity`。`FragmentActivity`可以通过`getActivity()`调用其所在的`Activity`。而`Activity`可以通过`getFragmentManager`方法来控制`Activity`和`Fragment`之间的交互。`AppCompatActivity`继承自`FragmentActivity`。通过`AppCompatDelegate`来扩展`Activity.AppCompatDelegate`是一个类似委托用来扩展`AppCompat`的类。（夜间模式）.

###  Intent

Android中提供了Intent机制来协助应用间的交互与通讯。同时其也可以用于Activity与Service内部的信息传递，同时也能用于Activity与Service之间的信息交换。其也可以`broadcaseIntent()`来传递给所有感兴趣的`BroadcaseReceiver。`



启动Intend主要有两种方式一种是指定目标Activity的显示启动，另一种是不明确指定启动哪个Activity而是通过Action在<intent-filter>中去匹配最合适的Activity.



Intent对象大致包括7大属性：Action（动作）、Data（数据）、Category（类别）、Type（数据类型）、Component（组件）、Extra（扩展信息）、Flag（标志位）。其中最常用的是Action属性和Data属性。

Action用来指定动作，Data用来指定传递的数据信息。Extra则用来传递其他信息。

### Service生命周期

![img](https://developer.android.com/images/service_lifecycle.png?hl=zh-cn)

先放一张官方的Service生命周期。Service有两种启动方式，一种是`startService()``startService()`在启动时会先调用`onCreate()`而后调用`onStartCommand()`。其在每次`Service`被启动时都会判断其是否是第一次调用`startService()`若不为第一次则会跳过`onCreate()`去执行`onStartCommand`.



`bindService()`在每次Service被绑定是都会判断是否第一次调用`bindService()`若不为第一次则会跳过`onCreate()`去执行`onBind()`。若已经绑定也会跳过`onBind()`;`bindService`与`startService`不仅仅是在生命周期上有一定的区别。前者会经常用于获取Service运行的状态和数据。且需要注意的是在`bind`时需要指定其依附的`Contex`t。可以通过`getApplicationContext`或者`Activity`的`this`。但这可能会导致内存泄露
### `AIDL`
`Android `接口定义语言 (`AIDL`) 与您可能使用过的其他接口语言 `(IDL)` 类似。您可以利用它定义客户端与服务均认可的编程接口，以便二者使用进程间通信 (`IPC`) 进行相互通信。
远程绑定与本地的绑定的最大区别就是远程绑定是另一个进程，且其必须被启动。其是一个CS结构。
服务端需要做以下工作

1. 创建 `.aidl `文件
   此文件定义带有方法签名的编程接口。对项目进行build
2. 实现接口
   Android` SDK` 工具会基于您的` .aidl` 文件，使用 Java 编程语言生成接口。此接口拥有一个名为` Stub` 的内部抽象类，用于扩展 Binder 类并实现 `AIDL` 接口中的方法。您必须扩展 Stub 类并实现这些方法。
3. 向客户端公开接口
   实现` Service `并重写 `onBind()`，从而返回 `Stub` 类的实现。
4. 在配置文件中定义`Service`并启动当前`APP.`
   **客户端需要做以下工作：**
   1. 在项目的`src/`目录中加入`.aidl`文件。
   2. 声明一个`IBinder`接口实例（基于 `AIDL `生成）。
   3. 实现 `ServiceConnection`。
   4. 调用`Context.bindService()`，从而传入您的` ServiceConnection` 实现。
   5. 在`onServiceConnected()`实现中，您将收到一个`IBinder`实例（名为service）。调用`YourInterfaceName.Stub.asInterface((IBinder)service)`，以将返回的参数转换为`YourInterface`类型。
   6. 调用您在接口上定义的方法。您应始终捕获` DeadObjectException `异常，系统会在连接中断时抛出此异常。您还应捕获 `SecurityException `异常，当 `IPC` 方法调用中两个进程的` AIDL` 定义发生冲突时，系统会抛出此异常。
      如要断开连接，请使用您的接口实例调用` Context.unbindService()`

