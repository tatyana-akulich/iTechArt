package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;

public class FileSearch {
    final static int DEFAULT_DEPTH = 20;
    final static long DEFAULT_CREATION_DATE_INTERVAL = 10_000;
    static String path;
    static String extension;
    static int maxDepth;
    static int filesOutputLimit;


    public static void main(String[] args) {
        //args = new String[]{"D:", "class", "5", "15"};
        path = args[0];
        extension = args[1];
        checkOptionalParameters(args);
        List<FileInfo> fileTree = getFileTree();
        printResults(fileTree);
    }

    static void checkOptionalParameters(String[] args) {
        try {
            filesOutputLimit = (args.length < 3) ? 0 : Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("Amount of files for output should be integer");
            System.exit(0);
        }
        if (filesOutputLimit < 0) {
            System.out.println("Amount of files for output should be positive");
            System.exit(0);
        }
        try {
            maxDepth = (args.length < 4) ? DEFAULT_DEPTH : Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            System.out.println("Depth parameter should be integer");
            System.exit(0);
        }
        if (maxDepth < 0) {
            System.out.println("Depth parameter should be positive");
            System.exit(0);
        }
    }

    static List<FileInfo> getFileTree() {
        List<FileInfo> result = new ArrayList<>();
        EnumSet<FileVisitOption> options = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
        try {
            Files.walkFileTree(Paths.get(path), options, maxDepth, new GetFiles(result));
        } catch (AccessDeniedException e) {
            System.out.println("Files with denied access were omitted");
        } catch (IOException e) {
            System.out.println("Entered path doesn't exist or is unavailable");
            System.exit(0);
        }
        return result;
    }

    static void printResults(List<FileInfo> files) {
        switch (files.size()) {
            case 0: {
                System.out.println("Files with entered extension were not found");
                break;
            }
            case 1: {
                System.out.println(files.get(0).getFilename());
                break;
            }
            default: {
                List<FileInfo> selectedFiles = sortFilesByCreationDate(files);
                System.out.println("The latest created file is " + selectedFiles.get(0).getFilename());
                if (selectedFiles.size() > 1) {
                    int amountOfElementsToPrint = checkOutputLimit(selectedFiles);
                    System.out.println("Files created no more than 10 seconds earlier");
                    for (int i = 1; i < amountOfElementsToPrint; i++) {
                        System.out.println(selectedFiles.get(i).getFilename());
                    }
                }
            }
        }
    }

    static List<FileInfo> sortFilesByCreationDate(List<FileInfo> files) {
        Set<FileInfo> initialFiles = new TreeSet<>(files);
        List<FileInfo> sortedFiles = new ArrayList<>(initialFiles);
        List<FileInfo> selectedFiles = new ArrayList<>();
        long latestDate = sortedFiles.get(0).creationTime.toMillis();
        long minDateForSelection = latestDate - DEFAULT_CREATION_DATE_INTERVAL;
        selectedFiles.add(sortedFiles.get(0));
        for (int i = 1; i < sortedFiles.size(); i++) {
            if (sortedFiles.get(i).getCreationTime().toMillis() >= minDateForSelection) {
                selectedFiles.add(sortedFiles.get(i));
            } else break;
        }
        return selectedFiles;
    }

    static int checkOutputLimit(List<FileInfo> files) {
        return (filesOutputLimit == 0) ? files.size() : (Math.min(files.size(), filesOutputLimit));
    }

    private static class GetFiles extends SimpleFileVisitor<Path> {
        private List<FileInfo> result;

        public GetFiles(List<FileInfo> result) {
            this.result = result;
        }

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
            File file = path.toFile();
            if (file.isFile()) {
                if (file.getName().substring(file.getName().lastIndexOf(".") + 1).equals(extension)) {
                    result.add(new FileInfo(path.getFileName().toString(), (FileTime) Files.getAttribute(path, "creationTime")));
                }
            }
            return super.visitFile(path, basicFileAttributes);
        }
    }
}


