About
-----

This is example / learning app showcasing use of [Kotlin Flow](https://developer.android.com/kotlin/flow) and [Jetpack Paging](https://developer.android.com/topic/libraries/architecture/paging/v3-overview).
It was created based on a book [Kickstart Modern Android Development with Jetpack and Kotlin](https://github.com/PacktPublishing/Kickstart-Modern-Android-Development-with-Jetpack-and-Kotlin) from [Catalin Ghita](https://www.linkedin.com/in/catalin-ghita-590504127/).

What is Kotlin Flow?
--------------------

Kotlin Flow is a data type built on top of coroutines that exposes a stream of multiple, asynchronously computed values.

As opposed to suspending functions, which emit a single result, Flow allows us to emit multiple values sequentially over time. However, just as a suspending function emits a result in an asynchronous manner that you can later obtain from within a coroutine, Flow also emits results asynchronously, so you must observe its results from within a launched coroutine.

The common use-case of Flow is then for example receiving live updates from REST API or database.

Just as the Compose State object holds data of a certain type (for example, `State<Int>` emits values of type `Int`), Flow also holds data of a certain type; so in this case, it would be `Flow<Int>`. However, in the example app, it will be `Flow<Repository>`, where `Repository` represents one Github repo with id, title and description.

What is Jetpack Paging?
-----------------------

Jetpack Paging abstracts away the principle of getting chunks (pages) of values based on which part of a list user visited in the app. It can be nicely expressed by this image:

Paging library has manny advantages:
 - Avoidance of data request duplication—your app will request data only when needed; for example, when the user reaches the end of the list and more items must be rendered.
 - Paged data is cached in memory out of the box. During the lifetime of the app process, once a page was loaded, your app will never request it again. If you cache the paginated data in a local database, then your application will not need to request a specific page for cases such as after an app restart.
 - Paginated data is exposed as a data stream of the type that fits your need: Kotlin Flow, LiveData, or RxJava.
 - Out-of-the-box support for View System or Compose-based UI components that request data automatically when the user scrolls toward the end of the list. With such support, we don't have to know when to request new pages with data as the UI layer will trigger that for us out of the box.
 - Retry and refresh capabilities triggered directly by the UI components.

There are several important parts of this library:
 - `PagingSource` component: Defines the source of data for the paginated content. This object decides which page to request and loads it from your remote or local data source. If you're looking to have both a local and remote data source for your paginated content, you could use the built-in `RemoteMediator` API of the Paging library.
 - `Pager` component: Based on the defined `PagingSource` component, you can construct a `Pager` object that will expose a stream of `PagingData` objects. You can configure the `Pager` object by passing a `PagingConfig` object to its constructor and specifying the page size of your data, for example. The `PagingData` class is a wrapper over your paginated data containing a set of items part of the corresponding page. The `PagingData` object is responsible for triggering a query for a new page with items that is then forwarded to the `PagingSource` component.
 - A dedicated UI component that supports pagination: To consume the stream of paginated content, your UI must make use of dedicated UI components that can handle paginated data. If your UI is based on the traditional View System, you could use the `PagingDataAdapter` component. In this app, UI is based on Compose, and `LazyColumn` is used, since it knows how to consume paginated data.
