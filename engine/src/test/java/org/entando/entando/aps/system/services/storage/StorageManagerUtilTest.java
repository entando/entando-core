/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.storage;

import org.junit.Assert;
import org.junit.Test;

public class StorageManagerUtilTest {

    @Test
    public void isValidFilename_1() {
        Assert.assertFalse(StorageManagerUtil.isValidFilename(null));
        Assert.assertFalse(StorageManagerUtil.isValidFilename("   "));
        Assert.assertFalse(StorageManagerUtil.isValidFilename(".txt"));
        Assert.assertFalse(StorageManagerUtil.isValidFilename("filename."));
        Assert.assertTrue(StorageManagerUtil.isValidFilename("filename.txt"));
        Assert.assertTrue(StorageManagerUtil.isValidFilename("file name.png"));
        Assert.assertTrue(StorageManagerUtil.isValidFilename("file_name.png"));
        Assert.assertTrue(StorageManagerUtil.isValidFilename("file_name (1).jpeg"));
        Assert.assertFalse(StorageManagerUtil.isValidFilename("file_?name (1).jpeg"));
        Assert.assertFalse(StorageManagerUtil.isValidFilename("file_../name"));
        Assert.assertFalse(StorageManagerUtil.isValidFilename("file_../name.jpg"));
    }

    @Test
    public void isValidFilename_2() {
        Assert.assertFalse(StorageManagerUtil.isValidFilename(null, null));
        Assert.assertFalse(StorageManagerUtil.isValidFilename("   ", null));
        Assert.assertFalse(StorageManagerUtil.isValidFilename(null, " "));
        Assert.assertFalse(StorageManagerUtil.isValidFilename("   ", ""));
        Assert.assertFalse(StorageManagerUtil.isValidFilename(null, ".txt"));
        Assert.assertTrue(StorageManagerUtil.isValidFilename("filename", ""));
        Assert.assertTrue(StorageManagerUtil.isValidFilename("filename", "txt"));
        Assert.assertTrue(StorageManagerUtil.isValidFilename("filename.txt", "txt"));
        Assert.assertTrue(StorageManagerUtil.isValidFilename("file name", "png"));
        Assert.assertTrue(StorageManagerUtil.isValidFilename("file_name", "png"));
        Assert.assertTrue(StorageManagerUtil.isValidFilename("file_name (1)", "jpeg"));
        Assert.assertTrue(StorageManagerUtil.isValidFilename("file_name (1).jpg", "jpeg"));
        Assert.assertFalse(StorageManagerUtil.isValidFilename("file_?name (1)", "jpeg"));
        Assert.assertFalse(StorageManagerUtil.isValidFilename("file_../name", ""));
        Assert.assertFalse(StorageManagerUtil.isValidFilename("file_../name", "jpg"));
    }

    @Test
    public void isValidFilenameNoExtension() {
        Assert.assertFalse(StorageManagerUtil.isValidFilenameNoExtension(null));
        Assert.assertFalse(StorageManagerUtil.isValidFilenameNoExtension("   "));
        Assert.assertTrue(StorageManagerUtil.isValidFilenameNoExtension("filename"));
        Assert.assertTrue(StorageManagerUtil.isValidFilenameNoExtension("filename.txt"));
        Assert.assertTrue(StorageManagerUtil.isValidFilenameNoExtension("file name.png"));
        Assert.assertTrue(StorageManagerUtil.isValidFilenameNoExtension("file_name.png"));
        Assert.assertTrue(StorageManagerUtil.isValidFilenameNoExtension("file_name (1).jpeg"));
        Assert.assertFalse(StorageManagerUtil.isValidFilenameNoExtension("file_?name (1).jpeg"));
        Assert.assertFalse(StorageManagerUtil.isValidFilenameNoExtension("file_../name"));
        Assert.assertFalse(StorageManagerUtil.isValidFilenameNoExtension("file_../name.jpg"));
        Assert.assertFalse(StorageManagerUtil.isValidFilenameNoExtension("file_nam%2e%2e/e.jpg"));
        Assert.assertFalse(StorageManagerUtil.isValidFilenameNoExtension("fi..%2fle_name.jpg"));
        Assert.assertFalse(StorageManagerUtil.isValidFilenameNoExtension("file_na%2e%2e%2fme.jpg"));
    }

    @Test
    public void isValidDirName() {
        Assert.assertTrue(StorageManagerUtil.isValidDirName(null));
        Assert.assertTrue(StorageManagerUtil.isValidDirName("dirname"));
        Assert.assertTrue(StorageManagerUtil.isValidDirName("dirname.txt"));
        Assert.assertTrue(StorageManagerUtil.isValidDirName("dirn ame.xht"));
        Assert.assertTrue(StorageManagerUtil.isValidDirName("dir_name.ney"));
        Assert.assertTrue(StorageManagerUtil.isValidDirName("dir name"));
        Assert.assertFalse(StorageManagerUtil.isValidDirName("dir_name (1)"));
        Assert.assertFalse(StorageManagerUtil.isValidDirName("dir_?name"));
        Assert.assertFalse(StorageManagerUtil.isValidDirName("dir_../name"));
        Assert.assertFalse(StorageManagerUtil.isValidDirName("dir../name.subname"));
        Assert.assertFalse(StorageManagerUtil.isValidDirName("dir_nam%2e%2e/e"));
        Assert.assertFalse(StorageManagerUtil.isValidDirName("dir..%2f_name"));
        Assert.assertFalse(StorageManagerUtil.isValidDirName("dir_na%2e%2e%2fme"));
    }

    @Test
    public void isValidExtension() {
        Assert.assertFalse(StorageManagerUtil.isValidExtension(null));
        Assert.assertTrue(StorageManagerUtil.isValidExtension("extension"));
        Assert.assertTrue(StorageManagerUtil.isValidExtension("txt"));
        Assert.assertTrue(StorageManagerUtil.isValidExtension("t_t"));
        Assert.assertFalse(StorageManagerUtil.isValidExtension("t..t"));
        Assert.assertFalse(StorageManagerUtil.isValidExtension("t t"));
        Assert.assertFalse(StorageManagerUtil.isValidExtension("(1)"));
        Assert.assertFalse(StorageManagerUtil.isValidExtension("r_?n"));
        Assert.assertFalse(StorageManagerUtil.isValidExtension("d_../ex"));
        Assert.assertFalse(StorageManagerUtil.isValidExtension("%2e%2e/e"));
        Assert.assertFalse(StorageManagerUtil.isValidExtension("ex..%2f_n"));
        Assert.assertFalse(StorageManagerUtil.isValidExtension("dir_na%2e%2e%2fme"));
    }

}
