package com.campus.cache.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.lang.StringUtils;


/** 
 * 序列化工具，将对象序列化/反序列化为字符串以便保存在tripodCache中
 * @author meng.liu 
 * @date 2014-7-22 下午2:27:13 
 */
public class SerializeUtil {
	
	private static int BUFFER_SIZE = 40960;
	
	public static String serialize(Object obj)
	        throws NullPointerException, IOException
	    {
	        if(obj == null)
	        {
	            throw new NullPointerException("obj == null");
	        } else
	        {
	            ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
	            ObjectOutputStream objStream = new ObjectOutputStream(out);
	            objStream.writeObject(obj);
	            byte bytes[] = out.toByteArray();
	            objStream.close();
	            return new String(bytes,"ISO-8859-1");
	        }
	    }

	    public static Object deserialize(String value)
	        throws NullPointerException, IOException
	    {
	        if(StringUtils.isBlank(value))
	            throw new NullPointerException("value == null or length is 0");
	        byte[] byteArray = value.getBytes("ISO-8859-1");
	        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
	        ObjectInputStream objStream = new ObjectInputStream(in);
	        Object obj = null;
	        try
	        {
	            obj = objStream.readObject();
	        }
	        catch(ClassNotFoundException e)
	        {
	            e.printStackTrace();
	        }
	        objStream.close();
	        return obj;
	    }

	    


}
