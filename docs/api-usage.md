## Developer API Usage

Now that we have a `FaceProvider` instance, we can use it to get the faces of any
player.

Note that faces are represented by `PlayerFaceComponent` instances, they implement
`ComponentLike` so you can just use `asComponent()` to convert them to components.

### `FaceProvider` Methods
```java
FaceProvider provider = ...;

PlayerFaceComponent face = provider.get(Player);
// (this is the one you will use the most)
//    this method obtains the face from cache, fetches
//    it if it is not cached yet, or returns a default
//    minecraft head (like Alex head or Steve head)

@Nullable PlayerFaceComponent face = provider.getOrFind(Player);
//   this method is similar to the previous one, but returns
//   null if the player doesn't have a skin, instead of returning
//   a default one

@Nullable PlayerFaceComponent face = provider.getCached(Player);
//   this method is similar to the previous ones too, but only
//   finds the head in cache, and returns null if it is not present

@Nullable PlayerFaceComponent face = provider.fetch(Player);
//  this method fetches the player face, doesn't check for heads
//  in cache. Returns null if the face couldn't be found
```

### `PlayerFaceComponent`
The `PlayerFaceComponent` instances are components containing the player's
face. So you can use it just like a component! You can even explicitly convert
it to a component using `asComponent()` method.

```java
PlayerFaceComponent face = ...;
Component component = face.asComponent();

// sends the face to a player via chat
player.sendMessage(component);

// shows the face to a player via action bar
player.sendActionBar(component);

// you can also concatenate components and send them
player.sendMessage(
        Component.text()
            .append(Component.text("Hey this is your face: "))
            .append(face)
            .build()
);
```

You can also serialize/deserialize `PlayerFaceComponent` instances so you can
store them and use them later

```java
PlayerFaceComponent face = ...;
int[][] data = face.pixels(); // <-- Here we obtain the important data

// Now we can just convert the face pixels to Base64
String serialized = Faces.pixelsToBase64(data);

// Then we can deserialize them
int[][] deserializedData = Faces.base64ToPixels(serialized);

// And convert the pixels to a face component using the FaceProvider
PlayerFaceComponent deserializedFace = faceProvider.fromPixels(deserializedData);
```

And that's it!