package org.example;

import java.nio.file.attribute.FileTime;

public class FileInfo implements Comparable<FileInfo>{
    String filename;
    FileTime creationTime;

    public FileInfo(String filename, FileTime creationTime) {
        this.filename = filename;
        this.creationTime = creationTime;
    }

    public String getFilename() {
        return filename;
    }

    public FileTime getCreationTime() {
        return creationTime;
    }

    @Override
    public int compareTo(FileInfo o) {
        if (this.creationTime.toMillis() < o.creationTime.toMillis()) {
            return 1;
        } else if (this.creationTime.toMillis() == o.creationTime.toMillis()) {
            return 0;
        } else return -1;
    }
}
