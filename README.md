# Maven internationalization plugin

Maven 国际化插件
> author &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;qkmango 芒果小洛<br/>
> homepage [http://qkmango.cn](http://qkmango.cn)

![logo](README/logo.svg)

## 1. 介绍 Introduction

Maven 国际化插件是一个 Maven 插件，它可以帮助你将你的项目国际化。

Maven internationalization plugin is a Maven plugin that can help you internationalize your project.

## 2. 使用方法 Usage

### 2.1. 安装 Install

在你的项目的 pom.xml 文件中添加如下依赖

````xml
<!--项目没有发布到 Maven 你可以下载源码进行安装以使用插件-->
<build>
    <plugins>
        <plugin>
            <groupId>cn.qkmango</groupId>
            <artifactId>maven-internationalization-plugin</artifactId>
            <version>1.0</version>
            <configuration>
                <srcBasePath>${project.basedir}/src/main/webapp/test</srcBasePath>
                <zhDestBasePath>${project.basedir}/target/zh</zhDestBasePath>
                <enDestBasePath>${project.basedir}/target/en</enDestBasePath>
                <fileSuffix>.html</fileSuffix>
            </configuration>
        </plugin>
    </plugins>
</build>
````

| 参数             | 接受               | 缺省      |
|----------------|------------------|---------|
| srcBasePath    | 需要配置需要国际化的文件路径   | 不允许     |
| zhDestBasePath | 需要配置中文文件的输出路径    | 不允许     |
| enDestBasePath | 需要配置英文文件的输出路径    | 不允许     |
| fileSuffix     | 需要配置需要国际化的文件的后缀名 | `.html` |

### 2.2. 配置 Configure

国际化的属性文件 `properties` 要放在需要国际化的文件同级目录下，且文件名相同

- 如需要国际化的文件为 `/src/main/webapp/index.html`
- 则中文国际化的属性文件为 `/src/main/webapp/index_zh_CN.properties`
- 则英文国际化的属性文件为 `/src/main/webapp/index_en_US.properties`

### 2.3. 注意 Attention

- 仅支持中英文 zh_CN 和 en_US
- 如果缺少国际化的属性文件，则不会进行国际化
- 如果国际化的属性文件中没有对应的属性，则对应的属性不会进行国际化

### 2.4. 示例 Example

- index.html 文件

````html
<span>$username$</span><input>
<span>$password$</span><input>
````

- index_zh_CN.properties 文件

````properties
$username$=用户名
$password$=密码
````

- index_en_US.properties 文件

````properties
$username$=username
$password$=password
````

## 2.5. 运行 Run

在项目根目录下运行命令

````shell
mvn internationalization:convert
````

## 3. License
