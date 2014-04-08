## Introduction

集结最新主流时尚开源技术的面向企业级Web应用的基础开发框架，提供一个J2EE相关主流开源技术架构整合及一些企业应用基础通用功能和组件的设计实现的最佳实践和原型参考。

以下两个站点代码和文档同步更新，请自由选择一个访问速度较快的站点即可：

### 项目托管同步更新站点列表：

**GitHub.com**

* 代码托管：  https://github.com/xautlx/s2jh 
* 项目文档：  https://github.com/xautlx/s2jh/wiki/Index

**OsChina.net**

* 代码托管：  http://git.oschina.net/xautlx/s2jh 
* 项目文档：   http://git.oschina.net/xautlx/s2jh/wikis/Index

### 个人空间： http://my.oschina.net/s2jh

> 欢迎关注的朋友在各大项目推广平台以提问、分享、收藏、点赞、投票等多种形式推广支持项目，以便更多人共同关注、问题反馈、功能优化等，促进整个项目质量不断提升

> **提示**：为了方便用户区分链接资源是外部和内部，文档特别以![link](https://raw.github.com/wiki/xautlx/s2jh/images/link.gif)标识：链接前面有此图标说明是外部链接，如果你已经熟悉相关概念可以忽略点击；没有此标识说明是文档内部链接，建议点击访问以完整查阅项目文档。

## Features

* 面向主流企业级WEB应用系统的界面和常用基础功能设计实现
* 主体基于主流的SSH（Struts2+Spring3+Hibernate4）架构
* 引入JPA、Spring-Data-JPA提升持久层架构规范性和开发效率
* 基于流行JQuery/Bootstrap等UI框架和插件整合，良好的浏览器兼容性和移动设备访问支持
* 提供一个基础分模块的代码生成框架，简化实现基本的CRUD功能开发
* 基于Maven的项目和组件依赖管理模式，便捷高效的与持续集成开发集成

## Quick Start

* [About License](License) - 关于开源的一点点说明
* [UI Experience](UI) - 第一感觉，UI交互界面设计体验
* [Executable War](ExecutableWAR) - 想进一步体验？下载独立运行封装包(75M左右)，本地一键快速启动运行应用

## Architecture

* [Technical List](TechList) - 框架主要技术(Java/Web/Tool)组件列表介绍
* [Enhanced Grid](Grid) - 功能强大的Grid表格组件扩展增强
* [Technical Features](TechFeature) - 主要技术选型和设计说明
* [Mobile Support](Mobile) - 以Android为例的Web App与Native App整合应用
* [RoadMap](RoadMap) - 粗略的开发框架路线计划

### Snapshot

![Snapshot View](https://raw.github.com/wiki/xautlx/s2jh/images/index1.gif)

![Snapshot View](https://raw.github.com/wiki/xautlx/s2jh/images/index2.gif)


> 项目最新自动化持续集成构建状态 Travis-CI Status: [![Build Status](https://travis-ci.org/xautlx/s2jh.png?branch=master)](https://travis-ci.org/xautlx/s2jh)

## Reference

* [关于作者](https://github.com/xautlx/s2jh/wiki/AboutAuthor)
* [SpringSide](https://github.com/springside/) - “SpringSide是以Spring Framework为核心的，Pragmatic风格的JavaEE应用参考示例，是JavaEE世界中的主流技术选型，最佳实践的总结与演示”。
本框架其中一些技术选型和整合也充分借鉴参考了SpringSide的最佳实践，如果说SpringSide是一把精巧多功能的瑞士军刀，那可以理解本框架目标是以此雕刻出一个面向企业应用开发的半成品工程。
* [Nutch Htmlunit Plugin](https://github.com/xautlx/nutch-htmlunit) - 作者项目：基于Htmlunit扩展的Nutch支持AJAX页面抓取插件
* [12306 Hunter](https://github.com/xautlx/12306-hunter) - 作者项目：Java Swing C/S版本12306订票助手，用处你懂的
