# Android-Material-Design
This application does not consume any data source. Its one and only purpose is to make developers 
familiarize with material design UI components and concepts come from Google support libraries. 
However, it does not limit the user to use built-in components inside support libraries. If 
developers want to achieve UI or UX that is not currently supported, they are free to write their
custom classes. Exceptions are libraries that simplify loading images or internet communication.

## Something inside this project

### CoordinatorLayout.Behavior
Custom behaviors to manipulate `CoordinatorLayout`'s child views

![CooridinatorLayout Behaivor](/arts/20181021_140531.gif?raw=true)

### Animator
Handle `RevealAnimator`, `ObjectAnimator` properly (at least, no weird behavior is noticed so far)

Use those animators to create `FloatingBehaviorButton` to options list transformation

![CooridinatorLayout Behaivor](/arts/20181021_140649.gif?raw=true)

### Glide
Custom `PreloadSizeProvider` for `RecyclerView` integration. The class `RemotePreloadSizeProvider`
make `ImageView` inside `RecyclerView` aware the size of remote images given urls
