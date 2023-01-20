package cn.qkmango.i18n;

import java.io.*;
import java.util.ArrayList;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * 国际化工具类
 *
 * @author qkmango
 * @version 1.0
 * @date 2023-01-20 13:15
 */
public class Internationalization {

    // 需要国际化的所有文件
    private ArrayList<File> fileArrayList;
    // 需要国际化的文件根目录
    private String srcBasePath;
    // 中文国际化文件存放的根目录
    private String zhDestBasePath;
    // 英文国际化文件存放的根目录
    private String enDestBasePath;
    // 需要国际化的文件的后缀名
    private String fileSuffix = ".html";

    public Internationalization(String srcBasePath,
                                String zhDestBasePath,
                                String enDestBasePath,
                                String fileSuffix) {
        this.srcBasePath = srcBasePath;
        this.zhDestBasePath = zhDestBasePath;
        this.enDestBasePath = enDestBasePath;
        if (fileSuffix != null) {
            this.fileSuffix = fileSuffix;
        }
        this.fileArrayList = new ArrayList<>();
    }


    /**
     * 转换
     *
     * @throws IOException
     */
    public void convert() throws IOException {
        File file = new File(srcBasePath);
        if (!file.exists() || !file.isDirectory()) {
            return;
        }
        fileList(file);
        for (File f : fileArrayList) {
            internationalization(f);
        }
    }

    /**
     * 国际化
     *
     * @param file
     * @throws IOException
     */
    private void internationalization(File file) throws IOException {
        // zh en 翻译过的文件存储到哪个文件
        String zhDestPath = zhDestBasePath + file.getAbsolutePath().replace(srcBasePath, "");
        String enDestPath = enDestBasePath + file.getAbsolutePath().replace(srcBasePath, "");

        // zh en 资源文件路径
        String zhPropertiesPath = file.getAbsolutePath().replace(fileSuffix, "_zh_CN.properties");
        String enPropertiesPath = file.getAbsolutePath().replace(fileSuffix, "_en_US.properties");

        // 读取文件内容
        String content = read(file);
        // 读取国际化资源文件
        ResourceBundle zhResourceBundle = readProperties(zhPropertiesPath);
        ResourceBundle enResourceBundle = readProperties(enPropertiesPath);

        if (zhResourceBundle != null) {
            // zh替换内容
            String zhContent = replaceContent(content, zhResourceBundle);
            // 写入文件
            write(zhContent, zhDestPath);
        } else {
            System.out.println("zhResourceBundle is null, path: " + zhPropertiesPath);
        }
        if (enResourceBundle != null) {
            // en替换内容
            String enContent = replaceContent(content, enResourceBundle);
            // 写入文件
            write(enContent, enDestPath);
        } else {
            System.out.println("enResourceBundle is null, path: " + enPropertiesPath);
        }
    }

    /**
     * 读取文件内容
     *
     * @param file
     * @return
     * @throws IOException
     */
    public String read(File file) throws IOException {
        StringBuilder builder = new StringBuilder();
        FileInputStream is = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");
        BufferedReader reader = new BufferedReader(isr);
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            builder.append(line);
            builder.append("\r\n");
        }
        isr.close();
        reader.close();
        is.close();
        return builder.toString();
    }

    /**
     * 读取资源文件
     *
     * @param path
     * @return
     */
    private ResourceBundle readProperties(String path) {
        FileInputStream fs = null;
        InputStream in = null;
        ResourceBundle rb = null;

        try {
            fs = new FileInputStream(path);
            in = new BufferedInputStream(fs);
            rb = new PropertyResourceBundle(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fs != null) {
                    fs.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rb;
    }

    /**
     * 获取所有的文件
     *
     * @param file
     */
    private void fileList(File file) {
        if (!file.exists() || !file.isDirectory()) {
            return;
        }

        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                fileList(f);
            } else {
                if (f.getName().endsWith(".html")) {
                    fileArrayList.add(f);
                }
            }
        }
    }

    /**
     * 替换内容
     *
     * @param content
     * @param resourceBundle
     * @return
     */
    private String replaceContent(String content, ResourceBundle resourceBundle) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder(content);
        Set<String> keySet = resourceBundle.keySet();
        for (String key : keySet) {
            String value = resourceBundle.getString(key);
            int indexOf = builder.indexOf(key);
            if (indexOf == -1) {
                System.out.println("未在原网页文件中找到 " + key + " =>" + value);
                continue;
            }
            builder.replace(indexOf, indexOf + key.length(), value);
        }
        return builder.toString();
    }

    /**
     * 写入文件
     *
     * @param content
     * @param path
     * @throws IOException
     */
    private void write(String content, String path) throws IOException {
        // 创建每个国际化后文件的父目录
        File parentFile = new File(path).getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        //创建每个国际化后文件
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }

        OutputStreamWriter write = null;
        BufferedWriter bufferedWriter = null;

        try {
            write = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
            bufferedWriter = new BufferedWriter(write);
            bufferedWriter.write(content);
            System.out.println("写入文件-->" + file.getAbsolutePath());
            bufferedWriter.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (write != null) {
                write.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        }

    }
}
