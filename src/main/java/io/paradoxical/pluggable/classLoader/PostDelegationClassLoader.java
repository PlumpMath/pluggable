package io.paradoxical.pluggable.classLoader;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

public class PostDelegationClassLoader extends URLClassLoader {

    public PostDelegationClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    public PostDelegationClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public PostDelegationClassLoader(URL[] urls) {
        super(urls);
    }

    public Class<?> loadClass(String name) throws ClassNotFoundException {
        // make sure anyone who reads off this thread's class loader
        ClassLoader originalClassLoader = null;

        try {
            originalClassLoader = Thread.currentThread().getContextClassLoader();

            // for any libraries dumb enough to grab the class loader from the thread local
            // make sure they use the right delegated loader
            Thread.currentThread().setContextClassLoader(this);

            Class loadedClass = findLoadedClass(name);

            // Nope, try to load it
            if (loadedClass == null) {
                try {
                    // Ignore parent delegation and just try to load locally
                    loadedClass = findClass(name);
                }
                catch (ClassNotFoundException e) {
                    // Swallow - does not exist locally
                }

                // If not found, just use the standard URLClassLoader (which follows normal parent delegation)
                if (loadedClass == null) {
                    // throws ClassNotFoundException if not found in delegation hierarchy at all
                    loadedClass = super.loadClass(name);
                }
            }
            return loadedClass;
        }
        finally {
            if (originalClassLoader != null) {
                Thread.currentThread().setContextClassLoader(originalClassLoader);
            }
        }
    }

    @Override
    public URL getResource(final String name) {
        final URL resource = findResource(name);

        if (resource != null) {
            return resource;
        }

        return super.getResource(name);
    }
}