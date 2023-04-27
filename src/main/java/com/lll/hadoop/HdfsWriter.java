package com.lll.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 实现对hdfs的写入
 */
public class HdfsWriter {
    private FileSystem fs;

    public HdfsWriter() throws URISyntaxException, IOException, InterruptedException {
        Configuration conf = new Configuration();
        fs = FileSystem.get(new URI("hdfs://8.130.49.23:9000"), conf, "root");
    }

    public void mkdir(String path) throws IOException {
        fs.mkdirs(new Path(path));
        fs.close();
    }

    public void write(String localFilePath, String hdfsFilePath) throws IOException {
        Path localPath = new Path(localFilePath);
        Path hdfsPath = new Path(hdfsFilePath);
        fs.copyFromLocalFile(localPath, hdfsPath);
    }

    public void close() throws IOException {
        fs.close();
    }
}
