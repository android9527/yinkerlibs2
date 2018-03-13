##  数据埋点SDK

### 前言

* 此SDK只提供数据的采集及上传功能，对采集数据的具体格式不做要求和处理
* 数据采用GZIP压缩后上传
* 所有涉及到数据库操作及网络操作均在异步任务中处理

### 模块分析

* 网络模块
	* http协议 
	* 是针对原生的 HttpURLConnection 进行的简单封装（异步）
* 异步任务
	* 采用ThreadPoolExecutor线程池处理
	* 核心线程数为1，最大线程数为5，额外线程数为10。
* 数据库
	* 采用android标准的SQLite轻量级数据库
* Json解析
	* 使用Google gson-2.6.2 json库
	
### API介绍

* BuriedSDKMgr单利模式，提供三个方法
	*  init（String appKey）初始化appKey
	*  uploadModule()上传本地采集的数据
	*  saveModule（String key,String moduleStr）保存采集数据，key代表模块名，moduleStr代表本模块采集的数据

### 配置信息

* 目前不提供对外配置API,相关配置信息只能在SDK内部配置，目前日志开关为打开，生产环境是手动关闭

### 遗留问题

* 埋点基础数据采集依托于app
* 优化鉴权失败后上传数据
* 鉴权失败三次的处理
* 配置信息的对外开放