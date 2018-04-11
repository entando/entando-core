/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.aps.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.agiletec.aps.system.exception.ApsSystemException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for reading the contents of files.
 *
 * @author E.Santoboni
 */
public class FileTextReader {

    private static final Logger logger = LoggerFactory.getLogger(FileTextReader.class);

    public static String getText(InputStream is) throws ApsSystemException, IOException {
        return getText(is, null);
    }

    public static String getText(InputStream is, String charset) throws ApsSystemException, IOException {
        Reader reader = null;
        BufferedReader br = null;
        try {
            reader = (null != charset) ? new InputStreamReader(is, charset) : new InputStreamReader(is);
            br = new BufferedReader(reader);
            return getText(br);
        } catch (Throwable t) {
            throw new ApsSystemException("Error reading text", t);
        } finally {
            if (null != br) {
                br.close();
            }
            if (null != reader) {
                reader.close();
            }
        }
    }

    public static String getText(String filename) throws ApsSystemException, IOException {
        Reader reader = null;
        BufferedReader br = null;
        try {
            reader = new FileReader(filename);
            br = new BufferedReader(reader);
            return getText(br);
        } catch (FileNotFoundException t) {
            throw new ApsSystemException("Error reading text", t);
        } finally {
            if (null != br) {
                br.close();
            }
            if (null != reader) {
                reader.close();
            }
        }
    }

    private static String getText(BufferedReader br) throws IOException {
        String lineSep = System.getProperty("line.separator");
        String nextLine = "";
        StringBuilder sb = new StringBuilder();
        while ((nextLine = br.readLine()) != null) {
            sb.append(nextLine);
            sb.append(lineSep);
        }
        return sb.toString();
    }

    public static File createTempFile(String filename, InputStream is) throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        String filePath = tempDir + File.separator + filename;
        FileOutputStream outStream = null;
        try {
            byte[] buffer = new byte[1024];
            int length = -1;
            outStream = new FileOutputStream(filePath);
            while ((length = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
                outStream.flush();
            }
        } catch (IOException t) {
            logger.error("Error on saving temporary file", t);
            throw t;
        } finally {
            if (null != outStream) {
                outStream.close();
            }
            if (null != is) {
                is.close();
            }
        }
        return new File(filePath);
    }

    public static byte[] fileToByteArray(File file) throws IOException {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            fis = new FileInputStream(file);
            byte[] buf = new byte[1024];
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
            }
        } catch (IOException ex) {
            logger.error("Error creating byte array", ex);
            throw ex;
        } finally {
            if (null != fis) {
                fis.close();
            }
        }
        return bos.toByteArray();
    }

}
