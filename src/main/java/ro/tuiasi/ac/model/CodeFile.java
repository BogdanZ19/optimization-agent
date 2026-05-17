package ro.tuiasi.ac.model;

import java.nio.file.Path;

/**
 * Immutable representation of a source code file.
 *
 * @param fileName the name of the file
 * @param fullPath the absolute path to the file
 * @param relativePath the path relative to the project root
 * @param sizeInKb the file size in kilobytes
 */
public record CodeFile(
    String fileName,
    Path fullPath,
    String relativePath,
    long sizeInKb) {}
