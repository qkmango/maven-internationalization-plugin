package cn.qkmango;


import cn.qkmango.maven.plugin.Internationalization;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;

/**
 * 将项目中需要国际化的网页进行国际化
 * <ol>
 * <li>需要配置需要国际化的文件路径 srcBasePath</li>
 * <li>需要配置中文文件的输出路径 zhDestBasePath</li>
 * <li>需要配置英文文件的输出路径 enDestBasePath</li>
 * <li>需要配置需要国际化的文件的后缀名, 默认为 <code>.html</code> fileSuffix</li>
 * </ol>
 * <p>通过遍历 srcBasePath 目录下的所有文件，将其中的中文进行国际化，输出到 zhDestBasePath 和 enDestBasePath 目录下</p>
 * <p>国际化的属性文件 properties 要放在需要国际化的文件同级目录下，且文件名相同<br>
 * 如需要国际化的文件为 /src/main/webapp/index.html<br>
 * 则中文国际化的属性文件为 /src/main/webapp/index_zh_CN.properties<br>
 * 则英文国际化的属性文件为 /src/main/webapp/index_en_US.properties<br>
 * </p>
 * <p>仅支持中英文 zh_CN 和 en_US<br>
 * 如果缺少国际化的属性文件，则不会进行国际化<br>
 * 如果国际化的属性文件中没有对应的属性，则对应的属性不会进行国际化<br>
 * </p>
 *
 * <p>
 * 示例：<br>
 * <pre>
 *     index.html:<br>
 *          &lt;span&gt;$password$&lt;/span&gt;&lt;input&gt;
 *          &lt;span&gt;$username$&lt;/span&gt;&lt;input&gt;
 * </pre>
 * <pre>
 *
 *     index_zh_CN.properties:<br>
 *          $username$=用户名<br>
 *          $password$=密码
 *  </pre>
 *  <pre>
 *     index_en_US.properties:<br>
 *          $username$=username<br>
 *          $password$=password
 * </pre>
 * </p>
 *
 * @author qkmango
 * @date 2023-01-20 17:00:00
 * @phase process-sources
 */
@Mojo(name = "convert")
public class Convert extends AbstractMojo {

    /**
     * 需要国际化的文件根目录
     */
    @Parameter
    private String srcBasePath;

    /**
     * 中文国际化文件存放的根目录
     */
    @Parameter
    private String zhDestBasePath;

    /**
     * 英文国际化文件存放的根目录
     */
    @Parameter
    private String enDestBasePath;

    /**
     * 需要国际化的文件后缀
     */
    @Parameter
    private String fileSuffix;

    public void execute() throws MojoExecutionException {
        if (srcBasePath == null) {
            throw new MojoExecutionException("srcBasePath can not be null");
        }
        if (zhDestBasePath == null) {
            throw new MojoExecutionException("zhDestBasePath can not be null");
        }
        if (enDestBasePath == null) {
            throw new MojoExecutionException("enDestBasePath can not be null");
        }
        if (fileSuffix == null) {
            throw new MojoExecutionException("fileSuffix can not be null");
        }

        srcBasePath = srcBasePath.replace("/", "\\");
        zhDestBasePath = zhDestBasePath.replace("/", "\\");
        enDestBasePath = enDestBasePath.replace("/", "\\");

        getLog().info("srcBasePath => " + srcBasePath);
        getLog().info("zhDestBasePath => " + zhDestBasePath);
        getLog().info("enDestBasePath => " + enDestBasePath);

        try {
            new Internationalization(
                    srcBasePath,
                    zhDestBasePath,
                    enDestBasePath,
                    fileSuffix
            ).convert();
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage());
        }
    }
}
