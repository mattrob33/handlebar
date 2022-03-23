# Handlebar
<div align="center">
  <img src ="https://user-images.githubusercontent.com/4062288/159766075-d59ba1ea-c2b1-4264-86f9-6975c7dc45eb.png" width="200" />
  <p>Typesafe SavedStateHandle in Android ViewModels.</p>
</div>

# Description
[Android ViewModels](https://developer.android.com/reference/androidx/lifecycle/ViewModel) typically interact with [SavedStateHandle](https://developer.android.com/reference/androidx/lifecycle/SavedStateHandle) through a key-value basis. SavedStateHandle allows for ViewModel state to survive process death, but it comes at the cost of having state that is essentially [stringly typed](https://www.techopedia.com/definition/31876/stringly-typed).

```kotlin
class LoginViewModel(
    api: API,
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    private val _status = stateHandle.getLiveData<ProgressStatus>("status")
    val status: LiveData<ProgressStatus> = _status
    
    private val _username = stateHandle.getLiveData<String>("username")
    val username: LiveData<String> = _username
    
    private val _password = stateHandle.getLiveData<String>("password")
    val password: LiveData<String> = _password
    
    suspend fun login() {
        _status.value = ProgressStatus.IN_PROGRESS
        
        viewModelScope.launch {
            api.login(username.value, password.value)
            _status.value = ProgressStatus.COMPLETE
        }
    }
}
```

Handlebar allows Android developers to capitalize on SavedStateHandle's benefits without sacrificing the safety and readability that type safety provides.

# Usage

Create an interface that describes your state properties, marked with the `@TypedState` annotation. Then create a property on your ViewModel to represent your state, using the extension function `SavedStateHandle.asType`.

```kotlin
class LoginViewModel(
    api: API,
    stateHandle: SavedStateHandle
) : ViewModel() {

    @TypedState interface LoginState {
        var username: LiveData<String>
        var email: LiveData<String>
        var status: LiveData<ProgressStatus>
    }
    private val state = stateHandle.asType<LoginStateContract>()
    
    // Expose status to observers
    val status: LiveData<ProgressStatus> = state.status
    
    val username: LiveData<String> = state.username
    val password: LiveData<String> = state.password
    
    suspend fun login() {
        state.updateStatus(ProgressStatus.IN_PROGRESS)
        
        viewModelScope.launch {
            api.login(username.value, password.value)        
            state.updateStatus(ProgressStatus.COMPLETE)
        }
    }
}
```

That's it. Handlebar will generate a new interface `[TypedState interface name]Contract` using the properties from your `@TypedState` interface, as well as a concrete implementation connecting this interface to the SavedStateHandle on your ViewModel, so all changes you make on your state property will be backed by the SavedStateHandle.
