[github]: https://github.com/Puotek/arma-config-plugin

[github:changelog]: https://github.com/Puotek/arma-config-plugin/blob/main/CHANGELOG.md

[github:issues]: https://github.com/Puotek/arma-config-plugin/issues

[marketplace:page]: https://plugins.jetbrains.com/plugin/29234-arma-config-support

[marketplace:versions]: https://plugins.jetbrains.com/plugin/29234-arma-config-support/versions

[puotek:discord]: https://discord.com/users/291967371646599169

<!--suppress HtmlDeprecatedAttribute -->
<div align="center">

[<img src="https://capsule-render.vercel.app/api?type=waving&height=222&color=0:2046fb,15:7442ec,60:fd3a53,100:fd442a&text=Arma%20Config%20Support&textBg=false&fontAlignY=30&fontSize=55&animation=fadeIn&reversal=false&section=header&fontColor=FFFFFF&desc=JetBrains%20IDE%20Plugin&descAlign=50&descAlignY=53"  alt="Arma Config Support JetBrains IDE Plugin"/>][marketplace:page]

[![Marketplace](https://img.shields.io/badge/Marketplace-2835a9?logo=jetbrains&logoColor=white)][marketplace:page]
[![Version](https://img.shields.io/jetbrains/plugin/v/29234-arma-config-support.svg?label=Version&color=713280&labelColor=151b23)][github:changelog]
[![Downloads](https://img.shields.io/jetbrains/plugin/d/29234-arma-config-support.svg?label=Downloads&color=aa3426&labelColor=151b23)][marketplace:versions]
[![Discord](https://img.shields.io/badge/Puotek-%235865F2.svg?logo=discord&logoColor=white)][puotek:discord]

</div>

<!-- Plugin description -->

Language support for [Arma 3 config files](https://community.bistudio.com/wiki/Config.cpp/bin_File_Format) in JetBrains IntelliJ-based IDEs

### Features

- Syntax highlighting with color settings
- Breadcrumbs and sticky lines
- Inspections
- Support for macro usage in syntax checking
- Support for comment keybinds
- Working file structure window
- Formatting
- Folding
- [and more...][github:changelog]

<!-- Plugin description end -->

I enjoy JetBrains IDEs, and the current existing arma plugin was old
and didn't support some of the nicer IntelliJ features like breadcrumbs and sticky lines, so I made my own.

If you encounter any errors, bugs or think a feature is not working as intended or missing, feel free to make an [issue on github][github:issues].

## Installation

#### Using the IDE built-in plugin system

<kbd>Settings</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Arma Config Support"</kbd> >
<kbd>Install</kbd>

#### Using JetBrains Marketplace

Download the [latest release][marketplace:versions] from JetBrains Marketplace and install it manually using:

<kbd>Settings</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

## Manual Version Build

To manually create a release, open project in IntelliJ with [Plugin DevKit](https://plugins.jetbrains.com/plugin/22851-plugin-devkit) and [Grammar-Kit](https://plugins.jetbrains.com/plugin/6606-grammar-kit) plugins installed.

Make sure to [set project JDK](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk) to version 21 (e.g. `Oracle OpenJDK 21.0.1`)

Run `buildDownloads` configuration or gradle task

Console might say error, but your plugin build should be in your `User/Downloads` folder. Install it manually using:

<kbd>Settings</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>
