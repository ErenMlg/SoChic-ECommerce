# SoChic E Commerce XML & Compose Clone
<br>
<p align="center">
This repository contains a clone implementation of the SoChic e-commerce application, created using Jetpack Compose and XML. 
  The purpose of this project is to demonstrate a feature-rich e-commerce UI and functionality built with modern Android development practices.
</p> <br>

## Project Overview
The SoChic clone project replicates the essential features of an e-commerce platform, including:

<ul>
  <li>Category Browsing: Explore categories and subcategories with expandable lists.</li>
  <li>Product Listing: View products in a grid format with lazy loading.</li>
  <li>Product Details: Detailed product pages with images, descriptions, and pricing.</li>
  <li>Navigation: Intuitive navigation between screens and categories.</li>
</ul>

<p align="center">
<a href="https://opensource.org/licenses/Apache-2.0"><img src="https://img.shields.io/badge/License-Apache%202.0-red.svg"></a>
<a href="https://android-arsenal.com/api?level=23"><img src="https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat"></a>
<a href="https://github.com/ErenMlg"><img src="https://img.shields.io/badge/github-ErenMlg-blue"></a>
</p>

 ## Project Screens
<img width=100% src="https://github.com/user-attachments/assets/e6433f8f-c413-49ed-9b19-27d9770404a7"/>



## Project Tech Stack
<ul>
  <li>This project developed with %100 with <a href="https://developer.android.com/kotlin?hl=tr">Kotlin</a></li>
  <li><a href="https://developer.android.com/jetpack/compose">Jetpack Compose:</a> Used Jetpack Compose, Android’s modern toolkit for building native UI. It simplifies and accelerates UI development with less boilerplate, intuitive Kotlin APIs, and declarative UI components.</li>
  <li>Made with <a href="https://developer.android.com/topic/architecture?hl=tr">Android Architecture Components</a> for the Collection of libraries that help you design robust, testable, and maintainable apps.</li>
  <li><a href="https://developer.android.com/topic/libraries/architecture/viewmodel?hl=tr">ViewModel</a>: The ViewModel class is a business logic or screen level state holder. It exposes state to the UI and encapsulates related business logic. Its principal advantage is that it caches state and persists it through configuration changes. This means that your UI doesn’t have to fetch data again when navigating between activities, or following configuration changes, such as when rotating the screen.</li>
  <li><a href="https://developer.android.com/kotlin/coroutines"">Kotlin Coroutine:</a> On Android, coroutines help to manage long-running tasks that might otherwise block the main thread and cause your app to become unresponsive. Over 50% of professional developers who use coroutines have reported seeing increased productivity. This topic describes how you can use Kotlin coroutines to address these problems, enabling you to write cleaner and more concise app code.</li>
  <li><a href="https://developer.android.com/training/dependency-injection/hilt-android">Dependency Injection with Hilt</a>: Hilt is a dependency injection library for Android that reduces the boilerplate of doing manual dependency injection in your project. Doing manual dependency injection requires you to construct every class and its dependencies by hand, and to use containers to reuse and manage dependencies.</li>
  <li><a href="https://developer.android.com/guide/navigation">Navigation</a>: Navigation refers to the interactions that allow users to navigate across, into, and back out from the different pieces of content within your app. Android Jetpack's Navigation component helps you implement navigation, from simple button clicks to more complex patterns, such as app bars and the navigation drawer.</li>
  <li><a href="https://kotlinlang.org/docs/ksp-overview.html">Kotlin KSP</a>: Kotlin Symbol Processing (KSP) is an API that you can use to develop lightweight compiler plugins</li>
  <li><a href="https://developer.android.com/topic/architecture/data-layer">Repository</a>: This located in data layer that contains application data and business logic. </li>
  <li><a href="https://coil-kt.github.io/coil/compose/">Coil</a>: Coil for show pictures on network.</li>
  <li><a href="https://www.apollographql.com/docs/android/">Apollo & GraphQL:</a> Apollo GraphQL is used to consume GraphQL APIs effectively. It enables declarative data fetching and caching, reducing boilerplate while providing a strongly typed API. Apollo facilitates seamless integration of GraphQL into Android projects, enhancing flexibility and productivity when working with remote data sources.</li>
    <li><a href="https://developer.android.com/topic/architecture/domain-layer?hl=tr">Use Case</a>: Located domain layer that sits between the UI layer and the data layer.</li>
  <li>Custom views for the create custom editable layouts or widgets.</li>
</ul>

 ## Architecture
