/**
 * 
 */
package com.easyway.spring.nio;

/**
 * buffer内部结构  

一个 buffer 主要由 position,limit,capacity 三个变量来控制读写的过程。此三个变量的含义见如下表格： 





参数 


写模式     


读模式 




position 


当前写入的单位数据数量。 


当前读取的单位数据位置。 




limit 


代表最多能写多少单位数据和容量是一样的。 


代表最多能读多少单位数据，和之前写入的单位数据量一致。 




capacity 


buffer 容量 


buffer 容量 


Buffer 常见方法： 

flip(): 写模式转换成读模式 

rewind() ：将 position 重置为 0 ，一般用于重复读。 

clear() ：清空 buffer ，准备再次被写入 (position 变成 0 ， limit 变成 capacity) 。 

compact(): 将未读取的数据拷贝到 buffer 的头部位。 

mark() 、 reset():mark 可以标记一个位置， reset 可以重置到该位置。 

Buffer 常见类型： ByteBuffer 、 MappedByteBuffer 、 CharBuffer 、 DoubleBuffer 、 FloatBuffer 、 IntBuffer 、 LongBuffer 、 ShortBuffer 。
 
channel 常见类型 :FileChannel 、 DatagramChannel(UDP) 、 SocketChannel(TCP) 、 ServerSocketChannel(TCP)
 
在本机上面做了个简单的性能测试。我的笔记本性能一般。 ( 具体代码可以见附件。见 nio.sample.filecopy 包下面的例子 ) 以下是参考数据： 

–        场景 1 ： Copy 一个 370M 的文件 

–        场景 2: 三个线程同时拷贝，每个线程拷贝一个 370M 文件 

  




场景 


FileInputStream+ 

FileOutputStream 


FileInputStream+ 

BufferedInputStream+ 

FileOutputStream 


ByteBuffer+ 

FileChannel 


MappedByteBuffer 

+FileChannel 




场景一时间 ( 毫秒 )                  


25155 


17500 


19000 


16500 




场景二时间 ( 毫秒 ) 


69000 


67031 


74031 


71016 


5.    nio.charset 

字符编码解码 : 字节码本身只是一些数字，放到正确的上下文中被正确被解析。向 ByteBuffer 中存放数据时需要考虑字符集的编码方式，读取展示 ByteBuffer 数据时涉及对字符集解码。
 
Java.nio.charset 提供了编码解码一套解决方案。 

以我们最常见的 http 请求为例，在请求的时候必须对请求进行正确的编码。在得到响应时必须对响应进行正确的解码。 

以下代码向 baidu 发一次请求，并获取结果进行显示。例子演示到了 charset 的使用。 

 * @author longgangbai
 * 2015-1-18  下午7:45:51
 */
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class CopyFile {
    public static void main(String[] args) throws Exception {
	String infile = "C:\\copy.sql";
	String outfile = "C:\\copy.txt";
	// 获取源文件和目标文件的输入输出流
	FileInputStream fin = new FileInputStream(infile);
	FileOutputStream fout = new FileOutputStream(outfile);
	// 获取输入输出通道
	FileChannel fcin = fin.getChannel();
	FileChannel fcout = fout.getChannel();
	// 创建缓冲区
	ByteBuffer buffer = ByteBuffer.allocate(1024);
	while (true) {
	    // clear方法重设缓冲区，使它可以接受读入的数据
	    buffer.clear();
	    // 从输入通道中将数据读到缓冲区
	    int r = fcin.read(buffer);
	    // read方法返回读取的字节数，可能为零，如果该通道已到达流的末尾，则返回-1
	    if (r == -1) {
		break;
	    }
	    // flip方法让缓冲区可以将新读入的数据写入另一个通道
	    buffer.flip();
	    // 从输出通道中将数据写入缓冲区
	    fcout.write(buffer);
	}
    }
}
