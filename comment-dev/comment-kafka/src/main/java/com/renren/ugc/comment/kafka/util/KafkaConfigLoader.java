/**
 * <p> @(#)KafkaConfigLoader.java, 2013-11-25. </p>
 * 
 * Copyright 2013 RenRen, Inc. All rights reserved.
 */
package com.renren.ugc.comment.kafka.util;

import java.io.*;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * 
 * @author wmc
 * 
 */
public class KafkaConfigLoader {
	private static final Logger logger = Logger.getLogger(KafkaConfigLoader.class);

	public static Properties loadPropertyFile(InputStream inputStream) {
		Properties pros = new Properties();
		try {
            pros.load(inputStream);
            return pros;
		} catch (FileNotFoundException e) {
            logger.fatal("Cannot find kafka config file", e);
		} catch (IOException e) {
            logger.fatal("Cannot read kafka config file", e);
        } finally {
			try {
                if (inputStream != null) {
				    inputStream.close();
                }
			} catch (IOException e) {
				// nothing to do
			}
		}
		return null;
	}
}