<p>
  XML version uses only MVVM otherwise Compose version uses <a href="https://developer.android.com/topic/architecture?hl=tr#recommended-app-arch"> MVVM (Model View View-Model)</a> 
and <a href="https://medium.com/@mohammedkhudair57/mvi-architecture-pattern-in-android-0046bf9b8a2e"> MVI (Model View Intent)</a> architecture structure as combined.
</p>
<img src="https://miro.medium.com/v2/resize:fit:1400/1*k3G2EYx8lCPYHbMH8x1tcg.png">

## Project Graph
This app uses <a href="https://developer.android.com/topic/architecture?hl=tr#recommended-app-arch">MVVM (Model View View-Model)</a> combined with <a href="https://medium.com/@mohammedkhudair57/mvi-architecture-pattern-in-android-0046bf9b8a2e">
MVI (Model View Intent)</a> architecture<br>
Core layer have 5 sub layer as common, data, di, domain, navigation, presentation;
<table>
  <tr>
    <td>
      <img src="https://github.com/user-attachments/assets/f22b1ba7-8972-45bf-898e-9830d7d27e17">
    </td>
    <td>
      <ul>
<li>Extensions: Contains map extensions for converting responses to entities, as well as other extensions for view manipulation, network responses, modifiers, and more.</li>
<li>Delegation: Manages delegation logic to simplify tasks and improve code modularity.</li>
<li>Response State: Handles mapping and transformation of return types for better state management and data consistency.</li>
<li>Singleton: Manages singleton objects like the category list, ensuring efficient reuse of shared resources across the app.</li>
      </ul>
    </td>
  <td><img src="https://github.com/user-attachments/assets/0202ab3e-5424-4cc5-b95b-6caa297ac28f"></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/8db5cecb-ac1e-463d-b53c-9167784b6e86"></td>
    <td>
      <ul>
        <li>Model: Hold database objects for the response</li>
        <li>Network: Hold dao's and database file</li>
        <li>Repository: Hold repository for the application data and business logic.</li>
      </ul>
    </td>
  <td><img src="https://github.com/user-attachments/assets/89491ea5-9270-4cf4-b3e0-575e359c981f"></td>
  </tr>
    <tr>
    <td><img src="https://github.com/user-attachments/assets/a2f81452-4c36-4f35-b1f9-861ad2eef461"></td>
    <td>
      <ul>
        <li>Di: Hold di objects and works</li>
      </ul>
    </td>
  <td><img src="https://github.com/user-attachments/assets/a2f81452-4c36-4f35-b1f9-861ad2eef461"></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/c1672ab5-97c6-4ec1-befa-c90d5e5945f3"></td>
    <td>
      <ul>
        <li>Source: Hold used source interface.</li>
        <li>Repository: Hold repository interfaces for the clean view.</li>
        <li>Usecase: Hold usecases for the common functions.</li>
      </ul>
    </td>
  <td><img src="https://github.com/user-attachments/assets/c1672ab5-97c6-4ec1-befa-c90d5e5945f3"></td>
  </tr>
  <tr>
    <td></td>
    <td>
      <ul>
        <li>Navigation: Hold navhost, route objects and bottom app bar, for only compose</li>
      </ul>
 <td><img src="https://github.com/user-attachments/assets/88935bd7-e725-4705-a0bb-93c1d47cb66b"></td>
    </td>
  </tr>
     <tr>
  <td><img src="https://github.com/user-attachments/assets/ec140413-6594-4e2d-9fa2-9eab256a4b64"></td>
 <td>
   <ul>
     <li>ViewModel: Communicate with data layer and send response to Contract for the state.</li>
     <li>Contract: Hold user actions and incoming data responses for the send UI for compose</li>
     <li>Route: Its hold Screen(UI) and route for the navigation</li>
     <li>Adapter: Its for the recylcer view layout holder for XML</li>
   </ul>
  <td><img src="https://github.com/user-attachments/assets/29639923-00c8-4068-a915-1c4591cb44ac"></td>
 </td>
 </tr>
 <tr>
  <td><img src="https://github.com/user-attachments/assets/2e6b2394-71e0-469e-b1ab-650705895275"></td>
 <td>
  Presentation layer for views, viewmodels , contracts, adapter and etc. 
 </td>
  <td><img src="https://github.com/user-attachments/assets/8a619db6-212e-480b-8f50-7b04ace831aa"></td>
 </tr>
</table>


## End Note
I may have mistakes, you can contact me for your feedback. 👉 📫 **eren.mollaoglu@outlook.com**<br>

## License
<pre>
Designed and developed by 2024 ErenMlg (Eren Mollaoğlu)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>
