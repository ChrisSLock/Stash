# Stash: Useful extensions for Guava Caches
Stash is a set of Decorators that extend the functionality of [Google Guava] in-memory caches.  

### External Invalidation
Stash contains decorators for both *Cache* and *LoadingCache* that provide External Invalidation functionality.  Caches that share source data can also share their invalidation state with each other through a common *Invalidator*.  An Invalidator is a class you implement that connects to your caches through the messaging mechanism of your choice. 

### Using a Stash Invalidation Decorator 
Stash provides two Decorators to that implement the invalidation functionality.  They are:
- *CacheInvalidationDecorator*
- *LoadingCacheInvalidationDecorator*

Before you can use either these, however, you must implement your *Invalidator*.

#### Defining an Invalidator.
An *Invalidator* provides invalidation functionality to the Invalidation Decorators.  Your Invalidator must:
- Provide an Invalidation Context string
- Implement *invalidate()* such that it sends an invalidation messages to other instances.
- Call *invalidationRequired()* when it receives an invalidation message from other instances.

A simple, local, version of an Invalidator can just be:
```java
public class MyInvalidator<K> extends Invalidator<K> {
    public MyInvalidator() {
        super(MyInvalidator.class.getSimpleName()); //class name as Invalidation Context
    }

    @Override
    public void invalidate(Invalidation<K> invalidation) {
        invalidationRequired(invalidation); //send locally when you receive
    }	
}
```

When Invalidators are implemented using pub/sub frameworks(ex. XMPP, MQTT) it enables Stash to provide Distributed Invalidation.  

Distributed Invalidation is useful in distributed applications(like web apps) that have local caches of common data with a long expiry, but require timely refresh when another actor or instance changes the source data.  Below is some pseudo-code for this:

```java
public class MyPubSubInvalidator<K> extends Invalidator<K> {
    public MyPubSubInvalidator() {
        super(MyPubSubInvalidator.class.getSimpleName());
        subscribeToChannel(); //your subscribe logic, registering your callback
    }

    @Override
    public void invalidate(Invalidation<K> invalidation) {
        sendMessage(invalidation); //your publish
    }	
  
    //your callback handler
    private void doMyMessageCallBack(String message) {
        Invalidation<K> invalidation = invalidationFromMessage(message);
        invalidationRequired(invalidation); //invoke Stash
    }
}
```

An [full example implementation] of an invalidator that uses [PubNub] is included in this project.

#### Creating the Cache
Once you have implemented your Invalidator, use it when instanciating your Guava caches like this:
```java
Invalidator<String> invalidator = new MyPubSubInvalidator();
Cache myCache =  new CacheInvalidationDecorator<String, String>(
    CacheBuilder.newBuilder().build(),
    invalidator);
```
Congratulations!  You now have a Distributed Invalidation Cache!

The [examples] include a write-through cache pattern for your reference as well.

## Links
Some more info on using [Guava caches].

[Guava caches]: https://github.com/google/guava/wiki
[Google Guava]: https://github.com/google/guava/
[full example implementation]: https://github.com/ChrisSLock/Stash/blob/master/src/main/java/com/chrisslock/stash/examples/PubNubInvalidator.java
[PubNub]: https://www.pubnub.com/
[examples]: https://github.com/ChrisSLock/Stash/tree/master/src/main/java/com/chrisslock/stash/examples
