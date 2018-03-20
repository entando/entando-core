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
package org.entando.entando.aps.util.argon2;

import com.agiletec.aps.util.IApsEncrypter;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;
import java.nio.charset.Charset;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author paddeo
 */
public class Argon2Encrypter implements IApsEncrypter {

    @Override
    public String encrypt(String text) {
        this.setPassword(text.getBytes());
        Argon2 argon2 = Argon2Factory.create(this.getType(), this.getSaltLen(), this.getHashLen());
        long start = System.nanoTime();
        String hash = argon2.hash(this.getIterations(), this.getMemory(), this.getParallelism(), text, charset);
        duration = (System.nanoTime() - start) / 1000000000.0;
        output = hash.getBytes();
        return hash;
    }

    public boolean verify(String hash, String text) {
        Argon2 argon2 = Argon2Factory.create(this.getType(), this.getSaltLen(), this.getHashLen());
        return argon2.verify(hash, text);
    }

    public static String encryptString(String text) {
        Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2i);
        long start = System.nanoTime();
        String hash = argon2.hash(8, 1 << 16, 4, text);
        System.out.println("duration: " + (System.nanoTime() - start) / 1000000000.0);
        return hash;
    }

    void printSummary() {
        System.out.println("Type:\t\t" + this.getType().name());
        System.out.println("Iterations:\t" + this.getIterations());
        System.out.println("Memory:\t\t" + this.getMemory() + " KiB");
        System.out.println("Parallelism:\t" + this.getParallelism());
        System.out.println("Hash:\t\t" + getOutputString());
        System.out.println("Encoded:\t " + getEncoded());
        System.out.println(this.getDuration() + " seconds");
    }

    public String getOutputString() {
        StringBuilder sb = new StringBuilder();
        for (byte b : output) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

    public String getEncoded() {
        return ""; //TODO
    }

    public byte[] getOutput() {
        return output;
    }

    public void setOutput(byte[] output) {
        this.output = output;
    }

    public int getOutputLength() {
        return outputLength;
    }

    public void setOutputLength(int outputLength) {
        this.outputLength = outputLength;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public int getHashLen() {
        String len = System.getProperties().getProperty("algo.argon2.hash.length");
        if (StringUtils.isNotBlank(len)) {
            hashLen = Integer.valueOf(len);
        }
        return hashLen;
    }

    public void setHashLen(int hashLen) {
        this.hashLen = hashLen;
    }

    public int getSaltLen() {
        String len = System.getProperties().getProperty("algo.argon2.salt.length");
        if (StringUtils.isNotBlank(len)) {
            saltLen = Integer.valueOf(len);
        }
        return saltLen;
    }

    public void setSaltLen(int saltLen) {
        this.saltLen = saltLen;
    }

    public int getIterations() {
        String len = System.getProperties().getProperty("algo.argon2.iterations");
        if (StringUtils.isNotBlank(len)) {
            iterations = Integer.valueOf(len);
        }
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getMemory() {
        String len = System.getProperties().getProperty("algo.argon2.memory");
        if (StringUtils.isNotBlank(len)) {
            memory = Integer.valueOf(len);
        }
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getParallelism() {
        String len = System.getProperties().getProperty("algo.argon2.parallelism");
        if (StringUtils.isNotBlank(len)) {
            parallelism = Integer.valueOf(len);
        }
        return parallelism;
    }

    public void setParallelism(int parallelism) {
        this.parallelism = parallelism;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Argon2Types getType() {
        String algoType = System.getProperties().getProperty("algo.argon2.type");
        if (StringUtils.isBlank(algoType)) {
            return type;
        } else {
            return Argon2Types.valueOf(algoType);
        }
    }

    public void setType(Argon2Types type) {
        this.type = type;
    }

    public boolean isClearMemory() {
        return clearMemory;
    }

    public void setClearMemory(boolean clearMemory) {
        this.clearMemory = clearMemory;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    private byte[] output;
    private int outputLength = 32; // -l N
    private double duration;

    private byte[] password;

    private int hashLen;
    private int saltLen;
    private int iterations; // -t N
    private int memory; // -m N
    private int parallelism; // -p N

    private int version = 0x13; // -v (10/13)

    private boolean clearMemory = true;
    private Charset charset = Charset.forName("UTF-8");
    private Argon2Types type = Argon2Types.ARGON2i;

}
