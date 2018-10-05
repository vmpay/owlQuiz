# OwlQuiz
[![Apache 2.0 License](https://img.shields.io/hexpm/l/plug.svg) ](LICENSE)

OwlQuiz is an application for playing the quiz. It was inspired by the popular TV quiz **game** from Eastern Europe called
"What? Where? When?". That's why questions, timer length and player rating features comply with the **game** rules.

## Features

### Timer

The timer can be set up to count down 20 sec, 30 sec and 60 sec. It also has sound notification indicating start, finish
and 10 seconds until finish (only in 60 seconds period).

### Account

You can search through the players of the official [rating database](http://rating.chgk.info/) (RU). 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

You need to install:

* Android development IDE such as [Android Studio](https://developer.android.com/studio/index.html)
* Android device or emulator with [Android 	JELLY_BEAN_MR1](https://developer.android.com/reference/android/os/Build.VERSION_CODES#JELLY_BEAN_MR1)
or newer. You can see  how to create an emulator in Android Studio on the [official site](https://developer.android.com/studio/run/managing-avds.html)

### Installing

Clone the OwlQuiz repository from github with the CLI

```
git clone https://github.com/vmpay/owlQuiz.git
```

or download it manually.

After that open this repo from your IDE.

OR

Download from [release section](https://github.com/vmpay/owlQuiz/releases) and install on your android device.

## Built With

* [Android Jetpack](https://developer.android.com/jetpack/) - Jetpack is a collection of Android software components to make it easier for you to develop great Android apps. These components help you follow best practices, free you from writing boilerplate code, and simplify complex tasks, so you can focus on the code you care about
* [RxJava](https://github.com/ReactiveX/RxJava) - a library for composing asynchronous and event-based programs by using observable sequences
* [RxAndroid](https://github.com/ReactiveX/RxAndroid) - This module adds the minimum classes to RxJava that make writing reactive components in Android applications easy and hassle-free. More specifically, it provides a Scheduler that schedules on the main thread or any given Looper
* [Retrofit](https://square.github.io/retrofit/) - Type-safe HTTP client for Android and Java
* [okhttp](http://square.github.io/okhttp/) - OkHttp is an HTTP client
* [Mockito](https://site.mockito.org/) - Tasty mocking framework for unit tests in Java

## Authors

* **Andrew** - *Initial work* - [vmpay](https://github.com/vmpay)

See also the list of [contributors](https://github.com/vmpay/owlQuiz/graphs/contributors) who participated in this project.

## License

This project is licensed under the Apache License 2.0 License - see the [LICENSE](LICENSE) file for details
