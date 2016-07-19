package io.paradoxical.pluggable.classLoader;

public class ThreadCurrentClassLoaderCapture implements AutoCloseable {
    final ClassLoader originalClassLoader;

    public ThreadCurrentClassLoaderCapture(final ClassLoader newClassLoader) {
        originalClassLoader = Thread.currentThread().getContextClassLoader();

        Thread.currentThread().setContextClassLoader(newClassLoader);
    }

    @Override
    public void close() {
        Thread.currentThread().setContextClassLoader(originalClassLoader);
    }
}
