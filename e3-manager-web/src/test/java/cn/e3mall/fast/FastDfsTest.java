package cn.e3mall.fast;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import cn.e3mall.common.utils.FastDFSClient;

public class FastDfsTest {
	/*@Test
	public void testUpload() throws Exception{
		//创建一个文件 文件名任意 内容是tracker的服务器地址
		//使用全局变量加载配置文件
		ClientGlobal.init("C:\\Users\\MyPC\\eclipse-workspace\\e3-manager-web\\src\\main\\resources\\client.conf");
		//创建一个TrackerClient对象
		TrackerClient tracker = new TrackerClient();
		//通过TrackerClient获得一个TrackerServer对象
		TrackerServer trackerServer = tracker.getConnection();
		//创建一个StorageServer的引用 可以是Null
		StorageServer storageServer =  null;
		//创建一个StorageClient 参数是TrackerServer StorageServer
		StorageClient storageClient = new StorageClient(trackerServer,storageServer);
		//使用StorageClient上传文件
		String[] strs = storageClient.upload_file("D:\\Backup\\我的文档\\My Pictures\\Feedback\\{AC1EAB76-E522-4F8E-8F3E-450CBDBDB33C}\\Capture001.png", "png",
				null);
		for(String string:strs) {
			System.out.println(string);
		}
	}*/
	@Test
	public void testFastDfsClient() throws Exception{
		FastDFSClient client = new FastDFSClient("C:\\Users\\MyPC\\eclipse-workspace\\e3-manager-web\\src\\main\\resources\\client.conf");
		String str = client.uploadFile("D:\\Backup\\桌面\\秋野花最高-插画100期-第7周-1521218107185.jpg");
		System.out.println(str);
	}
}
