package com.file.compressor.FileCompressor.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TreeNode implements Comparable<TreeNode> {
    private int freq;
    private char ch;
    private TreeNode left = null;
    private TreeNode right = null;

    public TreeNode(int freq, char ch) {
        this.freq = freq;
        this.ch = ch;
    }

    public TreeNode(int freq) {
        this.freq = freq;
        this.ch = '\0';
    }

    @Override
    public int compareTo(TreeNode other) {
        return Integer.compare(this.freq, other.freq);
    }
}
