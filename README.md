# PageStateManager
页面状态管理

### 简介

为页面提供简便添加状态布局，例如常见的 加载失败状态、加载为空状态、加载出错状态等等，需要自定义多状态layout布局，不干扰主页面的layout布局.
- 支持Activity 和Fragment 中任意内容布局替换为其他状态布局。
- 支持多状态替换。默认有三种【加载中状态、加载失败状态、空数据状态】，支持添加自定义状态
- 支持多状态公用一个布局。可以有效减少View绘制，支持显示状态布局时候，修改状态布局中的控件
- 异步解析生成状态布局，有效提高视图显示效率

### 使导入
加入到dependencies：
```
 implementation 'cn.blinkdagger:PageStateManage:1.1' 
```

在Activity 或者Fragment 中使用：
```
  //声明一个PageStateManager对象
 private var pageStateManager : PageStateManager? = null
 
 //在activity的setContentView() 之后， 或者在Fragmentde onViewCreated()之中初始化PageStateManager对象
 
 pageStateManager = PageStateMachine.with(this)
            .setContentViewId(R.id.ll_content) // 设置内容View的Id，如果不设置将会把内容View默认为根布局
            .setShowLoadingWhenCreate(true)// 设置是否在初始化的时候显示加载中 
            .setLoadingLayout(R.layout.layout_content_loading) //设置加载中状态布局资源ID
            .setFailedLayout(R.layout.layout_load_failed) //设置加载失败状态布局资源ID
            .setEmptyLayout(R.layout.layout_load_empty)//设置空数据状态布局资源ID
            .addCustomStateLayout(stateCode, R.layout.layout_load_failed)//增加自定义状态布局
            .setShowStateListener(this)//设置显示状态的时候监听
            .get()
          
 // 显示加载中  
 pageStateManager?.showLoading()

 // 显示空状态
 pageStateManager?.showLoadEmpty()
 
 // 显示加载失败
 pageStateManager?.showLoadFailed()
 
 // 显示加载成功
 pageStateManager?.showContent()
 
 // 显示自定义的状态,参数stateCode 为自定义的状态码
 pageStateManager?.showCustomStateView(stateCode)

       
```




