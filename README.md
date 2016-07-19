pluggable
========================

Includes post delegating class loader and thread current local class loader captures for plugin building.

Load all classes using the post delegation loader:

```
PostDelegationClassLoader loader = new PostDelegationClassLoader(..urls of your jar..)

YourClass yourClass = (YourClass) loader.loadClass(.. name)
```

Anytime you act on your class though, make sure you wrap it in a thread current capture.

This makes sure that the thread local classloader is of the correct type, and not of the parent type.

```
try(ThreadCurrentClassLoaderCapture capture = new ThreadCurrentClassLoaderCapture(yourClass.getClassLoader()){
  // do stuff
  yourClass.fooBar()
}
```
