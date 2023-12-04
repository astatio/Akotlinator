> [!CAUTION]
> This library is in very early stage and is not functional.

# Akotlinator
[![codecov](https://codecov.io/gh/astatio/Akotlinator/graph/badge.svg?token=T2T04AXNAJ)](https://codecov.io/gh/astatio/Akotlinator) ![GitHub last commit (branch)](https://img.shields.io/github/last-commit/astatio/Akotlinator) [![](https://jitpack.io/v/astatio/Akotlinator.svg)](https://jitpack.io/#astatio/Akotlinator) ![GitHub top language](https://img.shields.io/github/languages/top/astatio/Akotlinator?logo=kotlin&color=7F52FF) ![Code Climate maintainability](https://img.shields.io/codeclimate/maintainability/astatio/Akotlinator) ![Code Climate technical debt](https://img.shields.io/codeclimate/tech-debt/astatio/Akotlinator) ![Code Climate issues](https://img.shields.io/codeclimate/issues/astatio/Akotlinator)

Kotlin Multiplatform API wrapper for Akinator

### Library Status

| Platform       | Status       | Notes                                        |
|----------------|--------------|----------------------------------------------|
| Android        | Experimental |                                              |
| JVM            | Experimental |                                              |
| iOS            | Experimental |                                              |
| JS             | TBA          | Ktor CIO engine does not support this target |
| Native MacOS   | TBA          | Not planned                                  |
| Native Windows | TBA          | Ktor CIO engine does not support this target |


## Why Akotlinator when Akiwrapper exists?
The objective with Akotlinator is not to replace Akiwrapper but merely to serve as an extension to allow Kotlin developers to use a more Kotlin idiomatic way to interact with Akinator alongside allowing it to be used on [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) projects. Akotlinator assumes from the beginning that all logic in Akiwrapper is correct, as such, do consider opening an issue in Akiwrapper if you find something wrong with Akotlinator. Akotlinator makes use of KMP-compatible alternatives to work with the Akinator API.

## How to use
You can get the dependency from [JitPack](https://jitpack.io/#astatio/Akotlinator). The instructions are provided there as well.
For now, usage examples are not available. Once the library reaches an usable state, an example will be publicly available.

## How do i contribute?
There's not a defined standard for opening an issue or a pull request as of now. Any sort of contribution whether be it code, suggestion or a review is welcome!

### Notes
This *might* include code from [Akiwrapper](https://github.com/markozajc/Akiwrapper). It **does** follow Akiwrapper's logic to interact with Akinator undocumented API. This project would not be possible without it. Visit Akiwrapper repo to know more about the project.
