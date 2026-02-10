package com.example.javafx_inventoryms.objects;

import javafx.scene.Node;
import java.util.Stack;

public class NavigationHistory {
    private Stack<Node> backStack;
    private Stack<Node> forwardStack;
    private Node currentPage;

    public NavigationHistory() {
        this.backStack = new Stack<>();
        this.forwardStack = new Stack<>();
        this.currentPage = null;
    }

    public void navigateTo(Node page) {
        if (currentPage != null) {
            backStack.push(currentPage);
        }
        currentPage = page;
        forwardStack.clear();
    }

    public Node goBack() {
        if (!canGoBack()) {
            return null;
        }
        forwardStack.push(currentPage);
        currentPage = backStack.pop();
        return currentPage;
    }

    public Node goForward() {
        if (!canGoForward()) {
            return null;
        }
        backStack.push(currentPage);
        currentPage = forwardStack.pop();
        return currentPage;
    }

    public boolean canGoBack() {
        return !backStack.isEmpty();
    }

    public boolean canGoForward() {
        return !forwardStack.isEmpty();
    }

    public Node getCurrentPage() {
        return currentPage;
    }

    public void setInitialPage(Node page) {
        this.currentPage = page;
    }
}